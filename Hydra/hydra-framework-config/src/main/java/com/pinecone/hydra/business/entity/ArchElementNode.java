package com.pinecone.hydra.business.entity;

import java.time.LocalDateTime;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.business.BusinessInstrument;

public abstract class ArchElementNode implements ElementNode {

    protected transient BusinessInstrument mBusinessInstrument;

    protected long          mnEnumId;
    protected GUID          mGuid;
    protected String        mszType;
    protected String        mszName;
    protected String        mszCode;
    protected String        mszDescription;
    protected String        mszOwner;
    protected String        mszExtraInformation;
    protected LocalDateTime mCreateTime;
    protected LocalDateTime mUpdateTime;

    public long getEnumId() {
        return this.mnEnumId;
    }

    public void setEnumId( long nEnumId ) {
        this.mnEnumId = nEnumId;
    }

    @Override
    public GUID getGuid() {
        return this.mGuid;
    }

    public void setGuid( GUID guid ) {
        this.mGuid = guid;
    }

    @Override
    public String getType() {
        return this.mszType;
    }

    @Override
    public void setType( String szType ) {
        this.mszType = szType;
    }

    @Override
    public String getName() {
        return this.mszName;
    }

    public void setName( String szName ) {
        this.mszName = szName;
    }

    @Override
    public String getCode() {
        return this.mszCode;
    }

    @Override
    public void setCode( String szCode ) {
        this.mszCode = szCode;
    }

    @Override
    public String getDescription() {
        return this.mszDescription;
    }

    @Override
    public void setDescription( String szDescription ) {
        this.mszDescription = szDescription;
    }

    @Override
    public String getOwner() {
        return this.mszOwner;
    }

    @Override
    public void setOwner( String szOwner ) {
        this.mszOwner = szOwner;
    }

    @Override
    public String getExtraInformation() {
        return this.mszExtraInformation;
    }

    @Override
    public void setExtraInformation( String szExtraInformation ) {
        this.mszExtraInformation = szExtraInformation;
    }

    public void setBusinessInstrument( BusinessInstrument businessInstrument ) {
        this.mBusinessInstrument = businessInstrument;
    }

    @Override
    public String getKomPath() {
        if ( this.mBusinessInstrument == null || this.mGuid == null ) {
            return null;
        }

        return this.mBusinessInstrument.getPath( this.mGuid );
    }

    @Override
    public LocalDateTime getCreateTime() {
        return this.mCreateTime;
    }

    @Override
    public void setCreateTime( LocalDateTime createTime ) {
        this.mCreateTime = createTime;
    }

    @Override
    public LocalDateTime getUpdateTime() {
        return this.mUpdateTime;
    }

    @Override
    public void setUpdateTime( LocalDateTime updateTime ) {
        this.mUpdateTime = updateTime;
    }
}
