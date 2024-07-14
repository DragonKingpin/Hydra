package com.genius.common.UlfUMC;

import com.genius.pool.FunctionNamePool;

import java.util.Map;

/**
 * @author Genius
 * @date 2023/05/18 20:14
 **/
public class ErrorMessageBuilder implements MessageBuilder{

    private UlfUMCMessage errorMessage;

    public ErrorMessageBuilder(){
        errorMessage = new UlfUMCMessage(UlfUMCMessageType.GET, FunctionNamePool.ERROR,Map.of("error",""));
    }
    @Override
    public MessageBuilder func(String funcName) {
        return null;
    }

    @Override
    public MessageBuilder method(UlfUMCMessageType methodType) {
        return null;
    }

    @Override
    public MessageBuilder data(Map<String, Object> data) {
        return null;
    }

    public MessageBuilder error(Object data){
        errorMessage.getUlfUMCBody().getData().put("error",data);
        return this;
    }

    @Override
    public UlfUMCMessage build() {
        return errorMessage;
    }

    @Override
    public byte[] toByte() {
        return UlfUMCMessage.encode(errorMessage);
    }
}
