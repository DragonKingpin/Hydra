package com.acorn.skynet.device.husky.client.controller;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.device.registry.client.control.DeviceClientManipulationHandler;
import com.pinecone.hydra.device.registry.instruction.DeviceShutdownInstruction;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;

@Controller
@AddressMapping( "com.acorn.skynet.device.husky.protocol.PassiveDeviceManipulatedIface." )
public class PassiveDeviceManipulatedController implements Pinenut {

    protected final GuidAllocator guidAllocator;

    protected final DeviceClientManipulationHandler manipulationHandler;

    public PassiveDeviceManipulatedController(
            GuidAllocator guidAllocator,
            DeviceClientManipulationHandler manipulationHandler
    ) {
        this.guidAllocator = guidAllocator;
        this.manipulationHandler = manipulationHandler;
    }

    @AddressMapping( "shutdownDevice" )
    public void shutdownDevice( String instanceGuid, String reason ) {
        if ( this.manipulationHandler == null ) {
            return;
        }

        DeviceShutdownInstruction instruction = new DeviceShutdownInstruction();
        instruction.setInstanceGuid( this.guidAllocator.parse( instanceGuid ) );
        instruction.setReason( reason );
        this.manipulationHandler.shutdownDevice( instruction );
    }
}
