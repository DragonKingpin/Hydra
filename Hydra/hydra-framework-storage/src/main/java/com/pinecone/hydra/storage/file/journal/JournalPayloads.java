package com.pinecone.hydra.storage.file.journal;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.fat.entity.FileChunk;
import com.pinecone.hydra.storage.file.fat.entity.FileChunkLocation;

public final class JournalPayloads {
    private JournalPayloads() {
    }

    public static String fileNode( FileNode fileNode ) {
        if ( fileNode == null ) {
            return null;
        }
        return "{"
                + field( "guid", fileNode.getGuid() )
                + field( "path", fileNode.getPath() )
                + field( "definitionSize", fileNode.getDefinitionSize() )
                + field( "logicSize", fileNode.getLogicSize() )
                + field( "physicalSize", fileNode.getPhysicalSize() )
                + field( "checksum", fileNode.getChecksum() )
                + field( "crc32Xor", fileNode.getCrc32Xor(), true )
                + "}";
    }

    public static String chunk( FileChunk chunk ) {
        if ( chunk == null ) {
            return null;
        }
        return "{"
                + field( "guid", chunk.getGuid() )
                + field( "fileGuid", chunk.getFileGuid() )
                + field( "chunkIndex", chunk.getChunkIndex() )
                + field( "logicalOffset", chunk.getLogicalOffset() )
                + field( "chunkSize", chunk.getChunkSize() )
                + field( "validSize", chunk.getValidSize() )
                + field( "crc32", chunk.getCrc32() )
                + field( "checksum", chunk.getChecksum(), true )
                + "}";
    }

    public static String location( FileChunkLocation location ) {
        if ( location == null ) {
            return null;
        }
        return "{"
                + field( "guid", location.getGuid() )
                + field( "chunkGuid", location.getChunkGuid() )
                + field( "replicaNo", location.getReplicaNo() )
                + field( "volumeGuid", location.getVolumeGuid() )
                + field( "locationType", location.getLocationType() )
                + field( "objectKey", location.getObjectKey() )
                + field( "objectOffset", location.getObjectOffset() )
                + field( "volumeOffset", location.getVolumeOffset() )
                + field( "lengthBytes", location.getLengthBytes() )
                + field( "storageKey", location.getStorageKey() )
                + field( "version", location.getVersion(), true )
                + "}";
    }

    public static String cleanup( FileChunk chunk, FileChunkLocation location ) {
        return "{"
                + field( "chunk", chunk == null ? null : chunk.getGuid() )
                + field( "location", location == null ? null : location.getGuid() )
                + field( "locationType", location == null ? null : location.getLocationType() )
                + field( "objectKey", location == null ? null : location.getObjectKey() )
                + field( "volumeOffset", location == null ? 0L : location.getVolumeOffset() )
                + field( "lengthBytes", location == null ? 0L : location.getLengthBytes(), true )
                + "}";
    }

    protected static String field( String name, Object value ) {
        return field( name, value, false );
    }

    protected static String field( String name, Object value, boolean last ) {
        return "\"" + escape( name ) + "\":" + value( value ) + ( last ? "" : "," );
    }

    protected static String value( Object value ) {
        if ( value == null ) {
            return "null";
        }
        if ( value instanceof Number || value instanceof Boolean ) {
            return String.valueOf( value );
        }
        if ( value instanceof GUID ) {
            return "\"" + escape( String.valueOf( value ) ) + "\"";
        }
        return "\"" + escape( String.valueOf( value ) ) + "\"";
    }

    protected static String escape( String value ) {
        return value == null ? "" : value.replace( "\\", "\\\\" ).replace( "\"", "\\\"" );
    }
}
