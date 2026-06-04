package com.acorn.redqueen.service.registry.husky.client.port;

import com.acorn.redqueen.service.registry.husky.client.transformer.HuskyServiceLifecycleTransformer;
import com.acorn.redqueen.service.registry.husky.protocol.ServiceLifecycleIface;
import com.pinecone.hydra.service.registry.instruction.ServiceDeregisterInstruction;
import com.pinecone.hydra.service.registry.instruction.ServiceRegisterInstruction;
import com.pinecone.hydra.service.registry.client.entity.ServiceClientDeregisterResult;
import com.pinecone.hydra.service.registry.client.entity.ServiceClientRegisterResult;
import com.pinecone.hydra.service.registry.client.port.ServiceLifecyclePort;
import com.pinecone.hydra.service.registry.client.transport.ServiceClientTransportException;
import com.pinecone.hydra.service.registry.dto.RegisterServiceDTO;

public class HuskyServiceLifecyclePort implements ServiceLifecyclePort {

    protected long mnClientId;

    protected ServiceLifecycleIface mLifecycleIface;

    protected HuskyServiceLifecycleTransformer mTransformer;

    public HuskyServiceLifecyclePort(
            long nClientId,
            ServiceLifecycleIface lifecycleIface,
            HuskyServiceLifecycleTransformer transformer
    ) {
        this.bind( nClientId, lifecycleIface );
        this.mTransformer = transformer;
    }

    public void bind( long nClientId, ServiceLifecycleIface lifecycleIface ) {
        this.mnClientId = nClientId;
        this.mLifecycleIface = lifecycleIface;
    }

    @Override
    public ServiceClientRegisterResult register(
            ServiceRegisterInstruction instruction
    ) throws ServiceClientTransportException {
        try {
            RegisterServiceDTO dto = this.mTransformer.encodeRegisterInstruction( instruction, this.mnClientId );
            String szInstanceGuid = this.mLifecycleIface.registerService( dto );
            return this.mTransformer.decodeRegisterResult( instruction, szInstanceGuid );
        }
        catch ( Exception e ) {
            throw new ServiceClientTransportException( e );
        }
    }

    @Override
    public ServiceClientDeregisterResult deregister(
            ServiceDeregisterInstruction command
    ) throws ServiceClientTransportException {
        try {
            String szInstanceGuid = this.mTransformer.encodeDeregisterInstruction( command );
            this.mLifecycleIface.deregisterServiceByInstanceId( szInstanceGuid );
            return this.mTransformer.toDeregisterResult( command );
        }
        catch ( Exception e ) {
            throw new ServiceClientTransportException( e );
        }
    }

}

