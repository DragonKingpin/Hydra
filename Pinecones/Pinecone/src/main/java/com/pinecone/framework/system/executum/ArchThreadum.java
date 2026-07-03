package com.pinecone.framework.system.executum;

import com.pinecone.framework.system.ApoptosisRejectSignalException;

public abstract class ArchThreadum extends ArchExecutum implements Executum {

    protected ArchThreadum ( String szName, Processum parent, Thread affiliateThread ) {
        super( szName, parent, affiliateThread );
    }

    protected ArchThreadum ( String szName, Processum parent ) {
        this( szName, parent, null );
    }

    protected ArchThreadum ( Processum parent, Thread affiliateThread ) {
        this( affiliateThread.getName(), parent, affiliateThread );
    }

    @Override
    public void setName( String szName ) {
        super.setName( szName );
        if( this.getAffiliateThread() != null ) {
            this.getAffiliateThread().setName( szName );
        }
    }

    @Override
    public void  apoptosis() {
        this.interrupt();
    }

    @Override
    public void  kill() {
        this.interrupt();
    }

    @Override
    public void interrupt() {
        if( this.getAffiliateThread() != null ) {
            this.getAffiliateThread().interrupt();
        }
    }

    @Override
    public void  suspend() {
    }

    @Override
    public void  resume() {
    }

    @Override
    public void  entreatLive() {
        throw new ApoptosisRejectSignalException();
    }

}
