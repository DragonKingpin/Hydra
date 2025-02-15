package com.util;

import com.pinecone.Pinecone;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.datetime.compact.CompactTimeUnit32;
import com.pinecone.framework.util.datetime.compact.CompactTimestamp32;

public class TestCompactTimestamp {
    public static void testTimestamp32() {
        int encodedMs   = CompactTimestamp32.encode( 123, CompactTimeUnit32.MILLISECONDS );
        int encodedSec  = CompactTimestamp32.encode( 60, CompactTimeUnit32.SECONDS       );
        int encodedMin  = CompactTimestamp32.encode( 30, CompactTimeUnit32.MINUTES       );
        int encodedHour = CompactTimestamp32.encode( 12, CompactTimeUnit32.HOURS         );
        int encodedDay  = CompactTimestamp32.encode( 1, CompactTimeUnit32.DAYS           );
        int encodedInf  = CompactTimestamp32.INFINITE;

        Debug.trace( "Milliseconds: " + CompactTimestamp32.toMilliseconds(encodedMs));
        Debug.trace( "Seconds: "      + CompactTimestamp32.toMilliseconds(encodedSec));
        Debug.trace( "Minutes: "      + CompactTimestamp32.toMilliseconds(encodedMin));
        Debug.trace( "Hours: "        + CompactTimestamp32.toMilliseconds(encodedHour));
        Debug.trace( "Days: "         + CompactTimestamp32.toMilliseconds(encodedDay));
        Debug.trace( "Infinite: "     + CompactTimestamp32.toMilliseconds(encodedInf));
    }

    public static void testTimestamp32_norm() {
        long[] testValues = { 123, 60_000, 3_600_000, 86_400_000, 500_000_000_000L };

        for ( long millis : testValues ) {
            int encoded = CompactTimestamp32.fromMilliseconds( millis );
            System.out.printf(
                    "Millis: %d -> Encoded: %s -> Normalized: %d ms\n",
                    millis, CompactTimestamp32.format( encoded ), CompactTimestamp32.toMilliseconds( encoded )
            );
        }
    }

    public static void main( String[] args ) throws Exception {
        //String szJson = FileUtils.readAll("J:/120KWordsPhonetics.json5");
        Pinecone.init( (Object...cfg )->{

            TestCompactTimestamp.testTimestamp32();
            TestCompactTimestamp.testTimestamp32_norm();

            return 0;
        }, (Object[]) args );
    }
}
