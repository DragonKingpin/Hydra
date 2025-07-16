package com.pinecone.hydra.service.registry.client;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;

@Controller
@AddressMapping("com.pinecone.hydra.service.registry.client.ClientLifecycleIface.")
public class ClientLifecycleController implements Pinenut {

    // 这里有个疑问，客户端只要接收通知就行吗？真正的实例在service，客户端只能在部署上做文章
    boolean destroyServiceInstance( GUID instanceGuid ) {
        System.exit(0);
        return true;
    }
}
