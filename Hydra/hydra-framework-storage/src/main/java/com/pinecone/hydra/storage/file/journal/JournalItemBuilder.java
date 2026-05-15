package com.pinecone.hydra.storage.file.journal;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.fat.entity.FileChunkLocation;

public class JournalItemBuilder implements Pinenut {
    protected final GenericJournalItem mItem = new GenericJournalItem();

    public static JournalItemBuilder of( JournalItemType itemType, GUID targetGuid, int ordinalNo ) {
        JournalItemBuilder builder = new JournalItemBuilder();
        builder.mItem.setItemType( itemType );
        builder.mItem.setTargetGuid( targetGuid );
        builder.mItem.setOrdinalNo( ordinalNo );
        return builder;
    }

    public JournalItemBuilder fileGuid( GUID fileGuid ) {
        this.mItem.setFileGuid( fileGuid );
        return this;
    }

    public JournalItemBuilder chunkGuid( GUID chunkGuid ) {
        this.mItem.setChunkGuid( chunkGuid );
        return this;
    }

    public JournalItemBuilder locationGuid( GUID locationGuid ) {
        this.mItem.setLocationGuid( locationGuid );
        return this;
    }

    public JournalItemBuilder volumeGuid( GUID volumeGuid ) {
        this.mItem.setVolumeGuid( volumeGuid );
        return this;
    }

    public JournalItemBuilder location( FileChunkLocation location ) {
        if ( location != null ) {
            this.mItem.setLocationGuid( location.getGuid() );
            this.mItem.setChunkGuid( location.getChunkGuid() );
            this.mItem.setVolumeGuid( location.getVolumeGuid() );
            this.mItem.setObjectKey( location.getObjectKey() );
            this.mItem.setVolumeOffset( location.getVolumeOffset() );
            this.mItem.setLengthBytes( location.getLengthBytes() );
        }
        return this;
    }

    public JournalItemBuilder oldPayload( String oldPayload ) {
        this.mItem.setOldPayload( oldPayload );
        return this;
    }

    public JournalItemBuilder newPayload( String newPayload ) {
        this.mItem.setNewPayload( newPayload );
        return this;
    }

    public JournalItemBuilder extConfig( String extConfig ) {
        this.mItem.setExtConfig( extConfig );
        return this;
    }

    public GenericJournalItem build() {
        return this.mItem;
    }
}
