package com.uofs;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.bucket.Bucket;
import com.pinecone.hydra.storage.bucket.BucketInstrument;
import com.pinecone.hydra.storage.bucket.GenericBucket;
import com.pinecone.hydra.storage.bucket.TitanBucketInstrument;
import com.pinecone.hydra.storage.file.FileSystemConfig;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.KernelFileSystemConfig;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.fat.entity.FileChunk;
import com.pinecone.hydra.storage.file.fat.entity.FileChunkLocation;
import com.pinecone.hydra.storage.file.transmit.channel.InternalUFileChannel;
import com.pinecone.hydra.storage.file.transmit.channel.UFileChannel;
import com.pinecone.hydra.storage.file.transmit.channel.UFileOpenOption;
import com.pinecone.hydra.storage.file.source.FileMasterManipulator;
import com.pinecone.ulf.util.guid.GUIDs;

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

    static void cleanupOldUofsFile( KOMFileSystem fileSystem, UofsVolumeFixture fixture, String uofsPath ) {
        ElementNode oldNode = fileSystem.queryElement( uofsPath );
        if ( oldNode != null ) {
            fileSystem.remove( uofsPath, fixture.volumeManager );
            Debug.trace( "old uofs file removed", oldNode.getGuid() );
        }
    }

    static void ensureBucket( UofsSmokeContext context, String bucketName, GUID volumeGuid, String seed ) {
        BucketInstrument bucketInstrument = new TitanBucketInstrument(
                ( (FileMasterManipulator) context.fileMappingDriver.getMasterManipulator() ).getBucketManipulator()
        );
        Bucket bucket = bucketInstrument.getByUserIdentifierAndBucket( "root", bucketName );
        if ( bucket == null ) {
            GUID bucketGuid = GUIDs.GUID128( "01990000-0000-" + seed + "-8000-000000000701" );
            bucketInstrument.insert( new GenericBucket( bucketGuid, "root", bucketName, volumeGuid ) );
            Debug.trace( "bucket created", "root@" + bucketName, bucketGuid, "volume", volumeGuid );
            return;
        }
        if ( volumeGuid.equals( bucket.getVolumeGuid() ) ) {
            Debug.trace( "bucket ready", "root@" + bucketName, bucket.getGuid(), "volume", bucket.getVolumeGuid() );
            return;
        }
        bucketInstrument.bindVolume( bucket.getGuid(), volumeGuid );
        Debug.trace( "bucket rebound", "root@" + bucketName, bucket.getGuid(), "volume", volumeGuid );
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
              InternalUFileChannel channel = (InternalUFileChannel) fileSystem.open( uofsPath, UFileOpenOption.CREATE_OVERWRITE, fixture.volumeManager ) ) {
            long writtenBytes = channel.write( inputStream, sourceFile.length() );
            Debug.trace( "source file", sourceFile.getPath() );
            Debug.trace( "source size", sourceFile.length() );
            Debug.trace( "written bytes", writtenBytes );
            Debug.trace( "file guid", channel.getFile().getGuid() );
            return channel.getFile();
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
