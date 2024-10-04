package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.unit.KeyValue;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONEncoder;
import com.pinecone.framework.util.json.hometype.BeanJSONEncoder;
import com.pinecone.hydra.registry.DistributedRegistry;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GenericNamespaceNode implements NamespaceNode {
    protected long                            enumId;
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
            long enumId, GUID guid, LocalDateTime createTime, LocalDateTime updateTime, String name,
            GenericNamespaceNodeMeta namespaceNodeMeta, GenericNodeAttribute nodeCommonData
    ) {
        this.enumId            = enumId;
        this.guid              = guid;
        this.createTime        = createTime;
        this.updateTime        = updateTime;
        this.name              = name;
        this.namespaceNodeMeta = namespaceNodeMeta;
        this.nodeAttribute     = nodeCommonData;

        this.registry          = registry;
    }


    public void apply( DistributedRegistry registry ) {
        this.registry = registry;
    }


    @Override
    public long getEnumId() {
        return this.enumId;
    }

    @Override
    public void setEnumId(long enumId) {
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

    /** Thread unsafe */
    @Override
    public Map<String, RegistryTreeNode > getChildren() {
        if( this.children == null ) {
            HashMap<String, RegistryTreeNode> nodeHashMap = new HashMap<>();
            for( GUID guid : this.childrenGuids ){
                RegistryTreeNode registryTreeNode = this.registry.get( guid );
                nodeHashMap.put( registryTreeNode.getName(), registryTreeNode );
            }
            this.children = nodeHashMap;
        }
        return this.children;
    }

    @Override
    public List<GUID > getChildrenGuids() {
        return this.childrenGuids;
    }

    @Override
    public void setChildrenGuids( List<GUID > contentGuids, int depth ) {
        this.childrenGuids = contentGuids;
    }

    @Override
    public List<RegistryTreeNode > listItem() {
        ArrayList<RegistryTreeNode > registryTreeNodes = new ArrayList<>();
        registryTreeNodes.addAll( this.getChildren().values() );
        return registryTreeNodes;
    }

    @Override
    public void put( String key, RegistryTreeNode val ) {
        if ( this.getChildren().get(key) != null ){
            throw new IllegalArgumentException( "key is exist." );
        }
        this.getChildren().put(key, val);
        this.registry.affirmOwnedNode( this.guid, val.getGuid() );
    }

    @Override
    public void remove( String key ) {
        RegistryTreeNode registryTreeNode = this.getChildren().get(key);
        this.registry.remove(registryTreeNode.getGuid());
        this.getChildren().remove(key);
    }

    @Override
    public DistributedRegistry getRegistry() {
        return this.registry;
    }

    @Override
    public boolean containsKey( String key ) {
        return this.getChildren().containsKey(key);
    }

    @Override
    public ConfigNode getConfigNode( String key ) {
        return (ConfigNode) this.getChildren().get(key);
    }

    @Override
    public NamespaceNode getNamespaceNode( String key ) {
        return (NamespaceNode) this.getChildren().get(key);
    }

    @Override
    public int size() {
        return this.childrenGuids.size();
    }

    @Override
    public boolean isEmpty() {
        return this.childrenGuids.isEmpty();
    }

    @Override
    public Set<String> keySet() {
        return this.getChildren().keySet();
    }

    @Override
    public Set<Map.Entry<String, RegistryTreeNode > > entrySet() {
        return this.getChildren().entrySet();
    }

    @Override
    public void copyTo(GUID destinationGuid) {
        List<TreeNode> childrenNodes = this.registry.getChildren(this.guid);
        if ( !childrenNodes.isEmpty() && childrenNodes.get(0) instanceof RegistryTreeNode ){
            List<RegistryTreeNode> registryNodes = (List<RegistryTreeNode>) (List<?>) childrenNodes;
            for ( RegistryTreeNode node :  registryNodes){
                if (node.evinceNamespaceNode() != null){
                    this.copyTo( node.getGuid() );
                }
                else if ( node.evinceProperties() != null ){
                    node.evinceProperties().copyTo(this.guid);
                }
                else if( node.evinceTextConfigNode() != null ){
                    node.evinceTextConfigNode().copyTo( this.guid );
                }
            }
        }
       this.copyNamespaceMetaTo( destinationGuid );
    }

    @Override
    public void copyNamespaceMetaTo(GUID destinationGuid) {
        this.registry.copyNamespaceMetaTo(this.guid,destinationGuid);
    }

    @Override
    public String toJSONString() {
        return JSONEncoder.stringifyMapFormat( new KeyValue[]{
                new KeyValue<>( "name"        , this.getName()            ),
                new KeyValue<>( "guid"        , this.getGuid()            ),
                new KeyValue<>( "createTime"  , this.getCreateTime()      ),
                new KeyValue<>( "updateTime"  , this.getUpdateTime()      ),
                new KeyValue<>( "childrenSize", this.childrenGuids.size() ),
        } );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }
}
