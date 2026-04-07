package com.acorn.redqueen.service.conduct;

import com.pinecone.framework.system.regime.Regiment;
import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.service.registry.ServiceControlException;
import com.pinecone.hydra.service.registry.server.ServiceManager;
import com.pinecone.hydra.system.component.Slf4jTraceable;

public interface CollectiveServiceRegiment extends Regiment, Slf4jTraceable {

    ServiceManager serviceManager();

    ServiceInstrument serviceInstrument();

    void startServiceManage() throws ServiceControlException;
}
