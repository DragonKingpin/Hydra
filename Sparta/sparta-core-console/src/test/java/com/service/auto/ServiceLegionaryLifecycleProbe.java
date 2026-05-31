package com.service.auto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.acorn.redqueen.service.conduct.RedCollectiveServiceLegionary;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;

public class ServiceLegionaryLifecycleProbe implements Pinenut {

    protected String mszScenarioName;

    protected List<String> mEvents = new ArrayList<>();

    public ServiceLegionaryLifecycleProbe( String szScenarioName ) {
        this.mszScenarioName = szScenarioName;
    }

    public void record( String szCaseName, int nRound, String szEvent, RedCollectiveServiceLegionary legionary ) {
        GUID instanceGuid = legionary == null ? null : legionary.getInstanceGuid();
        String szState = legionary == null ? "null" : String.valueOf( legionary.getState() );
        String szLine = String.format(
                "[%s][%s][Round %02d] %s state=%s instance=%s time=%s",
                this.mszScenarioName,
                szCaseName,
                nRound,
                szEvent,
                szState,
                instanceGuid,
                LocalDateTime.now()
        );
        this.mEvents.add( szLine );
        Debug.bluefs( szLine );
    }

    public void dump() {
        for ( String szEvent : this.mEvents ) {
            Debug.greenfs( szEvent );
        }
    }
}
