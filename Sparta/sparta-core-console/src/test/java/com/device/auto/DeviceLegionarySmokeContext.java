package com.device.auto;

import com.acorn.skynet.device.conduct.SkyCollectiveDeviceLegionary;
import com.acorn.skynet.device.conduct.SkyCollectiveDeviceRegiment;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.UniformDeviceInstrument;
import com.pinecone.hydra.device.registry.client.UniformDeviceClient;
import com.pinecone.hydra.device.registry.server.UniformDeviceManager;
import com.pinecone.tritium.Tritium;

public class DeviceLegionarySmokeContext implements Pinenut {

    public Tritium system;

    public DeviceLegionaryTransportScenario scenario;

    public UniformDeviceInstrument deviceInstrument;

    public UniformDeviceManager deviceManager;

    public SkyCollectiveDeviceRegiment regiment;

    public UniformDeviceClient deviceClient;

    public SkyCollectiveDeviceLegionary legionary;

    public GUID deviceGuid;

    public String devicePath;

    public DeviceLegionaryLifecycleProbe probe;

    public DeviceLegionarySmokeContext( Tritium system, DeviceLegionaryTransportScenario scenario ) {
        this.system = system;
        this.scenario = scenario;
        this.probe = new DeviceLegionaryLifecycleProbe( scenario.name() );
    }

    public void cleanup() {
        try {
            if ( this.legionary != null ) {
                this.legionary.terminateDevice();
            }
        }
        catch ( Exception ignore ) {
        }
        try {
            if ( this.deviceClient != null ) {
                this.deviceClient.terminateDevice();
            }
        }
        catch ( Exception ignore ) {
        }
        try {
            if ( this.regiment != null ) {
                this.regiment.stopDeviceManager();
            }
        }
        catch ( Exception ignore ) {
        }
        try {
            if ( this.scenario != null ) {
                this.scenario.cleanup( this );
            }
        }
        catch ( Exception ignore ) {
        }
    }
}
