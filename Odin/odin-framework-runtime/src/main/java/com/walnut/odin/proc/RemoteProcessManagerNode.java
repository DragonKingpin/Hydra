package com.walnut.odin.proc;

import java.net.URI;

import com.pinecone.framework.system.RuntimeSystem;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.proc.image.URLImageLoader;
import com.pinecone.hydra.system.component.Slf4jTraceable;

public interface RemoteProcessManagerNode extends Slf4jTraceable {

    void startService () throws RemoteProcessServiceRPCException;

    void terminateService () throws IllegalStateException;

    ProcessManager localProcessManager();

    URLImageLoader imageLoader();

    ExecutionImage queryExecutionImage( String path );

    ExecutionImage queryExecutionImage( URI uri );

    RuntimeSystem superiorSystem();

    void registerLocalScopeExecutionImage ( String dirPath, ExecutionImage image );

}
