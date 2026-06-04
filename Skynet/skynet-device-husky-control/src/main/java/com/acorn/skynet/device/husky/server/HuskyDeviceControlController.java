package com.acorn.skynet.device.husky.server;

import com.acorn.skynet.device.husky.protocol.HuskyDeviceDeregisterInstruction;
import com.acorn.skynet.device.husky.protocol.HuskyDeviceDeregisterResult;
import com.acorn.skynet.device.husky.protocol.HuskyDeviceRegisterInstruction;
import com.acorn.skynet.device.husky.protocol.HuskyDeviceRegisterResult;
import com.acorn.skynet.device.husky.transformer.HuskyDeviceControlTransformer;
import com.pinecone.hydra.device.registry.server.connection.DeviceConnection;
import com.pinecone.hydra.device.registry.instruction.DeviceDeregisterInstruction;
import com.pinecone.hydra.device.kom.instance.DeviceInstanceEntry;
import com.pinecone.hydra.device.registry.server.DeviceManager;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;

@Controller
@AddressMapping( "com.acorn.skynet.device.husky.protocol.HuskyDeviceControlIface." )
public class HuskyDeviceControlController implements HuskyDeviceController {

    protected final DeviceManager deviceManager;

    protected final HuskyDeviceControlTransformer transformer;

    public HuskyDeviceControlController( DeviceManager deviceManager ) {
        this.deviceManager = deviceManager;
        this.transformer = new HuskyDeviceControlTransformer( deviceManager.getDeviceInstrument().getGuidAllocator() );
    }

    @AddressMapping( "registerDevice" )
    public HuskyDeviceRegisterResult registerDevice( String connectionId, HuskyDeviceRegisterInstruction instruction ) {
        DeviceInstanceEntry instance = this.deviceManager.deviceRuntimeService().registerDevice(
                this.createConnection( connectionId ),
                this.transformer.decodeRegisterInstruction( instruction )
        );
        return this.transformer.encodeRegisterResult( instance );
    }

    @AddressMapping( "deregisterDevice" )
    public HuskyDeviceDeregisterResult deregisterDevice( String connectionId, HuskyDeviceDeregisterInstruction huskyInstruction ) {
        DeviceDeregisterInstruction instruction = this.transformer.decodeDeregisterInstruction( huskyInstruction );
        this.deviceManager.deviceRuntimeService().deregisterDevice( this.createConnection( connectionId ), instruction );
        return this.transformer.encodeDeregisterResult( instruction );
    }

    @AddressMapping( "detachDevice" )
    public void detachDevice( String connectionId ) {
        this.deviceManager.deviceRuntimeService().detachConnection( this.createConnection( connectionId ) );
    }

    protected DeviceConnection createConnection( String connectionId ) {
        DeviceConnection connection = new DeviceConnection();
        connection.setConnectionId( connectionId );
        connection.setTransportType( "Husky" );
        connection.setConnectedTime( System.currentTimeMillis() );
        return connection;
    }
}
