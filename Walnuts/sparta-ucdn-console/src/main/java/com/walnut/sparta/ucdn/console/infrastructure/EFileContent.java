package com.walnut.sparta.ucdn.console.infrastructure;

public class EFileContent {
    private byte[] bytes;

    public EFileContent(){}

    public EFileContent( byte[] bytes ){
        this.bytes = bytes;
    }


    public byte[] getBytes(){
        return this.bytes;
    }

    public void setBytes( byte[] bytes ){
        this.bytes = bytes;
    }

}
