package com.pinecone.framework.util.datetime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.json.JSON;

import java.time.Month;

public class StorageDateTime implements Pinenut {
    private StorageDate mDate;
    private StorageTime mTime;

    public StorageDateTime( StorageDate date, StorageTime time ) {
        this.mDate = date;
        this.mTime = time;
    }

    public StorageDate getDate() {
        return this.mDate;
    }

    public void setDate(StorageDate date) {
        this.mDate = date;
    }

    public StorageTime getTime() {
        return this.mTime;
    }

    public void setTime(StorageTime time) {
        this.mTime = time;
    }

    public int getYear() {
        return this.mDate.getYear();
    }

    public void setYear( int year ) {
        this.mDate.setYear(year);
    }

    public int getMonthValue() {
        return this.mDate.getMonth();
    }

    public void setMonth( int month ) {
        this.mDate.setMonth((short) month);
    }

    public int getDayOfMonth() {
        return this.mDate.getDay();
    }

    public void setDay( int day ) {
        this.mDate.setDay((short) day);
    }

    public int getHour() {
        return this.mTime.getHour();
    }

    public void setHour( int hour ) {
        this.mTime.setHour(hour);
    }

    public int getMinute() {
        return this.mTime.getMinute();
    }

    public void setMinute( int minute ) {
        this.mTime.setMinute(minute);
    }

    public int getSecond() {
        return this.mTime.getSecond();
    }

    public void setSecond( int second ) {
        this.mTime.setSecond(second);
    }

    public int getNano() {
        return this.mTime.getNano();
    }

    public void setNano( int nano ) {
        this.mTime.setNano(nano);
    }


    @Override
    public String toString() {
        return this.mDate.toString() + " " + this.mTime.toString();
    }

    @Override
    public String toJSONString() {
        return JSON.stringify( this.toString() );
    }

    public static StorageDateTime of( int year, Month month, int dayOfMonth, int hour, int minute ) {
        return new StorageDateTime( StorageDate.of(year, month.getValue(), dayOfMonth ), StorageTime.of(hour, minute, 0, 0) );
    }

    public static StorageDateTime of( int year, Month month, int dayOfMonth, int hour, int minute, int second ) {
        return new StorageDateTime( StorageDate.of(year, month.getValue(), dayOfMonth), StorageTime.of(hour, minute, second, 0) );
    }

    public static StorageDateTime of( int year, Month month, int dayOfMonth, int hour, int minute, int second, int nanoOfSecond ) {
        return new StorageDateTime( StorageDate.of(year, month.getValue(), dayOfMonth), StorageTime.of(hour, minute, second, nanoOfSecond) );
    }

    public static StorageDateTime of( int year, int month, int dayOfMonth, int hour, int minute ) {
        return new StorageDateTime( StorageDate.of(year, month, dayOfMonth), StorageTime.of(hour, minute, 0, 0) );
    }

    public static StorageDateTime of( int year, int month, int dayOfMonth, int hour, int minute, int second ) {
        return new StorageDateTime( StorageDate.of(year, month, dayOfMonth), StorageTime.of( hour, minute, second, 0 ) );
    }

    public static StorageDateTime of( int year, int month, int dayOfMonth, int hour, int minute, int second, int nanoOfSecond ) {
        return new StorageDateTime(StorageDate.of(year, month, dayOfMonth),
                StorageTime.of(hour, minute, second, nanoOfSecond));
    }

    public static StorageDateTime of( StorageDate date, StorageTime time ) {
        return new StorageDateTime( date, time );
    }

}
