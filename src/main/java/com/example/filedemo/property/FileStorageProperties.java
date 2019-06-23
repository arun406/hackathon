package com.example.filedemo.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir;
    private String inceptionPath;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public String getInceptionPath() {
        return inceptionPath;
    }

    public void setInceptionPath(String inceptionPath) {
        this.inceptionPath = inceptionPath;
    }
}
