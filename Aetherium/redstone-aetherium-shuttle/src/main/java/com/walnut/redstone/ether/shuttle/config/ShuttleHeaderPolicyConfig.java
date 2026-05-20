package com.walnut.redstone.ether.shuttle.config;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.pinecone.framework.system.prototype.Pinenut;

public class ShuttleHeaderPolicyConfig implements Pinenut {
    protected boolean forwardHost;
    protected boolean forwardAuthorization;
    protected boolean forwardCookie;
    protected boolean forwardRange = true;
    protected boolean forwardContentType = true;
    protected boolean forwardContentLength = true;
    protected List<String> blockedHeaders = new ArrayList<>();

    public boolean isForwardHost() {
        return this.forwardHost;
    }

    public void setForwardHost( boolean forwardHost ) {
        this.forwardHost = forwardHost;
    }

    public boolean isForwardAuthorization() {
        return this.forwardAuthorization;
    }

    public void setForwardAuthorization( boolean forwardAuthorization ) {
        this.forwardAuthorization = forwardAuthorization;
    }

    public boolean isForwardCookie() {
        return this.forwardCookie;
    }

    public void setForwardCookie( boolean forwardCookie ) {
        this.forwardCookie = forwardCookie;
    }

    public boolean isForwardRange() {
        return this.forwardRange;
    }

    public void setForwardRange( boolean forwardRange ) {
        this.forwardRange = forwardRange;
    }

    public boolean isForwardContentType() {
        return this.forwardContentType;
    }

    public void setForwardContentType( boolean forwardContentType ) {
        this.forwardContentType = forwardContentType;
    }

    public boolean isForwardContentLength() {
        return this.forwardContentLength;
    }

    public void setForwardContentLength( boolean forwardContentLength ) {
        this.forwardContentLength = forwardContentLength;
    }

    public List<String> getBlockedHeaders() {
        return this.blockedHeaders;
    }

    public void setBlockedHeaders( List<String> blockedHeaders ) {
        this.blockedHeaders = blockedHeaders == null ? new ArrayList<>() : new ArrayList<>( blockedHeaders );
    }

    public Set<String> blockedHeaderSet() {
        Set<String> ret = new LinkedHashSet<>();
        for ( String header : this.blockedHeaders ) {
            if ( header != null && !header.trim().isEmpty() ) {
                ret.add( header.toLowerCase( Locale.ROOT ) );
            }
        }
        if ( !this.forwardHost ) {
            ret.add( "host" );
        }
        if ( !this.forwardAuthorization ) {
            ret.add( "authorization" );
        }
        if ( !this.forwardCookie ) {
            ret.add( "cookie" );
        }
        if ( !this.forwardRange ) {
            ret.add( "range" );
        }
        if ( !this.forwardContentType ) {
            ret.add( "content-type" );
        }
        if ( !this.forwardContentLength ) {
            ret.add( "content-length" );
        }
        return ret;
    }
}
