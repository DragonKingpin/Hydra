package com.walnut.odin.proc;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.system.RuntimeSystem;
import com.pinecone.framework.system.Unsafe;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.UProcessStatus;
import com.pinecone.hydra.proc.event.ProcessLifecycleHandler;
import com.pinecone.hydra.proc.image.EntryPointRunnable;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.proc.image.URLImageLoader;
import com.pinecone.hydra.proc.image.kom.ImageElement;
import com.pinecone.hydra.system.centrum.UniformCentralSystem;
import com.pinecone.hydra.unit.imperium.entity.EntityNode;
import com.walnut.odin.proc.client.RemoteProcessManagerClient;

public abstract class ArchRemoteProcessManagerNode implements RemoteProcessManagerNode {

    protected Logger                         mLogger;

    protected ProcessManager                 mProcessManager;

    protected List<ProcessLifecycleHandler>  mLifecycleHandlers;

    protected ReadWriteLock                  mnClientLock = new ReentrantReadWriteLock();

    protected ArchRemoteProcessManagerNode( ProcessManager processManager ) {
        this.mLogger             = LoggerFactory.getLogger( this.getClass() );
        this.mProcessManager     = processManager;
        this.mLifecycleHandlers  = new ArrayList<>();
    }

    @Override
    public URLImageLoader imageLoader() {
        return (URLImageLoader) this.mProcessManager.getImageLoader();
    }

    @Override
    public GuidAllocator getGuidAllocator() {
        return this.mProcessManager.getGuidAllocator();
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

    @Override
    public UProcess getProcess( GUID pid ) {
        return this.mProcessManager.getProcess( pid );
    }

    @Override
    public boolean hasOwnProcess( GUID pid ) {
        UProcess process = this.mProcessManager.getProcess( pid );
        if ( process instanceof RemoteProcess) {
            return false;
        }

        return process != null;
    }

    @Override
    public boolean containProcess( GUID pid ) {
        return this.mProcessManager.containProcess( pid );
    }

    @Override
    public Collection<UProcess> searchProcessesByName( String procName ) {
        return this.mProcessManager.searchProcessesByName( procName );
    }

    @Override
    public Collection<UProcess> searchProcessesByNameNoCase( String procName ) {
        return this.mProcessManager.searchProcessesByNameNoCase( procName );
    }

    protected void afterMediatedRemoteProcess(
            MediatedRemoteProcess process, String imageAddress, boolean isURI, RemoteImageResolutionMode imageResolutionMode
    ) {
        if ( imageAddress == null || imageAddress.isEmpty() ) {
            throw new IllegalStateException( "[MirrorCompromised] image address is required for mediated remote process." );
        }

        this.notifyProcessLifecycleHandlers( imageAddress, null, UProcessStatus.Preparing );

        ExecutionImage image;
        if ( isURI ) {
            image = this.queryExecutionImage( URI.create( imageAddress ) );
        }
        else {
            image = this.queryExecutionImage( imageAddress );
        }

        if ( image == null ) {
            if ( imageResolutionMode != RemoteImageResolutionMode.REMOTE_CLIENT_IMAGE ) {
                throw new IllegalStateException( "[MirrorCompromised] `" + imageAddress + "` is not a valid image address." );
            }
            else {
                this.getLogger().info( "[Notice] [MirrorAsymmetric] `{}` is not accessible in this server.", imageAddress );
                image = new RemoteSurrogateExecutionImage( imageAddress, this.imageLoader() );
            }
        }
        this.mProcessManager.getImageModifier().applyImageAddress( image, imageAddress );
        process.mExecutionImage = image;

        process.mszImageAddress = imageAddress;
        process.mImageResolutionMode = imageResolutionMode;
        if ( process.mImageResolutionMode == null ) {
            process.mImageResolutionMode = RemoteImageResolutionMode.REQUIRE_SERVER_IMAGE;
        }
        process.mProcessManager = this.mProcessManager;
    }


    @Override
    public RemoteProcessManagerNode addProcessLifecycleHandler(ProcessLifecycleHandler handler ) {
        this.mnClientLock.writeLock().lock();
        try {
            this.mLifecycleHandlers.add( handler );
            return this;
        }
        finally {
            this.mnClientLock.writeLock().unlock();
        }
    }

    @Override
    public RemoteProcessManagerNode removeProcessLifecycleHandler( ProcessLifecycleHandler handler ) {
        this.mnClientLock.writeLock().lock();
        try {
            this.mLifecycleHandlers.remove( handler );
            return this;
        }
        finally {
            this.mnClientLock.writeLock().unlock();
        }
    }

    @Override
    public int getProcessLifecycleHandlersSize() {
        this.mnClientLock.readLock().lock();
        try {
            return this.mLifecycleHandlers.size();
        }
        finally {
            this.mnClientLock.readLock().unlock();
        }
    }

    @Override
    @Unsafe
    public void notifyProcessLifecycleHandlers( String imageAddress, EntryPointRunnable runnable, UProcessStatus event ) {
        this.mnClientLock.readLock().lock();
        try {
            for ( ProcessLifecycleHandler handler : this.mLifecycleHandlers ) {
                handler.fired( imageAddress, runnable, event );
            }
        }
        finally {
            this.mnClientLock.readLock().unlock();
        }
    }

}
