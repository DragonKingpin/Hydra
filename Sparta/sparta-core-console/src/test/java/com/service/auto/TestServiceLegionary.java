package com.service.auto;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.tritium.Tritium;

public class TestServiceLegionary {

    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object... cfg) -> {
            ServiceLegionaryTestTask task = (ServiceLegionaryTestTask) Pinecone.sys()
                    .getTaskManager()
                    .add( new ServiceLegionaryTestTask( args, Pinecone.sys() ) );
            task.vitalize();
            return 0;
        }, (Object[]) args );
    }

    public static class ServiceLegionaryTestTask extends Tritium {

        protected String[] mArgs;

        public ServiceLegionaryTestTask( String[] args, CascadeSystem parent ) {
            super( args, "ServiceLegionaryTestTask", parent );
            this.mArgs = args;
        }

        @Override
        public void vitalize() throws Exception {
            ServiceLegionarySmokeCase smokeCase = new ServiceLegionarySmokeCase( this );
            ServiceLegionaryReconnectDevilCase devilCase = new ServiceLegionaryReconnectDevilCase( this );
            ServiceLegionaryIdempotencyDevilCase idempotencyCase = new ServiceLegionaryIdempotencyDevilCase( this );
            ServiceLegionaryHuskyAutoReconnectDevilCase huskyAutoReconnectCase =
                    new ServiceLegionaryHuskyAutoReconnectDevilCase( this );
            ServiceLegionaryDetachedGraceDevilCase detachedGraceCase =
                    new ServiceLegionaryDetachedGraceDevilCase( this );
            ServiceLegionaryExpiredDevilCase expiredCase = new ServiceLegionaryExpiredDevilCase( this );
            ServiceLegionaryOfflineDevilCase offlineCase = new ServiceLegionaryOfflineDevilCase( this );

            if ( this.shouldRun( "grpc" ) ) {
                smokeCase.run( new GrpcServiceLegionaryScenario() );
                devilCase.run( new GrpcServiceLegionaryScenario(), 10 );
                idempotencyCase.run( new GrpcServiceLegionaryScenario(), 10 );
            }
            if ( this.shouldRun( "husky" ) ) {
                smokeCase.run( new HuskyServiceLegionaryScenario() );
                devilCase.run( new HuskyServiceLegionaryScenario(), 10 );
                idempotencyCase.run( new HuskyServiceLegionaryScenario(), 10 );
            }
            if ( this.shouldRun( "detached" ) ) {
                detachedGraceCase.run( new GrpcServiceLegionaryScenario(), 5 );
                detachedGraceCase.run( new HuskyServiceLegionaryScenario(), 5 );
            }
            if ( this.shouldRun( "expired" ) ) {
                expiredCase.run( new GrpcServiceLegionaryScenario( false ) );
                expiredCase.run( new HuskyServiceLegionaryScenario() );
            }
            if ( this.shouldRun( "offline" ) ) {
                offlineCase.run( new GrpcServiceLegionaryScenario( false ) );
                offlineCase.run( new HuskyServiceLegionaryScenario() );
            }
            if ( this.shouldRun( "husky-auto" ) ) {
                huskyAutoReconnectCase.run( 10 );
            }
        }

        protected boolean shouldRun( String szTransport ) {
            if ( this.mArgs == null || this.mArgs.length == 0 ) {
                return true;
            }

            for ( String szArg : this.mArgs ) {
                if ( szArg == null ) {
                    continue;
                }
                if ( "all".equalsIgnoreCase( szArg ) || szTransport.equalsIgnoreCase( szArg ) ) {
                    return true;
                }
            }
            return false;
        }
    }
}
