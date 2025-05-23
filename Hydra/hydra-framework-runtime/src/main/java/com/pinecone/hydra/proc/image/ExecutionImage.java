package com.pinecone.hydra.proc.image;

import java.net.URI;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ExecutionImage extends Pinenut {

    String getName();

    URI getResourceURI();

    ImageLoader getImageLoader();

    boolean isReadOnly();

    boolean isReusable();

    String getSignature();


}
