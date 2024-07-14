package com.genius.common.UlfUMC;



import java.util.Map;

public interface MessageBuilder {


    MessageBuilder func(String funcName);

    MessageBuilder method(UlfUMCMessageType methodType);

    MessageBuilder data(Map<String,Object> data);

    UlfUMCMessage build();

    byte[] toByte();
}
