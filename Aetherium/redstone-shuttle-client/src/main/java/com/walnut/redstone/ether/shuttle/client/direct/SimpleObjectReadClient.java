package com.walnut.redstone.ether.shuttle.client.direct;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.walnut.redstone.ether.shuttle.client.RedShuttleClient;

public class SimpleObjectReadClient implements ObjectReadClient {
    protected final List<ObjectReadStrategy> strategies;

    public SimpleObjectReadClient( RedShuttleClient redShuttleClient ) {
        List<ObjectReadStrategy> entries = new ArrayList<>();
        entries.add( new RedObjectReadStrategy( redShuttleClient ) );
        entries.add( new HttpObjectReadStrategy() );
        this.strategies = Collections.unmodifiableList( entries );
    }

    public SimpleObjectReadClient( List<ObjectReadStrategy> strategies ) {
        if ( strategies == null || strategies.isEmpty() ) {
            throw new IllegalArgumentException( "Object read strategy is required." );
        }
        this.strategies = Collections.unmodifiableList( new ArrayList<>( strategies ) );
    }

    @Override
    public boolean supports( String uri ) {
        return this.supports( ObjectReadRequest.of( uri ) );
    }

    @Override
    public boolean supports( URI uri ) {
        return this.supports( ObjectReadRequest.of( uri ) );
    }

    @Override
    public boolean supports( ObjectReadRequest request ) {
        if ( request == null ) {
            return false;
        }
        for ( ObjectReadStrategy strategy : this.strategies ) {
            if ( strategy.supports( request ) ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ObjectReadStream read( String uri ) {
        return this.read( ObjectReadRequest.of( uri ) );
    }

    @Override
    public ObjectReadStream read( URI uri ) {
        return this.read( ObjectReadRequest.of( uri ) );
    }

    @Override
    public ObjectReadStream read( ObjectReadRequest request ) {
        if ( request == null ) {
            throw new ObjectReadException( "Object read request is required." );
        }
        for ( ObjectReadStrategy strategy : this.strategies ) {
            if ( strategy.supports( request ) ) {
                return strategy.read( request );
            }
        }
        throw new ObjectReadException( "Unsupported object read uri: " + request.getRawUri() );
    }
}
