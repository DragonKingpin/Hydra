package com.pinecone.hydra.umc.wolf;

import com.pinecone.framework.system.prototype.Pinenut;

public interface MCSecurityAuthentication extends Pinenut {
    String getUsername();

    void setUsername( String username );

    String getDomain();

    void setDomain( String domain );

    String getPassword();

    void setPassword( String password );
}