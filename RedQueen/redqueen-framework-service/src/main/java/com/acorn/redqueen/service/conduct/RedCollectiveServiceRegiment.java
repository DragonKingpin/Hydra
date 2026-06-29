package com.acorn.redqueen.service.conduct;

import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.service.registry.ServiceControlException;
import com.pinecone.hydra.service.registry.server.ServiceManager;
import com.pinecone.hydra.service.registry.server.inspection.ServiceControlInspection;
import com.pinecone.hydra.service.registry.server.transport.ServiceControlTransportRegistry;
import com.pinecone.hydra.system.Hydrogen;
import com.acorn.redqueen.service.deletion.RedServiceDirectoryDeleteService;
import com.acorn.redqueen.service.deletion.ServiceDirectoryDeleteService;
import com.acorn.redqueen.service.purge.PurgeService;
import com.acorn.redqueen.service.purge.RedServicePurgeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedCollectiveServiceRegiment implements CollectiveServiceRegiment {

    protected ServiceManager                    mServiceManager;

    protected ServiceInstrument                 mServiceInstrument;

    protected PurgeService                      mPurgeService;

    protected ServiceDirectoryDeleteService     mDirectoryDeleteService;

    protected Hydrogen                          mSystem;

    protected Logger                            mLogger;


    public RedCollectiveServiceRegiment(
            Hydrogen system, ServiceInstrument serviceInstrument,
            ServiceManager serviceManager
    ) {
        this.mSystem                    = system;
        this.mServiceInstrument         = serviceInstrument;
        this.mLogger                    = LoggerFactory.getLogger( "RedCollectiveServiceRegiment" );
        this.mServiceManager            = serviceManager;
        this.mPurgeService              = new RedServicePurgeService( serviceInstrument, serviceManager );
        this.mDirectoryDeleteService    = new RedServiceDirectoryDeleteService( serviceInstrument );
    }

    @Override
    public Logger getLogger() {
        return this.mLogger;
    }

    @Override
    public void startServiceManage() throws ServiceControlException {
        this.mServiceManager.startService();

        this.mLogger.info( "Service control initialized. <Done>" );
    }

    @Override
    public void stopServiceManage() throws ServiceControlException {
        try {
            this.mServiceManager.terminateService();
            this.mLogger.info( "Service control terminated. <Done>" );
        }
        catch ( IllegalStateException e ) {
            throw new ServiceControlException( e );
        }
    }

    @Override
    public ServiceManager serviceManager() {
        return this.mServiceManager;
    }


    @Override
    public ServiceInstrument serviceInstrument() {
        return this.mServiceInstrument;
    }

    @Override
    public ServiceControlTransportRegistry transportRegistry() {
        return this.mServiceManager.transportRegistry();
    }

    @Override
    public ServiceControlInspection inspectServiceControl() {
        return this.mServiceManager.inspectServiceControl();
    }

    @Override
    public PurgeService purgeService() {
        return this.mPurgeService;
    }

    @Override
    public ServiceDirectoryDeleteService directoryDeleteService() {
        return this.mDirectoryDeleteService;
    }
}
