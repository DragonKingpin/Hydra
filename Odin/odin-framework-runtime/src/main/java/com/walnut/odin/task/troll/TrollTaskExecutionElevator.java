package com.walnut.odin.task.troll;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.util.datetime.DatePattern;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.event.ProcessEvent;
import com.pinecone.hydra.proc.event.ProcessEventHandler;
import com.pinecone.hydra.proc.image.EntryPointRunnable;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.proc.image.ImageLoader;
import com.pinecone.hydra.proc.image.ImageModifier;
import com.pinecone.hydra.proc.image.URLImageLoader;
import com.pinecone.hydra.system.component.LogStatuses;
import com.pinecone.hydra.system.component.Slf4jTraceable;
import com.pinecone.hydra.system.ko.MetaPersistenceException;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.kom.instance.InstanceInstrument;
import com.pinecone.hydra.task.marshal.KernelTaskScheduleCycle;
import com.walnut.odin.conduct.CollectiveTaskRegiment;
import com.walnut.odin.proc.ProcessRemoteEventHandler;
import com.walnut.odin.proc.RemoteProcess;
import com.walnut.odin.proc.RemoteVitalizationStatus;
import com.walnut.odin.proc.server.RemoteProcessManagerServer;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.RavenTaskConfig;
import com.walnut.odin.task.RavenTaskInstance;

public class TrollTaskExecutionElevator implements TaskExecutionElevator, Slf4jTraceable {

    protected Logger mLogger;

    protected RemoteProcessManagerServer mRemoteProcessManagerServer;

    protected CollectiveTaskRegiment mCollectiveTaskRegiment;

    protected CentralizedTaskInstrument mTaskInstrument;

    protected InstanceInstrument mInstanceInstrument;

    protected ProcessManager mProcessManager;

    protected RavenTaskConfig mRavenTaskConfig;

    protected DateTimeFormatter mInstanceTitleTimeFormat;

    protected DateTimeFormatter mDefaultDateTimeFormat;

    protected GuidAllocator mGuidAllocator;

    protected ImageModifier mImageModifier;


    public TrollTaskExecutionElevator( CollectiveTaskRegiment taskRegiment ) {
        this.mLogger                      = LoggerFactory.getLogger( this.getClass() );
        this.mRemoteProcessManagerServer  = taskRegiment.remoteProcessManagerServer();
        this.mProcessManager              = taskRegiment.processManager();
        this.mCollectiveTaskRegiment      = taskRegiment;
        this.mTaskInstrument              = taskRegiment.taskInstrument();
        this.mInstanceInstrument          = this.mTaskInstrument.getInstanceInstrument();
        this.mRavenTaskConfig             = (RavenTaskConfig) this.mTaskInstrument.getConfig();
        this.mGuidAllocator               = this.mTaskInstrument.getGuidAllocator();
        this.mInstanceTitleTimeFormat     = DatePattern.createFormatter( this.mRavenTaskConfig.getInstanceTitleTimeFormat() );
        this.mDefaultDateTimeFormat       = DatePattern.createFormatter( this.mRavenTaskConfig.getDefaultDateTimeFormat() );
        this.mImageModifier               = this.mProcessManager.getImageModifier();

        this.infoLifecycle( "Welcome to use Skynet cloud deployment system, Odin Troll task execution system.", LogStatuses.StatusReady );
    }

    @Override
    public Logger getLogger() {
        return this.mLogger;
    }

    @Override
    public ProcessManager processManager() {
        return this.mProcessManager;
    }

    @Override
    public LocalDateTime evalBusinessTime( RavenTaskInstance instance, LocalDateTime biz ) {
        KernelTaskScheduleCycle cycle = instance.getKernelScheduleCycle();

        LocalDateTime adjustedTime;

        switch ( cycle ) {
            case Month:
            case Week:
            case Day: {
                adjustedTime = biz.withHour(0).withMinute(0).withSecond(0).withNano(0);
                break;
            }
            case Hour: {
                adjustedTime = biz.withMinute(0).withSecond(0).withNano(0);
                break;
            }
            case Minute: {
                adjustedTime = biz.withSecond(0).withNano(0);
                break;
            }
            case Undefined:
            default: {
                adjustedTime = biz;
                break;
            }
        }

        return adjustedTime;
    }

