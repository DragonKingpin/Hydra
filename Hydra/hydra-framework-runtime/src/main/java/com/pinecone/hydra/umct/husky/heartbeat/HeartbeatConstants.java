package com.pinecone.hydra.umct.husky.heartbeat;

import com.pinecone.hydra.umc.msg.InformMessage;
import com.pinecone.hydra.umc.wolfmc.UlfInstructMessage;

public final class HeartbeatConstants {
    public static final int HCTP_HEART_CONTROL_MASK              = 0xFFBEB000;   // 0x000EB000 | 0xFFB00000

    public static final int HCTP_HEART_REQUEST_ALIVE             = HCTP_HEART_CONTROL_MASK | 0x00000010;

    public static final int HCTP_HEART_RESPONSE_ACK              = HCTP_HEART_CONTROL_MASK | 0x00000011;

    public static final InformMessage HCTP_HEART_ALIVE           = new UlfInstructMessage( HCTP_HEART_REQUEST_ALIVE );

    public static final InformMessage HCTP_HEART_ACK             = new UlfInstructMessage( HCTP_HEART_RESPONSE_ACK );
}
