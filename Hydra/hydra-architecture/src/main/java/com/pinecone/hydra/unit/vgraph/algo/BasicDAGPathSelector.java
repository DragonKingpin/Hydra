package com.pinecone.hydra.unit.vgraph.algo;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.AtlasInstrument;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphManipulator;

import java.util.List;
import java.util.Stack;

public class BasicDAGPathSelector implements DAGPathSelector {
    protected DAGPathResolver mPathResolver;

    protected AtlasInstrument mAtlasInstrument;

    protected VectorGraphManipulator    mVectorGraphManipulator;

    public BasicDAGPathSelector(DAGPathResolver pathResolver, AtlasInstrument atlasInstrument, VectorGraphManipulator vectorGraphManipulator  ){
        this.mPathResolver = pathResolver;
        this.mAtlasInstrument = atlasInstrument;
        this.mVectorGraphManipulator = vectorGraphManipulator;
    }
    @Override
    public GUID searchId(String[] parts) {
        return this.searchId( parts, null );
    }

    @Override
    public GUID searchId(String[] parts, @Nullable String[] lpResolvedPath) {
        List<String > resolvedParts = this.mPathResolver.resolvePath( parts );
        if( lpResolvedPath != null ) {
            lpResolvedPath[ 0 ] = this.mPathResolver.assemblePath( resolvedParts );
        }

        return this.searchId( resolvedParts );
    }

    @Override
    public GUID searchId(List<String> resolvedParts) {
        return (GUID) this.dfsSearch( resolvedParts );
    }

    @Override
    public GUID searchId(GUID parentId, String[] parts) {
        return this.searchId(parentId, parts, null );
    }

    @Override
    public GUID searchId(GUID parentId, String[] parts, @Nullable String[] lpResolvedPath) {
        List<String > resolvedParts = this.mPathResolver.resolvePath( parts );
        if( lpResolvedPath != null ) {
            lpResolvedPath[ 0 ] = this.mPathResolver.assemblePath( resolvedParts );
        }

        return this.searchId( parentId, resolvedParts );
    }

    @Override
    public GUID searchId(GUID parentId, List<String> resolvedParts) {
        return (GUID) this.dfsSearch( parentId, resolvedParts );
    }

    @Override
    public boolean contains(GUID handleNode, GUID nodeGuid) {
        if( handleNode.equals(nodeGuid) ){
            return true;
        }

        List<GraphNode> nodes = this.mVectorGraphManipulator.fetchChildNodes(handleNode);
        for( GraphNode node : nodes ){
            contains( node.getId(), nodeGuid );
        }
        return false;
    }

    protected Object dfsSearch(List<String > parts ) {
        return this.dfsSearch( null, parts );
    }


    /** 使用递归实现图的DFS遍历 **/
    protected Object dfsSearch(GUID parentID, List<String> parts, int depth) {
        if (depth == parts.size() - 1) {
            List<GraphNode> nodes = this.mVectorGraphManipulator.fetchNodesByName(parts.get(depth));
            for (GraphNode graphNode : nodes) {
                if (parentID == null || this.mVectorGraphManipulator.fetchParentIds(graphNode.getId()).equals(parentID)) {
                    return graphNode;
                }
            }
            return null;
        }

        List<GraphNode> nodes = this.mVectorGraphManipulator.fetchNodesByName(parts.get(depth));
        for (GraphNode graphNode : nodes) {
            if (parentID == null || this.mVectorGraphManipulator.fetchParentIds(graphNode.getId()).equals(parentID)) {
                Object result = this.dfsSearch(graphNode.getId(), parts, depth + 1);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    /** 非递归形式DFS遍历 **/
    protected Object dfsSearch(GUID parentID, List<String> parts) {
        if (parts.isEmpty()) {
            return null; // 边界条件：路径为空
        }

        // 用栈保存当前状态：节点、父ID、当前深度
        Stack<Object[]> stack = new Stack<>();
        stack.push(new Object[]{parentID, 0}); // 初始状态：parentID, depth=0

        while (!stack.isEmpty()) {
            Object[] state = stack.pop();
            GUID currentParentID = (GUID) state[0];
            int currentDepth = (int) state[1];

            // 终止条件：到达路径末尾
            if (currentDepth == parts.size() - 1) {
                List<GraphNode> nodes = mVectorGraphManipulator.fetchNodesByName(parts.get(currentDepth));
                for (GraphNode node : nodes) {
                    if (currentParentID == null ||
                            mVectorGraphManipulator.fetchParentIds(node.getId()).equals(currentParentID)) {
                        return node; // 找到目标节点
                    }
                }
                continue; // 当前深度未找到，继续回溯
            }

            // 非终止条件：继续向下搜索
            List<GraphNode> nodes = mVectorGraphManipulator.fetchNodesByName(parts.get(currentDepth));
            // 注意：栈是后进先出，为了保证顺序，需要反向遍历节点（或直接按顺序压栈）
            for (int i = nodes.size() - 1; i >= 0; i--) {
                GraphNode node = nodes.get(i);
                if (currentParentID == null ||
                        mVectorGraphManipulator.fetchParentIds(node.getId()).equals(currentParentID)) {
                    // 压栈：子节点ID + 下一深度
                    stack.push(new Object[]{node.getId(), currentDepth + 1});
                }
            }
        }

        return null; // 栈空仍未找到
    }
}
