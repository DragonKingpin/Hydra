package com.genius.common.UlfUMC;

/**
 * @author Genius
 * @date 2023/05/14 20:42
 **/
public class MessageFactory{

    public enum MessageBuilderType{
        COMMON,
        SLAVE,
        ERROR
    }

    public static MessageBuilder getMessageBuilder(MessageBuilderType builderType){
        switch (builderType){
            case SLAVE:return new SlaveMessageBuilder();
            case ERROR: return new ErrorMessageBuilder();
            default:return new CommonMessageBuilder();
        }
    }
}
