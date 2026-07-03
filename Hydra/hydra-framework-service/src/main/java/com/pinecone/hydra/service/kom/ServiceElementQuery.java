package com.pinecone.hydra.service.kom;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class ServiceElementQuery implements Pinenut {

    public static final long DEFAULT_LIMIT = 20;

    public static final long MAX_LIMIT = 1000;

    protected long mOffset = 0;

    protected long mLimit = DEFAULT_LIMIT;

    protected String mszKeyword;

    protected GUID mServiceGuid;

    protected String mszType;

    protected String mszAlias;

    protected String mszResourceType;

    protected String mszServiceType;

    protected String mszScenario;

    protected String mszPrimaryImplLang;

    protected String mszLevel;

    public long getOffset() {
        return this.mOffset;
    }

    public void setOffset( long nOffset ) {
        this.mOffset = Math.max( 0, nOffset );
    }

    public long getLimit() {
        return this.mLimit;
    }

    public void setLimit( long nLimit ) {
        if ( nLimit <= 0 ) {
            this.mLimit = DEFAULT_LIMIT;
            return;
        }
        this.mLimit = Math.min( nLimit, MAX_LIMIT );
    }

    public String getKeyword() {
        return this.mszKeyword;
    }

    public void setKeyword( String szKeyword ) {
        this.mszKeyword = szKeyword;
    }

    public GUID getServiceGuid() {
        return this.mServiceGuid;
    }

    public void setServiceGuid( GUID serviceGuid ) {
        this.mServiceGuid = serviceGuid;
    }

    public String getType() {
        return this.mszType;
    }

    public void setType( String szType ) {
        this.mszType = szType;
    }

    public String getAlias() {
        return this.mszAlias;
    }

    public void setAlias( String szAlias ) {
        this.mszAlias = szAlias;
    }

    public String getResourceType() {
        return this.mszResourceType;
    }

    public void setResourceType( String szResourceType ) {
        this.mszResourceType = szResourceType;
    }

    public String getServiceType() {
        return this.mszServiceType;
    }

    public void setServiceType( String szServiceType ) {
        this.mszServiceType = szServiceType;
    }

    public String getScenario() {
        return this.mszScenario;
    }

    public void setScenario( String szScenario ) {
        this.mszScenario = szScenario;
    }

    public String getPrimaryImplLang() {
        return this.mszPrimaryImplLang;
    }

    public void setPrimaryImplLang( String szPrimaryImplLang ) {
        this.mszPrimaryImplLang = szPrimaryImplLang;
    }

    public String getLevel() {
        return this.mszLevel;
    }

    public void setLevel( String szLevel ) {
        this.mszLevel = szLevel;
    }
}
