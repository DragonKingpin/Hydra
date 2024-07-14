package com.sauron.radium.heistron.chronic;

import com.pinecone.framework.util.StringUtils;
import com.pinecone.framework.util.datetime.GenericMultiFormDateTimeAudit;
import com.pinecone.framework.util.datetime.StorageDateTime;
import com.pinecone.framework.util.datetime.UniformDateTimeAudit;
import com.pinecone.hydra.auto.ArchInstructation;
import com.pinecone.hydra.auto.ContinueException;
import com.sauron.radium.heistron.Heistum;
import com.sauron.radium.heistron.orchestration.Instructations;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Force slumber to prevent excessive actions within the same time period.
 */
public class SedationInstructation extends ArchInstructation {
    private Heistum                 mHeistum;
    private List<String >           mChronicPeriods;
    private UniformDateTimeAudit    mDateTimeAudit;

    public SedationInstructation( Heistum heistum, List<String > chronicPeriods ) {
        this.mHeistum        = heistum;
        this.mChronicPeriods = chronicPeriods;
        this.mDateTimeAudit  = UniformDateTimeAudit.DefaultAudit;

        Instructations.infoConformed( heistum, this );
    }

    public SedationInstructation( PeriodicHeistRehearsal kernel ) {
        this( kernel.getParentHeist(), kernel.getRawChronicPeriods() );
    }


    // Increments the first non-negative component(wildcard) from the smallest to the largest unit
    // (nano, second, minute, hour, day, month, year)
    protected LocalDateTime firstJumpOutTime( String period, LocalDateTime now ) {
        StorageDateTime dateTime        = GenericMultiFormDateTimeAudit.fromString( period );

        if ( dateTime.getNano() != -1 ) {
            dateTime.setNano( dateTime.getNano() + 1 );
        }
        else if ( dateTime.getSecond() != -1 ) {
            dateTime.setSecond( dateTime.getSecond() + 1 );
        }
        else if ( dateTime.getMinute() != -1 ) {
            dateTime.setMinute( dateTime.getMinute() + 1 );
        }
        else if ( dateTime.getHour() != -1 ) {
            dateTime.setHour( dateTime.getHour() + 1 );
        }
        else if ( dateTime.getDayOfMonth() != -1 ) {
            dateTime.setDay( dateTime.getDayOfMonth() + 1 );
        }
        else if ( dateTime.getMonthValue() != -1 ) {
            dateTime.setMonth( dateTime.getMonthValue() + 1 );
        }
        else if ( dateTime.getYear() != -1 ) {
            dateTime.setYear( dateTime.getYear() + 1 );
        }

        return GenericMultiFormDateTimeAudit.toLocalDateTime( dateTime, now );
    }

    @Override
    public void execute() throws Exception {
        LocalDateTime currentTime   = LocalDateTime.now();

        for ( String period : this.mChronicPeriods ) {
            if ( this.mDateTimeAudit.matches( period, currentTime ) ) {
                StorageDateTime dateTime        = GenericMultiFormDateTimeAudit.fromString( period );
                LocalDateTime previous          = GenericMultiFormDateTimeAudit.toLocalDateTime( dateTime, currentTime );
                LocalDateTime next              = this.firstJumpOutTime( period, currentTime );
                long differenceInMillis         = Math.abs( ChronoUnit.MILLIS.between( next, previous ) );
                if( differenceInMillis != 0 ) {
                    this.infoStarvation("Activates Sedative [Entrance] [ForceSlumber: " + (double)differenceInMillis / 1000d + "s]", "Activated" );
                    Thread.sleep( differenceInMillis );
                    this.infoStarvation("Deactivates Sedative [Slumber] [CurrentTime: " + LocalDateTime.now() + "]", "Deactivated" );
                }
                break;
            }
        }

        this.infoStarvation("Aborts Sedative [Slumber]", "Abort" );
    }


    protected SedationInstructation infoStarvation( String szWhat, String szStateOrExtra ) {
        this.mHeistum.tracer().info( "[Starvation] [{}] <{}>", szWhat, szStateOrExtra );
        return this;
    }
}
