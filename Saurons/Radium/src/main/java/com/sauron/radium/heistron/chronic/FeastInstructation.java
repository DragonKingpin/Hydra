package com.sauron.radium.heistron.chronic;

import com.pinecone.framework.util.datetime.UniformDateTimeAudit;
import com.pinecone.hydra.auto.ArchInstructation;
import com.pinecone.hydra.auto.ContinueException;
import com.sauron.radium.heistron.Heistum;
import com.sauron.radium.heistron.orchestration.Instructations;

import java.time.LocalDateTime;
import java.util.List;

public class FeastInstructation extends ArchInstructation {
    private Heistum                 mHeistum;
    private List<String >           mChronicPeriods;
    private UniformDateTimeAudit    mDateTimeAudit;

    public FeastInstructation( Heistum heistum, List<String > chronicPeriods ) {
        this.mHeistum        = heistum;
        this.mChronicPeriods = chronicPeriods;
        this.mDateTimeAudit  = UniformDateTimeAudit.DefaultAudit;

        Instructations.infoConformed( heistum, this );
    }

    public FeastInstructation( PeriodicHeistRehearsal kernel ) {
        this( kernel.getParentHeist(), kernel.getRawChronicPeriods() );
    }

    @Override
    public void execute() throws Exception {
        LocalDateTime currentTime   = LocalDateTime.now();

        boolean isFeastTime = false;

        for ( String period : this.mChronicPeriods ) {
            if ( this.mDateTimeAudit.matches( period, currentTime ) ) {
                isFeastTime = true;
                break;
            }
        }

        if ( !isFeastTime ) {
            this.infoStarvation("It`s time to feast?", "Slumber" );
            throw new ContinueException();
        }
        this.infoStarvation("It`s time to feast?", "Berserking" );
    }


    protected FeastInstructation infoStarvation( String szWhat, String szStateOrExtra ) {
        this.mHeistum.tracer().info( "[Starvation] [{}] <{}>", szWhat, szStateOrExtra );
        return this;
    }
}
