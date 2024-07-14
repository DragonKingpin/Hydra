package com.sauron.radium.system;

import com.pinecone.framework.util.config.JSONConfig;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.name.Namespace;
import com.pinecone.hydra.auto.ArchInstructation;
import com.pinecone.hydra.auto.Instructation;
import com.pinecone.hydra.auto.PeriodicAutomaton;
import com.pinecone.hydra.auto.PeriodicAutomatron;
import com.pinecone.hydra.system.ArchSystemCascadeComponent;
import com.pinecone.hydra.system.HyComponent;
import com.pinecone.hydra.system.Hydrarum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SystemDaemon extends ArchSystemCascadeComponent implements Saunut, HyComponent {
    protected long                     mnSurveillanceTime;
    protected JSONObject               mDaemonConfig;
    protected PeriodicAutomatron       mAutomatron;
    protected Logger                   mLogger;

    public SystemDaemon( Namespace name, Hydrarum system, HyComponent parent ) {
        super( name, system, system.getComponentManager(), parent );

        this.loadConfig();
        this.mLogger      = LoggerFactory.getLogger( String.format( "Tracer<%s>", this.className() ) );
        this.mAutomatron  = new PeriodicAutomaton( this.className(), system, this.mnSurveillanceTime, true );
        this.mAutomatron.command( new ArchInstructation() {
            boolean mbStarted = false;

            @Override
            public void execute() throws Exception {
                if( !this.mbStarted ) {
                    SystemDaemon.this.infoLifecycle( "DaemonStarted", "Start" );
                    this.mbStarted = true;
                    SystemDaemon.this.mAutomatron.withdraw( this );
                }
            }
        });

        this.getSystem().getTaskManager().add( this.mAutomatron );
        this.mAutomatron.start();
    }

    public SystemDaemon( Hydrarum system, HyComponent parent ) {
        this( null, system, parent );
    }

    public SystemDaemon( Hydrarum system ) {
        this( system, null );
    }

    protected void loadConfig() {
        JSONConfig sys  = (JSONConfig) this.getSystem().getSystemConfig();
        Object jDaemon  = sys.opt( "SystemDaemon" );
        if( jDaemon instanceof String ) {
            try {
                this.mDaemonConfig = sys.fromFile( this.getSystem().getWorkingPath().resolve( (String) jDaemon ).toFile()  );
            }
            catch ( IOException e ) {
                this.getSystem().handleKillException( e );
            }
        }
        else {
            this.mDaemonConfig = (JSONObject) jDaemon;
        }

        this.mnSurveillanceTime = this.mDaemonConfig.optLong( "SurveillanceTime" );
    }

    public PeriodicAutomatron getAutomatron() {
        return this.mAutomatron;
    }

    protected SystemDaemon infoLifecycle( String szWhat, String szStateOrExtra ) {
        this.mLogger.info( "[Lifecycle] [{}] <{}>", szWhat, szStateOrExtra );
        return this;
    }
}
