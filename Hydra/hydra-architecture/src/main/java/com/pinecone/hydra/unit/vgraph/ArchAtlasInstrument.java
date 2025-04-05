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
import com.pinecone.hydra.unit.vgraph.source.VectorGraphManipulator;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphMasterManipulator;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphPathCacheManipulator;
import com.pinecone.ulf.util.guid.GenericGuidAllocator;

import java.util.List;
import java.util.Objects;

public abstract class ArchAtlasInstrument implements AtlasInstrument{
    protected AtlasInstrument                   mParentInstrument;

    protected Hydrarum                          mHydrarum;

    protected Processum                         mSuperiorProcess;

    protected GuidAllocator                     mGuidAllocator;

    protected DAGPathResolver                   mPathResolver;

    protected DAGPathSelector                    mPathSelector;

    protected VectorGraphMasterManipulator      mMasterManipulator;

    protected VectorGraphManipulator            mVectorGraphManipulator;

    protected VectorGraphPathCacheManipulator   mVectorGraphPathCacheManipulator;

    protected VectorGraphConfig                 mVectorGraphConfig;

    public ArchAtlasInstrument (
            Processum superiorProcess, VectorGraphMasterManipulator masterManipulator, VectorGraphConfig vectorGraphConfig,
            AtlasInstrument parent, String name
    ){
        this.mMasterManipulator = masterManipulator;
        this.mVectorGraphConfig = vectorGraphConfig;
        this.mSuperiorProcess = superiorProcess;
        if ( this.mSuperiorProcess instanceof Hydrarum ) {
            this.mHydrarum                    = (Hydrarum) this.mSuperiorProcess;
        }
        else {
            this.mHydrarum                    = (Hydrarum) superiorProcess.getSystem();
        }
        this.mParentInstrument = parent;

        this.mGuidAllocator = new GenericGuidAllocator();
        this.mVectorGraphManipulator = this.mMasterManipulator.getVectorGraphManipulator();
        this.mVectorGraphPathCacheManipulator = this.mMasterManipulator.getVectorGraphPathCacheManipulator();
        this.mPathResolver = new BasicDAGPathResolver();//后续要使用配置类指定
        this.mPathSelector = new BasicDAGPathSelector( this.mPathResolver, this, this.mVectorGraphManipulator );
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
    public String getPath(GUID guid) {
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
        this.mVectorGraphManipulator.insertStartNode( graphNode );

        return guid;
    }

    @Override
    public GraphNode get(GUID guid) {
        return this.mVectorGraphManipulator.queryNode( guid );
    }

    @Override
    public GUID queryGUIDByNS(String path, String szBadSep, String szTargetSep) {
        if( szTargetSep != null ) {
            path = path.replace( szBadSep, szTargetSep );
        }

        String[] parts = this.mPathResolver.segmentPathParts( path );
        List<String > resolvedParts = this.mPathResolver.resolvePath( parts );
        path = this.mPathResolver.assemblePath( resolvedParts );

        GUID guid = this.mVectorGraphPathCacheManipulator.queryGUIDByPath( path );
        if ( guid != null ){
            return guid;
        }


        guid = this.mPathSelector.searchId( resolvedParts );
        if( guid != null ){
            this.mVectorGraphPathCacheManipulator.insert( path, guid );
        }
        return guid;
    }

    @Override
    public TreeNode get(GUID guid, int depth) {
        return null;
    }

    @Override
    public void remove(GUID guid) {
        this.mVectorGraphManipulator.removeNode( guid );
        this.mVectorGraphPathCacheManipulator.remove( guid );
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
        return this.mVectorGraphManipulator.fetchChildNodes( guid );
    }

    @Override
    public List<GUID> fetchChildrenIds(GUID guid) {
        return this.mVectorGraphManipulator.fetchChildNodeIds( guid );
    }

    @Override
    public void rename(GUID guid, String name) {

    }

    /**找一条可达路径**/
    protected String getNS( GUID guid, String szSeparator ){
        String path = this.mVectorGraphPathCacheManipulator.getPath(guid);
        if( path != null ){
            return path;
        }

        GraphNode node = this.get(guid);
        String assemblePath = node.getName();
        while( !node.getParentIds().isEmpty() && this.allNonNull( node.getParentIds() ) ){
            List<GUID> parentIds = node.getParentIds();
            for( int i = 0; i < parentIds.size(); ++i ){
                if( parentIds.get(i) != null ){
                    node = this.get( parentIds.get(i) );
                    break;
                }
            }
            String nodeName = node.getName();
            assemblePath = nodeName + szSeparator + assemblePath;
        }
        this.mVectorGraphPathCacheManipulator.insert( assemblePath, guid );
        return assemblePath;
    }

    private boolean allNonNull( List<?> list ) {
        return list.stream().noneMatch( Objects::isNull );
    }
}
