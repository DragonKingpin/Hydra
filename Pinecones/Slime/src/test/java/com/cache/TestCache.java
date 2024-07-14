package com.cache;

import com.pinecone.Pinecone;
import com.pinecone.framework.unit.KeyValue;
import com.pinecone.framework.unit.LinkedTreeMap;
import com.pinecone.framework.unit.Mapnut;
import com.pinecone.framework.unit.MultiValueMapper;
import com.pinecone.framework.unit.multi.MultiSetMaptron;
import com.pinecone.framework.unit.top.*;
import com.pinecone.framework.util.Debug;
import com.pinecone.slime.unitization.PartialOrderRange;
import com.pinecone.slime.unitization.PartialRange;

import java.util.*;

public class TestCache {
    public static void testRange() {
        PartialRange<String > partialOrderRange = new PartialOrderRange<>( "A", "F" );
        Debug.trace( partialOrderRange.contains( "C" ) );
        Debug.trace( partialOrderRange.contains( "G" ) );
    }

    public static void testRangeMap() {
        Map<PartialRange<Long >, Long > map = new LinkedTreeMap<>( PartialRange.DefaultIntervalRangeComparator );

        Debug.trace( (new PartialOrderRange<>(   10L,  20L )).compareTo( 10L ) );
        Debug.trace( "8".compareTo( "9" ) );

        map.put( new PartialOrderRange<>(   0L,  10L ),  10L );
        map.put( new PartialOrderRange<>(  10L,  20L ),  20L );
        map.put( new PartialOrderRange<>(  20L,  30L ),  30L );

        map.put( new PartialOrderRange<>(  40L,  50L ),  50L );


        map.put( new PartialOrderRange<>(  90L, 100L ), 100L );
        map.put( new PartialOrderRange<>(  60L,  70L ),  70L );
        map.put( new PartialOrderRange<>(  80L,  90L ),  90L );


        Debug.trace( map );
        Debug.trace( ((LinkedTreeMap<PartialRange<Long>, Long>) map).treeEntrySet() );

        for ( int i = 0; i < 100; i++ ) {
            Debug.trace( i, map.containsKey( (long)i ) );
        }
    }

    public static void testMultiValueEntity(){
        MultiValueMapper<Integer, Integer > maptron = new MultiSetMaptron<>();
        //MultiValueMapper<Integer, Integer > maptron = new MultiListMaptron<>();
        maptron.add( 1, 10 );
        maptron.add( 2, 20 );
        maptron.add( 2, 22 );
        maptron.add( 2, 21 );
        maptron.add( 2, 22 );

        Debug.trace( maptron, maptron.collection(), maptron.collectionValues() );
    }

    public static void testMultiTreeToptron(){
        MultiTreeToptron<Integer, Integer > toptron = new MultiTreeToptron<>( 8 );
        toptron = new LinkedMultiTreeToptron<>( 8 );

        toptron.add( 1, 10 );
        toptron.add( 1, 11 );
        toptron.add( 4, 40 );
        toptron.add( 1, 12 );
        toptron.add( 2, 20 );
        toptron.add( 3, 30 );
        toptron.add( 3, 31 );
        toptron.add( 5, 50 );
        toptron.add( 6, 60 );

        Debug.trace( toptron.getMap(), toptron.topEntrySet(), toptron.bottomEntrySet(), toptron.collection() );

        toptron.update( 1, 16, 12 );
        Debug.trace( toptron.getMap() );
        //toptron.setTopmostSize( 4 );
        //Debug.trace( toptron.getMap() );
    }

    public static void testTreeToptron(){
        //TreeToptron<Integer, Integer > toptron = new TreeToptron<>( 3 );
        LinkedTreeToptron<Integer, Integer > toptron = new LinkedTreeToptron<>( 3 );

        toptron.put( 1, 10 );
        toptron.put( 5, 50 );
        toptron.put( 1, 11 );
        toptron.put( 1, 12 );
        toptron.put( 2, 20 );
        toptron.put( 3, 30 );
        toptron.put( 3, 30 );
        toptron.put( 4, 40 );
        toptron.put( 6, 60 );

        Debug.trace( toptron.getMap(), toptron.topEntrySet(), toptron.bottomEntrySet() );
    }

    public static void testTopper(){
        Topper<KeyValue<Integer, Integer > > heapTopper = new HeapTopper<>(4, new Comparator<KeyValue<Integer, Integer>>() {
            @Override
            public int compare( KeyValue<Integer, Integer > o1, KeyValue<Integer, Integer > o2 ) {
                return o1.getKey().compareTo( o2.getKey() );
            }
        });
        heapTopper.add( new KeyValue<>( 1,10 ) );
        heapTopper.add( new KeyValue<>( 5,50 ) );
        heapTopper.add( new KeyValue<>( 2,20 ) );
        heapTopper.add( new KeyValue<>( 9,90 ) );
        heapTopper.add( new KeyValue<>( 4,40 ) );
        heapTopper.add( new KeyValue<>( 6,60 ) );
        heapTopper.add( new KeyValue<>( 3,30 ) );

        Debug.trace( heapTopper, heapTopper.nextEviction() );
    }

    public static void main( String[] args ) throws Exception {
        //String szJson = FileUtils.readAll("J:/120KWordsPhonetics.json5");
        Pinecone.init( (Object...cfg )->{

            //TestCache.testRange();
            //TestCache.testRangeMap();
            //TestCache.testMultiValueEntity();
            //TestCache.testMultiTreeToptron();
            //TestCache.testTreeToptron();
            TestCache.testTopper();


            Mapnut<Integer, Long > map = new LinkedTreeMap<>();
            map.put( 4, 40L );
            map.put( 3, 30L );
            Debug.trace( map );
            map.getEntryByKey( 4 ).setValue( 41L );

            Debug.trace( map );

            return 0;
        }, (Object[]) args );
    }
}
