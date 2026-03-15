package com.pinecone.hydra.task.marshal;

public enum KernelTaskScheduleCycle {
    Undefined      ( 0x00, "Undefined" ),
    Minute         ( 0x01, "Minute" ),
    Hour           ( 0x02, "Hour" ),
    Day            ( 0x03, "Day" ),
    Week           ( 0x04, "Week" ),
    Month          ( 0x05, "Month" ),


    // Sub second level scheduling, unable to use regular scheduling channels,
    // requires client caching status (instance at minimum minute level)
    // 亚秒级调度，无法走常规调度通道，需要客户端缓存状态（实例为最小分钟级）[无法生成秒级实例]
    TickSecond     ( 0xC0, "TickSecond" ),
    TickMills      ( 0xC1, "TickMills"  ),
    ;

    private final int code;

    private final String name;

    KernelTaskScheduleCycle( int code, String name ) {
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getCode() {
        return this.code;
    }

    public static KernelTaskScheduleCycle getByCode( int code ) {
        for ( KernelTaskScheduleCycle cycle : KernelTaskScheduleCycle.values() ) {
            if ( cycle.code == code ) {
                return cycle;
            }
        }

        return null;
    }
}
