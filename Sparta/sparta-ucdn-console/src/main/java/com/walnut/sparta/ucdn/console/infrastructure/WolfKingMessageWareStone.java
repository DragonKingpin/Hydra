package com.walnut.sparta.ucdn.console.infrastructure;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.service.registry.ServiceLifecycleIface;
import com.pinecone.hydra.service.registry.ServiceMetaManipulationIface;
import com.pinecone.hydra.system.component.ComponentInitializationException;
import com.pinecone.hydra.uma.DuplexAppointClient;
import com.pinecone.hydra.uma.DuplexAppointServer;
import com.pinecone.hydra.uma.HuskyDuplexExpress;
import com.pinecone.hydra.uma.wolf.WolvesAppointClient;
import com.pinecone.hydra.uma.wolf.WolvesAppointServer;
import com.pinecone.hydra.umb.kafka.WolfMCKafkaClient;
import com.pinecone.hydra.umb.rocket.WolfMCRocketClient;
import com.pinecone.hydra.umb.wolf.UlfBroadcastControlNode;
import com.pinecone.hydra.umb.wolf.WolfMCBClient;
import com.pinecone.hydra.umc.wolf.client.UlfClient;
import com.pinecone.hydra.umc.wolf.client.WolfMCClient;
import com.pinecone.hydra.umc.wolf.server.UlfServer;
import com.pinecone.hydra.umc.wolf.server.WolfMCServer;
import com.pinecone.hydra.umct.WolfMCExpress;
import com.pinecone.radium.Radium;
import com.walnut.redstone.messge.PrimaryMessageWareStone;
import com.walnut.sparta.ucdn.console.ufm.FileMultiDistributionIface;
import com.walnut.sparta.ucdn.console.ufm.SessionValidator;

public class WolfKingMessageWareStone implements PrimaryMessageWareStone {
    protected DuplexAppointServer      wolfKingAppointServer;

    protected DuplexAppointClient      wolfAppointClient;

    protected UlfBroadcastControlNode  primaryKafkaClient;

    protected UlfBroadcastControlNode  primaryRocketClient;

    protected Processum                parentProcess;

    public WolfKingMessageWareStone( Processum parentProcess ) throws ComponentInitializationException {
        this.parentProcess = parentProcess;

        this.initSelf();
    }

    private void initPrimaryAppointClientSegment() throws Exception {
        UlfClient embedRPCClient = new WolfMCClient( 2048, "PrimaryWolfMCClient", this.getSystem(), this.getSystem().getMiddlewareDirector().getMiddlewareConfig().queryJSONObject( "Messagers.Messagers.WolfMCKingpin" ) );
        this.wolfAppointClient = new WolvesAppointClient( embedRPCClient );

        this.wolfAppointClient.compile( ServiceLifecycleIface.class, false );
        this.wolfAppointClient.compile( ServiceMetaManipulationIface.class, false );
    }

    private void initPrimaryAppointServerSegment() throws Exception {
        UlfServer embedRPCServer = new WolfMCServer( "WolfKingMCServer", this.getSystem(), new JSONMaptron("{host: \"0.0.0.0\",\n" +
                "port: 5777, SocketTimeout: 800, KeepAliveTimeout: 3600, MaximumConnections: 1e6}") );
        this.wolfKingAppointServer = new WolvesAppointServer( embedRPCServer, HuskyDuplexExpress.class );
        //this.serviceManager = new UniformServiceManager( serviceInstrument, wolfServer );
    }

    private void initPrimaryBroadcastSegment() throws Exception {
        this.primaryKafkaClient = new WolfMCBClient(new WolfMCKafkaClient(UCDNConstants.KafkaServer), "", this.getSystem(), WolfMCExpress.class);
        this.primaryKafkaClient.compile( FileMultiDistributionIface.class,false );

        this.primaryRocketClient = new WolfMCBClient(new WolfMCRocketClient(UCDNConstants.RocketServer,UCDNConstants.UCDNFileServiceTransmitGroup), "", this.getSystem(), WolfMCExpress.class);
        this.primaryRocketClient.compile(SessionValidator.class,false);
    }

    private void initSelf() throws ComponentInitializationException {
        try {
            this.initPrimaryAppointServerSegment();
            this.initPrimaryAppointClientSegment();
            this.initPrimaryBroadcastSegment();
        }
        catch ( Exception e ) {
            throw new ComponentInitializationException( e );
        }
    }

    @Override
    public Processum getParentProcess() {
        return this.parentProcess;
    }

    @Override
    public DuplexAppointServer getWolfKingAppointServer() {
        return this.wolfKingAppointServer;
    }

    @Override
    public DuplexAppointClient getWolfAppointClient() {
        return this.wolfAppointClient;
    }

    @Override
    public UlfBroadcastControlNode getPrimaryKafkaClient() {
        return this.primaryKafkaClient;
    }

    @Override
    public UlfBroadcastControlNode getPrimaryRocketClient() {
        return this.primaryRocketClient;
    }

    @Override
    public Radium getSystem() {
        return (Radium)this.parentProcess.getSystem();
    }
}
