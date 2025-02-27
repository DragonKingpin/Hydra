package com.walnut.sparta.ucdn.console.infrastructure;

public class EFileContent {
    private byte[] bytes;

    private long fileSize;

    private String fileName;

    private long offset;

    private int  bufferLength;

    public EFileContent(){}

    public EFileContent( byte[] bytes, long fileSize, String fileName, long offset, int bufferLength  ){
        this.bytes = bytes;
        this.fileSize = fileSize;
        this.fileName = fileName;
        this.offset = offset;
        this.bufferLength = bufferLength;
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

    public long getOffset(){
        return this.offset;
    }

    public void setOffset( long offset ){
        this.offset = offset;
    }

    public long getBufferLength(){
        return this.bufferLength;
    }

    public void setBufferLength( int bufferLength ){
        this.bufferLength = bufferLength;
    }

}
