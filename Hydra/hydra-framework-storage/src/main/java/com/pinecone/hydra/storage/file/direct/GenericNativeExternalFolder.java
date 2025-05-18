package com.pinecone.hydra.storage.file.direct;

import com.pinecone.hydra.storage.file.entity.FileTreeNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class GenericNativeExternalFolder extends ArchNativeExternalFileObject implements ExternalFolder {

    public GenericNativeExternalFolder( File file ){
        super( file );
    }

    @Override
    public String[] list() {
        return this.mNativeFile.list();
    }

    @Override
    public File[] listFiles() {
        return this.mNativeFile.listFiles();
    }

    @Override
    public List<FileTreeNode> listItem() {
        ArrayList<FileTreeNode> fileTreeNodes = new ArrayList<>();
        File[] files = this.listFiles();
        if( files.length > 0 ){
            for( int i = 0;i < files.length; ++i ){
                File file = files[i];
                if( file.isDirectory() ){
                    fileTreeNodes.add( new GenericNativeExternalFolder(file) );
                }
                else {
                    fileTreeNodes.add( new GenericNativeExternalFile( file ) );
                }
            }
        }
        return fileTreeNodes;
    }

    @Override
    public boolean delete() {
        try {
            this.deleteDirectoryRecursively( this.mNativeFile.toPath() );
        }
        catch ( IOException e ) {
            return false;
        }
        return true;
    }

    protected void deleteDirectoryRecursively( Path directory ) throws IOException {
        if ( Files.exists(directory) ) {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

}
