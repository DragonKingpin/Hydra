package com.sauron.radium.heistron;

import com.sauron.radium.system.RadiumSystem;
import org.slf4j.Logger;

public abstract class ArchHeistum extends HeistEntity implements Heistum {
    protected Heistgram         mHeistgram ;
    protected Logger            mLogger;

    protected ArchHeistum( Heistgram heistgram ) {
        super();
        this.mHeistgram   = heistgram;
        this.mLogger      = this.getSystem().getTracerScope().newLogger( this.className() );
    }

    @Override
    public Logger tracer() {
        return this.mLogger;
    }

    protected ArchHeistum infoLifecycle( String szWhat, String szStateOrExtra ) {
        this.tracer().info( "[Lifecycle] [{}] <{}>", szWhat, szStateOrExtra );
        return this;
    }

    protected ArchHeistum infoLifecycle( String szStateOrExtra ) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        return this.infoLifecycle( stackTraceElements[ 2 ].getMethodName(), szStateOrExtra );
    }


    @Override
    public Heistgram getHeistgram() {
        return this.mHeistgram;
    }

    public RadiumSystem getSystem() {
        return (RadiumSystem) this.getHeistgram().getSystem();
    }

    /**
     * These exceptions will not interrupt the running state of single moulder, but only log them.
     */
    @Override
    public void handleAliveException( Exception e ) {
        this.tracer().info( "[{}] <AliveError:{}>", this.heistName(), e.getMessage() );
    }

    /**
     * These exceptions will kill the running state of single moulder, and interrupt and redirect to RuntimeException.
     */
    @Override
    public void handleKillException( Exception e ) throws IllegalStateException {
        this.tracer().info( "[{}] <KillError:{}>", this.heistName(), e.getMessage() );
        throw new IllegalStateException( e );
    }

    @Override
    public abstract Crew newCrew( int nCrewId ) ;

}
