package com.pinecone.framework.util.datetime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.json.JSON;

public class StorageDate implements Pinenut {
    private int   mnYear;
    private short mnMonth;
    private short mnDay;

    public StorageDate( int year, short month, short day ) {
        this.mnYear  = year;
        this.mnMonth = month;
        this.mnDay   = day;
    }

    public StorageDate( int year, int month, int day ) {
        this( year, (short) month, (short)day );
    }

    public int getYear() {
        return this.mnYear;
    }

    public void setYear( int year ) {
        this.mnYear = year;
    }

    public short getMonth() {
        return this.mnMonth;
    }

    public void setMonth( short month ) {
        this.mnMonth = month;
    }

    public short getDay() {
        return this.mnDay;
    }

    public void setDay( short day ) {
        this.mnDay = day;
    }

    @Override
    public String toString() {
        return String.format("%d-%02d-%02d", this.mnYear, this.mnMonth, this.mnDay);
    }

    @Override
    public String toJSONString() {
        return JSON.stringify( this.toString() );
    }

    public static StorageDate of( int year, int month, int day ) {
        return new StorageDate(year, month, day);
    }
}
