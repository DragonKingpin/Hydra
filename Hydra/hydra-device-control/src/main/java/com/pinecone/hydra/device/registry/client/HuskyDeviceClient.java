package com.pinecone.hydra.device.registry.client;

import com.pinecone.hydra.device.registry.DeviceControlRPCException;
import com.pinecone.hydra.device.registry.dto.DeviceRegistrationDTO;
import com.pinecone.hydra.device.registry.server.DeviceLifecycleIface;
import com.pinecone.hydra.device.registry.server.DeviceMetaManipulationIface;
import com.pinecone.hydra.device.registry.server.DeviceTopologyManipulationIface;
import com.pinecone.hydra.uma.DuplexAppointClient;
import com.pinecone.hydra.uma.wolf.WolvesAppointClient;
import com.pinecone.hydra.umc.wolf.client.UlfClient;

public class HuskyDeviceClient implements DeviceRPCClient {

    protected DuplexAppointClient mDuplexAppointClient;

    protected UlfClient mRPCClient;

    protected DeviceLifecycleIface mDeviceLifecycleIface;

    protected DeviceMetaManipulationIface mDeviceMetaManipulationIface;

    protected DeviceTopologyManipulationIface mDeviceTopologyManipulationIface;

    public HuskyDeviceClient( UlfClient ulfClient ) {
        this.mRPCClient = ulfClient;
    }

    public void startService() throws DeviceControlRPCException {
        this.initRPCSubsystem();
    }

    public void terminateService() {
        if ( this.mDuplexAppointClient != null ) {
            this.mDuplexAppointClient.terminate();
            this.mDuplexAppointClient = null;
        }
    }

    public DuplexAppointClient getAppointNodus() {
        return this.mDuplexAppointClient;
    }

    public DeviceLifecycleIface deviceLifecycleIface() {
        return this.mDeviceLifecycleIface;
    }

    public DeviceMetaManipulationIface deviceMetaManipulationIface() {
        return this.mDeviceMetaManipulationIface;
    }

    public DeviceTopologyManipulationIface deviceTopologyManipulationIface() {
        return this.mDeviceTopologyManipulationIface;
    }

    public String enrollDevice( DeviceRegistrationDTO registrationDTO ) {
        return this.mDeviceLifecycleIface.enrollDevice( registrationDTO );
    }

    protected void initRPCSubsystem() throws DeviceControlRPCException {
        if ( this.mDuplexAppointClient != null && !this.mDuplexAppointClient.getMessageNode().isTerminated() ) {
            throw new IllegalStateException( "DuplexAppointClient has started." );
        }

        this.mDuplexAppointClient = new WolvesAppointClient( this.mRPCClient );
        try {
            this.mDuplexAppointClient.execute();
            this.mDuplexAppointClient.compile( DeviceLifecycleIface.class, false );
            this.mDuplexAppointClient.compile( DeviceMetaManipulationIface.class, false );
            this.mDuplexAppointClient.compile( DeviceTopologyManipulationIface.class, false );
            this.mDeviceLifecycleIface = this.mDuplexAppointClient.getIface( DeviceLifecycleIface.class );
            this.mDeviceMetaManipulationIface = this.mDuplexAppointClient.getIface( DeviceMetaManipulationIface.class );
            this.mDeviceTopologyManipulationIface = this.mDuplexAppointClient.getIface( DeviceTopologyManipulationIface.class );
        }
        catch ( Exception e ) {
            this.mDeviceLifecycleIface = null;
            this.mDeviceMetaManipulationIface = null;
            this.mDeviceTopologyManipulationIface = null;
            throw new DeviceControlRPCException( e );
        }
    }
}
