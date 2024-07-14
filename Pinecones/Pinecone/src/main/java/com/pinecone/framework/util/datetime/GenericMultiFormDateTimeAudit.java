package com.pinecone.framework.util.datetime;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenericMultiFormDateTimeAudit implements UniformDateTimeAudit {
    public GenericMultiFormDateTimeAudit() {

    }

    @Override
    public boolean matches   ( String szDateTime, LocalDateTime targetTime ) {
        StorageDateTime dateTime = GenericMultiFormDateTimeAudit.fromString( szDateTime );

        // Extract components from LocalDateTime
        int year        = targetTime.getYear();
        int month       = targetTime.getMonthValue();
        int dayOfMonth  = targetTime.getDayOfMonth();
        int hour        = targetTime.getHour();
        int minute      = targetTime.getMinute();
        int second      = targetTime.getSecond();
        int nano        = targetTime.getNano();

        return this.matchesDateTime( year, month, dayOfMonth, hour, minute, second, nano, dateTime );
    }

    @Override
    public boolean betweenSec ( String szDateTime, LocalDateTime targetTime, int nSecondAccuracy ) {
        StorageDateTime dateTime        = GenericMultiFormDateTimeAudit.fromString( szDateTime );

        LocalDateTime localizedDateTime = GenericMultiFormDateTimeAudit.toLocalDateTime( dateTime, targetTime );
        long differenceInSeconds = ChronoUnit.SECONDS.between( localizedDateTime, targetTime );
        return Math.abs( differenceInSeconds ) <= nSecondAccuracy;
    }

    @Override
    public boolean betweenMin ( String szDateTime, LocalDateTime targetTime, int nMinuteAccuracy ) {
        StorageDateTime dateTime        = GenericMultiFormDateTimeAudit.fromString( szDateTime );

        LocalDateTime localizedDateTime = GenericMultiFormDateTimeAudit.toLocalDateTime( dateTime, targetTime );
        long differenceInMinutes        = ChronoUnit.SECONDS.between( localizedDateTime, targetTime );
        return Math.abs( differenceInMinutes ) <= nMinuteAccuracy * 60;
    }

    @Override
    public boolean between   ( String szDateTime, LocalDateTime targetTime, int nMillisAccuracy ) {
        StorageDateTime dateTime        = GenericMultiFormDateTimeAudit.fromString( szDateTime );

        LocalDateTime localizedDateTime = GenericMultiFormDateTimeAudit.toLocalDateTime( dateTime, targetTime );
        long differenceInMillis = ChronoUnit.MILLIS.between( localizedDateTime, targetTime );
        return Math.abs(differenceInMillis) <= nMillisAccuracy;
    }


    public static StorageDateTime fromString    ( String szDateTime ) {
        String szStandardizeDateTime = GenericMultiFormDateTimeAudit.standardize( szDateTime );
        if( szStandardizeDateTime == null ) {
            throw new IllegalArgumentException( "Datetime should be fmt `????-??-?? ??:??:??.???`" );
        }

        return GenericMultiFormDateTimeAudit.parseDateTime( szStandardizeDateTime );
    }

    // Usage methodology: Fill in all wildcard characters from `targetTime`.
    public static LocalDateTime toLocalDateTime ( StorageDateTime storageDateTime, LocalDateTime targetTime ) {
        StorageDate date = storageDateTime.getDate();
        StorageTime time = storageDateTime.getTime();

        int year   = date.getYear()   != -1   ? date.getYear()   : targetTime.getYear();
        int month  = date.getMonth()  != -1   ? date.getMonth()  : targetTime.getMonthValue();
        int day    = date.getDay()    != -1   ? date.getDay()    : targetTime.getDayOfMonth();

        int hour   = time.getHour()   != -1   ? time.getHour()   : targetTime.getHour();
        int minute = time.getMinute() != -1   ? time.getMinute() : targetTime.getMinute();
        int second = time.getSecond() != -1   ? time.getSecond() : targetTime.getSecond();
        int nano   = time.getNano()   != -1   ? time.getNano()   : targetTime.getNano();

        return LocalDateTime.of( year, month, day, hour, minute, second, nano );
    }

    protected boolean matchesDateTime           ( int year, int month, int dayOfMonth, int hour, int minute, int second, int nano, StorageDateTime dateTime ) {
        if ( dateTime.getYear() != -1 && dateTime.getYear() != year ) {
            return false;
        }

        if ( dateTime.getMonthValue() != -1 && dateTime.getMonthValue() != month ) {
            return false;
        }

        if ( dateTime.getDayOfMonth() != -1 && dateTime.getDayOfMonth() != dayOfMonth ) {
            return false;
        }

        if ( dateTime.getHour() != -1 && dateTime.getHour() != hour ) {
            return false;
        }

        if ( dateTime.getMinute() != -1 && dateTime.getMinute() != minute ) {
            return false;
        }

        if ( dateTime.getSecond() != -1 && dateTime.getSecond() != second ) {
            return false;
        }

        if ( dateTime.getNano() != -1 && dateTime.getNano() != nano ) {
            return false;
        }

        return true;
    }

    public static StorageDateTime parseDateTime ( String input ) {
        String[] parts = input.split( "[\\.\\-T:\\s]+" );

        if ( parts.length != 7 ) {
            throw new IllegalArgumentException("Invalid input format: " + input);
        }

        int year   = GenericMultiFormDateTimeAudit.parseComponent( parts[0] );
        int month  = GenericMultiFormDateTimeAudit.parseComponent( parts[1] );
        int day    = GenericMultiFormDateTimeAudit.parseComponent( parts[2] );
        int hour   = GenericMultiFormDateTimeAudit.parseComponent( parts[3] );
        int minute = GenericMultiFormDateTimeAudit.parseComponent( parts[4] );
        int second = GenericMultiFormDateTimeAudit.parseComponent( parts[5] );
        int nano   = GenericMultiFormDateTimeAudit.parseComponent( parts[6] );

        return StorageDateTime.of( year, month, day, hour, minute, second, nano );
    }

    private static int parseComponent           ( String component ) {
        if ( component.matches( "\\?{1,13}" ) ) {
            return -1;
        }
        else {
            return Integer.parseInt(component);
        }
    }

    public static String standardize            ( String input ) {
        if( input.contains("T") ) { // "????-??-??T??:??:??.???"
            input = input.replace( "T", " " );
        }

        boolean bMatchBase = StringUtils.containsOnce( input, "-/" );
        boolean hasColon   = input.contains(":");
        int dot            = 0;
        if( !bMatchBase ) {
            dot = StringUtils.countOccurrencesOf( input, '.', 2 );
        }

        if ( bMatchBase && hasColon || ( dot > 1/* yyyy.mm.dd */ && hasColon ) ) {
            return GenericMultiFormDateTimeAudit.standardizeDateTime( input, false );
        }
        else if ( bMatchBase || ( dot > 1/* yyyy.mm.dd */ || ( dot == 1 && !hasColon /* yyyy.mm*/ ) ) ) {
            return GenericMultiFormDateTimeAudit.standardizeDateTime( input, true ) + " ??:??:??.???";
        }
        else if ( hasColon ) {
            return "????-??-?? " + GenericMultiFormDateTimeAudit.standardizeTime( input );
        }
        else if ( input.equals("?") ) {
            return "????-??-?? ??:??:??.???";
        }

        return null;
    }

    private static String standardizeDateTime   ( String input, boolean bOnlyYear ) {
        Pattern pattern;

        if( bOnlyYear ) {
            pattern = Pattern.compile( "(\\d{1,13}|\\?{1,13})[-/\\.](\\d{1,2}|\\?{1,2})(?:[-/\\.](\\d{1,2}|\\?{1,2}))?" );

        }
        else {
            pattern = Pattern.compile( "(\\d{1,13}|\\?{1,13})[-/\\.](\\d{1,2}|\\?{1,2})(?:[-/\\.](\\d{1,2}|\\?{1,2}))? (\\d{1,2}|\\?{1,2}):(\\d{1,2}|\\?{1,2})(?:\\:(\\d{1,2}|\\?{1,2}))?(?:\\.(\\d{1,10}|\\?{1,10}))?" );
        }

        Matcher matcher = pattern.matcher(input);

        if ( !matcher.matches() ) {
            throw new IllegalArgumentException( "Invalid date-time format: " + input );
        }


        String year    = GenericMultiFormDateTimeAudit.formatComponent( matcher.group(1), 13 );
        String month   = GenericMultiFormDateTimeAudit.formatComponent( matcher.group(2), 2 );
        String day     = GenericMultiFormDateTimeAudit.formatComponent( matcher.group(3), 2 );

        if( bOnlyYear ) {
            return String.format( "%s-%s-%s", year, month, day );
        }
        else {
            String hour    = GenericMultiFormDateTimeAudit.formatComponent( matcher.group(4), 2 );
            String minute  = GenericMultiFormDateTimeAudit.formatComponent( matcher.group(5), 2 );
            String second  = GenericMultiFormDateTimeAudit.formatComponent( matcher.group(6), 2 );
            String nano    = GenericMultiFormDateTimeAudit.formatComponent( matcher.group(7), 10, true );
            return String.format( "%s-%s-%s %s:%s:%s.%s", year, month, day, hour, minute, second, nano );
        }
    }

    private static String standardizeTime       ( String input ) {
        Pattern pattern = Pattern.compile( "(\\d{1,2}|\\?{1,2}):(\\d{1,2}|\\?{1,2})(?:\\:(\\d{1,2}|\\?{1,2}))?(?:\\.(\\d{1,10}|\\?{1,10}))?" );
        Matcher matcher = pattern.matcher(input);

        if ( !matcher.matches() ) {
            throw new IllegalArgumentException( "Invalid time format: " + input );
        }

        String hour   = GenericMultiFormDateTimeAudit.formatComponent( matcher.group(1), 2 );
        String minute = GenericMultiFormDateTimeAudit.formatComponent( matcher.group(2), 2 );
        String second = GenericMultiFormDateTimeAudit.formatComponent( matcher.group(3), 2 );
        String nano   = GenericMultiFormDateTimeAudit.formatComponent( matcher.group(4), 10, true );

        return String.format( "%s:%s:%s.%s", hour, minute, second, nano );
    }

    private static String formatComponent       ( String component, int lengtn ) {
        return GenericMultiFormDateTimeAudit.formatComponent( component, lengtn, false );
    }

    private static String formatComponent       ( String component, int length, boolean bNano ) {
        if( component == null ) {
            component = "?";
        }

        if ( component.contains("?") ) {
            return component;
        }

        int n = Integer.parseInt( component );
        if( bNano ) {
            if( component.length() < 4 ) {
                n = n * 1000000;
            }
        }

        return String.format( "%0" + length + "d", n );
    }

}