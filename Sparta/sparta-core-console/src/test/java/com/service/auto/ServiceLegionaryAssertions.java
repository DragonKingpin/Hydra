package com.service.auto;

import java.util.List;
import java.util.function.BooleanSupplier;

import com.acorn.redqueen.service.conduct.RedCollectiveServiceLegionary;
import com.acorn.redqueen.service.conduct.ServiceLegionaryState;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.registry.dto.ServiceMetaDTO;

public class ServiceLegionaryAssertions implements Pinenut {

    public static void assertNotNull( Object value, String szMessage ) {
        if ( value == null ) {
            throw new IllegalStateException( szMessage );
        }
    }

    public static void assertTrue( boolean bValue, String szMessage ) {
        if ( !bValue ) {
            throw new IllegalStateException( szMessage );
        }
    }

    public static void assertOnline( RedCollectiveServiceLegionary legionary ) {
        assertNotNull( legionary, "Legionary is null." );
        assertTrue(
                legionary.getState() == ServiceLegionaryState.Online,
                "Legionary should be Online, actual => " + legionary.getState()
        );
        assertNotNull( legionary.getInstanceGuid(), "Legionary instance guid is null." );
    }

    public static void assertTerminated( RedCollectiveServiceLegionary legionary ) {
        assertNotNull( legionary, "Legionary is null." );
        assertTrue(
                legionary.getState() == ServiceLegionaryState.Terminated,
                "Legionary should be Terminated, actual => " + legionary.getState()
        );
    }

    public static void assertServiceMetaVisible( List<ServiceMetaDTO> metas ) {
        assertNotNull( metas, "Service meta list is null." );
        assertTrue( !metas.isEmpty(), "Service meta list is empty." );
    }

    public static void assertInstanceChanged( GUID before, GUID after ) {
        assertNotNull( before, "Before instance guid is null." );
        assertNotNull( after, "After instance guid is null." );
        assertTrue( !before.equals( after ), "Instance guid should change after reconnect. before => " + before + ", after => " + after );
    }

    public static void awaitTrue( String szMessage, long nTimeoutMillis, BooleanSupplier supplier ) {
        long nDeadline = System.currentTimeMillis() + nTimeoutMillis;
        while ( System.currentTimeMillis() < nDeadline ) {
            if ( supplier.getAsBoolean() ) {
                return;
            }
            try {
                Thread.sleep( 100L );
            }
            catch ( InterruptedException e ) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException( e );
            }
        }
        throw new IllegalStateException( szMessage );
    }
}
