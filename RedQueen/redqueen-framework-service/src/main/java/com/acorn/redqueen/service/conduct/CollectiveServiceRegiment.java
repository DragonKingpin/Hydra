package com.acorn.redqueen.service.conduct;

import com.acorn.redqueen.service.RedQueenServiceControllerException;
import com.pinecone.framework.system.regime.Regiment;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.service.registry.ClientServiceRegisterException;
import com.pinecone.hydra.service.registry.ServiceControlException;
import com.pinecone.hydra.service.registry.ServiceControlRPCException;
import com.pinecone.hydra.service.registry.ServiceInstanceCreationException;
import com.pinecone.hydra.service.registry.ServiceManager;
import com.pinecone.hydra.service.registry.client.ServiceManagerClient;
import com.pinecone.hydra.system.component.Slf4jTraceable;

public interface CollectiveServiceRegiment extends Regiment, Slf4jTraceable {

    ServiceManager serviceManager();

    ServiceInstrument serviceInstrument();

    void startServiceManage() throws ServiceControlException;
}
