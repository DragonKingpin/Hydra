package com.pinecone.framework.util.datetime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.json.JSON;

public class StorageTime implements Pinenut {
    private byte mnHour;
    private byte mnMinute;
    private byte mnSecond;
    private int  mnNano;

    public StorageTime( int hour, int minute, int second, int nano ) {
        this.mnHour   = (byte) hour;
        this.mnMinute = (byte) minute;
        this.mnSecond = (byte) second;
        this.mnNano   = nano;
    }


    public int getHour() {
        return this.mnHour;
    }

    public void setHour( int hour ) {
        this.mnHour = (byte) hour;
    }

    public int getMinute() {
        return this.mnMinute;
    }

    public void setMinute( int minute ) {
        this.mnMinute = (byte) minute;
    }

    public int getSecond() {
        return this.mnSecond;
    }

    public void setSecond( int second ) {
        this.mnSecond = (byte) second;
    }

    public int getNano() {
        return this.mnNano;
    }

    public void setNano( int nano ) {
        this.mnNano = nano;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d.%09d", this.mnHour, this.mnMinute, this.mnSecond, this.mnNano);
    }

    @Override
    public String toJSONString() {
        return JSON.stringify( this.toString() );
    }

    public static StorageTime of( int hour, int minute, int second, int nano ) {
        return new StorageTime( hour, minute, second, nano );
    }
}