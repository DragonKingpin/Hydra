package com.pinecone.hydra.atlas.advance;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.atlas.graph.RuntimeAtlasInstrument;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.unit.iqueue.MegaDeflectPriorityQueue;
import com.pinecone.hydra.unit.iqueue.entity.GenericQueueElement;
import com.pinecone.hydra.unit.vgraph.VectorDAG;

import java.util.ArrayDeque;
import java.util.List;

public class GenericTapedBFSGraphAdvancer implements TapedBFSGraphStratumAdvancer {
    private RuntimeAtlasInstrument          mRuntimeAtlasInstrument;

    private MegaDeflectPriorityQueue        mMegaDeflectPriorityQueue;

    public GenericTapedBFSGraphAdvancer( RuntimeAtlasInstrument runtimeAtlasInstrument, MegaDeflectPriorityQueue megaDeflectPriorityQueue ) {
        this.mRuntimeAtlasInstrument    = runtimeAtlasInstrument;
        this.mMegaDeflectPriorityQueue  = megaDeflectPriorityQueue;
    }


    public void traverse( VectorDAG vectorDAG ) {
        long handNodeNums = vectorDAG.countHandleNodes();
        List<GUID> handleGuids = vectorDAG.fetchHandleGuids(0, handNodeNums);
        ArrayDeque<QueueEntity> arrayDeque = new ArrayDeque<>();

        for( GUID guid : handleGuids ) {
            arrayDeque.add( new GenericQueueEntity( guid, 0 ) );
        }

        while( !arrayDeque.isEmpty() ) {
            QueueEntity pop = arrayDeque.pop();

            TaskElement taskElement = this.mRuntimeAtlasInstrument.queryTaskElementByGuid(pop.getGuid());
            GenericQueueElement element = new GenericQueueElement();
            element.setObjectGuid( pop.getGuid() );
            element.setPriority(taskElement.getPriority());
            this.mMegaDeflectPriorityQueue.pushBack( element );

            this.mRuntimeAtlasInstrument.putStratumMeta( vectorDAG.getGuid(), (short) pop.getStratum(), (short) element.getPriority(), this.mMegaDeflectPriorityQueue.getSegmentName() );

            List<GUID> childGuids = vectorDAG.fetchChildNodeGuids(pop.getGuid());

            for( GUID guid : childGuids ) {
                arrayDeque.add( new GenericQueueEntity( guid, pop.getStratum() + 1 ) );
            }
        }
    }

}
