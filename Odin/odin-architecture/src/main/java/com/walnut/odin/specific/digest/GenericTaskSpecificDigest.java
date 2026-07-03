package com.walnut.odin.specific.digest;

import com.pinecone.hydra.task.kom.digest.GenericTaskElementDigest;

public class GenericTaskSpecificDigest extends GenericTaskElementDigest implements TaskSpecificDigest {

    protected String mszProjectName;

    protected String mszProjectTitle;

    @Override
    public String getProjectName() {
        return this.mszProjectName;
    }

    @Override
    public void setProjectName( String szProjectName ) {
        this.mszProjectName = szProjectName;
    }

    @Override
    public String getProjectTitle() {
        return this.mszProjectTitle;
    }

    @Override
    public void setProjectTitle( String szProjectTitle ) {
        this.mszProjectTitle = szProjectTitle;
    }
}
