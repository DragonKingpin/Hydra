package com.walnut.odin.specific.digest;

import com.pinecone.hydra.task.kom.instance.InstanceEntry;

public interface TaskInstanceSpecificDigest extends InstanceEntry {

    String getProjectGuid();

    void setProjectGuid( String szProjectGuid );

    String getProjectName();

    void setProjectName( String szProjectName );

    String getProjectTitle();

    void setProjectTitle( String szProjectTitle );
}
