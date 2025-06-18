package com.walnut.odin.proc.dto;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class UProcessDTO implements Pinenut {
    private String      mName;

    private long        mPID;

    private String      mGuid;

    private String      mStartupArguments;

    private String      mEnvironmentVariables;

    public UProcessDTO( String name, long pid, String guid, String startupArguments, String environmentVariables ) {
        this.mName = name;
        this.mPID = pid;
        this.mGuid = guid;
        this.mStartupArguments = startupArguments;
        this.mEnvironmentVariables = environmentVariables;
    }

    public UProcessDTO(){}

    public UProcessDTO( String name, long pid, String guid ) {
        this( name, pid, guid, null, null );
    }

    public String getName() {
        return mName;
    }

    public void setName( String name ) {
        this.mName = name;
    }

    public long getPID() {
        return mPID;
    }

    public void setPID( long pid ) {
        this.mPID = pid;
    }

    public String getGuid() {
        return mGuid;
    }

    public void setGuid(String guid) {
        this.mGuid = guid;
    }

    public String getStartupArguments() {
        return mStartupArguments;
    }

    public void setStartupArguments( String startupArguments ) {
        this.mStartupArguments = startupArguments;
    }

    public String getEnvironmentVariables() {
        return mEnvironmentVariables;
    }

    public void setEnvironmentVariables( String environmentVariables ) {
        this.mEnvironmentVariables = environmentVariables;
    }


}
