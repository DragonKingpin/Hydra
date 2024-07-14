package com.pinecone.framework.system.prototype;

public interface OverridableFamily extends FamilyContext {

    boolean isOverriddenAffinity();

    void setOverriddenAffinity( boolean overrideAffinity ) ;

}
