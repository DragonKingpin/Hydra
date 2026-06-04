package com.acorn.skynet.device.conduct;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface CollectiveDeviceLegionary extends Pinenut {

    String getName();

    long getClientId();

    GUID getDeviceGuid();

    GUID getInstanceGuid();

    DeviceLegionaryState getState();

    void startDevice() throws DeviceLegionaryException;

    DeviceLegionaryJoinResponse joinRegiment() throws DeviceLegionaryException;

    void requestRejoinRegiment( String reason );

    void deregister( String reason ) throws DeviceLegionaryException;

    void terminateDevice();
}
