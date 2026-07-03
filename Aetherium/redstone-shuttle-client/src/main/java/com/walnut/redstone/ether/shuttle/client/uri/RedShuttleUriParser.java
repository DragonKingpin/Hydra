package com.walnut.redstone.ether.shuttle.client.uri;

import java.net.URI;
import java.util.Locale;
import java.util.Set;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.red.RedSchemes;
import com.walnut.redstone.ether.shuttle.client.RedShuttleClientConfig;
import com.walnut.redstone.ether.shuttle.client.error.RedShuttleUriException;

public class RedShuttleUriParser implements Pinenut {
    protected final Set<String> supportedSchemes = Set.of( RedSchemes.Red, RedSchemes.Uofs, RedSchemes.S3 );
    protected final Set<String> kernelRoots = Set.of(
            "conf",
            "dev",
            "home",
            "mnt",
            "sys",
            "proc",
            "var",
            "meta"
    );
    protected final String mszSystemBucket;

    public RedShuttleUriParser() {
        this( RedShuttleClientConfig.DefaultSystemBucket );
    }

    public RedShuttleUriParser( String szSystemBucket ) {
        if ( this.blank( szSystemBucket ) ) {
            this.mszSystemBucket = RedShuttleClientConfig.DefaultSystemBucket;
        }
        else {
            this.mszSystemBucket = szSystemBucket;
        }
    }

    public RedShuttleUri parse( String szValue ) {
        if ( this.blank( szValue ) ) {
            throw new RedShuttleUriException( "Red shuttle uri is blank." );
        }
        URI uri = URI.create( szValue );
        String szScheme = uri.getScheme();
        if ( this.blank( szScheme ) ) {
            return this.parsePath( szValue );
        }
        szScheme = szScheme.toLowerCase( Locale.ROOT );
        if ( !this.supportedSchemes.contains( szScheme ) ) {
            throw new RedShuttleUriException( "Unsupported red shuttle uri scheme: " + szScheme );
        }

        RedShuttleUri ret = new RedShuttleUri();
        ret.setRaw( szValue );
        ret.setScheme( szScheme );
        ret.setPath( this.normalizePath( uri.getPath() ) );
        this.fillAuthorityUri( ret, uri.getAuthority(), ret.getPath() );
        this.markResourceType( ret );
        return ret;
    }

    protected RedShuttleUri parsePath( String szPath ) {
        RedShuttleUri ret = new RedShuttleUri();
        ret.setRaw( szPath );
        ret.setScheme( RedSchemes.Red );
        ret.setPath( this.normalizePath( szPath ) );
        this.fillPathUri( ret, ret.getPath() );
        this.markResourceType( ret );
        return ret;
    }

    protected void fillAuthorityUri( RedShuttleUri ret, String szAuthority, String szPath ) {
        if ( this.blank( szAuthority ) ) {
            ret.setBucket( this.mszSystemBucket );
            ret.setKey( this.trimLeftSlash( szPath ) );
            ret.setResourceType( RedShuttleResourceType.Kernel );
            return;
        }
        if ( this.isEndpointAuthority( szAuthority ) ) {
            ret.setExplicitEndpoint( true );
            ret.setEndpoint( this.toHttpEndpoint( szAuthority ) );
            this.fillPathUri( ret, szPath );
            return;
        }
        ret.setBucket( szAuthority );
        ret.setKey( this.trimLeftSlash( szPath ) );
    }

    protected void fillPathUri( RedShuttleUri ret, String szPath ) {
        String szNormalizedPath = this.normalizePath( szPath );
        String szBody = this.trimLeftSlash( szNormalizedPath );
        int nSlash = szBody.indexOf( '/' );
        if ( nSlash < 0 ) {
            ret.setBucket( szBody );
            ret.setKey( "" );
            return;
        }
        ret.setBucket( szBody.substring( 0, nSlash ) );
        ret.setKey( szBody.substring( nSlash + 1 ) );
    }

    protected void markResourceType( RedShuttleUri uri ) {
        if ( this.mszSystemBucket.equals( uri.getBucket() ) ) {
            uri.setResourceType( RedShuttleResourceType.Kernel );
            return;
        }
        String szBucket = uri.getBucket();
        if ( this.blank( szBucket ) && this.isKernelKey( uri.getKey() ) ) {
            uri.setBucket( this.mszSystemBucket );
            uri.setResourceType( RedShuttleResourceType.Kernel );
            return;
        }
        if ( this.isKernelKey( szBucket ) ) {
            uri.setBucket( this.mszSystemBucket );
            uri.setKey( this.joinKey( szBucket, uri.getKey() ) );
            uri.setResourceType( RedShuttleResourceType.Kernel );
        }
    }

    protected boolean isEndpointAuthority( String szAuthority ) {
        if ( this.blank( szAuthority ) ) {
            return false;
        }
        return szAuthority.contains( ":" )
                || "localhost".equalsIgnoreCase( szAuthority )
                || szAuthority.contains( "." );
    }

    protected boolean isKernelKey( String szValue ) {
        if ( this.blank( szValue ) ) {
            return false;
        }
        String szBody = this.trimLeftSlash( szValue );
        int nSlash = szBody.indexOf( '/' );
        String szRoot = nSlash < 0 ? szBody : szBody.substring( 0, nSlash );
        return this.kernelRoots.contains( szRoot );
    }

    protected String toHttpEndpoint( String szAuthority ) {
        if ( szAuthority.startsWith( RedSchemes.Http + "://" ) || szAuthority.startsWith( RedSchemes.Https + "://" ) ) {
            return szAuthority;
        }
        return RedSchemes.Http + "://" + szAuthority;
    }

    protected String normalizePath( String szPath ) {
        if ( this.blank( szPath ) ) {
            return "/";
        }
        return szPath.startsWith( "/" ) ? szPath : "/" + szPath;
    }

    protected String trimLeftSlash( String szValue ) {
        String szRet = szValue == null ? "" : szValue;
        while ( szRet.startsWith( "/" ) ) {
            szRet = szRet.substring( 1 );
        }
        return szRet;
    }

    protected String joinKey( String szHead, String szTail ) {
        if ( this.blank( szTail ) ) {
            return szHead;
        }
        return this.trimLeftSlash( szHead ) + "/" + this.trimLeftSlash( szTail );
    }

    protected boolean blank( String szValue ) {
        return szValue == null || szValue.trim().isEmpty();
    }
}
