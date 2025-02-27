package com.walnut.sparta.ucdn.console.infrastructure.dto;

public class UpdateFileNameDTO {
    private String filePath;

    private String newFileName;

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getNewFileName() {
        return this.newFileName;
    }

    public void setNewFileName(String newFileName) {
        this.newFileName = newFileName;
    }
}
