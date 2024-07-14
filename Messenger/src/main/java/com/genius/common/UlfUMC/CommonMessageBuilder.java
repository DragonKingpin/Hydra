package com.genius.common.UlfUMC;


import java.util.Map;

/**
 * @author Genius
 * @date 2023/05/14 20:40
 **/
public class CommonMessageBuilder implements MessageBuilder{

    private UlfUMCMessage message;

    public CommonMessageBuilder(){
        message = new UlfUMCMessage();
    }

    public UlfUMCMessage getMessage(){
        return message;
    }

    @Override
    public MessageBuilder func(String funcName) {
        message.getUlfUMCBody().setFunction(funcName);
        return this;
    }

    @Override
    public MessageBuilder method(UlfUMCMessageType methodType) {
        message.getUlfUMCBody().setMethod(methodType);
        return this;
    }

    @Override
    public MessageBuilder data(Map<String,Object> data) {
        message.getUlfUMCBody().setData(data);
        return this;
    }

    @Override
    public UlfUMCMessage build() {
        return this.message;
    }

    @Override
    public byte[] toByte() {
        return UlfUMCMessage.encode(message);
    }
}
