package com.genius.common.UlfUMC;

/**
 * @author Genius
 * @date 2023/05/16 22:05
 **/
public class UlfUMCMessageException extends Exception{


    private String reason;

    public UlfUMCMessageException(String reason) {
        this.reason = reason;
    }

    @Override
    public String getMessage() {
        return reason;
    }
}
