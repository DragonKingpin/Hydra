package com.protobuf;

import java.util.List;

import com.pinecone.hydra.umct.stereotype.Iface;

//@Iface
public interface Raccoon {
    @Iface
    String scratch( String target, int time );

    @Iface
//    default String scratchA( String target, int time, byte[] bs ) {
//        return null;
//    }
    default String scratchA( String target, int time, Rabbit rabbit ) {
        return null;
    }

    @Iface
    default void scratchV( String target, int time ) {

    }

    @Iface
    default Rabbit[] scratchC( String target, int time, Rabbit[] more ) {
        return more;
    }

    @Iface
    default String[] scratchS( String target, int time, String[] more ) {
        return more;
    }

    @Iface
    default List<Rabbit> scratchList( String target, int time, List<Rabbit> more ) {
        return more;
    }

    @Iface
    default boolean scratchPrime( String target, int time ) { return time != 0; }

    @Iface
    default void scratchVoid() {

    }

//    @Iface( name = "scratchF1" )
//    default String scratch( String target, long[] times ) {
//        return null;
//    }
//
//    @Iface
//    default void nil() {
//
//    }
}
