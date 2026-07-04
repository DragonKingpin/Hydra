package com.pinecone.hydra.umc.msg;

/**
 *  Pinecone Ursus For Java UMCProtocol [ Unified Message Control Protocol ]
 *  Author: Harald.E / JH.W (DragonKing)
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 */
public interface UMCProtocol extends MsgProtocol {

    UMCProtocol applyMessageSource( Medium medium ) ;

    String getVersion();

    byte[] getSignature();

    void release();

}
