package com.walnut.odin.proc;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.RuntimeSystem;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.proc.image.URLImageLoader;
import com.pinecone.hydra.proc.image.kom.ImageElement;
import com.pinecone.hydra.proc.image.kom.VirtualExeImageInstrument;
import com.pinecone.hydra.system.centrum.UniformCentralSystem;
import com.pinecone.hydra.system.imperium.KernelObjectRootMountPoint;
import com.pinecone.hydra.system.ko.kom.KOMInstrument;
import com.pinecone.hydra.unit.imperium.entity.EntityNode;

public abstract class ArchRemoteProcessManagerNode implements RemoteProcessManagerNode {

    protected Logger         mLogger;

    protected ProcessManager mProcessManager;

    protected ArchRemoteProcessManagerNode( ProcessManager processManager ) {
        this.mLogger             = LoggerFactory.getLogger( this.getClass() );
        this.mProcessManager     = processManager;
    }

    @Override
    public URLImageLoader imageLoader() {
        return (URLImageLoader) this.mProcessManager.getImageLoader();
    }

    @Override
    public ProcessManager localProcessManager() {
        return this.mProcessManager;
    }

    @Override
    public RuntimeSystem superiorSystem() {
        return this.mProcessManager.superiorSystem();
    }

    @Override
    public Logger getLogger() {
        return this.mLogger;
    }

    @Override
    public ExecutionImage queryExecutionImage( String path ) {
        ExecutionImage image = this.imageLoader().queryExecutionImage( path );
        if ( image != null ) {
            return image;
        }

        if ( this.superiorSystem() instanceof UniformCentralSystem ) {
            EntityNode e = ((UniformCentralSystem) this.superiorSystem()).imperiumPrivy().getExpressInstrument().queryNode( path );
            if ( e instanceof ImageElement ) {
                return ((ImageElement) e).getImage();
            }
        }

        return null;
    }

    @Override
    public ExecutionImage queryExecutionImage( URI uri ) {
        return this.imageLoader().queryExecutionImage( uri );
    }

    @Override
    public void registerLocalScopeExecutionImage( String dirPath, ExecutionImage image ) {
        this.imageLoader().registerLocalScopeExecutionImage( dirPath, image );
    }

}
