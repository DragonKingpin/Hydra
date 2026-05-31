package com.rpc;

import com.acorn.redqueen.service.conduct.RedCollectiveServiceRegiment;
import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.grpc.client.GrpcAppointClient;
import com.pinecone.hydra.grpc.client.GrpcClientConfig;
import com.pinecone.hydra.grpc.server.GrpcAppointServer;
import com.pinecone.hydra.grpc.server.GrpcServerConfig;
import com.pinecone.hydra.service.ibatis.hydranium.ServiceMappingDriver;
import com.pinecone.hydra.service.kom.UniformServiceInstrument;
import com.pinecone.hydra.service.registry.server.UniformServiceManager;
import com.pinecone.hydra.service.registry.server.transport.ServiceControlTransport;
import com.acorn.redqueen.service.registry.grpc.server.GrpcServiceControlTransportFactory;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ClientMuster;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrame;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrameType;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlGrpc;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.tritium.Tritium;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.ulf.util.guid.i64.GuidAllocator72V2;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

class Brian extends Tritium {
    public Brian( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Brian( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }


    public void vitalize () throws Exception {
        KOIMappingDriver koiMappingDriver = new ServiceMappingDriver(
                this, (IbatisClient)this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );

        UniformServiceInstrument servicesTree = new UniformServiceInstrument( koiMappingDriver );

        UniformServiceManager serviceManager = new UniformServiceManager( servicesTree );
        ServiceControlTransport grpcTransport = new GrpcServiceControlTransportFactory().create(
                serviceManager,
                new GrpcAppointServer( new GrpcServerConfig( new JSONMaptron( "{ port: 5888 }" ) ) )
        );
        serviceManager.transportRegistry().hookTransport( grpcTransport );
        RedCollectiveServiceRegiment serviceRegiment = new RedCollectiveServiceRegiment(this, servicesTree, serviceManager);
        serviceRegiment.startServiceManage();

        GrpcAppointClient client = new GrpcAppointClient(
                new GuidAllocator72V2().nextGUIDi64(),
                new GrpcClientConfig( new JSONMaptron( "{ host: 'localhost', port: 5888 }" ) )
        );
        try {
            client.execute();
            testGrpcControlMuster( client );
        }
        finally {
            client.close();
            serviceManager.terminateService();
        }

    }

    public static void testGrpcControlMuster( GrpcAppointClient client ) throws Exception {
        CountDownLatch readyLatch = new CountDownLatch( 1 );
        AtomicReference<ServiceControlFrame> readyFrame = new AtomicReference<>();
        AtomicReference<Throwable> error = new AtomicReference<>();

        ServiceControlGrpc.ServiceControlStub stub = ServiceControlGrpc.newStub( client.getChannel() );
        StreamObserver<ServiceControlFrame> requestObserver = stub.control( new StreamObserver<ServiceControlFrame>() {
            @Override
            public void onNext( ServiceControlFrame frame ) {
                if ( frame.getFrameType() == ServiceControlFrameType.CLIENT_READY ) {
                    readyFrame.set( frame );
                    readyLatch.countDown();
                    return;
                }
                if ( frame.getFrameType() == ServiceControlFrameType.ERROR ) {
                    readyFrame.set( frame );
                    readyLatch.countDown();
                }
            }

            @Override
            public void onError( Throwable throwable ) {
                error.set( throwable );
                readyLatch.countDown();
            }

            @Override
            public void onCompleted() {
                readyLatch.countDown();
            }
        } );

        requestObserver.onNext(
                ServiceControlFrame.newBuilder()
                        .setFrameGuid( "sparta-test-" + System.nanoTime() )
                        .setClientId( client.getClientId() )
                        .setCreateTimeMillis( System.currentTimeMillis() )
                        .setFrameType( ServiceControlFrameType.CLIENT_MUSTER )
                        .setClientMuster(
                                ClientMuster.newBuilder()
                                        .setClientId( client.getClientId() )
                                        .setClientName( "sparta-test-grpc-service" )
                                        .setTransportVersion( "test" )
                                        .build()
                        )
                        .build()
        );

        if ( !readyLatch.await( 5, TimeUnit.SECONDS ) ) {
            requestObserver.onCompleted();
            throw new IllegalStateException( "Timeout while waiting for gRPC service control CLIENT_READY." );
        }
        if ( error.get() != null ) {
            throw new IllegalStateException( "gRPC service control stream failed.", error.get() );
        }
        if ( readyFrame.get() == null || readyFrame.get().getFrameType() != ServiceControlFrameType.CLIENT_READY ) {
            throw new IllegalStateException( "gRPC service control did not return CLIENT_READY: " + readyFrame.get() );
        }

        Debug.bluef( "gRPC service control ready: " + readyFrame.get().getClientReady().getSessionGuid() );
        requestObserver.onCompleted();
    }
}

public class TestGrpcService {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            Brian brian = (Brian) Pinecone.sys().getTaskManager().add( new Brian( args, Pinecone.sys() ) );
            brian.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
