package com.acorn.redqueen.service.conduct;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface CollectiveServiceLegionary extends Pinenut {

    String getName();

    long getClientId();

    GUID getServiceGuid();

    GUID getInstanceGuid();

    void startService() throws ServiceLegionaryException;

    ServiceLegionaryJoinResponse joinRegiment() throws ServiceLegionaryException;

    void requestRejoinRegiment( String reason );

    void deregister( String reason ) throws ServiceLegionaryException;

    void terminateService();

}

