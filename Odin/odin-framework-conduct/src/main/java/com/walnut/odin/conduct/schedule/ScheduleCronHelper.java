package com.walnut.odin.conduct.schedule;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.quartz.CronExpression;

import com.pinecone.hydra.task.marshal.TaskScheduleCycle;

public final class ScheduleCronHelper {

    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

    private ScheduleCronHelper() {
    }

    public static String generateDefaultCron( TaskScheduleCycle cycle ) {
        if ( cycle == null ) {
            throw new IllegalArgumentException( "TaskScheduleCycle is null." );
        }

        switch ( cycle ) {
            case Minute: {
                return "0 * * * * ?";
            }
            case Hour: {
                return "0 0 * * * ?";
            }
            case Day: {
                return "0 0 0 * * ?";
            }
            case Week: {
                return "0 0 0 ? * MON";
            }
            case Month: {
                return "0 0 0 1 * ?";
            }
            default: {
                throw new IllegalStateException( "Unsupported cycle: " + cycle );
            }
        }
    }

    public static LocalDateTime alignToCycleStart( TaskScheduleCycle cycle, LocalDateTime referenceTime ) {
        if ( cycle == null ) {
            throw new IllegalArgumentException( "TaskScheduleCycle is null." );
        }

        if ( referenceTime == null ) {
            throw new IllegalArgumentException( "Reference time is null." );
        }

        switch ( cycle ) {
            case Minute: {
                return referenceTime
                        .withSecond( 0 )
                        .withNano( 0 );
            }
            case Hour: {
                return referenceTime
                        .withMinute( 0 )
                        .withSecond( 0 )
                        .withNano( 0 );
            }
            case Day: {
                LocalDate date = referenceTime.toLocalDate();
                return date.atStartOfDay();
            }
            case Week: {
                LocalDate date = referenceTime
                        .toLocalDate()
                        .with( DayOfWeek.MONDAY );
                return date.atStartOfDay();
            }
            case Month: {
                LocalDate date = referenceTime
                        .withDayOfMonth( 1 )
                        .toLocalDate();
                return date.atStartOfDay();
            }
            default: {
                throw new IllegalStateException( "Unsupported cycle: " + cycle );
            }
        }
    }

    public static LocalDateTime advanceByCycle( TaskScheduleCycle cycle, LocalDateTime currentTime ) {
        if ( cycle == null ) {
            throw new IllegalArgumentException( "TaskScheduleCycle is null." );
        }

        if ( currentTime == null ) {
            throw new IllegalArgumentException( "Current time is null." );
        }

        switch ( cycle ) {
            case Minute: {
                return currentTime.plusMinutes( 1 );
            }
            case Hour: {
                return currentTime.plusHours( 1 );
            }
            case Day: {
                return currentTime.plusDays( 1 );
            }
            case Week: {
                return currentTime.plusWeeks( 1 );
            }
            case Month: {
                return currentTime.plusMonths( 1 );
            }
            default: {
                throw new IllegalStateException( "Unsupported cycle: " + cycle );
            }
        }
    }


    public static LocalDateTime computeNextScheduleTime(
            TaskScheduleCycle cycle, LocalDateTime nextScheduleTime, LocalDateTime referenceTime
    ) {
        if ( cycle == null ) {
            throw new IllegalArgumentException( "TaskScheduleCycle is null." );
        }

        if ( referenceTime == null ) {
            throw new IllegalArgumentException( "Reference time is null." );
        }

        if ( nextScheduleTime == null ) {
            return ScheduleCronHelper.alignToCycleStart( cycle, referenceTime );
        }

        if ( nextScheduleTime.isAfter( referenceTime ) ) {
            return nextScheduleTime;
        }

        LocalDateTime advanced = nextScheduleTime;

        while ( !advanced.isAfter( referenceTime ) ) {
            advanced = ScheduleCronHelper.advanceByCycle( cycle, advanced );
        }

        return advanced;
    }

    public static LocalDateTime computeNextByCron( String cron, LocalDateTime currentFireTime ) {
        try {
            CronExpression expression = new CronExpression( cron );
            Date next = expression.getNextValidTimeAfter( Date.from(
                    currentFireTime
                            .atZone( DEFAULT_ZONE_ID )
                            .toInstant()
            ));
            if ( next == null ) {
                return null;
            }

            return LocalDateTime.ofInstant(
                    next.toInstant(),
                    DEFAULT_ZONE_ID
            );

        }
        catch ( ParseException e ) {
            throw new IllegalStateException(
                    "Invalid cron expression: " + cron,
                    e
            );
        }
    }

}