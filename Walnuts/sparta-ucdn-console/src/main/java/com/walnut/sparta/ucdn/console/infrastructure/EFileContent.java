package com.walnut.sparta.ucdn.console.infrastructure;

public class EFileContent {
    private byte[] bytes;

    private long fileSize;

    private String fileName;

    public EFileContent(){}

    public EFileContent( byte[] bytes, long fileSize, String fileName ){
        this.bytes = bytes;
        this.fileSize = fileSize;
        this.fileName = fileName;
    }


    public byte[] getBytes(){
        return this.bytes;
    }

    public void setBytes( byte[] bytes ){
        this.bytes = bytes;
    }

    public long getFileSize(){
        return this.fileSize;
    }

    public void setFileSize( long fileSize ){
        this.fileSize = fileSize;
    }

    public String getFileName(){
        return this.fileName;
    }

    public void setFileName( String fileName ){
        this.fileName = fileName;
    }

}
