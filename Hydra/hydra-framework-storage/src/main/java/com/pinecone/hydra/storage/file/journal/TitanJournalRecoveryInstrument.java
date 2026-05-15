package com.pinecone.hydra.storage.file.journal;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.fat.FatChunkInstrument;
import com.pinecone.hydra.storage.file.fat.entity.FileChunkLocationType;
import com.pinecone.hydra.storage.file.fat.entity.GenericFileChunkLocation;
import com.pinecone.hydra.storage.file.fat.io.FatChunkStore;
import com.pinecone.hydra.storage.file.journal.source.JournalItemManipulator;
import com.pinecone.hydra.storage.file.journal.source.JournalManipulator;
import com.pinecone.hydra.storage.file.source.FileManipulator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class TitanJournalRecoveryInstrument implements JournalRecoveryInstrument {
    protected final JournalInstrument       mJournalInstrument;
    protected final JournalManipulator      mJournalManipulator;
    protected final JournalItemManipulator  mJournalItemManipulator;
    protected final FatChunkInstrument      mFatChunkInstrument;
    protected final FatChunkStore           mFatChunkStore;
    protected final FileManipulator         mFileManipulator;
    protected final JournalPayloadParser    mPayloadParser = new JournalPayloadParser();

    public TitanJournalRecoveryInstrument( JournalInstrument journalInstrument ) {
        this( journalInstrument, null, null, null, null, null );
    }

    public TitanJournalRecoveryInstrument(
            JournalInstrument journalInstrument,
            JournalManipulator journalManipulator,
            JournalItemManipulator journalItemManipulator,
            FatChunkInstrument fatChunkInstrument,
            FatChunkStore fatChunkStore,
            FileManipulator fileManipulator
    ) {
        this.mJournalInstrument = journalInstrument;
        this.mJournalManipulator = journalManipulator;
        this.mJournalItemManipulator = journalItemManipulator;
        this.mFatChunkInstrument = fatChunkInstrument;
        this.mFatChunkStore = fatChunkStore;
        this.mFileManipulator = fileManipulator;
    }

    @Override
    public void recover() {
        for ( Journal journal : this.mJournalInstrument.listRecoverable() ) {
            Debug.trace( "uofs journal recoverable", journal.getGuid(), journal.getJournalType(), journal.getJournalStatus() );
            this.recover( journal );
        }
    }

    protected void recover( Journal journal ) {
        if ( this.mJournalManipulator == null || this.mJournalItemManipulator == null ) {
            return;
        }
        if ( journal.getJournalStatus() == JournalStatus.PREPARED ) {
            this.mJournalManipulator.updateStatus( journal.getGuid(), JournalStatus.ABORTED, "Auto recovery aborted prepared journal" );
            return;
        }
        if ( journal.getJournalStatus() == JournalStatus.WRITING ) {
            this.rollbackWriting( journal );
            return;
        }
        if ( journal.getJournalStatus() == JournalStatus.COMMITTING ) {
            this.rollForwardCommitting( journal );
        }
    }

    protected void rollbackWriting( Journal journal ) {
        List<GenericJournalItem> items = this.mJournalItemManipulator.listByJournalGuid( journal.getGuid() );
        Collections.reverse( items );
        for ( GenericJournalItem item : items ) {
            if ( item.getItemStatus() != JournalItemStatus.APPLIED ) {
                continue;
            }
            try {
                this.rollbackItem( item );
            }
            catch ( Exception e ) {
                Debug.trace( "uofs journal rollback item failed", journal.getGuid(), item.getGuid(), item.getItemType(), e.getMessage() );
                this.mJournalManipulator.updateStatus( journal.getGuid(), JournalStatus.FAILED, e.getMessage() );
                return;
            }
        }
        this.mJournalManipulator.updateStatus( journal.getGuid(), JournalStatus.ABORTED, "Auto recovery rolled back writing journal" );
    }

    protected void rollbackItem( GenericJournalItem item ) throws IOException {
        if ( item.getItemType() == JournalItemType.OBJECT ) {
            if ( this.mFatChunkStore == null ) {
                Debug.trace( "uofs journal rollback object skipped without chunk store", item.getObjectKey() );
                return;
            }
            this.mFatChunkStore.delete( this.toLocation( item, FileChunkLocationType.VOLUME_DIRECT_OBJECT ) );
            return;
        }
        if ( item.getItemType() == JournalItemType.LOCATION ) {
            if ( this.mFatChunkInstrument == null ) {
                return;
            }
            this.mFatChunkInstrument.deleteLocation( item.getLocationGuid() );
            return;
        }
        if ( item.getItemType() == JournalItemType.CHUNK ) {
            if ( this.mFatChunkInstrument == null ) {
                return;
            }
            this.mFatChunkInstrument.deleteChunk( item.getChunkGuid() );
            return;
        }
        if ( item.getItemType() == JournalItemType.FILE_NODE ) {
            this.applyFileNodePayload( item.getFileGuid(), item.getOldPayload() );
            return;
        }
        if ( item.getItemType() == JournalItemType.VOLUME_EXTENT ) {
            Debug.trace( "uofs journal rollback skip block extent", item.getVolumeGuid(), item.getVolumeOffset(), item.getLengthBytes() );
        }
    }

    protected void rollForwardCommitting( Journal journal ) {
        List<GenericJournalItem> items = this.mJournalItemManipulator.listByJournalGuid( journal.getGuid() );
        for ( GenericJournalItem item : items ) {
            try {
                this.rollForwardItem( item );
            }
            catch ( Exception e ) {
                Debug.trace( "uofs journal roll-forward item failed", journal.getGuid(), item.getGuid(), item.getItemType(), e.getMessage() );
                this.mJournalManipulator.updateStatus( journal.getGuid(), JournalStatus.FAILED, e.getMessage() );
                return;
            }
        }
        this.mJournalManipulator.commit( journal.getGuid() );
    }

    protected void rollForwardItem( GenericJournalItem item ) throws IOException {
        if ( item.getItemType() == JournalItemType.OBJECT ) {
            this.checkReadable( this.toLocation( item, FileChunkLocationType.VOLUME_DIRECT_OBJECT ) );
            return;
        }
        if ( item.getItemType() == JournalItemType.VOLUME_EXTENT ) {
            this.checkReadable( this.toLocation( item, FileChunkLocationType.VOLUME_BLOCK_EXTENT ) );
            return;
        }
        if ( item.getItemType() == JournalItemType.CHUNK && this.mFatChunkInstrument != null && !this.mFatChunkInstrument.existsChunk( item.getChunkGuid() ) ) {
            Debug.trace( "uofs journal missing chunk during roll-forward", item.getChunkGuid() );
            return;
        }
        if ( item.getItemType() == JournalItemType.LOCATION && this.mFatChunkInstrument != null && !this.mFatChunkInstrument.existsLocation( item.getLocationGuid() ) ) {
            Debug.trace( "uofs journal missing location during roll-forward", item.getLocationGuid() );
            return;
        }
        if ( item.getItemType() == JournalItemType.FILE_NODE ) {
            this.applyFileNodePayload( item.getFileGuid(), item.getNewPayload() );
        }
    }

    protected void checkReadable( GenericFileChunkLocation location ) throws IOException {
        if ( this.mFatChunkStore == null ) {
            Debug.trace( "uofs journal readable check skipped without chunk store", location.getGuid(), location.getLocationType() );
            return;
        }
        if ( location.getLengthBytes() <= 0L ) {
            return;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( 1 );
        long read = this.mFatChunkStore.read( location, 0L, 1L, outputStream );
        if ( read != 1L ) {
            throw new IOException( "Journal recovery cannot read chunk data: " + location.getGuid() );
        }
    }

    protected GenericFileChunkLocation toLocation( GenericJournalItem item, FileChunkLocationType locationType ) {
        GenericFileChunkLocation location = new GenericFileChunkLocation();
        location.setGuid( item.getLocationGuid() == null ? item.getTargetGuid() : item.getLocationGuid() );
        location.setChunkGuid( item.getChunkGuid() );
        location.setVolumeGuid( item.getVolumeGuid() );
        location.setLocationType( locationType );
        location.setObjectKey( item.getObjectKey() );
        location.setObjectOffset( 0L );
        location.setVolumeOffset( item.getVolumeOffset() );
        location.setLengthBytes( item.getLengthBytes() );
        return location;
    }

    protected void applyFileNodePayload( com.pinecone.framework.util.id.GUID fileGuid, String payload ) {
        if ( fileGuid == null || payload == null || this.mFileManipulator == null ) {
            return;
        }
        FileNode fileNode = this.mFileManipulator.getFileNodeByGuid( fileGuid );
        if ( fileNode == null ) {
            Debug.trace( "uofs journal file node missing during recovery", fileGuid );
            return;
        }
        fileNode.setDefinitionSize( this.mPayloadParser.longValue( payload, "definitionSize", fileNode.getDefinitionSize() ) );
        fileNode.setLogicSize( this.mPayloadParser.longValue( payload, "logicSize", fileNode.getLogicSize() ) );
        fileNode.setPhysicalSize( this.mPayloadParser.longValue( payload, "physicalSize", fileNode.getPhysicalSize() ) );
        fileNode.setChecksum( this.mPayloadParser.longValue( payload, "checksum", fileNode.getChecksum() ) );
        fileNode.setCrc32Xor( this.mPayloadParser.longValue( payload, "crc32Xor", fileNode.getCrc32Xor() ) );
        this.mFileManipulator.update( fileNode );
    }
}
