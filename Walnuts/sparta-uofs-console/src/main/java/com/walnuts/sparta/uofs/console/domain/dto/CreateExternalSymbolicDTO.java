package com.walnuts.sparta.uofs.console.domain.dto;

public class CreateExternalSymbolicDTO {
    private String folderPath;

    private String externalSymbolicName;

    private String reparsedPoint;

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getExternalSymbolicName() {
        return externalSymbolicName;
    }

    public void setExternalSymbolicName(String externalSymbolicName) {
        this.externalSymbolicName = externalSymbolicName;
    }

    public String getReparsedPoint() {
        return reparsedPoint;
    }

    public void setReparsedPoint(String reparsedPoint) {
        this.reparsedPoint = reparsedPoint;
    }



}
