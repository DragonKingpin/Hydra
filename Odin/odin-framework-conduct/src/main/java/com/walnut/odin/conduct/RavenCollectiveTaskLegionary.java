package com.walnut.odin.conduct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.system.construction.Postpone;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.StringUtils;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UniformProcessManager;
import com.pinecone.hydra.uma.DuplexAppointClient;
import com.pinecone.hydra.umc.wolf.client.UlfClient;
import com.walnut.odin.conduct.entity.RegimentJoinRequest;
import com.walnut.odin.conduct.entity.RegimentJoinResponse;
import com.walnut.odin.proc.RemoteProcessServiceRPCException;
import com.walnut.odin.proc.client.RavenRemoteProcessManagerClient;
import com.walnut.odin.proc.client.RemoteProcessManagerClient;

public class RavenCollectiveTaskLegionary implements CollectiveTaskLegionary {

    protected String                           mszNodeName;
    protected RemoteProcessManagerClient       mRemoteProcessManagerClient;
    protected ProcessManager                   mLocalProcessManager;
    protected ProcessorLifecycleIface          mProcessLifecycleIface;

    protected Logger                           mLogger;

    protected RavenCollectiveTaskLegionary( ProcessManager processManager, @Postpone RemoteProcessManagerClient pmClient, String szNodeName ) {
        this.mszNodeName                 = szNodeName;
        this.mLocalProcessManager        = processManager;
        this.mRemoteProcessManagerClient = pmClient;
        this.mLogger                     = LoggerFactory.getLogger( this.getClass() );
    }

    public RavenCollectiveTaskLegionary( String szNodeName, ProcessManager processManager, RemoteProcessManagerClient pmClient ) {
        this( processManager, pmClient, szNodeName );
    }

    public RavenCollectiveTaskLegionary( String szNodeName, Processum superiorProcess, UlfClient rpcClient ) {
        this(
                new UniformProcessManager(
                        superiorProcess, null, ( szNodeName + "-process-manager" ).toLowerCase(), "", null
                ),
                null,
                szNodeName
        );

        this.mRemoteProcessManagerClient = new RavenRemoteProcessManagerClient( this.mLocalProcessManager, rpcClient );
    }


    @Override
    public String getName() {
        return this.mszNodeName;
    }

    @Override
    public long getClientId() {
        return this.mRemoteProcessManagerClient.getClientId();
    }

    @Override
    public ProcessManager processManager() {
        return this.mLocalProcessManager;
    }

    @Override
    public void startService () throws RemoteProcessServiceRPCException {
        this.mRemoteProcessManagerClient.startService();

        DuplexAppointClient duplexAppointClient = this.mRemoteProcessManagerClient.duplexAppointClient();
        duplexAppointClient.compile( ProcessorLifecycleIface.class,false );
        this.mProcessLifecycleIface = duplexAppointClient.getIface( ProcessorLifecycleIface.class );
    }

    @Override
    public RegimentJoinResponse joinRegiment() throws RegimentException {
        RegimentJoinRequest request = new RegimentJoinRequest();
        request.setClientId( this.mRemoteProcessManagerClient.getClientId() );
        request.setNodeName( this.mszNodeName );
        RegimentJoinResponse response = this.mProcessLifecycleIface.joinRegiment( request );
        if ( response == null ) {
            throw new RegimentException( "response is null" );
        }
        else if ( StringUtils.isNoneEmpty( response.getErrorMsg() ) ) {
            throw new RegimentException( response.getErrorMsg() );
        }

        this.mLogger.info(
                "[NewProcessorRegister] " +
                "( name:`{}`, clientId:`{}`, clusterPath:`{}`, priority:`{}`, queueMaxCapacity:`{}`, runtimeCapacity:`{}` ) " +
                "<RegimentServerAck>",

                response.getName(), response.getControlClientId(), response.getClusterPath(), response.getPriority(),
                response.getQueueMaxCapacity(), response.getQueueRuntimeInstanceCapacity()
        );
        return response;
    }

}
