package com.pinecone.hydra.umct.husky;

/**
 *  Pinecone Ursus For Java Wolf-Husky Control Transmission Protocol
 *  Author: Harold.E / JH.W (DragonKing)
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  *****************************************************************************************
 *  Bean Nuts Walnut Ulfhedinn Wolves/Ulfar Family.
 *  HCTP is an archetypal implementation of the Uniform Message Control Transmission Protocol (UMCT)
 *  哈士奇控制传输协议（HCTP）是统一消息控制传输协议（UMCT）的典型实现
 *  *****************************************************************************************
 */
public final class HuskyCTPConstants {

    public static final String HCTP_DUP_PASSIVE_CHANNEL_KEY      = "HCTPPassiveChannel";

    public static final int HCTP_CONTROL_MASK                   = 0xFFB00000;

    public static final int HCTP_DUP_CONTROL_MASK               = 0xFFBEA000;

    public static final int HCTP_DUP_CONTROL_REGISTER           = HCTP_DUP_CONTROL_MASK | 0x00000001;

    public static final int HCTP_DUP_CONTROL_ALIVE              = HCTP_DUP_CONTROL_MASK | 0x00000002;

    public static final int HCTP_DUP_CONTROL_PASSIVE_REQUEST    = HCTP_DUP_CONTROL_MASK | 0x00000010;

    public static final int HCTP_DUP_CONTROL_PASSIVE_RESPONSE   = HCTP_DUP_CONTROL_MASK | 0x00000011;
}
