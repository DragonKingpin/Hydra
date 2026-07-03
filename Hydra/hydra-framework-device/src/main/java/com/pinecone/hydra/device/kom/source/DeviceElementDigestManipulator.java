package com.pinecone.hydra.device.kom.source;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.DeviceElementDigestQuery;
import com.pinecone.hydra.device.kom.digest.DeviceElementDigest;

public interface DeviceElementDigestManipulator extends Pinenut {

    List<DeviceElementDigest> fetchDeviceElementDigests( DeviceElementDigestQuery query );

    List<DeviceElementDigest> fetchDeviceElementDigestsByGuids( List<GUID> guids );

    long countDeviceElementDigests( DeviceElementDigestQuery query );
}
