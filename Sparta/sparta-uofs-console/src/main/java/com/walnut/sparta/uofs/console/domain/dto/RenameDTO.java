package com.walnut.sparta.uofs.console.domain.dto;

public class RenameDTO {
    private String path;

    private String newName;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
