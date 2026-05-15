package com.uofs;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.FileSystemConfig;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.KernelFileSystemConfig;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.fat.entity.FileChunk;
import com.pinecone.hydra.storage.file.fat.entity.FileChunkLocation;
import com.pinecone.hydra.storage.file.transmit.channel.UFileChannel;
import com.pinecone.hydra.storage.file.transmit.channel.UFileOpenOption;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class UofsSmokeTools {
    private UofsSmokeTools() {
    }

    static FileSystemConfig newFileSystemConfig( GUID defaultVolumeGuid ) {
        return newFileSystemConfig( defaultVolumeGuid, UofsSmokePaths.TEMP_FOLDER );
    }

    static FileSystemConfig newFileSystemConfig( GUID defaultVolumeGuid, String tempFolder ) {
        Map<String, Object> config = new HashMap<>();
        config.put( "DefaultVolumeGuid", defaultVolumeGuid.toString() );
        config.put( "DefaultTempFilePath", tempFolder );
        return new KernelFileSystemConfig( config );
    }

    static void cleanupOldUofsFile( KOMFileSystem fileSystem, String uofsPath ) {
        ElementNode oldNode = fileSystem.queryElement( uofsPath );
        if ( oldNode != null ) {
            fileSystem.getFatChunkInstrument().deleteFileChunks( oldNode.getGuid() );
            fileSystem.remove( oldNode.getGuid() );
            Debug.trace( "old uofs file removed", oldNode.getGuid() );
        }
    }

    static FileNode writeFile( KOMFileSystem fileSystem, UofsVolumeFixture fixture, String uofsPath ) throws Exception {
        return writeFile( fileSystem, fixture, uofsPath, UofsSmokePaths.SOURCE_FILE );
    }

    static FileNode writeFile( KOMFileSystem fileSystem, UofsVolumeFixture fixture, String uofsPath, String sourcePath ) throws Exception {
        File sourceFile = new File( sourcePath );
        if ( !sourceFile.exists() ) {
            throw new IllegalStateException( "Source file not found: " + sourceFile.getPath() );
        }
        try ( InputStream inputStream = Files.newInputStream( sourceFile.toPath() );
              UFileChannel channel = fileSystem.open( uofsPath, UFileOpenOption.CREATE_OVERWRITE, fixture.volumeManager ) ) {
            long writtenBytes = channel.write( inputStream, sourceFile.length() );
            Debug.trace( "source file", sourceFile.getPath() );
            Debug.trace( "source size", sourceFile.length() );
            Debug.trace( "written bytes", writtenBytes );
            Debug.trace( "file guid", channel.getFileNode().getGuid() );
            return channel.getFileNode();
        }
    }

    static long readFile( KOMFileSystem fileSystem, UofsVolumeFixture fixture, String uofsPath, File readbackFile ) throws Exception {
        File parent = readbackFile.getParentFile();
        if ( parent != null ) {
            parent.mkdirs();
        }
        if ( readbackFile.exists() ) {
            readbackFile.delete();
        }
        try ( OutputStream outputStream = Files.newOutputStream( readbackFile.toPath() );
              UFileChannel channel = fileSystem.open( uofsPath, UFileOpenOption.READ, fixture.volumeManager ) ) {
            long readBytes = channel.read( outputStream );
            Debug.trace( "read bytes", readBytes );
            Debug.trace( "readback file", readbackFile.getPath() );
            Debug.trace( "readback size", readbackFile.length() );
            return readBytes;
        }
    }

    static void traceFixture( UofsVolumeFixture fixture ) throws Exception {
        Debug.trace( "case", fixture.caseName );
        Debug.trace( "volume guid", fixture.volumeGuid );
        Debug.trace( "logical size", fixture.volumeManager.loadVolume( fixture.volumeGuid ).getLogicalSize() );
        Debug.trace( "committed bytes", fixture.volumeManager.loadVolume( fixture.volumeGuid ).getCommittedBytes() );
        if ( fixture.objectRoot != null ) {
            Debug.trace( "object root", fixture.objectRoot.getPath() );
        }
        for ( File physicalFile : fixture.physicalFiles ) {
            Debug.trace( "physical file", physicalFile.getPath(), "length", physicalFile.exists() ? physicalFile.length() : 0L );
        }
    }

    static void traceFatLayout( KOMFileSystem fileSystem, FileNode fileNode ) {
        List<FileChunk> chunks = fileSystem.getFatChunkInstrument().fetchChunks( fileNode.getGuid() );
        Debug.trace( "chunk count", chunks.size() );
        for ( FileChunk chunk : chunks ) {
            FileChunkLocation location = fileSystem.getFatChunkInstrument().getReadyLocation( chunk.getGuid() );
            Debug.trace(
                    "chunk",
                    chunk.getChunkIndex(),
                    "logicalOffset",
                    chunk.getLogicalOffset(),
                    "validSize",
                    chunk.getValidSize(),
                    "locationType",
                    location == null ? null : location.getLocationType(),
                    "objectKey",
                    location == null ? null : location.getObjectKey(),
                    "volumeOffset",
                    location == null ? null : location.getVolumeOffset()
            );
        }
    }

    static void verifySize( long expectedSize, long readBytes, File readbackFile ) {
        if ( readBytes != expectedSize || readbackFile.length() != expectedSize ) {
            throw new IllegalStateException(
                    "UOFS smoke size mismatch, expected=" + expectedSize
                            + ", read=" + readBytes
                            + ", readback=" + readbackFile.length()
            );
        }
    }
}
