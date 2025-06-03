package com.walnut.odin.conduct;

import com.pinecone.hydra.atlas.advance.GenericTapedBFSGraphAdvancer;
import com.pinecone.hydra.atlas.advance.strategy.AtlasPriorityProcessStrategy;
import com.pinecone.hydra.atlas.advance.strategy.InDegreeFirstStrategy;
import com.pinecone.hydra.atlas.graph.RuntimeAtlasInstrument;
import com.pinecone.hydra.orchestration.ParallelAction;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.system.Hydrogen;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.unit.iqueue.MagnitudeDPQueue;
import com.pinecone.hydra.unit.iqueue.MegaDPStratumQueue;
import com.pinecone.hydra.unit.iqueue.QueueTableMeta;
import com.pinecone.hydra.unit.vgraph.VectorDAG;
import com.pinecone.hydra.unit.vgraph.layer.Layer;
import com.pinecone.hydra.unit.vgraph.layer.LayerInstrument;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.ups.conduct.TaskRegimentDomain;

import java.util.ArrayList;
import java.util.List;

public class RavenCollectiveTaskRegiment implements CollectiveTaskRegiment {

    protected Hydrogen                  mSystem;

    protected CentralizedTaskInstrument     mTaskInstrument;

    protected ProcessManager                mProcessManager;

    protected TaskRegimentDomain            mTaskRegimentDomain;

    private VectorDAG                       mVectorDAG;

    private LayerInstrument                 mLayerInstrument;

    private RuntimeAtlasInstrument          mRuntimeAtlasInstrument;

    private KOIMappingDriver                mQueueDriver;

    private ParallelAction              mParallelAction;

    private long                        mnCurrentPos;

    private long                        mnBatchSize;

    private List<VectorDAG> mExecuteGraph;

    public RavenCollectiveTaskRegiment(VectorDAG vectorDAG, LayerInstrument layerInstrument,
                                      long batchSize, RuntimeAtlasInstrument runtimeAtlasInstrument,KOIMappingDriver queueDriver) {
        this.mVectorDAG                 = vectorDAG;
        this.mLayerInstrument           = layerInstrument;
        this.mRuntimeAtlasInstrument    = runtimeAtlasInstrument;
        this.mParallelAction            = new ParallelAction();
        this.mnCurrentPos               = 0;
        this.mnBatchSize                = batchSize;
        this.mQueueDriver               = queueDriver;
    }

    @Override
    public void execute() {
        // 将图分解为可执行的子图
        this.createExecuteGraph();

        // 对每个子图生成最终执行队列
        for( VectorDAG vectorDAG : this.mExecuteGraph ) {
            this.createExecuteQueue( vectorDAG );
        }

        // 将队列中生成的节点转换成执行任务加入执行器

    }

    private void createExecuteGraph() {
        List<Layer> layers = this.mLayerInstrument.splitGraphLayer(this.mVectorDAG);
        ArrayList<VectorDAG> vectorDAGS = new ArrayList<>();
        for( Layer layer : layers ) {
            VectorDAG vectorDAG = this.mRuntimeAtlasInstrument.toVectorDAG(layer);
            vectorDAGS.add( vectorDAG );
        }
        this.mExecuteGraph = vectorDAGS;
    }

    private void createExecuteQueue( VectorDAG vectorDAG ) {
        QueueTableMeta meta1 = new QueueTableMeta();
        meta1.setQueueTableName( "hydra_queue_nodes" );
        QueueTableMeta meta2 = new QueueTableMeta();
        meta2.setQueueTableName( "hydra_temporary_queue_nodes" );

        MagnitudeDPQueue magnitudeDPQueue = new MagnitudeDPQueue(this.mQueueDriver, 0, "segment_name", vectorDAG.getAffiliateLayerGuid().toString(), meta1);
        MegaDPStratumQueue megaDPStratumQueue = new MegaDPStratumQueue(this.mQueueDriver, "segment_name", vectorDAG.getAffiliateLayerGuid().toString(), meta2);

        AtlasPriorityProcessStrategy strategy = new AtlasPriorityProcessStrategy();
        strategy.addStrategy( new InDegreeFirstStrategy(this.mRuntimeAtlasInstrument, magnitudeDPQueue, megaDPStratumQueue,this.mLayerInstrument) );
        GenericTapedBFSGraphAdvancer advancer = new GenericTapedBFSGraphAdvancer(this.mRuntimeAtlasInstrument, magnitudeDPQueue,strategy);
        advancer.traverse( vectorDAG );
    }



}
