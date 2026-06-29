package com.walnut.odin.processor.event;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.walnut.odin.processor.event.TaskProcessorEstablishment;
import com.walnut.odin.processor.event.TaskProcessorEvent;
import com.walnut.odin.processor.event.TaskProcessorEventType;

public class GenericTaskProcessorEvent implements TaskProcessorEvent {

    protected TaskProcessorEventType       mType;
    protected TaskProcessorEstablishment   mEstablishment;
    protected long                         mnClientId;
    protected String                       mszNodeName;
    protected String                       mszProcessorGuid;
    protected Map<String, String>          mMetadata;
    protected String                       mszReason;
    protected long                         mnTimestamp;

    public GenericTaskProcessorEvent(
            TaskProcessorEventType type,
            TaskProcessorEstablishment establishment,
            long nClientId,
            String szNodeName,
            String szProcessorGuid,
            Map<String, String> metadata,
            String szReason
    ) {
        this.mType            = type;
        this.mEstablishment   = establishment;
        this.mnClientId       = nClientId;
        this.mszNodeName      = szNodeName;
        this.mszProcessorGuid = szProcessorGuid;
        this.mMetadata        = metadata == null ? new LinkedHashMap<>() : new LinkedHashMap<>( metadata );
        this.mszReason        = szReason;
        this.mnTimestamp      = System.currentTimeMillis();
    }

    public static GenericTaskProcessorEvent of(
            TaskProcessorEventType type,
            TaskProcessorEstablishment establishment,
            long nClientId,
            String szNodeName,
            String szProcessorGuid,
            Map<String, String> metadata,
            String szReason
    ) {
        return new GenericTaskProcessorEvent(
                type, establishment, nClientId, szNodeName, szProcessorGuid, metadata, szReason
        );
    }

    @Override
    public TaskProcessorEventType getType() {
        return this.mType;
    }

    @Override
    public TaskProcessorEstablishment getEstablishment() {
        return this.mEstablishment;
    }

    @Override
    public long getClientId() {
        return this.mnClientId;
    }

    @Override
    public String getNodeName() {
        return this.mszNodeName;
    }

    @Override
    public String getProcessorGuid() {
        return this.mszProcessorGuid;
    }

    @Override
    public Map<String, String> getMetadata() {
        return Collections.unmodifiableMap( this.mMetadata );
    }

    @Override
    public String getReason() {
        return this.mszReason;
    }

    @Override
    public long getTimestamp() {
        return this.mnTimestamp;
    }
}
