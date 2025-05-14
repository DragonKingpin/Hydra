package com.pinecone.hydra.atlas.advance.chain;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.atlas.graph.RuntimeAtlasInstrument;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.unit.iqueue.MegaDeflectPriorityQueue;
import com.pinecone.hydra.unit.iqueue.MegaStratumQueue;
import com.pinecone.hydra.unit.iqueue.entity.GenericQueueElement;
import com.pinecone.hydra.unit.iqueue.entity.GenericStratumQueueElement;
import com.pinecone.hydra.unit.iqueue.entity.QueueStratumElement;
import com.pinecone.hydra.unit.vgraph.VectorDAG;

import java.util.List;

public class InDegreeFirstStrategyChain extends AbstractStrategyChain implements StrategyChain {
    private RuntimeAtlasInstrument      mRuntimeAtlasInstrument;

    private MegaDeflectPriorityQueue    mMegaDeflectPriorityQueue;

    private MegaStratumQueue            mTempMegaStratumQueue;

    private int mnPriority = 0;

    public InDegreeFirstStrategyChain( RuntimeAtlasInstrument runtimeAtlasInstrument, MegaDeflectPriorityQueue megaDeflectPriorityQueue,
                                       MegaStratumQueue megaStratumQueue ) {
        this.mRuntimeAtlasInstrument    = runtimeAtlasInstrument;
        this.mMegaDeflectPriorityQueue  = megaDeflectPriorityQueue;
        this.mTempMegaStratumQueue = megaStratumQueue;
    }


    @Override
    public void execute( VectorDAG vectorDAG ) {


        long handNodeNums = vectorDAG.countHandleNodes();
        long offset = 0;

        for( long i = 0; i < handNodeNums; i+=1000 ) {
            List<GUID> handleGuids = vectorDAG.fetchHandleGuidsByTaskPriority(offset, 1000);
            for (GUID handleGuid : handleGuids) {
                TaskElement taskElement = mRuntimeAtlasInstrument.queryTaskElementByGuid(handleGuid);
                if (taskElement.getPriority() > mnPriority) {
                    this.dpPop(vectorDAG,this.mnPriority);
                    this.mnPriority++;
                }
                GenericStratumQueueElement element = new GenericStratumQueueElement();
                element.setObjectGuid(handleGuid);
                element.setStratum((short) 0);
                this.mTempMegaStratumQueue.pushBack(element);
                offset++;
            }
        }
        this.dpPop( vectorDAG,this.mnPriority );
    }

    private void dpPop(VectorDAG vectorDAG,int priority){
        while( !this.mTempMegaStratumQueue.isEmpty() ) {
            QueueStratumElement pop = this.mTempMegaStratumQueue.popFront();

            TaskElement taskElement = this.mRuntimeAtlasInstrument.queryTaskElementByGuid(pop.getObjectGuid());
            GenericQueueElement element = new GenericQueueElement();
            element.setObjectGuid( pop.getObjectGuid() );
            element.setPriority(taskElement.getPriority());
            this.mMegaDeflectPriorityQueue.pushBack( element );

            this.mRuntimeAtlasInstrument.putStratumMeta( vectorDAG.getGuid(), (short) pop.getStratum(), (short) element.getPriority(), this.mMegaDeflectPriorityQueue.getSegmentName() );

            long childNodeNum = vectorDAG.countChildNodeNum(pop.getObjectGuid());
            long childOffset = 0;

            for( int i = 0; i < childNodeNum; i+=1000 ) {
                List<GUID> guids = vectorDAG.fetchChildNodeGuids(childOffset, 1000, pop.getObjectGuid());
                for( GUID guid : guids ) {
                    TaskElement childtaskElement = this.mRuntimeAtlasInstrument.queryTaskElementByGuid(guid);
                    if( childtaskElement.getPriority() <= this.mnPriority  ) {
                        GenericStratumQueueElement stratumQueueElement = new GenericStratumQueueElement();
                        stratumQueueElement.setObjectGuid( guid );
                        stratumQueueElement.setStratum((short) (pop.getStratum() + 1));
                        this.mTempMegaStratumQueue.pushBack( stratumQueueElement );
                    }
                }
            }

        }
    }

}
