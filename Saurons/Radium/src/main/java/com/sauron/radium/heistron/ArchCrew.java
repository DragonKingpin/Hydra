package com.sauron.radium.heistron;

import com.sauron.radium.system.MissionTerminateException;
import com.pinecone.framework.system.functions.FunctionTraits;
import com.pinecone.framework.util.json.JSONObject;
import com.sauron.radium.system.RadiumSystem;
import com.sauron.radium.system.StorageSystem;
import org.apache.commons.vfs2.FileSystemManager;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Random;

public abstract class ArchCrew extends LocalCrewnium implements Crew {
    protected Logger      logger;

    protected String      crewInstanceName;

    protected JSONObject  joFailureConf;
    protected int         fileRetrieveTime  = 1          ;
    protected long        fragBase          = 10000      ;  // unit: W
    protected long        fragRange         = 1000000    ;  // unit: 1

    protected Heist       heist;


    public ArchCrew( Heist heist, int nCrewId ){
        super( heist, nCrewId );

        this.heist              = (Heist) this.mParentHeist;
        this.fragBase           = this.heist.fragBase;
        this.fragRange          = this.heist.fragRange;
        this.joFailureConf      = this.heist.getConfig().optJSONObject( "FailureConf" );
        this.fileRetrieveTime   = this.joFailureConf.optInt( "FileRetrieveTime", 1 );

        this.crewInstanceName   = this.className() + this.mnCrewId;
        //this.failureRetryTimes = this.heistCenter.getProtoConfig().getFailureRetryTimes();

        this.logger             = this.getSystem().getTracerScope().newLogger( this.crewInstanceName );
    }

    @Override
    public Heistum parentHeist() {
        return this.heist;
    }

    //根据任务数量获取线程数

    protected String lifecycleTracerSignature() {
        return String.format( "%s::Lifecycle", FunctionTraits.thatName(3) );
    }

    protected ArchCrew traceTaskState( long idx, String szState ) {
        this.logger.info( "[TaskState] <[{},{}], ID:{}> <{}>", this.heist.taskFrom, this.heist.taskTo ,idx, szState );
        return this;
    }

    protected boolean handleTask( long index ){
        this.traceTaskState( index, "Handle" );
        return new Random().nextInt(100)+1>80;
    }

    protected boolean noticeTaskDone ( long index, boolean bIsRecovered ){
        if( bIsRecovered ) {
            this.traceTaskState( index, "Recovered" );
        }
        else {
            this.traceTaskState( index, Heistum.StatusDone );
        }
        //this.heist.getSpoilsLock().countDown();
        return true;
    }

    protected boolean noticeTaskDone ( long index ){
        return this.noticeTaskDone( index, false );
    }


    @Override
    public void startBatchTask() {
        this.mTaskConsumer.consume();
    }

    protected void consumeById( long index ) {
        try {
            this.traceTaskState( index, Heistum.StatusStart );
            this.tryConsumeById( index );
            this.noticeTaskDone( index, false );
        }
        catch ( LootAbortException e ) {
            this.traceTaskState( index, "Abort" );
        }
        catch ( LootRecoveredException e1 ) {
            this.noticeTaskDone( index, true );
        }
        catch ( IllegalStateException e2 ) {
            this.traceTaskState( index, "Error:" + e2.getMessage() );
        }
        catch ( IOException io ) {
            this.traceTaskState( index, "IOException:" + io.getMessage() );
        }
        catch ( MissionTerminateException mte ) {
            this.parentHeist().terminate();
        }
        catch ( Exception e3 ) {
            // Keep this task alive, and ignore other exceptions.
            this.parentHeist().handleAliveException( e3 );
        }
    }

    protected void tryConsumeById( long index ) throws LootRecoveredException, LootAbortException, IllegalStateException, IOException {

    }

    @Override
    public void run() {
        //this.lootFromSignal();
        this.isTimeToFeast();
    }

    @Override
    public String crewName() {
        return this.heist.heistName();
    }

    @Override
    public void validateSpoil( String sz ) {

    }


    @Override
    public RadiumSystem getSystem() {
        return (RadiumSystem) super.getSystem();
    }

    @Override
    public StorageSystem getStorageSystem() {
        return this.getSystem().getStorageSystem();
    }

    public FileSystemManager getDafaultFileSystemManager() {
        return this.getStorageSystem().getFileSystemManager();
    }

    @Override
    public Logger tracer(){
        return this.logger;
    }

    protected void handleAliveException( Exception e ) {
        this.parentHeist().handleAliveException( e );
    }

    protected void handleKillException( Exception e ) throws IllegalStateException {
        this.parentHeist().handleKillException( e );
    }
}

