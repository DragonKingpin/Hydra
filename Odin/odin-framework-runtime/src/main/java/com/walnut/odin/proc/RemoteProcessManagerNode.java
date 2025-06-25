package com.walnut.odin.proc;

import java.net.URI;
import java.util.Collection;

import com.pinecone.framework.system.RuntimeSystem;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.proc.image.URLImageLoader;
import com.pinecone.hydra.system.component.Slf4jTraceable;
import com.walnut.odin.proc.dto.UProcessRuntimeMeta;

public interface RemoteProcessManagerNode extends Slf4jTraceable {

    void startService () throws RemoteProcessServiceRPCException;

    void terminateService () throws IllegalStateException;

    GuidAllocator getGuidAllocator();

    ProcessManager localProcessManager();

    URLImageLoader imageLoader();

    ExecutionImage queryExecutionImage( String path );

    ExecutionImage queryExecutionImage( URI uri );

    RuntimeSystem superiorSystem();

    void registerLocalScopeExecutionImage ( String dirPath, ExecutionImage image );

    void register( UProcess that );

    void erase( UProcess that );

    UProcess getProcess( GUID pid );

    /**
     * Checks only whether the current node directly owns the specified process, without involving any child nodes or proxy mirrors.
     * 仅检查当前节点自身是否直接持有该进程，不涉及任何下级节点或代理镜像。
     */
    boolean hasOwnProcess( GUID pid );

    /**
     * Determines whether the specified process exists in the current node or any of its child nodes.
     * 判断当前节点或其下级节点中是否存在指定进程。
     */
    boolean containProcess( GUID pid );

    UProcessRuntimeMeta queryProcessRuntimeMeta( GUID pid ) throws RemoteProcessLifecycleException ;

    Collection<UProcess> searchProcessesByName( String procName ) ;

    Collection<UProcess> searchProcessesByNameNoCase( String procName );


}
