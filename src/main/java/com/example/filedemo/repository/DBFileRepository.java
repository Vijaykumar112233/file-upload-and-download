package com.example.filedemo.repository;

import com.example.filedemo.model.DBFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DBFileRepository extends JpaRepository<DBFile, String> {

    List< DBFile > findByFileNameContaining(String fileName);

}
