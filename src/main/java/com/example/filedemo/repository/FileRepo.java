package com.example.filedemo.repository;


import com.example.filedemo.model.EncryptFiles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepo extends JpaRepository<EncryptFiles, Long> {
}
