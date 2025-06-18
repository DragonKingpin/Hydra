package com.walnut.odin.proc.client;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.LocalUProcess;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.uma.wolf.WolvesAppointClient;
import com.pinecone.hydra.umc.wolf.client.WolfMCClient;
import com.pinecone.tritium.Tritium;
import com.pinecone.ulf.util.guid.GUIDs;
import com.pinecone.ulf.util.guid.i64.GuidAllocator72;
import com.walnut.odin.proc.RavenRemoteProcess;
import com.walnut.odin.proc.dto.UProcessDTO;

import java.util.Map;

public class RavenLocalProcessManagerClient implements LocalProcessManagerClient {
    protected Processum                 mParentProcess;

    protected ProcessManager            mProcessManager;

    // 注意这个要传入GUID64的构造器
    protected GuidAllocator72           mGuidAllocator;

    protected WolvesAppointClient       mWolfClient;

    protected PMCMethodIface            mPMCMethodIface;

    protected long                      mlsPMCId;

    public RavenLocalProcessManagerClient( Processum parentProcess,ProcessManager processManager, GuidAllocator72 guidAllocator ) {
        this.mParentProcess  = parentProcess;
        this.mProcessManager = processManager;
        this.mGuidAllocator  = guidAllocator;
        this.mlsPMCId = guidAllocator.getGUID64();
        this.initClient();
    }


    private void initClient() {
        Debug.trace("初始化客户端：" + this.mlsPMCId);
        this.mWolfClient = new WolvesAppointClient(
                new WolfMCClient(this.mlsPMCId, "", this.mParentProcess, this.parentSystem().getMiddlewareDirector().getMiddlewareConfig().queryJSONObject( "Messagers.Messagers.WolfMCKingpin" ) )
        );
        try {
            this.mWolfClient.execute();
            this.mWolfClient.compile( PMCMethodIface.class,false );
            this.mPMCMethodIface = this.mWolfClient.getIface( PMCMethodIface.class );
            this.mWolfClient.getRouteDispatcher().registerController( new PMCController( this ) );
            this.mWolfClient.embraces(2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Tritium parentSystem() {
        return (Tritium) this.mParentProcess;
    }

    @Override
    public UProcess createProcess( ExecutionImage image, UProcess parent, Map<String, String[]> startupArgs, Map<String, String[]> contextEnvironmentVars ) {
        LocalUProcess localHostedProcess = this.mProcessManager.createLocalHostedProcess(image, parent, startupArgs, contextEnvironmentVars);

        UProcessDTO uProcessDTO = new UProcessDTO(localHostedProcess.getName(), localHostedProcess.getLocalPID(), localHostedProcess.getGuid().toString());
        this.mPMCMethodIface.createProcess( this.mlsPMCId, uProcessDTO );
        return localHostedProcess;
    }

    @Override
    public void start(String processId) {
        UProcess process = this.mProcessManager.getProcess(GUIDs.GUID128( processId ));
        process.start();
    }

    @Override
    public void test() {
        this.mPMCMethodIface.test();
    }

    private LocalUProcess createLocalHostedProcess(ExecutionImage image, UProcess parent,
                                                   Map<String, String[]> startupArgs) {
        LocalUProcess localHostedProcess = this.mProcessManager.createLocalHostedProcess(image, parent, startupArgs);

        return localHostedProcess;
    }
}
