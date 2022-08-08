package com.example.filedemo.controller;

import com.example.filedemo.model.EncryptFiles;
import com.example.filedemo.payload.FileUploadResponse;
import com.example.filedemo.service.FileStorageService;

import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
public class FileEncryptController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/fileupload")
    public FileUploadResponse uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();

        PDDocument document = PDDocument.load(convFile);

        //Creating access permission object
        AccessPermission ap = new AccessPermission();

        //Creating StandardProtectionPolicy object
        StandardProtectionPolicy spp = new StandardProtectionPolicy("1234", "abcd", ap);

        //Setting the length of the encryption key
        spp.setEncryptionKeyLength(128);

        //Setting the access permissions
        spp.setPermissions(ap);

        //Protecting the document
        document.protect(spp);

        System.out.println("PDF Document encrypted Successfully.");

        File file1 = File.createTempFile(file.getName(),".pdf");


        //Saving the document
        document.save(file1);

        document.close();

        FileInputStream input = new FileInputStream(file1);
        MultipartFile multipartFile = new MockMultipartFile(file1.getName(),
                file.getName(), "application/pdf", IOUtils.toByteArray(input));

        EncryptFiles files = fileStorageService.storeFile(multipartFile);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/filedownload/")
                .path(String.valueOf(files.getId()))
                .toUriString();

        fileDownloadUri=   fileDownloadUri.replaceAll("http://localhost:8080","https://b4cf-2409-4071-2380-7bb0-5faa-a39a-852b-757d.in.ngrok.io/");

        return new FileUploadResponse(file.getName(), fileDownloadUri,
                file.getContentType(), file.getSize());


    }


    @GetMapping("/filedownload/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        EncryptFiles dbFile = fileStorageService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "/attachment; filename=\"" + dbFile.getName() + "\"")
                .body(new ByteArrayResource(dbFile.getFilecontent()));
    }

}