    @Override
    public LocalDateTime evalBusinessTime( RavenTaskInstance instance ) {
        return this.evalBusinessTime( instance, LocalDateTime.now() );
    }

    @Override
    public String evalBusinessTimeLabel( RavenTaskInstance instance, LocalDateTime biz ) {
        return this.evalBusinessTime( instance, biz ).format( this.mInstanceTitleTimeFormat );
    }

    @Override
    public String evalBusinessTimeLabel( RavenTaskInstance instance ) {
        return this.evalBusinessTime( instance ).format( this.mInstanceTitleTimeFormat );
    }

    @Override
    public String evalInstanceName( RavenTaskInstance instance, LocalDateTime now, LocalDateTime bizTimeEpoch ) {
        String bizTimeLab     = this.evalBusinessTimeLabel( instance, bizTimeEpoch );
        String execTimeLab    = now.format( this.mInstanceTitleTimeFormat );
        String szInstanceName = String.format(
                "%s_%s_ET_%s",
                instance.getOwnedTask().getName(),
                bizTimeLab,
                execTimeLab
        );
        return szInstanceName;
    }

    @Override
    public String evalInstanceName( RavenTaskInstance instance, LocalDateTime bizTimeEpoch ) {
        return this.evalInstanceName( instance, LocalDateTime.now(), bizTimeEpoch );
    }



