package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.hometype.BeanJSONEncoder;
import com.pinecone.hydra.registry.DistributedRegistry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GenericNamespaceNode implements NamespaceNode {
    protected int                             enumId;
    protected GUID                            guid;
    protected LocalDateTime                   createTime;
    protected LocalDateTime                   updateTime;
    protected String                          name;
    protected NamespaceNodeMeta               namespaceNodeMeta;
    protected NodeAttribute                   nodeAttribute;
    protected DistributedRegistry             registry;
    protected Map<String, RegistryTreeNode >  children;
    protected List<GUID >                     childrenGuids;


    public GenericNamespaceNode() {

    }

    public GenericNamespaceNode( DistributedRegistry registry ) {
        this.registry = registry;
    }

    public GenericNamespaceNode(
            DistributedRegistry registry,
            int enumId, GUID guid, LocalDateTime createTime, LocalDateTime updateTime, String name,
            GenericNamespaceNodeMeta namespaceNodeMeta, GenericNodeAttribute nodeCommonData
    ) {
        this.enumId            = enumId;
        this.guid              = guid;
        this.createTime        = createTime;
        this.updateTime        = updateTime;
        this.name              = name;
        this.namespaceNodeMeta = namespaceNodeMeta;
        this.nodeAttribute = nodeCommonData;

        this.registry          = registry;
    }


    public void apply( DistributedRegistry registry ) {
        this.registry = registry;
    }


    @Override
    public int getEnumId() {
        return this.enumId;
    }

    @Override
    public void setEnumId(int enumId) {
        this.enumId = enumId;
    }

    @Override
    public GUID getGuid() {
        return this.guid;
    }

    @Override
    public void setGuid(GUID guid) {
        this.guid = guid;
    }

    @Override
    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    @Override
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }

    @Override
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public NamespaceNodeMeta getNamespaceNodeMeta() {
        return this.namespaceNodeMeta;
    }

    @Override
    public void setNamespaceNodeMeta( NamespaceNodeMeta namespaceNodeMeta ) {
        this.namespaceNodeMeta = namespaceNodeMeta;
    }

    @Override
    public NodeAttribute getNodeAttribute() {
        return this.nodeAttribute;
    }

    @Override
    public void setNodeAttribute(NodeAttribute nodeAttribute) {
        this.nodeAttribute = nodeAttribute;
    }

    @Override
    public Map<String, RegistryTreeNode > getChildren() {
        return this.children;
    }

    @Override
    public List<GUID > getChildrenGuids() {
        return this.childrenGuids;
    }

    @Override
    public void setContentGuids( List<GUID> contentGuids ) {
        this.childrenGuids = contentGuids;
        HashMap<String, RegistryTreeNode> nodeHashMap = new HashMap<>();
        for( GUID guid : contentGuids ){
            RegistryTreeNode registryTreeNode = this.registry.get( guid );
            nodeHashMap.put( registryTreeNode.getName(), registryTreeNode );
        }
        this.children = nodeHashMap;
    }

    @Override
    public List<RegistryTreeNode > listItem() {
        ArrayList<RegistryTreeNode > registryTreeNodes = new ArrayList<>();
        registryTreeNodes.addAll( this.children.values() );
        return registryTreeNodes;
    }

    @Override
    public void put( String key,RegistryTreeNode val ) {
        if ( this.children.get(key) != null ){
            throw new RuntimeException("key is exist!!!");
        }
        this.children.put(key,val);
        this.registry.affirmOwnedNode( this.guid, val.getGuid() );
    }

    @Override
    public void remove( String key ) {
        RegistryTreeNode registryTreeNode = this.children.get(key);
        this.registry.remove(registryTreeNode.getGuid());
        this.children.remove(key);
    }

    @Override
    public DistributedRegistry getRegistry() {
        return this.registry;
    }

    @Override
    public boolean containsKey( String key ) {
        return this.children.containsKey(key);
    }

    @Override
    public ConfigNode getConfigNode( String key ) {
       return (ConfigNode) this.children.get(key);
    }

    @Override
    public NamespaceNode getNamespaceNode( String key ) {
        return (NamespaceNode) this.children.get(key);
    }

    @Override
    public int size() {
        return this.children.size();
    }

    @Override
    public boolean isEmpty() {
        return this.children.isEmpty();
    }

    @Override
    public Set<String> keySet() {
        return this.children.keySet();
    }

    @Override
    public Set<Map.Entry<String, RegistryTreeNode > > entrySet() {
        return this.children.entrySet();
    }

    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }
}
