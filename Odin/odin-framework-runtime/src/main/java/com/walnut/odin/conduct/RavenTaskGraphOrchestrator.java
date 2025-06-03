package com.walnut.odin.conduct;

import java.util.ArrayList;
import java.util.List;

import com.pinecone.hydra.atlas.advance.GenericTapedBFSGraphAdvancer;
import com.pinecone.hydra.atlas.advance.strategy.AtlasPriorityProcessStrategy;
import com.pinecone.hydra.atlas.advance.strategy.InDegreeFirstStrategy;
import com.pinecone.hydra.atlas.graph.RuntimeAtlasInstrument;
import com.pinecone.hydra.orchestration.ParallelAction;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.unit.iqueue.ConfigurableMegaDeflectPriorityQueueMeta;
import com.pinecone.hydra.unit.iqueue.ConfigurableMegaStratumQueueMeta;
import com.pinecone.hydra.unit.iqueue.MagnitudeDPQueue;
import com.pinecone.hydra.unit.iqueue.MegaDPStratumQueue;
import com.pinecone.hydra.unit.iqueue.MegaDeflectPriorityQueueMeta;
import com.pinecone.hydra.unit.iqueue.MegaStratumQueueMeta;
import com.pinecone.hydra.unit.vgraph.VectorDAG;
import com.pinecone.hydra.unit.vgraph.layer.Layer;
import com.pinecone.hydra.unit.vgraph.layer.LayerInstrument;

public class RavenTaskGraphOrchestrator implements TaskGraphOrchestrator {
    protected VectorDAG                       mVectorDAG;

    protected LayerInstrument                 mLayerInstrument;

    protected RuntimeAtlasInstrument          mRuntimeAtlasInstrument;

    protected KOIMappingDriver                mQueueDriver;

    protected ParallelAction                  mParallelAction;

    protected long                            mnCurrentPos;

    protected long                            mnBatchSize;

    protected TaskGraphOrchestratorConfig     mConfig;


    protected List<VectorDAG> mExecuteGraph;

    public RavenTaskGraphOrchestrator(
            VectorDAG vectorDAG, LayerInstrument layerInstrument,
            long batchSize, RuntimeAtlasInstrument runtimeAtlasInstrument,KOIMappingDriver queueDriver
    ) {
        this.mVectorDAG                 = vectorDAG;
        this.mLayerInstrument           = layerInstrument;
        this.mRuntimeAtlasInstrument    = runtimeAtlasInstrument;
        this.mParallelAction            = new ParallelAction();
        this.mnCurrentPos               = 0;
        this.mnBatchSize                = batchSize;
        this.mQueueDriver               = queueDriver;

        this.mConfig = new ConfigurableTaskGraphOrchestratorConfig();
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
        MegaDeflectPriorityQueueMeta meta1 = new ConfigurableMegaDeflectPriorityQueueMeta();
        meta1.setQueueTableName( this.mConfig.getQueueNodesTableName() );

        MegaStratumQueueMeta meta2 = new ConfigurableMegaStratumQueueMeta();
        meta2.setQueueTableName( this.mConfig.getTemporaryQueueNodesTableName() );


        MagnitudeDPQueue magnitudeDPQueue = new MagnitudeDPQueue(this.mQueueDriver, 0, "segment_name", vectorDAG.getAffiliateLayerGuid().toString(), meta1);
        MegaDPStratumQueue megaDPStratumQueue = new MegaDPStratumQueue(this.mQueueDriver, "segment_name", vectorDAG.getAffiliateLayerGuid().toString(), meta2);

        AtlasPriorityProcessStrategy strategy = new AtlasPriorityProcessStrategy();
        strategy.addStrategy( new InDegreeFirstStrategy( this.mRuntimeAtlasInstrument, magnitudeDPQueue, megaDPStratumQueue,this.mLayerInstrument ) );
        GenericTapedBFSGraphAdvancer advancer = new GenericTapedBFSGraphAdvancer( this.mRuntimeAtlasInstrument, magnitudeDPQueue,strategy );
        advancer.traverse( vectorDAG );
    }



}
