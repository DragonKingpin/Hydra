package com.acorn.skynet.device.husky.transformer;

import com.acorn.skynet.device.husky.protocol.HuskyDeviceDeregisterInstruction;
import com.acorn.skynet.device.husky.protocol.HuskyDeviceDeregisterResult;
import com.acorn.skynet.device.husky.protocol.HuskyDeviceRegisterInstruction;
import com.acorn.skynet.device.husky.protocol.HuskyDeviceRegisterResult;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.device.kom.instance.DeviceInstanceEntry;
import com.pinecone.hydra.device.registry.client.entity.DeviceClientDeregisterResult;
import com.pinecone.hydra.device.registry.client.entity.DeviceClientRegisterResult;
import com.pinecone.hydra.device.registry.instruction.DeviceDeregisterInstruction;
import com.pinecone.hydra.device.registry.instruction.DeviceRegisterInstruction;

public class HuskyDeviceControlTransformer implements Pinenut {

    protected final GuidAllocator guidAllocator;

    public HuskyDeviceControlTransformer( GuidAllocator guidAllocator ) {
        this.guidAllocator = guidAllocator;
    }

    public HuskyDeviceRegisterInstruction encodeRegisterInstruction( DeviceRegisterInstruction instruction ) {
        HuskyDeviceRegisterInstruction dto = new HuskyDeviceRegisterInstruction();
        if ( instruction == null ) {
            return dto;
        }

        dto.setDeviceGuid( this.toString( instruction.getDeviceGuid() ) );
        dto.setInstanceGuid( this.toString( instruction.getInstanceGuid() ) );
        dto.setDeviceGuidText( instruction.getDeviceGuidText() );
        dto.setDevicePath( instruction.getDevicePath() );
        dto.setClientId( instruction.getClientId() );
        dto.setEndpointProtocol( instruction.getEndpointProtocol() );
        dto.setEndpointHost( instruction.getEndpointHost() );
        dto.setEndpointPort( instruction.getEndpointPort() );
        dto.setEndpointPath( instruction.getEndpointPath() );
        dto.setEndpointAddress( instruction.getEndpointAddress() );
        dto.setMetadataJson( instruction.getMetadataJson() );
        return dto;
    }

    public DeviceRegisterInstruction decodeRegisterInstruction( HuskyDeviceRegisterInstruction dto ) {
        DeviceRegisterInstruction instruction = new DeviceRegisterInstruction();
        if ( dto == null ) {
            return instruction;
        }

        instruction.setDeviceGuid( this.toGuid( dto.getDeviceGuid() ) );
        instruction.setInstanceGuid( this.toGuid( dto.getInstanceGuid() ) );
        instruction.setDeviceGuidText( dto.getDeviceGuidText() );
        instruction.setDevicePath( dto.getDevicePath() );
        instruction.setClientId( dto.getClientId() );
        instruction.setEndpointProtocol( dto.getEndpointProtocol() );
        instruction.setEndpointHost( dto.getEndpointHost() );
        instruction.setEndpointPort( dto.getEndpointPort() );
        instruction.setEndpointPath( dto.getEndpointPath() );
        instruction.setEndpointAddress( dto.getEndpointAddress() );
        instruction.setMetadataJson( dto.getMetadataJson() );
        return instruction;
    }

    public HuskyDeviceRegisterResult encodeRegisterResult( DeviceInstanceEntry instance ) {
        HuskyDeviceRegisterResult result = new HuskyDeviceRegisterResult();
        if ( instance == null ) {
            return result;
        }

        result.setDeviceGuid( this.toString( instance.getDeviceGuid() ) );
        result.setInstanceGuid( this.toString( instance.getInstanceGuid() ) );
        result.setClientId( instance.getClientId() );
        return result;
    }

    public DeviceClientRegisterResult decodeRegisterResult( HuskyDeviceRegisterResult dto ) {
        DeviceClientRegisterResult result = new DeviceClientRegisterResult();
        if ( dto == null ) {
            return result;
        }

        result.setDeviceGuid( this.toGuid( dto.getDeviceGuid() ) );
        result.setInstanceGuid( this.toGuid( dto.getInstanceGuid() ) );
        result.setClientId( dto.getClientId() );
        return result;
    }

    public HuskyDeviceDeregisterInstruction encodeDeregisterInstruction( DeviceDeregisterInstruction instruction ) {
        HuskyDeviceDeregisterInstruction dto = new HuskyDeviceDeregisterInstruction();
        if ( instruction == null ) {
            return dto;
        }

        dto.setInstanceGuid( this.toString( instruction.getInstanceGuid() ) );
        dto.setReason( instruction.getReason() );
        return dto;
    }

    public DeviceDeregisterInstruction decodeDeregisterInstruction( HuskyDeviceDeregisterInstruction dto ) {
        DeviceDeregisterInstruction instruction = new DeviceDeregisterInstruction();
        if ( dto == null ) {
            return instruction;
        }

        instruction.setInstanceGuid( this.toGuid( dto.getInstanceGuid() ) );
        instruction.setReason( dto.getReason() );
        return instruction;
    }

    public HuskyDeviceDeregisterResult encodeDeregisterResult( DeviceDeregisterInstruction instruction ) {
        HuskyDeviceDeregisterResult result = new HuskyDeviceDeregisterResult();
        if ( instruction != null ) {
            result.setInstanceGuid( this.toString( instruction.getInstanceGuid() ) );
        }
        return result;
    }

    public DeviceClientDeregisterResult decodeDeregisterResult( HuskyDeviceDeregisterResult dto ) {
        DeviceClientDeregisterResult result = new DeviceClientDeregisterResult();
        if ( dto != null ) {
            result.setInstanceGuid( this.toGuid( dto.getInstanceGuid() ) );
        }
        return result;
    }

    protected GUID toGuid( String guid ) {
        if ( guid == null || guid.trim().isEmpty() ) {
            return null;
        }
        return this.guidAllocator.parse( guid );
    }

    protected String toString( GUID guid ) {
        return guid == null ? null : guid.toString();
    }
}
