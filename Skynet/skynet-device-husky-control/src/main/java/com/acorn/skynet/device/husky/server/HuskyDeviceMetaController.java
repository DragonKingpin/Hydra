package com.acorn.skynet.device.husky.server;

import com.pinecone.hydra.device.registry.dto.DeviceMetaDTO;
import com.pinecone.hydra.device.registry.server.DeviceManager;
import com.pinecone.hydra.device.registry.server.DeviceMetaService;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;

@Controller
@AddressMapping( "com.acorn.skynet.device.husky.protocol.HuskyDeviceMetaIface." )
public class HuskyDeviceMetaController implements HuskyDeviceController {

    protected final DeviceMetaService deviceMetaService;

    public HuskyDeviceMetaController( DeviceManager deviceManager ) {
        this.deviceMetaService = deviceManager.deviceMetaService();
    }

    @AddressMapping( "queryDeviceMetaByPath" )
    public DeviceMetaDTO queryDeviceMetaByPath( String path ) {
        return this.deviceMetaService.queryDeviceMetaByPath( path );
    }

    @AddressMapping( "queryDeviceMetaByGuid" )
    public DeviceMetaDTO queryDeviceMetaByGuid( String guid ) {
        return this.deviceMetaService.queryDeviceMetaByGuid( guid );
    }

    @AddressMapping( "updateDeviceMetaByPath" )
    public boolean updateDeviceMetaByPath( String path, DeviceMetaDTO meta ) {
        return this.deviceMetaService.updateDeviceMetaByPath( path, meta );
    }

    @AddressMapping( "updateDeviceMetaByGuid" )
    public boolean updateDeviceMetaByGuid( String guid, DeviceMetaDTO meta ) {
        return this.deviceMetaService.updateDeviceMetaByGuid( guid, meta );
    }
}
