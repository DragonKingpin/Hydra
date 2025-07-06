package com.walnut.sparta.utask.console;

import com.pinecone.Pinecone;
import com.walnut.sparta.utask.console.infrastructure.TaskContentDelivery;

public class OdinTaskCenterBoot {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            TaskContentDelivery utask = (TaskContentDelivery) Pinecone.sys().getTaskManager().add(
                    new TaskContentDelivery( args, Pinecone.sys() )
            );
            utask.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
