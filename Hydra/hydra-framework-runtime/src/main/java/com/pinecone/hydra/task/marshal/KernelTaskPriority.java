package com.pinecone.hydra.task.marshal;

import java.util.Arrays;

public enum KernelTaskPriority {
    L0(50),
    L1(40),
    L2(30),
    L3(20),
    L4(10),
    L5(0);

    private final int value;

    private KernelTaskPriority( int value ) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

    public static Integer byName( String name ) {
        try {
            KernelTaskPriority taskPriority = valueOf(name);
            return taskPriority.getValue();
        }
        catch ( IllegalArgumentException e ) {
            return null;
        }
    }

    public static KernelTaskPriority of( int value ) {
        return (KernelTaskPriority) Arrays.stream(values())
                .filter((it) -> it.value == value)
                .findFirst().orElse((KernelTaskPriority) null);
    }
}