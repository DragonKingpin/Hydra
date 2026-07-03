package com.walnut.odin.proc.server.detached;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.proc.RemoteProcess;

public class DetachedClientObservationRoom implements Pinenut {

    protected Map<Long, ClientCustody> mCustodies;

    public DetachedClientObservationRoom() {
        this.mCustodies = new ConcurrentHashMap<>();
    }

    public ClientCustody admit(
            long nClientId, Collection<RemoteProcess> processes,
            String szReason, long nDetachedAtMillis, long nDeadlineMillis
    ) {
        if ( processes == null || processes.isEmpty() ) {
            this.mCustodies.remove( nClientId );
            return null;
        }

        ClientCustody custody = new ClientCustody( nClientId, nDetachedAtMillis, nDeadlineMillis, processes, szReason );
        this.mCustodies.put( nClientId, custody );
        return custody;
    }

    public ClientCustody query( long nClientId ) {
        return this.mCustodies.get( nClientId );
    }

    public ClientCustody remove( long nClientId ) {
        return this.mCustodies.remove( nClientId );
    }

    public boolean isObserved( long nClientId ) {
        return this.mCustodies.containsKey( nClientId );
    }

    public Collection<ClientCustody> sweepExpired( long nNowMillis ) {
        Collection<ClientCustody> expiredCustodies = new ArrayList<>();
        for ( ClientCustody custody : this.mCustodies.values() ) {
            if ( !custody.isExpired( nNowMillis ) ) {
                continue;
            }
            if ( this.mCustodies.remove( custody.getClientId(), custody ) ) {
                expiredCustodies.add( custody );
            }
        }
        return expiredCustodies;
    }
}
