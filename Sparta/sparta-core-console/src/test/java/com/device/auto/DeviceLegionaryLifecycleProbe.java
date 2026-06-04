package com.device.auto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.acorn.skynet.device.conduct.SkyCollectiveDeviceLegionary;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;

public class DeviceLegionaryLifecycleProbe implements Pinenut {

    protected String scenarioName;

    protected List<String> events = new ArrayList<>();

    public DeviceLegionaryLifecycleProbe( String scenarioName ) {
        this.scenarioName = scenarioName;
    }

    public void record( String caseName, int round, String event, SkyCollectiveDeviceLegionary legionary ) {
        GUID instanceGuid = legionary == null ? null : legionary.getInstanceGuid();
        String state = legionary == null ? "null" : String.valueOf( legionary.getState() );
        String line = String.format(
                "[%s][%s][Round %02d] %s state=%s instance=%s time=%s",
                this.scenarioName,
                caseName,
                round,
                event,
                state,
                instanceGuid,
                LocalDateTime.now()
        );
        this.events.add( line );
        Debug.bluefs( line );
    }

    public void dump() {
        for ( String event : this.events ) {
            Debug.greenfs( event );
        }
    }
}
