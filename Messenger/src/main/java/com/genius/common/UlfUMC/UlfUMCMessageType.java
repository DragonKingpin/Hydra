package com.genius.common.UlfUMC;

/**
 * @author Genius
 * @date 2023/05/18 18:39
 **/
public enum UlfUMCMessageType {
    GET("Get"),
    POST("Post");

    private final String value;
    UlfUMCMessageType(String value){this.value = value;}

    public String getName(){
        return this.value;
    }
}
