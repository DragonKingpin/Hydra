package com.pinecone.hydra.system.component.infra;

public enum WareDomain {
    Undefined                  ( "Undefined"        , 0x0000 ),
    Data                       ( "Data"             , 0x0001 ),
    Storage                    ( "Storage"          , 0x0002 ),
    Message                    ( "Message"          , 0x0003 ),
    Config                     ( "Config"           , 0x0004 ),
    Log                        ( "Log"              , 0x0005 ),
    Compute                    ( "Compute"          , 0x0006 ),
    Monitor                    ( "Monitor"          , 0x0007 ),
    Security                   ( "Security"         , 0x0008 ),
    Network                    ( "Network"          , 0x0009 ),
    Business                   ( "Business"         , 0x000A ),
    User                       ( "User"             , 0x000B ),
    Device                     ( "Device"           , 0x000C ),
    Other                      ( "Other"            , 0xFFFF );

    private final String value;

    private final short code;

    WareDomain( String value, int code ){
        this.value = value;
        this.code  = (short) code;
    }

    public String getName(){
        return this.value;
    }

    public short getCode() {
        return this.code;
    }
}
