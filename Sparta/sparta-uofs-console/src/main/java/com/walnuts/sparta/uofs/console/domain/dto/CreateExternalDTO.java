package com.walnuts.sparta.uofs.console.domain.dto;

import com.pinecone.framework.system.prototype.Pinenut;

public class CreateExternalDTO implements Pinenut {
    private String folderPath;

    private String externalSymbolicName;

    private String reparsedPoint;

    public String getReparsedPoint() {
        return reparsedPoint;
    }

    public void setReparsedPoint(String reparsedPoint) {
        this.reparsedPoint = reparsedPoint;
    }

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
}
