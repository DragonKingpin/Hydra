package com.pinecone.hydra.umc.msg;

import java.io.IOException;
import java.util.Map;

public interface UMCReceiver extends UMCProtocol{
    Map<String, Object > readPutMsg() throws IOException;

    UMCMessage readPostMsg() throws IOException;

    UMCMessage readPostMsgBytes() throws IOException;

    UMCMessage readMsg() throws IOException;

    UMCMessage readMsgBytes() throws IOException;
}
