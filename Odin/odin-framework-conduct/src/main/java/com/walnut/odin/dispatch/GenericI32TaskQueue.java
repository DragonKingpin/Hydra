package com.walnut.odin.dispatch;

public class GenericI32TaskQueue extends ArchTaskExecutionI32Queue implements TaskExecutionQueue {

    public GenericI32TaskQueue( TaskQueueMeta queueMeta ) {
        super();

        if ( queueMeta == null ) {
            throw new IllegalArgumentException( "TaskQueueMeta cannot be null." );
        }
        this.mszName                    = queueMeta.getName();
        this.mnMaxCapacity              = queueMeta.getMaxCapacity();
        this.mnMinCapacity              = queueMeta.getMinCapacity();
        this.mnRuntimeInstanceCapacity  = queueMeta.getRuntimeInstanceCapacity();

        this.validateInitialMeta();
    }

    private void validateInitialMeta() {
        if ( this.mnMaxCapacity < 0 ) {
            throw new IllegalArgumentException( "Max capacity cannot be negative." );
        }

        if ( this.mnMinCapacity < 0 ) {
            throw new IllegalArgumentException( "Min capacity cannot be negative." );
        }

        if ( this.mnRuntimeInstanceCapacity < 0 ) {
            throw new IllegalArgumentException( "Runtime instance capacity cannot be negative." );
        }

        if ( this.mnMinCapacity > this.mnMaxCapacity ) {
            throw new IllegalArgumentException( "Min capacity cannot exceed max capacity." );
        }

        if ( this.mnRuntimeInstanceCapacity > this.mnMaxCapacity ) {
            throw new IllegalArgumentException( "Runtime instance capacity cannot exceed max capacity." );
        }
    }

}
