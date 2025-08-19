package com.walnut.sparta.uis.console.domain.dto;

import java.util.List;

public class PageResultDTO<T> {
    private long total;
    private int pages;
    private int page;
    private int size;
    private List<T> records;

    public PageResultDTO() {
    }

    public PageResultDTO(long total, int pages, int page, int size, List<T> records) {
        this.total = total;
        this.pages = pages;
        this.page = page;
        this.size = size;
        this.records = records;
    }

    // getter和setter方法
    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}