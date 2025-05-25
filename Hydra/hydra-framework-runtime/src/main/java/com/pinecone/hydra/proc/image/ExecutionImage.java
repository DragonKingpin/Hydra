package com.pinecone.hydra.proc.image;

import java.net.URI;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.proc.ControllableLevel;

public interface ExecutionImage extends Pinenut {

    String getName();

    URI getResourceURI();

    Class<?> processMainClass();

    EntryPointRunnable getEntryPoint();

    ImageLoader getImageLoader();

    boolean isReadOnly();

    boolean isReusable();

    String getSignature();

    ControllableLevel getControllableLevel();


}
