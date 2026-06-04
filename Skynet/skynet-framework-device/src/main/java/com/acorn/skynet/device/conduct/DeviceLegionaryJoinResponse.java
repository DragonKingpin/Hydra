package com.acorn.skynet.device.conduct;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.DeviceStatus;
import com.pinecone.hydra.device.registry.client.entity.DeviceClientRegisterResult;

public class DeviceLegionaryJoinResponse implements Pinenut {

    protected GUID deviceGuid;

    protected GUID instanceGuid;

    protected long clientId;

    protected String status;

    public GUID getDeviceGuid() {
        return this.deviceGuid;
    }

    public void setDeviceGuid( GUID deviceGuid ) {
        this.deviceGuid = deviceGuid;
    }

    public GUID getInstanceGuid() {
        return this.instanceGuid;
    }

    public void setInstanceGuid( GUID instanceGuid ) {
        this.instanceGuid = instanceGuid;
    }

    public long getClientId() {
        return this.clientId;
    }

    public void setClientId( long clientId ) {
        this.clientId = clientId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus( String status ) {
        this.status = status;
    }

    public static DeviceLegionaryJoinResponse from( DeviceClientRegisterResult response ) {
        DeviceLegionaryJoinResponse result = new DeviceLegionaryJoinResponse();
        if ( response == null ) {
            return result;
        }

        result.setDeviceGuid( response.getDeviceGuid() );
        result.setInstanceGuid( response.getInstanceGuid() );
        result.setClientId( response.getClientId() );
        result.setStatus( DeviceStatus.Online.getName() );
        return result;
    }
}
