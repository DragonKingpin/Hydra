package com.genius.pojo;

/**
 * @author Genius
 * @date 2023/04/26 01:48
 **/
public enum FileType {
    LOGGER("日志"),
    COMMON("普通文件");
    private final String name;
    FileType(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
