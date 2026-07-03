package com.walnut.redstone.ether.shuttle.route;

import com.walnut.redstone.ether.red.uri.RedNamespace;
import com.walnut.redstone.ether.red.ReservedPaths;
import com.walnut.redstone.ether.red.uri.RedUri;
import com.walnut.redstone.ether.red.uri.RedUriParser;
import com.walnut.redstone.ether.s3.path.S3PathStyleResolver;
import com.walnut.redstone.ether.shuttle.config.ShuttleConfig;
import com.walnut.redstone.ether.shuttle.config.ShuttleTargetConfig;

public class StaticShuttlePathLocator implements ShuttlePathLocator {
    protected final ShuttleConfig config;
    protected final RedUriParser redUriParser = new RedUriParser();
    protected final S3PathStyleResolver s3PathStyleResolver = new S3PathStyleResolver();

    public StaticShuttlePathLocator( ShuttleConfig config ) {
        this.config = config;
    }

    @Override
    public ShuttleLocateResult locatePath( String path ) {
        String normalized = this.normalizePath( path );
        if ( this.isKernelNamespacePath( normalized ) ) {
            return this.kernel( normalized, null );
        }

        String bucket = this.bucket( normalized );
        String key = this.key( normalized );
        ShuttleTargetConfig target = this.resolveTarget( normalized );
        ShuttleLocateResult ret = this.object( normalized, null, bucket, key, target );
        ret.setRewrittenPath( normalized );
        return ret;
    }

    @Override
    public ShuttleLocateResult locateUri( String uri ) {
        RedUri redUri = this.redUriParser.parse( uri );
        if ( redUri.getNamespace() == RedNamespace.Kernel || redUri.getNamespace() == RedNamespace.Reserved ) {
            return this.kernel( redUri.getPath(), uri );
        }

        String path = "/" + redUri.getBucket() + this.normalizePath( redUri.getPath() );
        ShuttleLocateResult ret = this.locatePath( path );
        ret.setUri( uri );
        return ret;
    }

    protected ShuttleLocateResult object( String path, String uri, String bucket, String key, ShuttleTargetConfig target ) {
        ShuttleLocateResult ret = new ShuttleLocateResult();
        ret.setRouteType( ShuttleRouteType.S3_OBJECT );
        ret.setPath( path );
        ret.setUri( uri );
        ret.setBucket( bucket );
        ret.setKey( key );
        if ( target != null ) {
            ret.setTargetName( target.getName() );
            ret.setTargetBaseUrl( target.getBaseUrl() );
        }
        return ret;
    }

    protected ShuttleLocateResult kernel( String path, String uri ) {
        ShuttleLocateResult ret = new ShuttleLocateResult();
        ret.setRouteType( ShuttleRouteType.KERNEL_NAMESPACE );
        ret.setPath( path );
        ret.setUri( uri );
        ret.setRewrittenPath( this.normalizeKernelPath( path ) );
        return ret;
    }

    protected ShuttleTargetConfig resolveTarget( String path ) {
        ShuttleTargetConfig best = null;
        int bestLength = -1;
        if ( this.config == null || this.config.getTargets() == null ) {
            return null;
        }
        for ( ShuttleTargetConfig target : this.config.getTargets() ) {
            if ( target == null || !target.isEnabled() ) {
                continue;
            }
            int length = this.matchedPrefixLength( path, target );
            if ( length > bestLength ) {
                best = target;
                bestLength = length;
            }
        }
        if ( best != null ) {
            return best;
        }
        String defaultTarget = this.config.getDefaultTarget();
        for ( ShuttleTargetConfig target : this.config.getTargets() ) {
            if ( target != null && target.isEnabled() && this.same( defaultTarget, target.getName() ) ) {
                return target;
            }
        }
        return null;
    }

    protected int matchedPrefixLength( String path, ShuttleTargetConfig target ) {
        if ( target.getPathPrefixes() == null || target.getPathPrefixes().isEmpty() ) {
            return 0;
        }
        int ret = -1;
        for ( String prefix : target.getPathPrefixes() ) {
            String normalized = this.normalizePath( prefix );
            if ( path.equals( normalized ) || path.startsWith( normalized.endsWith( "/" ) ? normalized : normalized + "/" ) ) {
                ret = Math.max( ret, normalized.length() );
            }
        }
        return ret;
    }

    protected boolean isKernelNamespacePath( String path ) {
        return path.equals( ReservedPaths.System )
                || path.startsWith( ReservedPaths.System + "/" )
                || path.startsWith( "/proc/" )
                || path.equals( "/proc" )
                || path.startsWith( "/sys/" )
                || path.equals( "/sys" )
                || path.startsWith( "/mnt/" )
                || path.equals( "/mnt" );
    }

    protected String normalizeKernelPath( String path ) {
        String ret = this.normalizePath( path );
        if ( ret.equals( ReservedPaths.System ) ) {
            return "/";
        }
        if ( ret.startsWith( ReservedPaths.System + "/" ) ) {
            ret = ret.substring( ReservedPaths.System.length() );
        }
        return this.normalizePath( ret );
    }

    protected String bucket( String path ) {
        String value = this.normalizePath( path );
        int next = value.indexOf( "/", 1 );
        String bucket = next < 0 ? value.substring( 1 ) : value.substring( 1, next );
        return this.s3PathStyleResolver.normalizeBucketName( bucket );
    }

    protected String key( String path ) {
        String value = this.normalizePath( path );
        int next = value.indexOf( "/", 1 );
        return next < 0 ? "" : this.s3PathStyleResolver.normalizeKey( value.substring( next + 1 ) );
    }

    protected String normalizePath( String path ) {
        String ret = path == null ? "/" : path.trim();
        if ( ret.isEmpty() ) {
            return "/";
        }
        ret = ret.replace( "\\", "/" );
        while ( ret.contains( "//" ) ) {
            ret = ret.replace( "//", "/" );
        }
        return ret.startsWith( "/" ) ? ret : "/" + ret;
    }

    protected boolean same( String left, String right ) {
        return left != null && left.equals( right );
    }
}
