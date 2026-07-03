package com.device.auto;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.tritium.Tritium;

public class TestDeviceLegionary {

    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object... cfg) -> {
            DeviceLegionaryTestTask task = (DeviceLegionaryTestTask) Pinecone.sys()
                    .getTaskManager()
                    .add( new DeviceLegionaryTestTask( args, Pinecone.sys() ) );
            task.vitalize();
            return 0;
        }, (Object[]) args );
    }

    public static class DeviceLegionaryTestTask extends Tritium {

        protected String[] args;

        public DeviceLegionaryTestTask( String[] args, CascadeSystem parent ) {
            super( args, "DeviceLegionaryTestTask", parent );
            this.args = args;
        }

        @Override
        public void vitalize() throws Exception {
            DeviceLegionarySmokeCase smokeCase = new DeviceLegionarySmokeCase( this );
            DeviceLegionaryHuskyAutoReconnectDevilCase huskyAutoReconnectCase =
                    new DeviceLegionaryHuskyAutoReconnectDevilCase( this );
            DeviceLegionaryDetachedGraceDevilCase detachedGraceCase =
                    new DeviceLegionaryDetachedGraceDevilCase( this );
            DeviceLegionaryOwnerLifecycleDevilCase ownerLifecycleCase =
                    new DeviceLegionaryOwnerLifecycleDevilCase( this );

            if ( this.shouldRun( "smoke" ) ) {
                smokeCase.run( new HuskyAutoReconnectDeviceLegionaryScenario() );
            }
            if ( this.shouldRun( "husky-auto" ) ) {
                huskyAutoReconnectCase.run( 10 );
            }
            if ( this.shouldRun( "detached" ) ) {
                detachedGraceCase.run();
            }
            if ( this.shouldRun( "owner" ) ) {
                ownerLifecycleCase.run();
            }
        }

        protected boolean shouldRun( String transport ) {
            if ( this.args == null || this.args.length == 0 ) {
                return true;
            }

            for ( String arg : this.args ) {
                if ( arg == null ) {
                    continue;
                }
                if ( "all".equalsIgnoreCase( arg ) || transport.equalsIgnoreCase( arg ) ) {
                    return true;
                }
            }
            return false;
        }
    }
}
