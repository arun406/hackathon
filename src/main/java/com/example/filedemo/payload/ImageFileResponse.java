package com.example.filedemo.payload;

import lombok.Data;

@Data
public class ImageFileResponse extends UploadFileResponse {

    private String bread;


    /**
     * @param fileName
     * @param fileDownloadUri
     * @param fileType
     * @param size
     * @param bread
     */
    public ImageFileResponse(String fileName, String fileDownloadUri, String fileType, long size, String bread) {
        super(fileName, fileDownloadUri, fileType, size);
        this.bread = bread;
    }

    public String getBread() {
        return bread;
    }

    public void setBread(String bread) {
        this.bread = bread;
    }

}
