package com.walnut.odin.proc.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.json.JSON;
import com.walnut.odin.proc.RemoteImageResolutionMode;

import java.util.LinkedHashMap;
import java.util.Map;

public class UProcessMirrorDTO implements Pinenut {

    private String      mszName;

    private long        mnLocalPID;

    private String      mszParentPID;

    private String      mszProcessId;

    private String      mStartupArguments;

    private String      mEnvironmentVariables;

    private String      mszImageAddress;
    private boolean     mbImageAddressURI;
    private String      mImageResolutionMode = RemoteImageResolutionMode.REQUIRE_SERVER_IMAGE.name();

    public UProcessMirrorDTO( String name, long localPID, String processId, String startupArguments, String environmentVariables ) {
        this.mszName               = name;
        this.mnLocalPID            = localPID;
        this.mszProcessId          = processId;
        this.mStartupArguments     = startupArguments;
        this.mEnvironmentVariables = environmentVariables;
    }

    public UProcessMirrorDTO( String name, long localPID, String processId ) {
        this( name, localPID, processId, null, null );
    }

    public UProcessMirrorDTO(){}



    public String getImageAddress() {
        return this.mszImageAddress;
    }

    public void setImageAddress( String szImageAddress ) {
        this.mszImageAddress = szImageAddress;
    }

    public void setImageAddressURI( boolean bImageAddressURI ) {
        this.mbImageAddressURI = bImageAddressURI;
    }

    public boolean isImageAddressURI() {
        return this.mbImageAddressURI;
    }

    public String getImageResolutionMode() {
        return this.mImageResolutionMode;
    }

    public void setImageResolutionMode( String imageResolutionMode ) {
        this.mImageResolutionMode = RemoteImageResolutionMode.parse( imageResolutionMode ).name();
    }

    public RemoteImageResolutionMode optImageResolutionMode() {
        return RemoteImageResolutionMode.parse( this.mImageResolutionMode );
    }

    public String getName() {
        return mszName;
    }

    public void setName( String name ) {
        this.mszName = name;
    }

    public String getParentPID() {
        return this.mszParentPID;
    }

    public void setParentPID( String szParentPID ) {
        this.mszParentPID = szParentPID;
    }

    public long getLocalPID() {
        return mnLocalPID;
    }

    public void setLocalPID( long pid ) {
        this.mnLocalPID = pid;
    }

    public String getPID() {
        return mszProcessId;
    }

    public void setPID( String pid ) {
        this.mszProcessId = pid;
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

    @Override
    public String toJSONString() {
        Map<String, Object> json = new LinkedHashMap<>();
        json.put( "name", this.mszName );
        json.put( "localPID", this.mnLocalPID );
        json.put( "parentPID", this.mszParentPID );
        json.put( "PID", this.mszProcessId );
        json.put( "startupArguments", this.mStartupArguments );
        json.put( "environmentVariables", this.mEnvironmentVariables );
        json.put( "imageAddress", this.mszImageAddress );
        json.put( "imageAddressURI", this.mbImageAddressURI );
        json.put( "imageResolutionMode", this.mImageResolutionMode );
        return JSON.stringify( json );
    }

}
