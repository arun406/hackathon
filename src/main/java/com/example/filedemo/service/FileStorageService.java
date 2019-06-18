package com.example.filedemo.service;

import com.example.filedemo.exception.FileStorageException;
import com.example.filedemo.exception.MyFileNotFoundException;
import com.example.filedemo.payload.UploadFileResponse;
import com.example.filedemo.property.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private final String uploadDir;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.uploadDir = fileStorageProperties.getUploadDir();
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    public List<UploadFileResponse> getFilesList() {

        List<UploadFileResponse> files = new ArrayList<>();
        try {
            Files.list(Paths.get(this.uploadDir)).forEach(path -> {
                String fileName = path.getFileName().toString();
                int lastIndexOfDot = fileName.lastIndexOf('.');
                String mimeType = StringUtils.cleanPath(fileName).substring(lastIndexOfDot + 1);
                long size = path.toFile().length();
                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/downloadFile/")
                        .path(fileName)
                        .toUriString();
                files.add(new UploadFileResponse(fileName, fileDownloadUri, mimeType, size));

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;

    }
}
