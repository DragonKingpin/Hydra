package com.sparta;


import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.queue.ibatis.hydranium.QueueMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.unit.iqueue.MagnitudeDPQueue;
import com.pinecone.hydra.unit.iqueue.QueueTableMeta;
import com.pinecone.hydra.unit.iqueue.entity.GenericQueueElement;
import com.pinecone.radium.Radium;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.ulf.util.guid.GUIDs;

class Chris extends Radium {
    public Chris( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Chris( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    public void vitalize () throws Exception {
        KOIMappingDriver koiMappingDriver = new QueueMappingDriver(
                this, (IbatisClient)this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );
        QueueTableMeta queueTableMeta = new QueueTableMeta();
        queueTableMeta.setQueueTableName( "hydra_queue_nodes" );
        MagnitudeDPQueue dpQueue = new MagnitudeDPQueue(koiMappingDriver, 6L, "segment_name", "测试队列", queueTableMeta);
        //this.testInsert( dpQueue );
        this.testQuery( dpQueue );
    }

    public void testInsert( MagnitudeDPQueue dpQueue ) {
        GenericQueueElement element = new GenericQueueElement();
        element.setObjectGuid(GUIDs.GUID72("22989c2-000225-0000-4c"));
        element.setPriority(2);
        dpQueue.pushBack( element );
    }

    public void testQuery( MagnitudeDPQueue dpQueue ) {
        Debug.trace("目前的队列是否为空：" + dpQueue.isEmpty());
        for( int i = 0; i < 3; i++ ) {
            Debug.trace( "输出队列头数据：" +dpQueue.popFront() + "目前的位置是：" + dpQueue.currentPosition() );
            Debug.trace( "目前队列的size是：" + dpQueue.size() );
        }
    }
}
public class TestQueue {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            Chris chris = (Chris) Pinecone.sys().getTaskManager().add( new Chris( args, Pinecone.sys() ) );
            chris.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
