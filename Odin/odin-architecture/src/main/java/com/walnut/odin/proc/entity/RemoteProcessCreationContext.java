package com.walnut.odin.proc.entity;

import java.net.URI;
import java.util.Map;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;
import com.walnut.odin.proc.RemoteImageResolutionMode;

public class RemoteProcessCreationContext implements Pinenut {

    private String              mszImageAddress;

    private boolean             mbImageAddressURI;

    private GUID                mParentPID;

    private Map<String, String> mStartupArguments;

    private Map<String, String> mEnvironmentVariables;

    private String              mszImageResolutionMode = RemoteImageResolutionMode.REQUIRE_SERVER_IMAGE.name();

    public RemoteProcessCreationContext() {
    }

    public RemoteProcessCreationContext(
            String szImageAddress, boolean bImageAddressURI, GUID parentPID,
            Map<String, String> startupArguments, Map<String, String> environmentVariables
    ) {
        this.mszImageAddress = szImageAddress;
        this.mbImageAddressURI = bImageAddressURI;
        this.mParentPID = parentPID;
        this.mStartupArguments = startupArguments;
        this.mEnvironmentVariables = environmentVariables;
    }

    public static RemoteProcessCreationContext of(
            String szImageAddress, boolean bImageAddressURI, GUID parentPID,
            Map<String, String> startupArguments, Map<String, String> environmentVariables
    ) {
        return new RemoteProcessCreationContext( szImageAddress, bImageAddressURI, parentPID, startupArguments, environmentVariables );
    }

    public static RemoteProcessCreationContext of(
            String szImagePath, GUID parentPID,
            Map<String, String> startupArguments, Map<String, String> environmentVariables
    ) {
        return of( szImagePath, false, parentPID, startupArguments, environmentVariables );
    }

    public static RemoteProcessCreationContext of(
            URI imageURI, GUID parentPID,
            Map<String, String> startupArguments, Map<String, String> environmentVariables
    ) {
        String szImageURI = null;
        if ( imageURI != null ) {
            szImageURI = imageURI.toString();
        }
        return of( szImageURI, true, parentPID, startupArguments, environmentVariables );
    }

    public String getImageAddress() {
        return this.mszImageAddress;
    }

    public void setImageAddress( String szImageAddress ) {
        this.mszImageAddress = szImageAddress;
    }

    public boolean isImageAddressURI() {
        return this.mbImageAddressURI;
    }

    public void setImageAddressURI( boolean bImageAddressURI ) {
        this.mbImageAddressURI = bImageAddressURI;
    }

    public GUID getParentPID() {
        return this.mParentPID;
    }

    public void setParentPID( GUID parentPID ) {
        this.mParentPID = parentPID;
    }

    public Map<String, String> getStartupArguments() {
        return this.mStartupArguments;
    }

    public void setStartupArguments( Map<String, String> startupArguments ) {
        this.mStartupArguments = startupArguments;
    }

    public Map<String, String> getEnvironmentVariables() {
        return this.mEnvironmentVariables;
    }

    public void setEnvironmentVariables( Map<String, String> environmentVariables ) {
        this.mEnvironmentVariables = environmentVariables;
    }

    public String getImageResolutionMode() {
        return this.mszImageResolutionMode;
    }

    public void setImageResolutionMode( String szImageResolutionMode ) {
        this.mszImageResolutionMode = RemoteImageResolutionMode.parse( szImageResolutionMode ).name();
    }

    public RemoteImageResolutionMode optImageResolutionMode() {
        return RemoteImageResolutionMode.parse( this.mszImageResolutionMode );
    }

    public RemoteProcessCreationContext withImageAddress( String szImageAddress, boolean bImageAddressURI ) {
        this.mszImageAddress = szImageAddress;
        this.mbImageAddressURI = bImageAddressURI;
        return this;
    }

    public RemoteProcessCreationContext withImageAddress( URI imageURI ) {
        String szImageURI = null;
        if ( imageURI != null ) {
            szImageURI = imageURI.toString();
        }
        return this.withImageAddress( szImageURI, true );
    }

    public RemoteProcessCreationContext withParentPID( GUID parentPID ) {
        this.mParentPID = parentPID;
        return this;
    }

    public RemoteProcessCreationContext withStartupArguments( Map<String, String> startupArguments ) {
        this.mStartupArguments = startupArguments;
        return this;
    }

    public RemoteProcessCreationContext withEnvironmentVariables( Map<String, String> environmentVariables ) {
        this.mEnvironmentVariables = environmentVariables;
        return this;
    }

    public RemoteProcessCreationContext withImageResolutionMode( RemoteImageResolutionMode imageResolutionMode ) {
        RemoteImageResolutionMode effectiveImageResolutionMode = imageResolutionMode;
        if ( effectiveImageResolutionMode == null ) {
            effectiveImageResolutionMode = RemoteImageResolutionMode.REQUIRE_SERVER_IMAGE;
        }
        this.mszImageResolutionMode = effectiveImageResolutionMode.name();
        return this;
    }

    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }
}
