package com.walnut.odin.specific.digest;

import com.pinecone.hydra.task.kom.digest.TaskElementDigest;

public interface TaskSpecificDigest extends TaskElementDigest {

    String getProjectName();

    void setProjectName( String szProjectName );

    String getProjectTitle();

    void setProjectTitle( String szProjectTitle );
}
