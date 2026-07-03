package com.walnut.odin.conduct.entity;

import com.pinecone.framework.system.prototype.Pinenut;

public class TaskInstanceOperationActor implements Pinenut {

    public static final TaskInstanceOperationActor Root =
            new TaskInstanceOperationActor( "__ROOT__", "root" );

    public static final TaskInstanceOperationActor System =
            new TaskInstanceOperationActor( "__SYSTEM__", "SYSTEM" );

    protected String mszUserIdentifier;
    protected String mszUserName;

    public TaskInstanceOperationActor() {
    }

    public TaskInstanceOperationActor( String userIdentifier, String userName ) {
        this.mszUserIdentifier = userIdentifier;
        this.mszUserName = userName;
    }

    public String getUserIdentifier() {
        return this.mszUserIdentifier;
    }

    public void setUserIdentifier( String userIdentifier ) {
        this.mszUserIdentifier = userIdentifier;
    }

    public String getUserName() {
        return this.mszUserName;
    }

    public void setUserName( String userName ) {
        this.mszUserName = userName;
    }
}
