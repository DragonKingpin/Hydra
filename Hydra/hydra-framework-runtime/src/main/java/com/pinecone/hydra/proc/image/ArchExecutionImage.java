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
    protected EntryPointRunnable            mEntryPoint;
    protected ImageLoader                   mImageLoader;
    protected boolean                       mbReadOnly;
    protected boolean                       mbReusable;
    protected String                        mszSignature;
    protected ControllableLevel             mControllableLevel;

    protected ArchExecutionImage(
            String name, EntryPointRunnable entryPoint, Class<? extends UProcess> processClassType,
            URI resourceURI, ClassLoader classLoader, ImageLoader imageLoader,
            boolean readOnly, boolean reusable, String signature,
            ControllableLevel controllableLevel
    ) {
        this.mszName            = name;
        this.mEntryPoint        = entryPoint;
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
            String name, EntryPointRunnable entryPoint, Class<? extends UProcess> processClassType, URI resourceURI, ImageLoader imageLoader, String signature, ControllableLevel controllableLevel
    ) {
        this( name, entryPoint, processClassType, resourceURI, imageLoader.getClassLoader(), imageLoader,
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
        return null;
    }

    @Override
    public EntryPointRunnable getEntryPoint() {
        return this.mEntryPoint;
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

    @Override
    public ExecutionImage clone() {
        try {
            ArchExecutionImage that = (ArchExecutionImage) super.clone();
            if ( this.mEntryPoint != null ) {
                that.mEntryPoint = this.mEntryPoint.clone();
            }
            return that;
        }
        catch ( CloneNotSupportedException e ) {
            throw new IllegalStateException( "Execution image is not cloneable.", e );
        }
    }

}
