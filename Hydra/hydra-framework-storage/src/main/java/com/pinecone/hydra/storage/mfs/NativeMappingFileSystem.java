package com.pinecone.hydra.storage.mfs;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.external.GenericNativeExternalFile;
import com.pinecone.hydra.storage.file.external.GenericNativeExternalFolder;
import com.pinecone.hydra.storage.natives.NativeExternalFileSystems;
import com.pinecone.hydra.system.ko.handle.ArchKHandle;
import com.pinecone.hydra.unit.imperium.entity.EntityNode;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class NativeMappingFileSystem extends ArchKHandle implements MappingFileSystem {

    protected URI    mMountPointURI;

    public NativeMappingFileSystem( URI mountPointURI, String treeNodeName, GUID treeNodeGuid ) {
        super( treeNodeName, treeNodeGuid );
        this.mMountPointURI  = mountPointURI;
    }

    public NativeMappingFileSystem( String localFileMountScope, String treeNodeName, GUID treeNodeGuid ) {
        this( URI.create( "file:///" + localFileMountScope ), treeNodeName, treeNodeGuid );
    }

    public NativeMappingFileSystem( URI mountPointURI ) {
        this( mountPointURI, null, null );
    }

    public NativeMappingFileSystem( String localFileMountScope ) {
        this( localFileMountScope, null, null );
    }

    @Override
    public EntityNode queryNode( String path ) {
        URI fullURI = this.mMountPointURI.resolve( path );
        return new GenericNativeMFile( fullURI );
    }

    @Override
    public void copy( String sourcePath, String destinationPath ) throws IOException {
        NativeExternalFileSystems.copy( sourcePath, destinationPath );
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
