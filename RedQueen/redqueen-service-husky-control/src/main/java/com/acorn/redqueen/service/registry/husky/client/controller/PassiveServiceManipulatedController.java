package com.acorn.redqueen.service.registry.husky.client.controller;

import com.acorn.redqueen.service.registry.husky.protocol.PassiveServiceManipulatedIface;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.service.registry.client.control.ServiceClientManipulationHandler;
import com.pinecone.hydra.service.registry.client.control.ServiceClientShutdownInstruction;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;

@Controller
@AddressMapping( "com.acorn.redqueen.service.registry.husky.protocol.PassiveServiceManipulatedIface." )
public class PassiveServiceManipulatedController implements Pinenut {

    protected GuidAllocator mGuidAllocator;

    protected ServiceClientManipulationHandler mManipulationHandler;

    public PassiveServiceManipulatedController(
            GuidAllocator guidAllocator,
            ServiceClientManipulationHandler manipulationHandler
    ) {
        this.mGuidAllocator = guidAllocator;
        this.mManipulationHandler = manipulationHandler;
    }

    @AddressMapping( "shutdownService" )
    public void shutdownService( String szInstanceGuid, String szReason ) {
        if ( this.mManipulationHandler == null ) {
            return;
        }

        ServiceClientShutdownInstruction instruction = new ServiceClientShutdownInstruction();
        instruction.setInstanceGuid( this.mGuidAllocator.parse( szInstanceGuid ) );
        instruction.setReason( szReason );
        this.mManipulationHandler.shutdownService( instruction );
    }

}
