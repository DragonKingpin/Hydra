package com.pinecone.hydra.unit.vgraph;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.hydra.unit.vgraph.algo.BasicDAGPathResolver;
import com.pinecone.hydra.unit.vgraph.algo.BasicDAGPathSelector;
import com.pinecone.hydra.unit.vgraph.algo.DAGPathResolver;
import com.pinecone.hydra.unit.vgraph.algo.DAGPathSelector;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.hydra.unit.vgraph.source.AtlasMasterManipulator;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphManipulator;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphMasterManipulator;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphPathCacheManipulator;
import com.pinecone.ulf.util.guid.GenericGuidAllocator;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

public abstract class ArchAtlasInstrument implements AtlasInstrument{
    protected AtlasInstrument                   mParentInstrument;

    protected MegaVectorDAG                     mMegaVectorDAG;

    protected Hydrarum                          mHydrarum;

    protected Processum                         mSuperiorProcess;

    protected GuidAllocator                     mGuidAllocator;

    protected DAGPathResolver                   mPathResolver;

    protected DAGPathSelector                    mPathSelector;

    protected AtlasMasterManipulator            mAtlasMasterManipulator;

    protected VectorGraphConfig                 mVectorGraphConfig;

    public ArchAtlasInstrument (
            Processum superiorProcess, MegaVectorDAG megaVectorDAG, AtlasMasterManipulator atlasMasterManipulator, VectorGraphConfig vectorGraphConfig,
            AtlasInstrument parent, String name
    ){
        this.mVectorGraphConfig = vectorGraphConfig;
        this.mSuperiorProcess = superiorProcess;
        this.mMegaVectorDAG = megaVectorDAG;
        this.mAtlasMasterManipulator = atlasMasterManipulator;
        if ( this.mSuperiorProcess instanceof Hydrarum ) {
            this.mHydrarum                    = (Hydrarum) this.mSuperiorProcess;
        }
        else {
            this.mHydrarum                    = (Hydrarum) superiorProcess.getSystem();
        }
        this.mParentInstrument = parent;

        this.mGuidAllocator = new GenericGuidAllocator();
        this.mPathResolver = new BasicDAGPathResolver();//后续要使用配置类指定
        this.mPathSelector = new BasicDAGPathSelector( this.mPathResolver, this.mMegaVectorDAG.getMasterManipulator().getVectorGraphManipulator() );
    }

    @Override
    public AtlasInstrument parent() {
        return this.mParentInstrument;
    }

    @Override
    public Processum getSuperiorProcess() {
        return this.mSuperiorProcess;
    }

    @Override
    public void setParent(AtlasInstrument atlasInstrument) {
        this.mParentInstrument = atlasInstrument;
    }

    @Override
    public List<String> getPath(GUID guid) {
        return this.getNS( guid, this.mVectorGraphConfig.getPathNameSeparator() );
    }

    @Override
    public GUID queryGUIDByPath(String path) {
        return this.queryGUIDByNS( path, null, null );
    }

    @Override
    public GUID queryParentID(GUID guid) {
        return null;
    }

    @Override
    public boolean contains( GUID handleNode, GUID nodeGuid) {
        return this.mPathSelector.contains( handleNode, nodeGuid );
    }

    @Override
    public GUID put(GraphNode graphNode) {
        GUID guid = this.mGuidAllocator.nextGUID();
        graphNode.setId( guid );
        this.mMegaVectorDAG.put(graphNode);

        return guid;
    }

    @Override
    public GraphNode get(GUID guid) {
        return this.mMegaVectorDAG.get(guid);
    }

    @Override
    public GUID queryGUIDByNS(String path, String szBadSep, String szTargetSep) {
        if( szTargetSep != null ) {
            path = path.replace( szBadSep, szTargetSep );
        }

        String[] parts = this.mPathResolver.segmentPathParts( path );
        List<String > resolvedParts = this.mPathResolver.resolvePath( parts );
        path = this.mPathResolver.assemblePath( resolvedParts );

        GUID guid = this.mMegaVectorDAG.getGuidByCachePath(path);
        if ( guid != null ){
            return guid;
        }


        guid = this.mPathSelector.searchId( resolvedParts );
        if( guid != null ){
            this.mMegaVectorDAG.putCachePath( path, guid );
        }
        return guid;
    }

    @Override
    public TreeNode get(GUID guid, int depth) {
        return null;
    }

    @Override
    public void remove(GUID guid) {
        this.mMegaVectorDAG.remove( guid );
        this.mMegaVectorDAG.removeCache( guid );
    }

    @Override
    public void remove(String path) {
        GUID guid = this.queryGUIDByPath(path);
        if( guid != null ){
            this.remove( guid );
        }
    }

    @Override
    public List<GraphNode> getChildren(GUID guid) {
        return this.mMegaVectorDAG.getChildren(guid);
    }

    @Override
    public List<GUID> fetchChildrenIds(GUID guid) {
        return this.mMegaVectorDAG.fetchChildrenIds( guid );
    }

    @Override
    public void rename(GUID guid, String name) {

    }

    /**使用bfs找到所有可达路径**/
    protected List<String> getNS( GUID guid, String szSeparator ){
        // 先检查缓存
        List<String> path = this.mMegaVectorDAG.getCachePath(guid);
        if (path != null) {
            return path;
        }

        GraphNode startNode = this.get(guid);
        if (startNode == null) {
            return Collections.emptyList();
        }

        List<String> allPaths = new ArrayList<>();
        Queue<GraphNodePair> queue = new LinkedList<>();
        queue.offer(new GraphNodePair(startNode, startNode.getName()));

        while (!queue.isEmpty()) {
            GraphNodePair current = queue.poll();
            GraphNode currentNode = current.getGraphNode();
            String currentPath = current.getCurrentPath();

            List<GUID> parentIds = currentNode.getParentIds();
            if (parentIds.isEmpty() || !this.allNonNull(parentIds)) {
                allPaths.add(currentPath);
                continue;
            }

            // 遍历所有非空的父节点
            for (GUID parentId : parentIds) {
                if (parentId != null) {
                    GraphNode parentNode = this.get(parentId);
                    if (parentNode != null) {
                        String newPath = parentNode.getName() + szSeparator + currentPath;
                        queue.offer(new GraphNodePair(parentNode, newPath));
                    }
                }
            }
        }

        if (!allPaths.isEmpty()) {
            for( String s : allPaths ){
                this.mMegaVectorDAG.putCachePath(s, guid);
            }

        }

        return allPaths;
    }

    private boolean allNonNull( List<?> list ) {
        return list.stream().noneMatch( Objects::isNull );
    }
}
