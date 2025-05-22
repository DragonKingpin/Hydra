package com.pinecone.hydra.system.ko.action;

public interface EventObject extends ActionObject {

    String FunctionName = EventObject.class.getSimpleName().replace( "Object", "" );

    @Override
    default String getObjectFunctionName() {
        return FunctionName;
    }


}
