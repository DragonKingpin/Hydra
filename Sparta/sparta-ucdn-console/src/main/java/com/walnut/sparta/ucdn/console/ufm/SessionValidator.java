package com.walnut.sparta.ucdn.console.ufm;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umb.UMBServiceException;
import com.pinecone.hydra.umct.stereotype.Iface;

import java.io.IOException;

@Iface
public interface SessionValidator extends Pinenut {

    void stageClusterGroupComplete( String path ) throws IOException;

    void stageFileTransmitComplete( String path ) throws IOException;

    void fileTransmitComplete( String path, String serviceId ) throws IOException;

    void start() throws UMBServiceException;

    void shutdown();

    boolean hasStarted();

}
