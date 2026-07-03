package com.pinecone.hydra.proc.image;

import java.net.URI;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.proc.ControllableLevel;
import com.pinecone.hydra.proc.UProcess;

public interface ExecutionImage extends Pinenut {

    String getName();

    URI getResourceURI();

    String getImageAddress();

    Class<UProcess> processClassType();

    EntryPointRunnable createEntryPoint();

    ClassLoader getClassLoader();

    ImageLoader getImageLoader();

    boolean isReadOnly();

    boolean isReusable();

    String getSignature();

    ControllableLevel getControllableLevel();

}
