package com.example.filedemo.service;




import com.example.filedemo.exception.FileStorageException;
import com.example.filedemo.model.EncryptFiles;
import com.example.filedemo.repository.FileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Service
public class FileStorageService {

    @Autowired
    private FileRepo fileRepo;

    public EncryptFiles storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequenth " + fileName);
            }

            EncryptFiles dbFile = new EncryptFiles(fileName, file.getContentType(), file.getBytes());

            return fileRepo.save(dbFile);

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public EncryptFiles getFile(Long fileId) {
        try {
            return fileRepo.findById(fileId)
                    .orElseThrow(() -> new FileNotFoundException("File you requested not found with id " + fileId));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<EncryptFiles> getFiles(){
        return fileRepo.findAll();
    }
}
