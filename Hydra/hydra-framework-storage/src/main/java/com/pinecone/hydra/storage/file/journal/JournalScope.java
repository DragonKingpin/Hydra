package com.pinecone.hydra.storage.file.journal;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.entity.FileNode;

public class JournalScope implements Pinenut {
    protected final JournalInstrument mJournalInstrument;
    protected final Journal           mJournal;
    protected int                     mnOrdinalNo;

    protected JournalScope( JournalInstrument journalInstrument, Journal journal ) {
        this.mJournalInstrument = journalInstrument;
        this.mJournal           = journal;
    }

    public static JournalScope open( JournalInstrument journalInstrument, JournalType type, FileNode fileNode ) {
        if ( journalInstrument == null ) {
            return new JournalScope( null, null );
        }
        return new JournalScope( journalInstrument, journalInstrument.begin( type, fileNode.getGuid(), fileNode.getPath() ) );
    }

    public boolean isEnabled() {
        return this.mJournalInstrument != null && this.mJournal != null;
    }

    public GUID getJournalGuid() {
        return this.mJournal == null ? null : this.mJournal.getGuid();
    }

    public int nextOrdinal() {
        return this.mnOrdinalNo++;
    }

    public void markWriting() {
        if ( this.isEnabled() ) {
            this.mJournalInstrument.markWriting( this.mJournal.getGuid() );
        }
    }

    public JournalItem record( JournalItemBuilder builder ) {
        if ( !this.isEnabled() ) {
            return null;
        }
        return this.mJournalInstrument.addItem( this.mJournal.getGuid(), builder.build() );
    }

    public JournalItem recordApplied( JournalItemBuilder builder ) {
        JournalItem item = this.record( builder );
        this.markApplied( item );
        return item;
    }

    public void markApplied( JournalItem item ) {
        if ( this.isEnabled() && item != null ) {
            this.mJournalInstrument.markItemApplied( item.getGuid() );
        }
    }

    public void commit() {
        if ( this.isEnabled() ) {
            this.mJournalInstrument.markCommitting( this.mJournal.getGuid() );
            this.mJournalInstrument.commit( this.mJournal.getGuid() );
        }
    }

    public void fail( Throwable throwable ) {
        if ( this.isEnabled() ) {
            this.mJournalInstrument.fail( this.mJournal.getGuid(), throwable == null ? null : throwable.getMessage() );
        }
    }
}
