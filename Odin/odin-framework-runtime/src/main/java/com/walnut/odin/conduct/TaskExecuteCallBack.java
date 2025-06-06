package com.walnut.odin.conduct;

import com.pinecone.hydra.atlas.graph.RuntimeAtlasInstrument;
import com.pinecone.hydra.orchestration.SequentialAction;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.unit.iqueue.ConfigurableMegaDeflectPriorityQueueMeta;
import com.pinecone.hydra.unit.iqueue.MagnitudeDPQueue;
import com.pinecone.hydra.unit.iqueue.MegaDeflectPriorityQueue;
import com.pinecone.hydra.unit.iqueue.MegaDeflectPriorityQueueMeta;
import com.pinecone.hydra.unit.iqueue.entity.QueueElement;
import com.pinecone.hydra.unit.vgraph.VectorDAG;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class TaskExecuteCallBack implements ExecuteCallBack {
    private RuntimeAtlasInstrument          mRuntimeAtlasInstrument;

    protected KOIMappingDriver              mQueueDriver;

    private TaskGraphOrchestratorConfig     mConfig;

    private MegaDeflectPriorityQueue        mMegaDeflectPriorityQueue;

    private Deque<VectorDAG>                mExecuteVectorDAG;

    private int                             mnTaskBatchSize;

    private int                             mnCurrentPos;

    public TaskExecuteCallBack(MegaDeflectPriorityQueue megaDeflectPriorityQueue, RuntimeAtlasInstrument runtimeAtlasInstrument,TaskGraphOrchestratorConfig config,
                               KOIMappingDriver driver,Deque<VectorDAG> vectorDAGDeque, int taskBatchSize ) {
        this.mMegaDeflectPriorityQueue      = megaDeflectPriorityQueue;
        this.mRuntimeAtlasInstrument        = runtimeAtlasInstrument;
        this.mQueueDriver                   = driver;
        this.mConfig                        = config;
        this.mExecuteVectorDAG              = vectorDAGDeque;
        this.mnTaskBatchSize                = taskBatchSize;
    }

    @Override
    public List<TaskElement> introduceTask() {
        List<QueueElement> queueElements = this.mMegaDeflectPriorityQueue.fetchElements(mnCurrentPos, mnTaskBatchSize);
        mnCurrentPos += queueElements.size();
        // todo 目前不知道那边的逻辑先写成多次io的形式
        ArrayList<TaskElement> taskElements = new ArrayList<>();
        for( QueueElement queueElement : queueElements ) {
            TaskElement node = this.mRuntimeAtlasInstrument.queryTaskElementByGuid(queueElement.getObjectGuid());
            taskElements.add( node );
        }
        return taskElements;
    }

    @Override
    public synchronized void nextTask() {
        VectorDAG pop = this.mExecuteVectorDAG.pop();
        if( pop != null ) {
            SequentialAction action = new SequentialAction();
            MegaDeflectPriorityQueueMeta meta = new ConfigurableMegaDeflectPriorityQueueMeta();
            meta.setQueueTableName( this.mConfig.getQueueNodesTableName() );
            MagnitudeDPQueue magnitudeDPQueue = new MagnitudeDPQueue(this.mQueueDriver, 0, "segment_name", pop.getAffiliateLayerGuid().toString(), meta);

            TaskExecuteCallBack callBack = new TaskExecuteCallBack( magnitudeDPQueue, this.mRuntimeAtlasInstrument,this.mConfig,this.mQueueDriver, this.mExecuteVectorDAG,this.mnTaskBatchSize);
            TaskExertium taskExertium = new TaskExertium( callBack );
            action.add( taskExertium );
            action.start();
        }
    }
}
