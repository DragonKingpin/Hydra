package com.pinecone.hydra.business.entity;

import com.pinecone.framework.util.id.GUID;

public class GenericIdeaElement extends ArchElementNode implements IdeaElement {

    protected String mszIdeaCode;
    protected String mszIdeaStatus = "Draft";
    protected GUID   mRedMineGuid;
    protected String mszValueLevel;

    public GenericIdeaElement() {
        this.setType( "Idea" );
    }

    @Override
    public String getIdeaCode() {
        return this.mszIdeaCode;
    }

    @Override
    public void setIdeaCode( String szIdeaCode ) {
        this.mszIdeaCode = szIdeaCode;
    }

    @Override
    public String getIdeaStatus() {
        return this.mszIdeaStatus;
    }

    @Override
    public void setIdeaStatus( String szIdeaStatus ) {
        this.mszIdeaStatus = szIdeaStatus;
    }

    @Override
    public GUID getRedMineGuid() {
        return this.mRedMineGuid;
    }

    @Override
    public void setRedMineGuid( GUID redMineGuid ) {
        this.mRedMineGuid = redMineGuid;
    }

    @Override
    public String getValueLevel() {
        return this.mszValueLevel;
    }

    @Override
    public void setValueLevel( String szValueLevel ) {
        this.mszValueLevel = szValueLevel;
    }
}
