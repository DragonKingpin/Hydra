package com.pinecone.hydra.business.entity;

import java.util.Collection;
import java.util.Collections;

import com.pinecone.framework.util.id.GUID;

public abstract class ArchFolderElement extends ArchElementNode implements FolderElement {

    @Override
    public Collection<ElementNode > fetchChildren() {
        if ( this.mBusinessInstrument == null || this.mGuid == null ) {
            return Collections.emptyList();
        }

        return this.mBusinessInstrument.fetchChildren( this.mGuid );
    }

    @Override
    public Collection<GUID > fetchChildrenGuids() {
        if ( this.mBusinessInstrument == null || this.mGuid == null ) {
            return Collections.emptyList();
        }

        return this.mBusinessInstrument.fetchChildrenGuids( this.mGuid );
    }

    @Override
    public void addChild( ElementNode child ) {
        if ( this.mBusinessInstrument == null || this.mGuid == null ) {
            throw new IllegalStateException( "Business instrument is not bound." );
        }

        this.mBusinessInstrument.addChild( this.mGuid, child );
    }

    @Override
    public boolean containsChild( String szChildName ) {
        for ( ElementNode child : this.fetchChildren() ) {
            if ( szChildName.equals( child.getName() ) ) {
                return true;
            }
        }

        return false;
    }
}
