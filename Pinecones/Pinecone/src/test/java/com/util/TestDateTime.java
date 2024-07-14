package com.util;

import com.pinecone.Pinecone;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.datetime.GenericMultiFormDateTimeAudit;

import java.time.LocalDateTime;

public class TestDateTime {
    public static void testAudit() {
        GenericMultiFormDateTimeAudit audit = new GenericMultiFormDateTimeAudit();
        LocalDateTime currentTime = LocalDateTime.of(2024, 6, 24, 1, 2, 3);

        Debug.trace(1,audit.matches("2024-06-24T01:02:03", currentTime)); // true

        Debug.trace(2,audit.matches("2024-06-24 1:02:03", currentTime)); // true

        Debug.trace(3,audit.matches("2024-06-24 1:2:03", currentTime)); // true

        Debug.trace(4,audit.matches("2024-06-24 1:2:3", currentTime)); // true

        Debug.trace(5,audit.matches("2024-06-24 01:2:3", currentTime)); // true

        Debug.trace(6,audit.matches("2024-06-24 01:02:3", currentTime)); // true

        Debug.trace(7,audit.matches("2024-06-24 01:2:03", currentTime)); // true
        Debug.trace(-7,audit.matches("2024-06-24 01:2:02", currentTime));

        Debug.trace(8,audit.matches("2024-06-24", currentTime)); // true

        Debug.trace(9,audit.matches("2024-6-24", currentTime)); // true

        Debug.trace(10,audit.matches("1:02:03", currentTime)); // true

        Debug.trace(11,audit.matches("1:2:03", currentTime)); // true

        Debug.trace(12,audit.matches("1:2:3", currentTime)); // true

        Debug.trace(13,audit.matches("01:2:3", currentTime)); // true

        Debug.trace(14,audit.matches("01:02:3", currentTime)); // true

        Debug.trace(15,audit.matches("01:2:03", currentTime)); // true

        Debug.trace(16,audit.matches("?", currentTime)); // true

        Debug.trace(17,audit.matches("2024-06-24 01:02:??", currentTime)); // true

        Debug.trace(18,audit.matches("2024-06-24 01:??:??", currentTime)); // true

        Debug.trace(19,audit.matches("2024-06-24 ??:??:??", currentTime)); // true

        Debug.trace(20,audit.matches("2024-06-?? ??:??:??", currentTime)); // true

        Debug.trace(21,audit.matches("2024-??-?? ??:??:??", currentTime)); // true

        Debug.trace(22,audit.matches("????-??-?? ??:??:??", currentTime)); // true

        Debug.trace(23,audit.matches("2024-06-24 01:??:03", currentTime)); // true

        Debug.trace(24,audit.matches("2024-??-24 01:??:03", currentTime)); // true

        Debug.trace(25,audit.matches("????-??-24 01:??:03", currentTime)); // true
        Debug.trace(-25,audit.matches("????-??-24 13:??:03", currentTime));

        Debug.trace(26,audit.matches("????-??-24 01:??:03", currentTime)); // true


        Debug.trace(27,audit.matches("01:2", currentTime)); // true
        Debug.trace(-27,audit.matches("01:3", currentTime));

        Debug.trace(28,audit.matches("2024-06-24 01:2", currentTime)); // true
        Debug.trace(-28,audit.matches("2024-06-24 01:03", currentTime));

        Debug.trace(29,audit.matches("2024-06", currentTime)); // true
        Debug.trace(-29,audit.matches("2024-07", currentTime));

        Debug.trace(30,audit.matches("01:?", currentTime)); // true
        Debug.trace(-30,audit.matches("02:?", currentTime));

        Debug.trace(31,audit.matches("2024-06/24 01:2", currentTime)); // true
        Debug.trace(-31,audit.matches("2024/06/24 01:03", currentTime));

        Debug.trace(32,audit.matches("2024.06.24 01:2", currentTime)); // true
        Debug.trace(-32,audit.matches("2024.06.24 01:03", currentTime));

        Debug.trace(33,audit.matches("2024.06", currentTime)); // true
        Debug.trace(-33,audit.matches("2024.07", currentTime));

        Debug.trace(34,audit.matches("2024.06.24", currentTime)); // true
        Debug.trace(-34,audit.matches("2024.06.25", currentTime));
    }

    public static void testAuditAccuracy() {
        GenericMultiFormDateTimeAudit audit = new GenericMultiFormDateTimeAudit();
        LocalDateTime currentTime = LocalDateTime.of(2024, 6, 24, 1, 2, 3);

        Debug.trace(1,audit.betweenSec("2024-06-24 01:??:13", currentTime, 10 ));
        Debug.trace(2,audit.betweenSec("2024-06-24 01:01:58", currentTime, 10 ));
        Debug.trace(3,audit.betweenSec("2024-06-?? 01:01:58", currentTime, 10 ));
        Debug.trace(-3,audit.betweenSec("2024-06-?? 01:02:58", currentTime, 10 ));
        Debug.trace(-3,audit.betweenSec("2024-06-?? 01:02:14", currentTime, 10 ));

        Debug.trace(4,audit.betweenMin("2024-06-?? 01:01:58", currentTime, 1 ));
        Debug.trace(-4,audit.betweenMin("2024-06-?? 01:03:52", currentTime, 1 ));
        Debug.trace(5,audit.betweenMin("2024-06-?? 01:03:02", currentTime, 1 ));
        Debug.trace(6,audit.betweenMin("2024-06-?? 01:03:03", currentTime, 1 ));
        Debug.trace(-6,audit.betweenMin("2024-06-?? 01:03:04", currentTime, 1 ));
//
//        Debug.trace(7,audit.between("2024-06-?? 01:02:04", currentTime, 1000 ));
//        Debug.trace(-7,audit.between("2024-06-?? 01:02:05", currentTime, 1000 ));

        Debug.trace(8,audit.between("2024-06-?? 01:02:02.950", currentTime, 100 ));
        Debug.trace(-8,audit.between("2024-06-?? 01:02:02.850", currentTime, 100 ));


        Debug.trace(9,audit.betweenSec("??:2", currentTime, 10 ));
        Debug.trace(-9,audit.betweenSec("??:3", currentTime, 10 ));
    }

    public static void main( String[] args ) throws Exception {
        //String szJson = FileUtils.readAll("J:/120KWordsPhonetics.json5");
        Pinecone.init( (Object...cfg )->{

            //TestDateTime.testAudit();
            TestDateTime.testAuditAccuracy();

            return 0;
        }, (Object[]) args );
    }
}
