package com.genius.common;

/**
 * @author Genius
 * @date 2023/05/09 02:43
 **/
public enum MessageType {
    QUERY("Query"),
    REPLY("Reply"),
    POST("Post"),
    REPLY_POST("ReplyPost"),
    SHUTDOWN("ShutDown");

    private final String value;
    MessageType(String value){this.value = value;}

    public String getName(){
        return this.value;
    }
}
