package com.pinecone.hydra.storage.file.journal;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.storage.file.journal.source.JournalItemManipulator;
import com.pinecone.hydra.storage.file.journal.source.JournalManipulator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TitanJournalInstrument implements JournalInstrument {
    protected final JournalManipulator     mJournalManipulator;
    protected final JournalItemManipulator mJournalItemManipulator;
    protected final GuidAllocator          mGuidAllocator;

    public TitanJournalInstrument(
            JournalManipulator journalManipulator,
            JournalItemManipulator journalItemManipulator,
            GuidAllocator guidAllocator
    ) {
        this.mJournalManipulator     = journalManipulator;
        this.mJournalItemManipulator = journalItemManipulator;
        this.mGuidAllocator          = guidAllocator;
    }

    @Override
    public Journal begin( JournalType type, GUID fileGuid, String path ) {
        GenericJournal journal = new GenericJournal();
        journal.setGuid( this.mGuidAllocator.nextGUID() );
        journal.setJournalType( type );
        journal.setJournalStatus( JournalStatus.PREPARED );
        journal.setFileGuid( fileGuid );
        journal.setPath( path );
        journal.setBeginTime( LocalDateTime.now() );
        this.mJournalManipulator.insert( journal );
        return journal;
    }

    @Override
    public void markWriting( GUID journalGuid ) {
        this.mJournalManipulator.updateStatus( journalGuid, JournalStatus.WRITING, null );
    }

    @Override
    public void markCommitting( GUID journalGuid ) {
        this.mJournalManipulator.updateStatus( journalGuid, JournalStatus.COMMITTING, null );
    }

    @Override
    public void commit( GUID journalGuid ) {
        this.mJournalManipulator.commit( journalGuid );
    }

    @Override
    public void abort( GUID journalGuid, String errorMessage ) {
        this.mJournalManipulator.updateStatus( journalGuid, JournalStatus.ABORTED, errorMessage );
    }

    @Override
    public void fail( GUID journalGuid, String errorMessage ) {
        this.mJournalManipulator.updateStatus( journalGuid, JournalStatus.FAILED, errorMessage );
    }

    @Override
    public JournalItem addItem( GUID journalGuid, JournalItemType itemType, GUID targetGuid, int ordinalNo ) {
        GenericJournalItem item = new GenericJournalItem();
        item.setItemType( itemType );
        item.setTargetGuid( targetGuid );
        item.setOrdinalNo( ordinalNo );
        return this.addItem( journalGuid, item );
    }

    @Override
    public JournalItem addItem( GUID journalGuid, JournalItem item ) {
        item.setGuid( this.mGuidAllocator.nextGUID() );
        item.setJournalGuid( journalGuid );
        item.setItemStatus( JournalItemStatus.PREPARED );
        this.mJournalItemManipulator.insert( item );
        return item;
    }

    @Override
    public void markItemApplied( GUID itemGuid ) {
        this.mJournalItemManipulator.updateStatus( itemGuid, JournalItemStatus.APPLIED );
    }

    @Override
    public void markItemRolledBack( GUID itemGuid ) {
        this.mJournalItemManipulator.updateStatus( itemGuid, JournalItemStatus.ROLLED_BACK );
    }

    @Override
    public List<Journal> listRecoverable() {
        List<Journal> journals = new ArrayList<>();
        journals.addAll( this.mJournalManipulator.listByStatus( JournalStatus.PREPARED ) );
        journals.addAll( this.mJournalManipulator.listByStatus( JournalStatus.WRITING ) );
        journals.addAll( this.mJournalManipulator.listByStatus( JournalStatus.COMMITTING ) );
        journals.addAll( this.mJournalManipulator.listByStatus( JournalStatus.FAILED ) );
        return journals;
    }
}
