package com.pinecone.framework.system.prototype;

public interface Summoner extends Pinenut {
    Object summon( String szClassPath, Object... args ) throws Exception ;
}
