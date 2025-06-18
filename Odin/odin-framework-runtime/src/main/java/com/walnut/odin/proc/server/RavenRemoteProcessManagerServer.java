package com.walnut.odin.proc.server;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.uma.DuplexAppointServer;
import com.pinecone.hydra.uma.HuskyDuplexExpress;
import com.pinecone.hydra.uma.wolf.WolvesAppointServer;
import com.pinecone.hydra.umc.wolf.server.WolfMCServer;
import com.walnut.odin.proc.OdinRemoteProcess;
import com.walnut.odin.proc.RavenRemoteProcess;
import com.walnut.odin.proc.dto.UProcessDTO;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RavenRemoteProcessManagerServer implements RemoteProcessManagerServer {
    protected Processum                                 mParentProcess;

    protected GuidAllocator                             mGuidAllocator;

    protected Map<Long, OdinRemoteProcess>              mPMCMap;

    protected Map<GUID, Long>                           mPMCMappingGuid;

    protected DuplexAppointServer                       mWolvesAppointServer;

    public RavenRemoteProcessManagerServer( Processum parentProcess, GuidAllocator guidAllocator ) {
        this.mPMCMap = new ConcurrentHashMap<>();
        this.mPMCMappingGuid = new ConcurrentHashMap<>();
        this.mParentProcess     = parentProcess;
        this.mGuidAllocator = guidAllocator;
        initServer();
    }

    private void initServer() {
        WolfMCServer wolfKing = new WolfMCServer( "", this.mParentProcess, new JSONMaptron("{host: \"0.0.0.0\",\n" +
                "port: 5777, SocketTimeout: 800, KeepAliveTimeout: 3600, MaximumConnections: 1e6}") );
        this.mWolvesAppointServer = new WolvesAppointServer( wolfKing, HuskyDuplexExpress.class );
        PMSController pmsController = new PMSController(this);
        this.mWolvesAppointServer.registerController( pmsController );
        try {
            this.mWolvesAppointServer.execute();
            this.mWolvesAppointServer.compile( PMSMethodIface.class, false );
        } catch (Exception e) {
            // todo 暂时先抛RuntimeException
            throw new RuntimeException(e);
        }
    }

    @Override
    public void registerProcess(long pmcId, UProcessDTO processDTO) {
        String name = processDTO.getName();
        GUID guid = this.mGuidAllocator.parse(processDTO.getGuid());
        long pid = processDTO.getPID();
        String startupArguments = processDTO.getStartupArguments();
        String environmentVariables = processDTO.getEnvironmentVariables();

        RavenRemoteProcess remoteProcess = new RavenRemoteProcess(this, name, pid, guid);

        Debug.trace("注册client：" + pmcId);
        this.mPMCMap.put( pmcId, remoteProcess );
        this.mPMCMappingGuid.put( guid, pmcId );
    }

    @Override
    public void start(GUID processId) throws IOException {
        Long pmcId = this.mPMCMappingGuid.get(processId);
        this.mWolvesAppointServer.invokeInform( pmcId, "com.walnut.odin.proc.server.PMSMethodIface.start", processId);
    }
}
