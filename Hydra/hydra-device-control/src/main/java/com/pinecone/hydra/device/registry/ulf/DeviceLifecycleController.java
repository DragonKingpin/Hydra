package com.pinecone.hydra.device.registry.ulf;

import com.pinecone.hydra.device.registry.dto.DeviceRegistrationDTO;
import com.pinecone.hydra.device.registry.server.DeviceLifecycleService;
import com.pinecone.hydra.device.registry.server.DeviceManager;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;

@Controller
@AddressMapping( "com.pinecone.hydra.device.registry.server.DeviceLifecycleIface." )
public class DeviceLifecycleController implements DeviceRPCController {

    protected DeviceLifecycleService deviceLifecycleService;

    public DeviceLifecycleController( DeviceManager deviceManager ) {
        this.deviceLifecycleService = deviceManager.deviceLifecycleService();
    }

    @AddressMapping( "enrollDevice" )
    public String enrollDevice( DeviceRegistrationDTO registrationDTO ) {
        return this.deviceLifecycleService.enrollDevice( registrationDTO );
    }

    @AddressMapping( "dismissDeviceByGuid" )
    public void dismissDeviceByGuid( String guid ) {
        this.deviceLifecycleService.dismissDeviceByGuid( guid );
    }

    @AddressMapping( "dismissDeviceByPath" )
    public void dismissDeviceByPath( String path ) {
        this.deviceLifecycleService.dismissDeviceByPath( path );
    }

    @AddressMapping( "hasDeviceByGuid" )
    public boolean hasDeviceByGuid( String guid ) {
        return this.deviceLifecycleService.hasDeviceByGuid( guid );
    }

    @AddressMapping( "hasDeviceByPath" )
    public boolean hasDeviceByPath( String path ) {
        return this.deviceLifecycleService.hasDeviceByPath( path );
    }
}
