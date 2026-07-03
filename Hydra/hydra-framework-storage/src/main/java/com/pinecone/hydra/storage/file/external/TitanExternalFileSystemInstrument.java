package com.pinecone.hydra.storage.file.external;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.name.path.PathResolver;
import com.pinecone.hydra.storage.StorageConstants;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.ExternalSymbolic;
import com.pinecone.hydra.storage.file.source.ExternalSymbolicManipulator;
import com.pinecone.hydra.storage.file.source.FileManipulator;
import com.pinecone.hydra.storage.file.source.FileMasterManipulator;
import com.pinecone.hydra.storage.file.source.FolderManipulator;
import com.pinecone.hydra.storage.natives.NativeExternalFileSystems;
import com.pinecone.hydra.system.identifier.KOPathResolver;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.system.ko.kom.PathSelector;
import com.pinecone.hydra.unit.imperium.ImperialTree;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

public class TitanExternalFileSystemInstrument implements ExternalFileSystemInstrument {
    protected KOMFileSystem                 fileSystem;

    protected PathResolver                  pathResolver;

    protected PathSelector                  pathSelector;

    protected FileMasterManipulator         fileMasterManipulator;

    protected FolderManipulator             folderManipulator;

    protected FileManipulator               fileManipulator;

    protected ExternalSymbolicManipulator   externalSymbolicManipulator;

    protected ImperialTree                  imperialTree;


    public TitanExternalFileSystemInstrument( KOMFileSystem fileSystem ){
        this.fileSystem                     = fileSystem;
        this.pathResolver                   = new KOPathResolver( fileSystem.getConfig() );
        this.fileMasterManipulator          = this.fileSystem.getFileMasterManipulator();
        this.fileManipulator                = this.fileMasterManipulator.getFileManipulator();
        this.folderManipulator              = this.fileMasterManipulator.getFolderManipulator();
        this.externalSymbolicManipulator    = this.fileMasterManipulator.getExternalSymbolicManipulator();
        this.imperialTree                   = fileSystem.getMasterTrieTree();

        this.pathSelector                   = new TitanExternalSymbolicSelector(
                this.pathResolver, this.fileSystem.getMasterTrieTree(),this.folderManipulator, new GUIDNameManipulator[] { this.fileManipulator },
                this.externalSymbolicManipulator
        );
    }

    @Override
    public ElementNode queryElement( String path ) {
        Path nativePath = this.resolveNativePath( path );
        if ( nativePath == null ) {
            return null;
        }

        File file = nativePath.toFile();
        if( file.isDirectory() ){
            return new GenericNativeExternalFolder( file );
        }
        else {
            return new GenericNativeExternalFile( file );
        }
    }

    @Override
    public ExternalFolder affirmFolder( String path ) {
        Path nativePath = this.resolveNativePath( path );
        if ( nativePath == null ) {
            return null;
        }
        try {
            Files.createDirectories( nativePath );
        }
        catch ( IOException e ) {
            throw new IllegalStateException( "Failed to create native external folder: " + path, e );
        }
        return new GenericNativeExternalFolder( nativePath.toFile() );
    }

    @Override
    public boolean remove( String path ) {
        ElementNode node = this.queryElement( path );
        if ( node instanceof ExternalFile ) {
            ExternalFile file = (ExternalFile) node;
            if ( !file.exists() ) {
                return false;
            }
            return file.delete();
        }
        if ( node instanceof ExternalFolder ) {
            ExternalFolder folder = (ExternalFolder) node;
            if ( !folder.getNativeFile().exists() ) {
                return false;
            }
            return folder.delete();
        }
        return false;
    }

    protected Path resolveNativePath( String path ) {
        GUID guid = this.queryGUIDByPath(path);
        if( guid == null ) {
            return null;
        }
        ExternalSymbolic externalSymbolic = this.externalSymbolicManipulator.getSymbolicByGuid(guid);
        if ( externalSymbolic == null ) {
            return null;
        }
        String separator = this.fileSystem.getConfig().getPathNameSeparator();
        String externalPath = this.normalizeUofsPath( this.fileSystem.getPath( externalSymbolic.getGuid() ) );
        String logicalPath = this.normalizeUofsPath( path );
        String remainingPath = this.resolveRemainingPath( logicalPath, externalPath, separator );
        return this.resolveNativePath( externalSymbolic.getReparsedPoint(), remainingPath, separator );
    }

    @Override
    public void insertExternalSymbolic( ExternalSymbolic externalSymbolic ) {
        this.externalSymbolicManipulator.insert( externalSymbolic );
    }


    @Override
    public void createExternalSymbolic( String folderPath, String externalSymbolicName, String reparsedPoint ) {
        ElementNode elementNode = this.fileSystem.queryElement(folderPath);
        elementNode.evinceFolder().createExternalSymbolic( externalSymbolicName,reparsedPoint );
    }

    @Override
    public void copy( String sourcePath, String destinationPath ) throws IOException {
        NativeExternalFileSystems.copy( sourcePath, destinationPath );
    }

    private GUID queryGUIDByPath( String path ) {
        return this.queryGUIDByNS( path, null, null );
    }

    private GUID queryGUIDByNS( String path, String szBadSep, String szTargetSep ) {
        if( szTargetSep != null ) {
            path = path.replace( szBadSep, szTargetSep );
        }

        String[] parts = this.pathResolver.segmentPathParts( path );
        List<String > resolvedParts = this.pathResolver.resolvePath( parts );
        path = this.pathResolver.assemblePath( resolvedParts );

        GUID guid = this.imperialTree.queryGUIDByPath( path );
        if ( guid != null ){
            return guid;
        }


        guid = this.pathSelector.searchGUID( resolvedParts );
        return guid;
    }

    protected String trimLeadingSeparator( String path, String separator ) {
        String ret = path == null ? "" : path;
        while ( ret.startsWith( separator ) ) {
            ret = ret.substring( separator.length() );
        }
        return ret;
    }

    protected String normalizeUofsPath( String path ) {
        String[] parts = this.pathResolver.segmentPathParts( path );
        List<String > resolvedParts = this.pathResolver.resolvePath( parts );
        return this.pathResolver.assemblePath( resolvedParts );
    }

    protected String resolveRemainingPath( String logicalPath, String externalPath, String separator ) {
        if ( logicalPath == null || logicalPath.isBlank() || externalPath == null || externalPath.isBlank() ) {
            return "";
        }
        if ( logicalPath.equals( externalPath ) ) {
            return "";
        }
        String prefix = externalPath + separator;
        if ( logicalPath.startsWith( prefix ) ) {
            return logicalPath.substring( prefix.length() );
        }
        return "";
    }

    protected Path resolveNativePath( String rootPath, String logicalPath, String separator ) {
        Path ret = Paths.get( rootPath );
        if ( logicalPath == null || logicalPath.isBlank() ) {
            return ret;
        }
        for ( String part : logicalPath.split( Pattern.quote( separator ) ) ) {
            if ( part == null || part.isBlank() ) {
                continue;
            }
            ret = ret.resolve( part );
        }
        return ret;
    }

}
