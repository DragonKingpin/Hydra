package com.acorn.redqueen.service.registry.husky.protocol;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umct.stereotype.Iface;

@Iface
public interface PassiveServiceManipulatedIface extends Pinenut {

    void shutdownService( String szInstanceGuid, String szReason );

}
