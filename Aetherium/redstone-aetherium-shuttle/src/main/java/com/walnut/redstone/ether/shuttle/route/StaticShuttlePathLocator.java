package com.walnut.redstone.ether.shuttle.route;

import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.walnut.redstone.ether.red.uri.RedNamespace;
import com.walnut.redstone.ether.red.uri.RedUri;
import com.walnut.redstone.ether.red.uri.RedUriParser;
import com.walnut.redstone.ether.shuttle.config.ShuttleConfig;
import com.walnut.redstone.ether.shuttle.config.ShuttleTargetConfig;
import com.walnut.redstone.ether.shuttle.error.ShuttleErrorCode;
import com.walnut.redstone.ether.shuttle.error.ShuttleException;

public class StaticShuttlePathLocator implements ShuttlePathLocator {
    protected final ShuttleConfig config;
    protected final ShuttleTargetResolver targetResolver;
    protected final RedUriParser redUriParser = new RedUriParser();
    protected final Set<String> mKernelRootMounts = new HashSet<>( Arrays.asList(
            "",
            "conf",
            "dev",
            "home",
            "mnt",
            "sys",
            "proc",
            "var",
            "meta"
    ) );

    public StaticShuttlePathLocator( ShuttleConfig config ) {
        this.config = config;
        this.targetResolver = new ShuttleTargetResolver( config );
    }

    @Override
    public ShuttleLocateResult locatePath( String path ) {
        String normalizedPath = this.normalizePath( path );
        if ( normalizedPath.startsWith( "/__red__" ) ) {
            return this.control( normalizedPath );
        }
        if ( this.isKernelPath( normalizedPath ) ) {
            return this.kernel( normalizedPath, null );
        }
        return this.object( normalizedPath, null );
    }

    @Override
    public ShuttleLocateResult locateUri( String uri ) {
        if ( this.blank( uri ) ) {
            throw new ShuttleException( ShuttleErrorCode.InvalidRequest, "Red URI is blank." );
        }
        RedUri redUri = this.redUriParser.parse( uri );
        if ( redUri.getNamespace() == RedNamespace.Object ) {
            String path = this.normalizePath( "/" + redUri.getBucket() + this.normalizePath( redUri.getPath() ) );
            return this.object( path, uri );
        }
        return this.kernel( this.normalizePath( redUri.getPath() ), uri );
    }

    protected ShuttleLocateResult object( String path, String uri ) {
        ShuttleTargetConfig target = this.resolveTarget( path );
        ShuttleLocateResult ret = new ShuttleLocateResult();
        ret.setRouteType( ShuttleRouteType.S3_OBJECT );
        ret.setPath( path );
        ret.setUri( uri == null ? this.toObjectUri( path ) : uri );
        ret.setBucket( this.bucket( path ) );
        ret.setKey( this.key( path ) );
        ret.setTargetName( target.getName() );
        ret.setTargetBaseUrl( target.getBaseUrl() );
        ret.setRewrittenPath( path );
        ret.setRedirectUrl( this.trimRightSlash( target.getBaseUrl() ) + path );
        return ret;
    }

    protected ShuttleLocateResult kernel( String path, String uri ) {
        ShuttleLocateResult ret = new ShuttleLocateResult();
        ret.setRouteType( ShuttleRouteType.KERNEL_NAMESPACE );
        ret.setPath( path );
        ret.setUri( uri == null ? this.toKernelUri( path ) : uri );
        ret.setBucket( "" );
        ret.setKey( this.kernelKey( path ) );
        ret.setTargetName( "__kernel__" );
        ret.setRewrittenPath( path );
        return ret;
    }

    protected ShuttleLocateResult control( String path ) {
        ShuttleLocateResult ret = new ShuttleLocateResult();
        ret.setRouteType( ShuttleRouteType.CONTROL );
        ret.setPath( path );
        ret.setRewrittenPath( path );
        ret.setSupported( false );
        ret.setReason( "Control path is handled by Red Shuttle." );
        return ret;
    }

    protected ShuttleTargetConfig resolveTarget( String path ) {
        ShuttleTargetConfig best = null;
        int bestLength = -1;
        List<ShuttleTargetConfig> targets = this.config.getTargets();
        if ( targets != null ) {
            for ( ShuttleTargetConfig target : targets ) {
                if ( target == null || !target.isEnabled() ) {
                    continue;
                }
                for ( String prefix : target.getPathPrefixes() ) {
                    String normalizedPrefix = this.normalizePath( prefix );
                    if ( this.matchesPrefix( path, normalizedPrefix ) && normalizedPrefix.length() > bestLength ) {
                        best = target;
                        bestLength = normalizedPrefix.length();
                    }
                }
            }
        }
        return best == null ? this.targetResolver.resolve( this.config.getDefaultTarget() ) : best;
    }

    protected boolean matchesPrefix( String path, String prefix ) {
        if ( "/".equals( prefix ) ) {
            return true;
        }
        return path.equals( prefix ) || path.startsWith( prefix + "/" );
    }

    protected boolean isKernelPath( String path ) {
        return this.mKernelRootMounts.contains( this.bucket( path ) );
    }

    protected String bucket( String path ) {
        String normalizedPath = this.normalizePath( path );
        String body = normalizedPath.substring( 1 );
        int slash = body.indexOf( '/' );
        return slash < 0 ? body : body.substring( 0, slash );
    }

    protected String key( String path ) {
        String normalizedPath = this.normalizePath( path );
        String body = normalizedPath.substring( 1 );
        int slash = body.indexOf( '/' );
        return slash < 0 ? "" : body.substring( slash + 1 );
    }

    protected String toObjectUri( String path ) {
        String bucket = this.bucket( path );
        String key = this.key( path );
        if ( this.blank( bucket ) ) {
            return "red:///";
        }
        return this.blank( key ) ? "red://" + bucket + "/" : "red://" + bucket + "/" + key;
    }

    protected String toKernelUri( String path ) {
        return "red://" + this.normalizePath( path );
    }

    protected String kernelKey( String path ) {
        String normalizedPath = this.normalizePath( path );
        if ( "/".equals( normalizedPath ) ) {
            return "";
        }
        return normalizedPath.substring( 1 );
    }

    protected String normalizePath( String path ) {
        if ( this.blank( path ) ) {
            return "/";
        }
        String ret = path.startsWith( "/" ) ? path : "/" + path;
        return URI.create( "red://local" + ret ).getPath();
    }

    protected String trimRightSlash( String value ) {
        if ( value != null && value.endsWith( "/" ) ) {
            return value.substring( 0, value.length() - 1 );
        }
        return value;
    }

    protected boolean blank( String value ) {
        return value == null || value.trim().isEmpty();
    }
}
