package com.pinecone.framework.system;

public interface AsynSystem extends RuntimeSystem {

    void handleAsynLiveException( Exception e ) throws ProvokeHandleException;

    void handleAsynKillException( Exception e ) throws ProvokeHandleException;

}
