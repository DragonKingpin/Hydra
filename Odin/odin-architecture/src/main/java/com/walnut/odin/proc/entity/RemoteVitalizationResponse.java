package com.walnut.odin.proc.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.proc.RemoteImageResolutionMode;
import com.walnut.odin.proc.RemoteVitalizationStatus;

public class RemoteVitalizationResponse implements Pinenut {

    protected long mnLocalPID;

    protected String mszName;

    protected GUID mPID;

    protected String mszPID;

    protected int mnStatus;

    protected String mszErrorMsg;

    private String mStartupArguments;

    private String mEnvironmentVariables;

    private String mszImageAddress;
    private boolean mbImageAddressURI;
    private String mImageResolutionMode = RemoteImageResolutionMode.REQUIRE_SERVER_IMAGE.name();

    public RemoteVitalizationResponse() {
        this.mnStatus = RemoteVitalizationStatus.Vitalized.getCode();
    }

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
        return this.mszName;
    }

    public void setName( String szName ) {
        this.mszName = szName;
    }

    public long getLocalPID() {
        return this.mnLocalPID;
    }

    public void setLocalPID( long nLocalPID ) {
        this.mnLocalPID = nLocalPID;
    }

    public String getPID() {
        return this.mszPID;
    }

    public void setPID( String szPID ) {
        this.mszPID = szPID;
    }

    public void setStatus( int nStatus ) {
        this.mnStatus = nStatus;
    }

    public int getStatus() {
        return this.mnStatus;
    }

    public String getErrorMsg() {
        return this.mszErrorMsg;
    }

    public void setErrorMsg( String szErrorMsg ) {
        this.mszErrorMsg = szErrorMsg;
    }

    public void setProcessID( GUID pid ) {
        this.setPID( pid.toString() );
        this.mPID = pid;
    }

    public GUID optProcessID() {
        return this.mPID;
    }

    public void setRemoteVitalizationStatus( RemoteVitalizationStatus status ) {
        this.setStatus( status.getCode() );
    }

    public RemoteVitalizationStatus optStatus() {
        return RemoteVitalizationStatus.getByCode( this.getStatus() );
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
