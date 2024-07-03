package com.example.minorproject1.service;

import com.example.minorproject1.model.FileData;
import com.example.minorproject1.repository.StorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    @Autowired
    StorageRepository storageRepository;

    public FileData saveAttachment(MultipartFile file) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if(fileName.contains("..")) {
                throw  new Exception("Filename contains invalid path sequence "
                        + fileName);
            }

            FileData fileData = FileData.builder()
                    .name(fileName)
                    .type(file.getContentType())
                    .fileData(file.getBytes())
                    .build();

            return storageRepository.save(fileData);

        } catch (Exception e) {
            throw new Exception("Could not save File: " + fileName);
        }
    }

    public FileData getAttachment(String fileId) throws Exception {
        return storageRepository
                .findById(fileId)
                .orElseThrow(
                        () -> new Exception("File not found with Id: " + fileId));
    }

}
