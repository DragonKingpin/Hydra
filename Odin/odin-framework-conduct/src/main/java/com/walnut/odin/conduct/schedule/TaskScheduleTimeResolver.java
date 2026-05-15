package com.walnut.odin.conduct.schedule;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.marshal.TaskScheduleCycle;

public class TaskScheduleTimeResolver implements Pinenut {

    public LocalDateTime resolveBusinessTime( TaskElement element, LocalDateTime expectTime ) {
        if ( expectTime == null ) {
            return null;
        }

        TaskScheduleCycle cycle = element.getScheduleCycle();
        if ( cycle == null ) {
            return expectTime;
        }

        switch ( cycle ) {
            case Month:
            case Week:
            case Day: {
                return expectTime.withHour( 0 ).withMinute( 0 ).withSecond( 0 ).withNano( 0 );
            }
            case Hour: {
                return expectTime.withMinute( 0 ).withSecond( 0 ).withNano( 0 );
            }
            case Minute: {
                return expectTime.withSecond( 0 ).withNano( 0 );
            }
            case Undefined:
            default: {
                return expectTime;
            }
        }
    }
}
