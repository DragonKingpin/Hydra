package com.service.auto;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.system.regime.arch.Lord;
import com.acorn.redqueen.system.ServiceCentralControl;
import com.walnut.archcraft.ender.EnderHydra;

public class TestRedQueenConfigProbe {

    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object... cfg) -> {
            RedQueenConfigProbeTask task = (RedQueenConfigProbeTask) Pinecone.sys()
                    .getTaskManager()
                    .add( new RedQueenConfigProbeTask( args, Pinecone.sys() ) );
            task.vitalize();
            return 0;
        }, (Object[]) args );
    }

    public static class RedQueenConfigProbeTask extends EnderHydra {

        public RedQueenConfigProbeTask( String[] args, CascadeSystem parent ) {
            super( args, "RedQueenConfigProbeTask", parent );
        }

        @Override
        public void vitalize() throws Exception {
            Lord lord = this.getLordFederation().instantiate(
                    "KernelRedQueenLord",
                    "./system/setup/lords/redqueen.json5"
            );
            ServiceCentralControl redQueen = (ServiceCentralControl) lord;
            try {
                redQueen.vitalize();

                this.infoLifecycle(
                        "[RedQueenConfigProbe] RedQueen detached observation should be configured by system/setup/lords/redqueen.json5.",
                        "Done"
                );
            }
            finally {
                redQueen.terminate();
            }
        }
    }
}
