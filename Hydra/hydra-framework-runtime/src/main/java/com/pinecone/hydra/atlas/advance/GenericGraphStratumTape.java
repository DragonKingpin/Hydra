package com.pinecone.hydra.atlas.advance;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.atlas.graph.RuntimeAtlasInstrument;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.unit.iqueue.ConfigurableMegaDeflectPriorityQueueMeta;
import com.pinecone.hydra.unit.iqueue.MagnitudeDPQueue;
import com.pinecone.hydra.unit.iqueue.MegaDeflectPriorityQueue;
import com.pinecone.hydra.unit.iqueue.ArchQueueTableMeta;
import com.pinecone.hydra.unit.iqueue.MegaDeflectPriorityQueueMeta;
import com.pinecone.hydra.unit.iqueue.entity.QueueElement;
import com.pinecone.hydra.unit.vgraph.VectorDAG;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericGraphStratumTape implements GraphStratumTape {
    protected RuntimeAtlasInstrument                          mRuntimeAtlasInstrument;

    // StratumId => RuntimePriority => MegaDeflectPriorityQueue
    protected List<Map<Short, MegaDeflectPriorityQueue>>      mMegaDeflectPriorityQueues;

    protected MegaDeflectPriorityQueue                        mExecutionPriorityQueue;

    protected VectorDAG                                       mVectorDAG;

    public GenericGraphStratumTape(RuntimeAtlasInstrument runtimeAtlasInstrument, VectorDAG vectorDAG, KOIMappingDriver queueDrive ) {
        this.mRuntimeAtlasInstrument = runtimeAtlasInstrument;
        this.mVectorDAG = vectorDAG;
        ArrayList<Map<Short, MegaDeflectPriorityQueue>> list = new ArrayList<>();
        int stratumNum = this.mRuntimeAtlasInstrument.countStratum(vectorDAG.getAffiliateLayerGuid());

        for( int i = 0; i < stratumNum; i++ ) {
            HashMap<Short, MegaDeflectPriorityQueue> map = new HashMap<>();
            int priorityNum = this.mRuntimeAtlasInstrument.countPriority(vectorDAG.getAffiliateLayerGuid(), (short) i);
            for( int j = 0; j < priorityNum; j++ ) {
                String segmentName = this.mRuntimeAtlasInstrument.querySegmentName(vectorDAG.getAffiliateLayerGuid(), (short) i, (short) j);
                MegaDeflectPriorityQueueMeta meta = new ConfigurableMegaDeflectPriorityQueueMeta();
                meta.setQueueTableName("hydra_queue_nodes");
                MagnitudeDPQueue magnitudeDPQueue = new MagnitudeDPQueue(queueDrive, 0, "segment_name", segmentName, meta );
                map.put((short) j, magnitudeDPQueue );
            }
            list.add( map );
        }
        this.mMegaDeflectPriorityQueues = list;
    }


    @Override
    public GraphNode queryNodeByIndex( long index ) {
        long currentNum = 0;
        for( int i = 0; i < this.mMegaDeflectPriorityQueues.size(); i++ ) {
            for( MegaDeflectPriorityQueue queue : this.mMegaDeflectPriorityQueues.get(i).values() ) {
                currentNum += queue.size();
                if( currentNum >= index ) {
                    QueueElement queueElement = queue.getByIndex(index);
                    return this.mVectorDAG.get( queueElement.getObjectGuid() );
                }
            }
        }
        return null;
    }

    @Override
    public GUID queryNodeGuidByIndex( long index ) {
        long currentNum = 0;
        for( int i = 0; i < this.mMegaDeflectPriorityQueues.size(); i++ ) {
            for( MegaDeflectPriorityQueue queue : this.mMegaDeflectPriorityQueues.get(i).values() ) {
                currentNum += queue.size();
                if( currentNum >= index ) {
                    return queue.getByIndex( index ).getObjectGuid();
                }
            }
        }
        return null;
    }

    @Override
    public List<GraphNode> fetchNodes( List<GUID> guids ) {
        ArrayList<GraphNode> nodes = new ArrayList<>();
        for( GUID guid : guids ) {
            nodes.add( this.mVectorDAG.get( guid ) );
        }
        return nodes;
    }

    @Override
    public List<GraphNode> fetchNodes( long offset, long limit ) {
        long currentNum = 0;
        long maxIndex = offset + limit;

        ArrayList<GraphNode> graphNodes = new ArrayList<>();
        for( int i = 0; i < this.mMegaDeflectPriorityQueues.size(); ++i ) {
            for( MegaDeflectPriorityQueue queue : this.mMegaDeflectPriorityQueues.get(i).values() ) {
                currentNum += queue.size();
                if( currentNum >= offset ) {
                    List<QueueElement> queueElements = queue.fetchElements(offset, limit);
                    ArrayList<GraphNode> nodes = new ArrayList<>();
                    for( QueueElement element : queueElements ) {
                        nodes.add( this.mVectorDAG.get( element.getObjectGuid() ) );
                    }
                    graphNodes.addAll( nodes );
                }

                if( currentNum > maxIndex ) {
                    return graphNodes;
                }
            }
        }
        return null;
    }

    @Override
    public List<GraphNode> fetchNodes( long queuePriority, long offset, long limit ) {
        long currentNum = 0;
        long maxIndex = offset + limit;

        ArrayList<GraphNode> graphNodes = new ArrayList<>();
        for( int i = 0; i < this.mMegaDeflectPriorityQueues.size(); ++i ) {
            for( MegaDeflectPriorityQueue queue : this.mMegaDeflectPriorityQueues.get(i).values() ) {
                currentNum += queue.size();
                if( currentNum >= offset ) {
                    List<QueueElement> queueElements = queue.fetchElementByPriority( queuePriority, offset, limit );
                    ArrayList<GraphNode> nodes = new ArrayList<>();
                    for( QueueElement element : queueElements ) {
                        nodes.add( this.mVectorDAG.get( element.getObjectGuid() ) );
                    }
                    graphNodes.addAll( nodes );
                }

                if( currentNum > maxIndex ) {
                    return graphNodes;
                }
            }
        }
        return null;
    }

    @Override
    public List<GUID> fetchGuids( long offset, long limit ) {
        long currentNum = 0;
        long maxIndex = offset + limit;

        ArrayList<GUID> graphNodes = new ArrayList<>();
        for( int i = 0; i < this.mMegaDeflectPriorityQueues.size(); ++i ) {
            for( MegaDeflectPriorityQueue queue : this.mMegaDeflectPriorityQueues.get(i).values() ) {
                currentNum += queue.size();
                if( currentNum >= offset ) {
                    List<QueueElement> queueElements = queue.fetchElements(offset, limit);
                    ArrayList<GUID> nodes = new ArrayList<>();
                    for( QueueElement element : queueElements ) {
                        nodes.add( element.getObjectGuid() );
                    }
                    graphNodes.addAll( nodes );
                }

                if( currentNum > maxIndex ) {
                    return graphNodes;
                }
            }
        }
        return null;
    }

    @Override
    public List<GUID> fetchGuids( long queuePriority, long offset, long limit ) {
        long currentNum = 0;
        long maxIndex = offset + limit;

        ArrayList<GUID> graphNodes = new ArrayList<>();
        for( int i = 0; i < this.mMegaDeflectPriorityQueues.size(); ++i ) {
            for( MegaDeflectPriorityQueue queue : this.mMegaDeflectPriorityQueues.get(i).values() ) {
                currentNum += queue.size();
                if( currentNum >= offset ) {
                    List<QueueElement> queueElements = queue.fetchElementByPriority( queuePriority, offset, limit );
                    ArrayList<GUID> nodes = new ArrayList<>();
                    for( QueueElement element : queueElements ) {
                        nodes.add( element.getObjectGuid() );
                    }
                    graphNodes.addAll( nodes );
                }

                if( currentNum > maxIndex ) {
                    return graphNodes;
                }
            }
        }
        return null;
    }

    @Override
    public int countStratum() {
        return this.mRuntimeAtlasInstrument.countStratum( this.mVectorDAG.getAffiliateLayerGuid() );
    }

    @Override
    public MegaDeflectPriorityQueue query( int stratumId, short runtimePriority ) {
        Map<Short, MegaDeflectPriorityQueue> queueMap = this.mMegaDeflectPriorityQueues.get( stratumId );
        if ( queueMap != null ) {
            return queueMap.get( runtimePriority );
        }

        return null;
    }

    @Override
    public MegaDeflectPriorityQueue getExecutionPriorityQueue() {
        return this.mExecutionPriorityQueue;
    }
}
