package com.pinecone.hydra.storage.file.direct;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.system.ko.handle.ArchKHandle;
import com.pinecone.hydra.unit.imperium.entity.EntityNode;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class NativeDirectFileSystemAccessor extends ArchKHandle implements DirectFileSystemAccessor {

    protected URI    mMountPointURI;

    public NativeDirectFileSystemAccessor( URI mountPointURI, String treeNodeName, GUID treeNodeGuid ) {
        super( treeNodeName, treeNodeGuid );
        this.mMountPointURI  = mountPointURI;
    }

    public NativeDirectFileSystemAccessor( String localFileMountScope, String treeNodeName, GUID treeNodeGuid ) {
        this( URI.create( "file:///" + localFileMountScope ), treeNodeName, treeNodeGuid );
    }

    public NativeDirectFileSystemAccessor( URI mountPointURI ) {
        this( mountPointURI, null, null );
    }

    public NativeDirectFileSystemAccessor( String localFileMountScope ) {
        this( localFileMountScope, null, null );
    }

    @Override
    public ElementNode queryElement( String path ) {
        URI fullURI = this.mMountPointURI.resolve( path );
        File file = new File( fullURI );
        if( file.isDirectory() ){
            return new GenericNativeExternalFolder( file );
        }
        else {
            return new GenericNativeExternalFile( file );
        }
    }

    @Override
    public EntityNode queryNode( String path ) {
        return this.queryElement( path );
    }

    @Override
    public void copy( String sourcePath, String destinationPath ) throws IOException {
        NativeDirectFileSystemAccessors.copy( sourcePath, destinationPath );
    }

    @Override
    public String getName() {
        return this.mszTreeNodeName;
    }

    @Override
    public GUID getGuid() {
        return this.mTreeNodeGuid;
    }
}
