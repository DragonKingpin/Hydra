package com.pinecone.hydra.storage.mfs;

import java.io.File;
import java.net.URI;

import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;
import com.pinecone.hydra.system.ko.handle.ArchKHandle;

public class GenericNativeMFile extends ArchKHandle implements NativeMFile {
    protected File mNativeFile;

    public GenericNativeMFile( File file ) {
        this.mNativeFile  = file;
    }

    public GenericNativeMFile( URI uri ) {
        this( new File( uri ) );
    }


    @Override
    public URI toURI() {
        return this.mNativeFile.toURI();
    }

    @Override
    public String getName() {
        return this.mNativeFile.getName();
    }

    @Override
    public Number size() {
        return this.mNativeFile.length();
    }

    @Override
    public String getURI() {
        return this.toURI().toString();
    }

    @Override
    public String getPath() {
        return this.mNativeFile.getPath();
    }

    @Override
    public boolean delete() {
        return this.mNativeFile.delete();
    }

    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    @Override
    public File getNativeHandle() {
        return this.mNativeFile;
    }

    @Override
    public boolean exists() {
        return this.mNativeFile.exists();
    }

    @Override
    public boolean isAbsolute() {
        return this.mNativeFile.isAbsolute();
    }

    @Override
    public boolean isDirectory() {
        return this.mNativeFile.isDirectory();
    }

    @Override
    public MFile[] listFiles() {
        File[] files = this.mNativeFile.listFiles();
        if( files == null ){
            return new MFile[0];
        }
        MFile[] mFiles = new MFile[ files.length ];
        for( int i = 0; i < files.length; ++i ){
            mFiles[i] = new GenericNativeMFile( files[i] );
        }
        return mFiles;
    }

    @Override
    public String getMetaType() {
        return MetaType;
    }

}
