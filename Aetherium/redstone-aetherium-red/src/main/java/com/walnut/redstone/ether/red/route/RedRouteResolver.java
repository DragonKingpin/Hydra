package com.walnut.redstone.ether.red.route;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.red.RedSchemes;
import com.walnut.redstone.ether.red.uri.RedNamespace;
import com.walnut.redstone.ether.red.uri.RedUri;
import com.walnut.redstone.ether.resource.ResourceNamespace;
import com.walnut.redstone.ether.resource.ResourcePath;
import com.walnut.redstone.ether.resource.ResourceProjection;
import com.walnut.redstone.ether.resource.ResourceRoute;

public class RedRouteResolver implements Pinenut {
    public RedResolveResult resolve( RedUri uri ) {
        RedResolveResult ret = new RedResolveResult();
        ret.setUri( uri );
        if ( uri.getNamespace() == RedNamespace.Object ) {
            ret.setRouteType( RedRouteType.Object );
            ret.setBackend( "S3ifiedObject" );
            ret.setTarget( RedSchemes.Red + "://" + uri.getBucket() + uri.getPath() );
            this.attachRoute( ret, ResourceNamespace.Object, uri.getBucket(), uri.getPath(), "S3ifiedObject", "Object" );
        }
        else if ( uri.getNamespace() == RedNamespace.Reserved ) {
            ret.setRouteType( RedRouteType.Reserved );
            ret.setBackend( "RedControl" );
            ret.setTarget( uri.getPath() );
            this.attachRoute( ret, ResourceNamespace.Reserved, null, uri.getPath(), "RedControl", "Reserved" );
        }
        else {
            ret.setRouteType( RedRouteType.Kernel );
            ret.setBackend( "KernelProjection" );
            ret.setTarget( uri.getPath() );
            this.attachRoute( ret, ResourceNamespace.Kernel, null, uri.getPath(), "KernelProjection", "Kernel" );
        }
        return ret;
    }

    protected void attachRoute(
            RedResolveResult result,
            ResourceNamespace namespace,
            String bucket,
            String path,
            String backend,
            String projectionType
    ) {
        ResourcePath resourcePath = new ResourcePath( namespace, bucket, path );

        ResourceRoute route = new ResourceRoute();
        route.setNamespace( namespace );
        route.setBackend( backend );
        route.setRouteMode( result.getRouteType().name() );
        route.setResourcePath( resourcePath );

        ResourceProjection projection = new ResourceProjection();
        projection.setSource( result.getUri().getRaw() );
        projection.setTarget( result.getTarget() );
        projection.setProjectionType( projectionType );
        projection.setRoute( route );

        result.setRoute( route );
        result.setProjection( projection );
    }
}
