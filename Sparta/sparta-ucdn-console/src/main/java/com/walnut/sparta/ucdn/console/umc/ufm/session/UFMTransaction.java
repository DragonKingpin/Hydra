package com.walnut.sparta.ucdn.console.umc.ufm.session;

import com.pinecone.framework.util.id.GUID;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class UFMTransaction {
    protected GUID          localFileGUID;

    protected AtomicBoolean startTransmit;

    protected AtomicBoolean transmitFileContent;

    protected AtomicBoolean fileDistributionComplete;

    protected long          lastEventArrivedMills;

    public UFMTransaction( GUID localFileGUID ) {
        this.localFileGUID = localFileGUID;
        this.startTransmit = new AtomicBoolean(false);
        this.transmitFileContent = new AtomicBoolean(false);
        this.fileDistributionComplete = new AtomicBoolean(false);
    }

    public GUID getLocalFileGUID() {
        return this.localFileGUID;
    }

    public long getLastEventArrivedMills() {
        return this.lastEventArrivedMills;
    }

    public void setLastEventArrivedMills( long lastEventArrivedMills ) {
        this.lastEventArrivedMills = lastEventArrivedMills;
    }

    public boolean finishStartTransmit() {
        return this.startTransmit.compareAndSet(false, true);
    }

    public boolean finishTransmitFileContent() {
        return this.transmitFileContent.compareAndSet(false, true);
    }

    public boolean finishFileDistributionComplete() {
        return this.fileDistributionComplete.compareAndSet(false, true);
    }


    public boolean isStartTransmit() {
        return this.startTransmit.get();
    }

    public boolean isTransmitFileContent() {
        return this.transmitFileContent.get();
    }

    public boolean isFileDistributionComplete() {
        return this.fileDistributionComplete.get();
    }

}
