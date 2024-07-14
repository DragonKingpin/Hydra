package com.pinecone.hydra.umc.io;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.json.JSONObject;

public class IOCounter implements Pinenut {
    protected long             mnSessionCount    ;     // How many this communication channel/stream/other established. [Transmit and Receive]
    protected long             mnByteTransmitted ;     // How many bytes transfer, that though transmit. [ Send / Write ]
    protected long             mnByteReceived    ;     // How many bytes transfer, that though receive.  [ Receive / Read ]
    protected long             mnByteOther       ;     // How many bytes transfer, that though other operations.
    protected long             mnTransmitCall    ;     // How many the transmission operation called.
    protected long             mnReceiveCall     ;     // How many the receive operation called.
    protected long             mnOtherCall       ;     // How many other operations called.
    protected long             mnLastConTime     ;     // The last time when this channel/stream/other established.

    public long getSessionCount() {
        return this.mnSessionCount;
    }

    public void setSessionCount( long sessionCount ) {
        this.mnSessionCount = sessionCount;
    }

    public long getByteTransmitted() {
        return this.mnByteTransmitted;
    }

    public void setByteTransmitted( long byteTransmitted ) {
        this.mnByteTransmitted = byteTransmitted;
    }

    public long getByteReceived() {
        return this.mnByteReceived;
    }

    public void setByteReceived( long byteReceived ) {
        this.mnByteReceived = byteReceived;
    }

    public long getByteOther() {
        return this.mnByteOther;
    }

    public void setByteOther( long byteOther ) {
        this.mnByteOther = byteOther;
    }

    public long getTransmitCall() {
        return this.mnTransmitCall;
    }

    public void setTransmitCall( long transmitCall ) {
        this.mnTransmitCall = transmitCall;
    }

    public long getReceiveCall() {
        return this.mnReceiveCall;
    }

    public void setReceiveCall( long receiveCall ) {
        this.mnReceiveCall = receiveCall;
    }

    public long getOtherCall() {
        return this.mnOtherCall;
    }

    public void setOtherCall( long otherCall ) {
        this.mnOtherCall = otherCall;
    }

    public long getLastConTime() {
        return this.mnLastConTime;
    }

    public void setLastConTime( long lastConTime ) {
        this.mnLastConTime = lastConTime;
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONMaptron();
        json.put( "sessionCount"    , this.mnSessionCount    );
        json.put( "byteTransmitted" , this.mnByteTransmitted );
        json.put( "byteReceived"    , this.mnByteReceived    );
        json.put( "byteOther"       , this.mnByteOther       );
        json.put( "transmitCall"    , this.mnTransmitCall    );
        json.put( "receiveCall"     , this.mnReceiveCall     );
        json.put( "otherCall"       , this.mnOtherCall       );
        json.put( "lastConTime"     , this.mnLastConTime     );
        return json;
    }

    @Override
    public String toJSONString() {
        return this.toJSONObject().toJSONString();
    }
}
