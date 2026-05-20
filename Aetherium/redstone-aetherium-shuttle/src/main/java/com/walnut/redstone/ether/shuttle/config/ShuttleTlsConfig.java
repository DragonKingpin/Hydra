package com.walnut.redstone.ether.shuttle.config;

import com.pinecone.framework.system.prototype.Pinenut;

public class ShuttleTlsConfig implements Pinenut {
    protected boolean trustAll;
    protected boolean verifyHostname = true;
    protected String keyStorePath;
    protected String keyStorePassword;
    protected String trustStorePath;
    protected String trustStorePassword;

    public boolean isTrustAll() {
        return this.trustAll;
    }

    public void setTrustAll( boolean trustAll ) {
        this.trustAll = trustAll;
    }

    public boolean isVerifyHostname() {
        return this.verifyHostname;
    }

    public void setVerifyHostname( boolean verifyHostname ) {
        this.verifyHostname = verifyHostname;
    }

    public String getKeyStorePath() {
        return this.keyStorePath;
    }

    public void setKeyStorePath( String keyStorePath ) {
        this.keyStorePath = keyStorePath;
    }

    public String getKeyStorePassword() {
        return this.keyStorePassword;
    }

    public void setKeyStorePassword( String keyStorePassword ) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getTrustStorePath() {
        return this.trustStorePath;
    }

    public void setTrustStorePath( String trustStorePath ) {
        this.trustStorePath = trustStorePath;
    }

    public String getTrustStorePassword() {
        return this.trustStorePassword;
    }

    public void setTrustStorePassword( String trustStorePassword ) {
        this.trustStorePassword = trustStorePassword;
    }
}
