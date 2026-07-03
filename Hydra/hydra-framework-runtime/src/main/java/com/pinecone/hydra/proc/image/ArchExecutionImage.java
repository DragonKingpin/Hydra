package com.pinecone.hydra.proc.image;

import java.net.URI;

import com.pinecone.hydra.proc.ControllableLevel;
import com.pinecone.hydra.proc.UProcess;

public abstract class ArchExecutionImage implements ExecutionImage {

    protected String                        mszName;
    protected URI                           mResourceURI;
    protected String                        mszImageAddress;
    protected Class<? extends UProcess>     mProcessClassType;
    protected ClassLoader                   mClassLoader;
    protected EntryPointRunnableFactory     mEntryPointFactory;
    protected ImageLoader                   mImageLoader;
    protected boolean                       mbReadOnly;
    protected boolean                       mbReusable;
    protected String                        mszSignature;
    protected ControllableLevel             mControllableLevel;

    protected ArchExecutionImage(
            String name, EntryPointRunnableFactory entryPointFactory, Class<? extends UProcess> processClassType,
            URI resourceURI, ClassLoader classLoader, ImageLoader imageLoader,
            boolean readOnly, boolean reusable, String signature,
            ControllableLevel controllableLevel
    ) {
        this.mszName            = name;
        this.mEntryPointFactory = entryPointFactory;
        this.mProcessClassType  = processClassType;
        this.mResourceURI       = resourceURI;
        this.mClassLoader       = classLoader;
        this.mImageLoader       = imageLoader;
        this.mbReadOnly         = readOnly;
        this.mbReusable         = reusable;
        this.mszSignature       = signature;
        this.mControllableLevel = controllableLevel;
    }

    protected ArchExecutionImage(
            String name, EntryPointRunnableFactory entryPointFactory, Class<? extends UProcess> processClassType,
            URI resourceURI, ImageLoader imageLoader, String signature, ControllableLevel controllableLevel
    ) {
        this( name, entryPointFactory, processClassType, resourceURI, imageLoader.getClassLoader(), imageLoader,
              true, true, signature, controllableLevel
        );
    }

    @Override
    public String getName() {
        return this.mszName;
    }

    @Override
    public URI getResourceURI() {
        return this.mResourceURI;
    }

    protected void  applyImageAddress( String address ) {
        this.mszImageAddress = address;
    }

    @Override
    public String getImageAddress() {
        return this.mszImageAddress;
    }

    @Override
    public Class<UProcess> processClassType() {
        return (Class<UProcess>) this.mProcessClassType;
    }

    @Override
    public EntryPointRunnable createEntryPoint() {
        if ( this.mEntryPointFactory == null ) {
            throw new IllegalStateException( "Execution image entry point factory is not available." );
        }

        EntryPointRunnable entryPoint = this.mEntryPointFactory.create();
        if ( entryPoint == null ) {
            throw new IllegalStateException( "Execution image entry point factory returned null." );
        }
        return entryPoint;
    }

    @Override
    public ClassLoader getClassLoader() {
        return this.mClassLoader;
    }

    @Override
    public ImageLoader getImageLoader() {
        return this.mImageLoader;
    }

    @Override
    public boolean isReadOnly() {
        return this.mbReadOnly;
    }

    @Override
    public boolean isReusable() {
        return this.mbReusable;
    }

    @Override
    public String getSignature() {
        return this.mszSignature;
    }

    @Override
    public ControllableLevel getControllableLevel() {
        return this.mControllableLevel;
    }

}
