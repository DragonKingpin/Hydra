package com.walnut.sparta.uis.console.domain.entity;

public class ArchTableMeta implements TableMeta{

    protected String tableName;

    public ArchTableMeta() {
    }

    public ArchTableMeta(String tableName) {
        this.tableName = tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String getTableName() {
        return this.tableName;
    }

}
