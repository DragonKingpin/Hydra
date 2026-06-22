package com.walnut.odin.proc.server.detached;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.proc.RemoteProcess;

public class ClientCustody implements Pinenut {

    protected long                     mnClientId;
    protected long                     mnDetachedAtMillis;
    protected long                     mnDeadlineMillis;
    protected String                   mszReason;
    protected Map<GUID, RemoteProcess> mProcesses;

    public ClientCustody(
            long nClientId, long nDetachedAtMillis, long nDeadlineMillis,
            Collection<RemoteProcess> processes, String szReason
    ) {
        this.mnClientId          = nClientId;
        this.mnDetachedAtMillis  = nDetachedAtMillis;
        this.mnDeadlineMillis    = nDeadlineMillis;
        this.mszReason           = szReason;
        this.mProcesses          = new ConcurrentHashMap<>();
        this.acceptProcesses( processes );
    }

    protected void acceptProcesses( Collection<RemoteProcess> processes ) {
        if ( processes == null || processes.isEmpty() ) {
            return;
        }

        for ( RemoteProcess process : processes ) {
            if ( process == null || process.getPID() == null ) {
                continue;
            }
            this.mProcesses.put( process.getPID(), process );
        }
    }

    public long getClientId() {
        return this.mnClientId;
    }

    public long getDetachedAtMillis() {
        return this.mnDetachedAtMillis;
    }

    public long getDeadlineMillis() {
        return this.mnDeadlineMillis;
    }

    public String getReason() {
        return this.mszReason;
    }

    public boolean isExpired( long nNowMillis ) {
        return nNowMillis >= this.mnDeadlineMillis;
    }

    public boolean isEmpty() {
        return this.mProcesses.isEmpty();
    }

    public Collection<RemoteProcess> processes() {
        return Collections.unmodifiableCollection( new ArrayList<>( this.mProcesses.values() ) );
    }

    public Collection<GUID> processIds() {
        return Collections.unmodifiableCollection( new ArrayList<>( this.mProcesses.keySet() ) );
    }

    public Map<GUID, RemoteProcess> processMap() {
        return Collections.unmodifiableMap( new LinkedHashMap<>( this.mProcesses ) );
    }

    public Collection<RemoteProcess> fetchMissingProcesses( Set<GUID> livePids ) {
        Collection<RemoteProcess> missingProcesses = new ArrayList<>();
        for ( Map.Entry<GUID, RemoteProcess> entry : this.mProcesses.entrySet() ) {
            GUID pid = entry.getKey();
            if ( livePids != null && livePids.contains( pid ) ) {
                continue;
            }
            missingProcesses.add( entry.getValue() );
        }
        return missingProcesses;
    }
}
