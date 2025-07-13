package com.acorn.redqueen.service.conduct;

import com.acorn.redqueen.service.RedQueenServiceControllerException;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.ProcessManagerSystema;
import com.pinecone.hydra.service.registry.ClientServiceRegisterException;
import com.pinecone.hydra.service.registry.ServiceControlException;
import com.pinecone.hydra.service.registry.ServiceControlRPCException;
import com.pinecone.hydra.service.registry.ServiceInstanceCreationException;
import com.pinecone.hydra.service.registry.client.ServiceManagerClient;
import org.slf4j.Logger;

import com.pinecone.hydra.service.registry.ServiceManager;
import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.system.Hydrogen;
import org.slf4j.LoggerFactory;

public class RedCollectiveServiceRegiment implements CollectiveServiceRegiment {

    protected ServiceManager                    mServiceManager;

    protected ServiceManagerClient              mServiceManagerClient;

    protected ServiceInstrument                 mServiceInstrument;

    protected Hydrogen                          mSystem;

    protected Logger                            mLogger;


    public RedCollectiveServiceRegiment( Hydrogen system, ServiceInstrument serviceInstrument,
                                         ServiceManager serviceManager, ServiceManagerClient serviceManagerClient ) {
        this.mSystem                    = system;
        this.mServiceInstrument         = serviceInstrument;
        this.mServiceManagerClient      = serviceManagerClient;
        this.mServiceManager            = serviceManager;
        this.mLogger                    = LoggerFactory.getLogger( "RedCollectiveServiceRegiment" );
    }

    @Override
    public Logger getLogger() {
        return this.mLogger;
    }

    @Override
    public void startServiceManage() throws ServiceControlException {
        // 这里有个问题，红后作为一个总管，若存在于客户端，客户端没有serviceManager，那要如何初始化，会出现报错
        if( mServiceManager == null || this.mServiceManagerClient == null ) {
            throw new RedQueenServiceControllerException( "serviceManage or serviceManageClient is null" );
        }
        this.mServiceManager.startService();
        this.mServiceManagerClient.startService();

        this.mLogger.info( "RPC init success" );
    }

    @Override
    public ServiceManager serviceManager() {
        return this.mServiceManager;
    }

    @Override
    public ServiceManagerClient serviceManagerClient() {
        return this.mServiceManagerClient;
    }

    @Override
    public ServiceInstrument serviceInstrument() {
        return this.mServiceInstrument;
    }

    @Override
    public void registerService(GUID serviceId, GUID deployGuid) throws ServiceControlException {
        if( this.mServiceManagerClient == null ) {
            throw new RedQueenServiceControllerException( "client is not initialized" );
        }

        this.mServiceManagerClient.registerService( serviceId, deployGuid );
    }
}
