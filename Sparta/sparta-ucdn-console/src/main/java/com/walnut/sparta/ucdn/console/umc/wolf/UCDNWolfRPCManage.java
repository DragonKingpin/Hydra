package com.walnut.sparta.ucdn.console.umc.wolf;

import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.service.registry.ServiceLifecycleIface;
import com.pinecone.hydra.service.registry.ServiceMetaManipulationIface;
import com.pinecone.hydra.service.registry.UniformServiceManager;
import com.pinecone.hydra.uma.DuplexAppointClient;
import com.pinecone.hydra.uma.HuskyDuplexExpress;
import com.pinecone.hydra.uma.wolf.WolvesAppointServer;
import com.pinecone.hydra.umc.wolf.server.WolfMCServer;
import com.walnut.sparta.ucdn.console.infrastructure.UOFSContentDelivery;

public class UCDNWolfRPCManage implements WolfRPCManage {
    private WolfMCServer                    wolfKing;

    private WolvesAppointServer             wolfServer;

    private UniformServiceManager           serviceManager;

    private ServicesInstrument              servicesInstrument;

    private DuplexAppointClient             wolfClient;

    private ServiceLifecycleIface           lifecycleIFace;

    private ServiceMetaManipulationIface     mateIFace;

    public UCDNWolfRPCManage(ServicesInstrument servicesInstrument, UOFSContentDelivery uofsContentDelivery, DuplexAppointClient wolfClient) throws Exception {
        this.servicesInstrument = servicesInstrument;
        this.wolfKing = new WolfMCServer( "", uofsContentDelivery, new JSONMaptron("{host: \"0.0.0.0\",\n" +
                "port: 5777, SocketTimeout: 800, KeepAliveTimeout: 3600, MaximumConnections: 1e6}") );
        this.wolfServer = new WolvesAppointServer( wolfKing, HuskyDuplexExpress.class );
        this.serviceManager = new UniformServiceManager( servicesInstrument, wolfServer );
        wolfKing.execute();

        this.wolfClient = wolfClient;
        this.wolfClient.execute();
        this.wolfClient.compile( ServiceLifecycleIface.class, false );
        this.wolfClient.compile( ServiceMetaManipulationIface.class, false );

        this.lifecycleIFace = this.wolfClient.getIface( ServiceLifecycleIface.class );
        this.mateIFace  = this.wolfClient.getIface( ServiceMetaManipulationIface.class );
    }

    @Override
    public WolvesAppointServer getWolfServer() {
        return this.wolfServer;
    }

    @Override
    public ServicesInstrument getServicesInstrument() {
        return this.servicesInstrument;
    }

    @Override
    public DuplexAppointClient getDuplexAppointClient() {
        return this.wolfClient;
    }

    @Override
    public ServiceLifecycleIface getLifecycleIFace() {
        return this.lifecycleIFace;
    }

    @Override
    public ServiceMetaManipulationIface getMateIFace() {
        return this.mateIFace;
    }
}
