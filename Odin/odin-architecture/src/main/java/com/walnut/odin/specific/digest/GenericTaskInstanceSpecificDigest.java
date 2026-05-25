package com.walnut.odin.specific.digest;

import com.pinecone.hydra.task.kom.instance.GenericInstanceEntry;

public class GenericTaskInstanceSpecificDigest extends GenericInstanceEntry implements TaskInstanceSpecificDigest {

    protected String mszProjectGuid;

    protected String mszProjectName;

    protected String mszProjectTitle;

    @Override
    public String getProjectGuid() {
        return this.mszProjectGuid;
    }

    @Override
    public void setProjectGuid( String szProjectGuid ) {
        this.mszProjectGuid = szProjectGuid;
    }

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
