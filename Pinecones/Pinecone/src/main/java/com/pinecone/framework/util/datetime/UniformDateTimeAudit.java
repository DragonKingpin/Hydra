package com.pinecone.framework.util.datetime;

import com.pinecone.framework.system.prototype.Pinenut;

import java.time.LocalDateTime;

public interface UniformDateTimeAudit extends Pinenut {
    UniformDateTimeAudit DefaultAudit = new GenericMultiFormDateTimeAudit();

    boolean matches     ( String szDateTime, LocalDateTime targetTime ) ;

    boolean betweenSec  ( String szDateTime, LocalDateTime targetTime, int nSecondAccuracy ) ;

    boolean betweenMin  ( String szDateTime, LocalDateTime targetTime, int nMinuteAccuracy ) ;

    boolean between     ( String szDateTime, LocalDateTime targetTime, int nMillisAccuracy ) ;

    default boolean matches    ( String szDateTime ) {
        return this.matches( szDateTime, LocalDateTime.now() );
    }

    default boolean betweenSec ( String szDateTime, int nSecondAccuracy ) {
        return this.betweenSec( szDateTime, LocalDateTime.now(), nSecondAccuracy );
    }

    default boolean betweenMin ( String szDateTime, int nMinuteAccuracy ) {
        return this.betweenMin( szDateTime, LocalDateTime.now(), nMinuteAccuracy );
    }

    default boolean between    ( String szDateTime, int nMillisAccuracy ) {
        return this.between( szDateTime, LocalDateTime.now(), nMillisAccuracy );
    }
}
