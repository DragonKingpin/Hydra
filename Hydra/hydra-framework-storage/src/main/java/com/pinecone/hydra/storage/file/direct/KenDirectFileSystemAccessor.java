package com.pinecone.hydra.storage.file.direct;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.ExternalSymbolic;
import com.pinecone.hydra.unit.imperium.entity.EntityNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

public class KenDirectFileSystemAccessor extends ArchDirectFileSystemAccessor implements DirectFileSystemAccessor {

    protected String mszTreeNodeName;

    protected GUID   mTreeNodeGuid;

    public KenDirectFileSystemAccessor( String treeNodeName, GUID treeNodeGuid ) {
        this.mszTreeNodeName = treeNodeName;
        this.mTreeNodeGuid = treeNodeGuid;
    }


    @Override
    public ElementNode queryElement( String path ) {
        File file = new File(path);
        if( file.isDirectory() ){
            return new GenericExternalFolder(file);
        }
        else {
            return new GenericExternalFile(file);
        }
    }



    @Override
    public EntityNode queryNode(String path) {
        File file = new File(path);
        if( file.isDirectory() ){
            return new GenericExternalFolder(file);
        }
        else {
            return new GenericExternalFile(file);
        }
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
