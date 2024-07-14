package com.pinecone.framework.system.construction;

import com.pinecone.framework.system.prototype.Pinenut;

public interface StructureDefinition extends Pinenut {
    String getLookup();
    void setLookup( String lookup );

    Class<?> getType();
    void setType( Class<?> type );

    ReuseCycle getCycle();
    void setCycle( ReuseCycle cycle );

    Structure.AuthenticationType getAuthenticationType();
    void setAuthenticationType( Structure.AuthenticationType authenticationType );

    boolean isShareable();
    void setShareable( boolean shareable );

    String getDescription();
    void setDescription( String description );

    Class<?> getProvide();
    String getProvideMethod();
}
