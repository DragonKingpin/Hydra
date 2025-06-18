package com.walnut.odin.proc;

import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.proc.server.RemoteProcessManagerServer;

import java.time.LocalDateTime;
import java.util.Map;

public class RavenRemoteProcess implements OdinRemoteProcess {
    protected RemoteProcessManagerServer    mRemoteProcessManagerServer;

    protected String                        mName;

    protected long                          mlsPID;

    protected GUID                          mGUID;

    protected Map<String, String[]>         mStartupArguments;

    protected Map<String, String[]>         mEnvironmentVariables;

    public RavenRemoteProcess(RemoteProcessManagerServer server, String name, long pid, GUID guid,
                              Map<String, String[]> startupArguments, Map<String, String[]> environmentVariables ) {
        this.mRemoteProcessManagerServer = server;
        this.mName = name;
        this.mlsPID = pid;
        this.mGUID = guid;
        this.mStartupArguments = startupArguments;
        this.mEnvironmentVariables = environmentVariables;
    }

    public RavenRemoteProcess( RemoteProcessManagerServer server, String name, long pid, GUID guid ) {
        this( server, name, pid, guid, null, null );
    }

    @Override
    public String getName() {
        return this.mName;
    }

    @Override
    public long getPID() {
        return this.mlsPID;
    }

    @Override
    public GUID getGuid() {
        return this.mGUID;
    }

    @Override
    public LocalDateTime remoteGetEndTime() {
        return null;
    }

    @Override
    public LocalDateTime remoteGetLastUpdateTime() {
        return null;
    }

    @Override
    public Map<String, String[]> getStartupArguments() {
        return this.mStartupArguments;
    }

    @Override
    public Map<String, String[]> getEnvironmentVariables() {
        return this.mEnvironmentVariables;
    }

    @Override
    public void start() {

    }
}
