package com.pinecone.hydra.umc.wolfmc;

public class StandardRemoteUserAuthentication implements MCSecurityAuthentication {
    protected String mszUsername;
    protected String mszDomain;
    protected String mszPassword;

    public StandardRemoteUserAuthentication( String szUsername, String szDomain, String szPassword ) {
        this.mszUsername   = szUsername;
        this.mszDomain     = szDomain;
        this.mszPassword   = szPassword;
    }

    public StandardRemoteUserAuthentication( String szUsername, String szPassword ) {
        this( szUsername, "", szPassword );
    }


    @Override
    public String getUsername() {
        return this.mszUsername;
    }

    @Override
    public void setUsername( String username ) {
        this.mszUsername = username;
    }

    @Override
    public String getDomain() {
        return this.mszDomain;
    }

    @Override
    public void setDomain( String domain ) {
        this.mszDomain = domain;
    }

    @Override
    public String getPassword() {
        return this.mszPassword;
    }

    @Override
    public void setPassword( String password ) {
        this.mszPassword = password;
    }
}

