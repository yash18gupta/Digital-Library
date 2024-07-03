package com.example.minorproject1.repository;

import com.example.minorproject1.model.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StorageRepository extends JpaRepository<FileData,String> {
}
