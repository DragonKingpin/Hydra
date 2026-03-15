package com.pinecone.hydra.atlas.advance.strategy;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.atlas.graph.RuntimeAtlasInstrument;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.unit.iqueue.DeflectPriorityQueue;
import com.pinecone.hydra.unit.iqueue.MegaStratumQueue;
import com.pinecone.hydra.unit.iqueue.entity.GenericQueueElement;
import com.pinecone.hydra.unit.iqueue.entity.GenericStratumQueueElement;
import com.pinecone.hydra.unit.iqueue.entity.QueueStratumElement;
import com.pinecone.hydra.unit.vgraph.VectorDAG;
import com.pinecone.hydra.unit.vgraph.layer.Layer;
import com.pinecone.hydra.unit.vgraph.layer.LayerInstrument;

import java.util.List;

public class MegaInDegreeFirstStrategy implements GraphPriorityProcessStrategy {
    private RuntimeAtlasInstrument      mRuntimeAtlasInstrument;

    private DeflectPriorityQueue        mDeflectPriorityQueue;

    private MegaStratumQueue            mTempMegaStratumQueue;

    private LayerInstrument             mLayerInstrument;

    private int mnPriority = 0;

    public MegaInDegreeFirstStrategy(
            RuntimeAtlasInstrument runtimeAtlasInstrument, DeflectPriorityQueue deflectPriorityQueue,
            MegaStratumQueue megaStratumQueue, LayerInstrument layerInstrument
    ) {
        this.mRuntimeAtlasInstrument    = runtimeAtlasInstrument;
        this.mDeflectPriorityQueue = deflectPriorityQueue;
        this.mTempMegaStratumQueue      = megaStratumQueue;
        this.mLayerInstrument           = layerInstrument;
    }


    @Override
    public void process( VectorDAG vectorDAG ) {
        Layer layer = (Layer)this.mLayerInstrument.get(vectorDAG.getAffiliateLayerGuid());
        long handNodeNums = this.mLayerInstrument.countSourceNode( vectorDAG.getAffiliateLayerGuid() );
        long offset = 0;

        //todo 后面记得将这个每次遍历的节点数量改成配置
        for ( long i = 0; i < handNodeNums; i += 1000 ) {
            List<GUID> handleGuids = this.mLayerInstrument.fetchSourceGuidsByTaskPriority(vectorDAG.getAffiliateLayerGuid(),offset, 1000);
            for (GUID handleGuid : handleGuids) {
                TaskElement taskElement = this.mRuntimeAtlasInstrument.queryTaskElementByGuid(handleGuid);
                if (taskElement.getPriority() > this.mnPriority) {
                    this.bfsGraph(vectorDAG, this.mnPriority, layer.getSinkGuids());
                    ++this.mnPriority;
                }
                GenericStratumQueueElement element = new GenericStratumQueueElement();
                element.setObjectGuid(handleGuid);
                element.setStratum((short) 0);
                this.mTempMegaStratumQueue.pushBack(element);
                ++offset;
            }
        }
        // 跳出循环后要将所有节点入队，直接降低成最低优先级
        this.bfsGraph( vectorDAG, 10,layer.getSinkGuids() );
    }

    protected void bfsGraph( VectorDAG vectorDAG, int priority, List<GUID> sinkNodes ) {
        while ( !this.mTempMegaStratumQueue.isEmpty() ) {
            QueueStratumElement pop = this.mTempMegaStratumQueue.popFront();
            GUID currentNodeGuid = pop.getObjectGuid();

            TaskElement taskElement = this.mRuntimeAtlasInstrument.queryTaskElementByGuid(currentNodeGuid);
            GenericQueueElement element = new GenericQueueElement();
            element.setObjectGuid(currentNodeGuid);
            element.setPriority(taskElement.getPriority());
            this.mDeflectPriorityQueue.pushBack(element);

            this.mRuntimeAtlasInstrument.putStratumMeta(
                    vectorDAG.getAffiliateLayerGuid(),
                    (short) pop.getStratum(),
                    (short) element.getPriority(),
                    this.mDeflectPriorityQueue.getSegmentName()
            );

            if (sinkNodes != null && sinkNodes.contains(currentNodeGuid)) {
                continue;
            }

            long childNodeNum = vectorDAG.countChildNodeNum(currentNodeGuid);
            long childOffset = 0;

            for ( int i = 0; i < childNodeNum; i += 1000 ) {
                List<GUID> guids = vectorDAG.fetchChildNodeGuids(childOffset, 1000, currentNodeGuid);
                for (GUID guid : guids) {
                    TaskElement childtaskElement = this.mRuntimeAtlasInstrument.queryTaskElementByGuid(guid);
                    if (childtaskElement.getPriority() <= priority) {
                        GenericStratumQueueElement stratumQueueElement = new GenericStratumQueueElement();
                        stratumQueueElement.setObjectGuid(guid);
                        stratumQueueElement.setStratum((short) (pop.getStratum() + 1));
                        this.mTempMegaStratumQueue.pushBack(stratumQueueElement);
                    }
                }
            }
        }
    }

}
