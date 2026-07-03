package com.acorn.skynet.system;

import com.acorn.skynet.device.conduct.CollectiveDeviceRegiment;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.registry.server.DeviceManager;
import com.pinecone.hydra.system.imperium.FacilityClass;
import com.pinecone.hydra.system.imperium.FacilitySynergicSystem;

public interface SkynetSubsystem extends FacilitySynergicSystem {

    CollectiveDeviceRegiment deviceRegiment();

    DeviceInstrument deviceInstrument();

    DeviceManager deviceManager();

    @Override
    default FacilityClass facilityClass() {
        return FacilityClass.Device;
    }

    @Override
    default FacilityClass[] ownedClass() {
        return new FacilityClass[] { FacilityClass.Device };
    }
}