    @Override
    public UProcess launch( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException {
        UProcess process = instance.affinityProcess();
        if ( process == null ) {
            instance.startLocalProcess();
            process = instance.affinityProcess();
        }


        return process;
    }

    protected void initializeInstance( RavenTaskInstance instance, LaunchFeature feature ) {
        LocalDateTime now = LocalDateTime.now();
        this.getLogger().info(
                "[TaskLaunchSequence] (TaskName: `{}`, KernelHandleName: `/{}`, TaskGuid: `{}`, Time: `{}`) <Start>",
                instance.getOwnedTask().getName(),
                instance.getOwnedTask().getFullName(),
                instance.getOwnedTask().getId(),
                now.format( this.mDefaultDateTimeFormat )
        );

        String szInstanceName = this.evalInstanceName( instance, now, feature.getBizTimeEpoch() );
        InstanceEntry entry   = instance.getInstanceEntry();

        entry.setInstanceName( szInstanceName );
        String bizTimeLab = this.evalBusinessTimeLabel( instance, feature.getBizTimeEpoch() );
        InstanceEntry previous = this.mInstanceInstrument.findLastExecuted( instance.getTaskGuid(), bizTimeLab );

        int runCount      = 0;
        int sequenceCnt   = 0;
        int retryCnt      = 0;

        if ( previous != null ) {
            runCount = previous.getRunCount() + 1;

            if ( feature.isRetry() ) {
                sequenceCnt = previous.getSequenceCnt();
                retryCnt    = previous.getRetryCnt() + 1;
            }
            else {
                sequenceCnt = previous.getSequenceCnt() + 1;
                retryCnt    = 0;
            }
        }

        if ( previous == null ) {
            runCount    = 1;
            sequenceCnt = 1;
            retryCnt    = 0;
        }

        LocalDateTime bizTime = this.evalBusinessTime( instance, feature.getBizTimeEpoch() );
        entry.setRunCount( runCount );
        entry.setSequenceCnt( sequenceCnt );
        entry.setRetryCnt( retryCnt );
        if ( entry.getGuid() == null ) {
            entry.setGuid( this.mGuidAllocator.nextGUID() );
        }
        entry.setInstanceStatus( TaskInstanceStatus.New );
        entry.setBusinessTime( bizTime );

        this.mTaskInstrument.getInstanceInstrument().addInstance( entry );
        this.getLogger().info(
                "[TaskLaunchSequence] [Schema] (Task: `{}`, InstanceName: `{}`, InsGuid: `{}`, RunCount: {}, SequenceCnt: {}, RetryCnt: {}, RetryMode: {}, BusinessTime: {}) <Ready to elevate>",
                instance.getOwnedTask().getName(),
                szInstanceName,
                entry.getGuid(),
                runCount,
                sequenceCnt,
                retryCnt,
                feature.isRetry(),
                bizTime
        );
    }

    protected void afterProcessLaunched( RavenTaskInstance instance, UProcess process ) throws MetaPersistenceException {
        instance.getInstanceEntry().setInstanceStatus( TaskInstanceStatus.Standby );
        instance.update();
    }

    protected URI evalImageURI( RavenTaskInstance instance, LaunchFeature feature ) {
        URI imageURI = feature.getDesignatedImageURI();
        if ( imageURI == null ) {
            imageURI = instance.getProcessImageURI();
        }

        return imageURI;
    }

    @Override
    public UProcess launchLocally( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException {
        try {
            this.initializeInstance( instance, feature );
            URI imageURI = this.evalImageURI( instance, feature );
            ImageLoader imageLoader = this.mProcessManager.getImageLoader();
            ExecutionImage image;
            UProcess process = null;
            if ( imageLoader instanceof URLImageLoader ) {
                URLImageLoader urlImageLoader = (URLImageLoader) imageLoader;
                image = urlImageLoader.queryExecutionImage( imageURI );
            }
            else {
                image = imageLoader.queryExecutionImage( imageURI.getPath() );
            }

            if ( image == null ) {
                instance.getInstanceEntry().setErrorCause( LaunchErrorCauses.NoSuchImage );
            }
            else {
                instance.getInstanceEntry().setImagePath( imageURI.toString() );
                this.mLogger.info( "[TaskLaunchSequence] [LocalProcessAnchored] (Process: `{}`) <Standby>", imageURI );
                process = this.mProcessManager.createLocalHostedProcess(
                        image, feature.getParentProcess(), feature.getStartupArgs(), feature.getContextEnvironmentVars()
                );
            }

            this.afterProcessLaunched( instance, process );
            return process;
        }
        catch ( Exception e ) {
            throw new InstanceLaunchException( e );
        }
    }

    @Override
    public UProcess launchRemotely( RavenTaskInstance instance, long pmClientId, LaunchFeature feature ) throws InstanceLaunchException {
        try {
            this.initializeInstance( instance, feature );
            URI imageURI = this.evalImageURI( instance, feature );
            RemoteProcess process = null;

            GUID parentPid = null;
            if ( feature.getParentProcess() != null ) {
                parentPid = feature.getParentProcess().getPID();
            }
            else if ( feature.getParentPid() != null ) {
                parentPid = feature.getParentPid();
            }

            instance.getInstanceEntry().setImagePath( imageURI.toString() );
            this.mLogger.info( "[TaskLaunchSequence] [RemoteProcessAnchored] (Process: `{}`, DestinationDeployClient: `{}`) <Standby>", imageURI, pmClientId );
            RemoteProcessManagerServer.RemoteCreationResult result = this.mRemoteProcessManagerServer.createRemoteUProcess(
                    pmClientId, imageURI.toString(), true, parentPid, feature.getStartupArgs(), feature.getContextEnvironmentVars()
            );
            process = result.getProcess();
            if ( result.getResponse().getStatus() != RemoteVitalizationStatus.New.getCode() || process == null ) {
                instance.getInstanceEntry().setErrorCause( LaunchErrorCauses.RemoteProcessCreationFailure );
            }


            this.afterProcessLaunched( instance, process );
            return process;
        }
        catch ( Exception e ) {
            throw new InstanceLaunchException( e );
        }
    }


    protected void afterOwnedProcessTerminated( RavenTaskInstance instance, UProcess process ) {
        try {
            instance.getInstanceEntry().setInstanceStatus( TaskInstanceStatus.Finished );
            instance.getInstanceEntry().setLastEndTime( LocalDateTime.now() );
            instance.update();
        }
        catch ( MetaPersistenceException e ) {
            mLogger.error(
                    "[TaskElevationSequence] [MetaPersistenceException] (Process: `{}`, PID: `{}`) <Error>", process.getName(), process.getPID()
            );
            mLogger.error( "[TaskElevationSequence] [MetaPersistenceException: `{}`]", e );
        }
    }

    protected void afterOwnedProcessStarted( RavenTaskInstance instance, UProcess process ) throws InstanceLaunchException {
        try {
            instance.getInstanceEntry().setInstanceStatus( TaskInstanceStatus.Running );
            instance.getInstanceEntry().setLastStartTime( LocalDateTime.now() );
            instance.update();
        }
        catch ( MetaPersistenceException e ) {
            throw new InstanceLaunchException( e );
        }
    }

    @Override
    public UProcess elevate( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException {
        return null;
    }

    @Override
    public UProcess elevateLocally( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException {
        this.getLogger().info(
                "[TaskElevationSequence] [ElevateLocally] (TaskName: `{}`, KernelHandleName: `/{}`, TaskGuid: `{}`) <Start>",
                instance.getOwnedTask().getName(),
                instance.getOwnedTask().getFullName(),
                instance.getOwnedTask().getId()
        );

        UProcess process = this.launchLocally( instance, feature );
        if ( process == null ) {
            return null;
        }

        this.mLogger.info( "[TaskElevationSequence] [LocalProcessStandby] (Process: `{}`, PID: `{}`) <ElevationServerAck>", process.getName(), process.getPID() );
        this.mLogger.info( "[TaskElevationSequence] [ExecutingVitalizationInstruction] (Process: `{}`, PID: `{}`) <Start>", process.getName(), process.getPID() );


        this.mImageModifier.addSystemProcessEventHandler(process.getExecutionImage().getEntryPoint(), new ProcessEventHandler() {
            @Override
            public void fired( EntryPointRunnable runnable, ProcessEvent event ) {
                if ( event == ProcessEvent.Terminated ) {
                    afterOwnedProcessTerminated( instance, process );

                    mLogger.info(
                            "[TaskElevationSequence] [LocalTaskFinished] (TaskName: `{}`, KernelHandleName: `/{}`, TaskGuid: `{}`) <Done>",
                            instance.getOwnedTask().getName(),
                            instance.getOwnedTask().getFullName(),
                            instance.getOwnedTask().getId()
                    );
                }
            }
        });
        process.start();
        this.afterOwnedProcessStarted( instance, process );

        this.mLogger.info(
                "[TaskElevationSequence] [LocalTaskElevated] (TaskName: `{}`, KernelHandleName: `/{}`, TaskGuid: `{}`, PID: `{}`) <Done>",
                instance.getOwnedTask().getName(),
                instance.getOwnedTask().getFullName(),
                instance.getOwnedTask().getId(),
                process.getPID()
        );
        return process;
    }

    @Override
    public UProcess elevateRemotely( RavenTaskInstance instance, long pmClientId, LaunchFeature feature ) throws InstanceLaunchException {
        this.getLogger().info(
                "[TaskElevationSequence] [ElevateRemotely] (TaskName: `{}`, KernelHandleName: `/{}`, TaskGuid: `{}`) <Start>",
                instance.getOwnedTask().getName(),
                instance.getOwnedTask().getFullName(),
                instance.getOwnedTask().getId()
        );

        UProcess process = this.launchRemotely( instance, pmClientId, feature );
        if ( process == null ) {
            return null;
        }

        this.mLogger.info( "[TaskElevationSequence] [RemoteProcessStandby] (Process: `{}`, PID: `{}`) <ElevationServerAck>", process.getName(), process.getPID() );
        this.mLogger.info( "[TaskElevationSequence] [SendingVitalizationInstruction] (Process: `{}`, PID: `{}`, DestinationClient: `{}`) <Start>", process.getName(), process.getPID(), pmClientId );

        RemoteProcess remoteProcess = (RemoteProcess) process;
        remoteProcess.addRemoteEventHandler(new ProcessRemoteEventHandler() {
            @Override
            public void fired( long pmClientId, ProcessEvent event, Object caused ) {
                if ( event == ProcessEvent.Terminated ) {
                    afterOwnedProcessTerminated( instance, process );

                    mLogger.info(
                            "[TaskElevationSequence] [RemoteTaskFinished] (TaskName: `{}`, KernelHandleName: `/{}`, TaskGuid: `{}`) <Done>",
                            instance.getOwnedTask().getName(),
                            instance.getOwnedTask().getFullName(),
                            instance.getOwnedTask().getId()
                    );
                }
            }
        });
        process.start();
        this.afterOwnedProcessStarted( instance, process );

        this.mLogger.info(
                "[TaskElevationSequence] [RemoteTaskElevated] (TaskName: `{}`, KernelHandleName: `/{}`, TaskGuid: `{}`, PID: `{}`) <Done>",
                instance.getOwnedTask().getName(),
                instance.getOwnedTask().getFullName(),
                instance.getOwnedTask().getId(),
                process.getPID()
        );
        return process;
    }

}
