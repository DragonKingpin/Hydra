package com.pinecone.hydra.umb.broadcast;

import com.pinecone.framework.system.prototype.Pinenut;

/**
 *  Pinecone Ursus For Java [ Uniform Namespaced Topic ]
 *  Author: Harald.E (Dragon King), Ken
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  *****************************************************************************************
 *  Topic + Namespace
 *  *****************************************************************************************
 */
public interface UNT extends Pinenut {
    String getTopic();

    String getNamespace();

    String[] getNameSegments();
}
