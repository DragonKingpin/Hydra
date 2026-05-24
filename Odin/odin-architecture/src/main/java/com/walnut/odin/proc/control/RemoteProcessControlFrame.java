package com.walnut.odin.proc.control;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.json.JSON;
import com.walnut.odin.proc.entity.UProcessMirrorDTO;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RemoteProcessControlFrame implements Pinenut {

    protected String             mszFrameGuid;

    protected String             mszCorrelationGuid;

    protected long               mnClientId;

    protected String             mszSessionGuid;

    protected String             mszFrameType;

    protected long               mnCreateTimeMillis;

    protected UProcessMirrorDTO  mProcessMirror;

    protected List<UProcessMirrorDTO> mProcessMirrors;

    protected String             mszMessage;

    public RemoteProcessControlFrame() {
        this.mnCreateTimeMillis = System.currentTimeMillis();
    }

    public String getFrameGuid() {
        return this.mszFrameGuid;
    }

    public void setFrameGuid( String szFrameGuid ) {
        this.mszFrameGuid = szFrameGuid;
    }

    public String getCorrelationGuid() {
        return this.mszCorrelationGuid;
    }

    public void setCorrelationGuid( String szCorrelationGuid ) {
        this.mszCorrelationGuid = szCorrelationGuid;
    }

    public long getClientId() {
        return this.mnClientId;
    }

    public void setClientId( long nClientId ) {
        this.mnClientId = nClientId;
    }

    public String getSessionGuid() {
        return this.mszSessionGuid;
    }

    public void setSessionGuid( String szSessionGuid ) {
        this.mszSessionGuid = szSessionGuid;
    }

    public String getFrameType() {
        return this.mszFrameType;
    }

    public void setFrameType( String szFrameType ) {
        this.mszFrameType = szFrameType;
    }

    public RemoteProcessControlFrameType optFrameType() {
        return RemoteProcessControlFrameType.parse( this.mszFrameType );
    }

    public void applyFrameType( RemoteProcessControlFrameType frameType ) {
        this.mszFrameType = frameType.getCode();
    }

    public long getCreateTimeMillis() {
        return this.mnCreateTimeMillis;
    }

    public void setCreateTimeMillis( long nCreateTimeMillis ) {
        this.mnCreateTimeMillis = nCreateTimeMillis;
    }

    public UProcessMirrorDTO getProcessMirror() {
        return this.mProcessMirror;
    }

    public void setProcessMirror( UProcessMirrorDTO processMirror ) {
        this.mProcessMirror = processMirror;
    }

    public List<UProcessMirrorDTO> getProcessMirrors() {
        return this.mProcessMirrors;
    }

    public void setProcessMirrors( List<UProcessMirrorDTO> processMirrors ) {
        this.mProcessMirrors = processMirrors;
    }

    public String getMessage() {
        return this.mszMessage;
    }

    public void setMessage( String szMessage ) {
        this.mszMessage = szMessage;
    }

    @Override
    public String toJSONString() {
        Map<String, Object> json = new LinkedHashMap<>();
        json.put( "frameGuid", this.mszFrameGuid );
        json.put( "correlationGuid", this.mszCorrelationGuid );
        json.put( "clientId", this.mnClientId );
        json.put( "sessionGuid", this.mszSessionGuid );
        json.put( "frameType", this.mszFrameType );
        json.put( "createTimeMillis", this.mnCreateTimeMillis );
        json.put( "processMirror", this.mProcessMirror );
        json.put( "processMirrors", this.mProcessMirrors );
        json.put( "message", this.mszMessage );
        return JSON.stringify( json );
    }
}
