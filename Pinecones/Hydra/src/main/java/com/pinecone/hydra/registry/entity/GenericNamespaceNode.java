package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.hometype.BeanJSONEncoder;
import com.pinecone.hydra.registry.DistributedRegistry;
import lombok.val;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
    protected NodeCommonData                  nodeCommonData;
    protected DistributedRegistry             registry;
    protected Map<String,RegistryTreeNode>    contents;
    protected List<GUID>                      contentGuids;


    protected GenericNamespaceNode() {

    }

    public GenericNamespaceNode( DistributedRegistry registry ) {
        this.registry = registry;
    }

    public GenericNamespaceNode(
            DistributedRegistry registry,
            int enumId, GUID guid, LocalDateTime createTime, LocalDateTime updateTime, String name,
            GenericNamespaceNodeMeta namespaceNodeMeta, GenericNodeCommonData nodeCommonData
    ) {
        this.enumId            = enumId;
        this.guid              = guid;
        this.createTime        = createTime;
        this.updateTime        = updateTime;
        this.name              = name;
        this.namespaceNodeMeta = namespaceNodeMeta;
        this.nodeCommonData    = nodeCommonData;

        this.registry          = registry;
    }


    public void apply( DistributedRegistry registry ) {
        this.registry = registry;
    }


    /**
     * 获取
     * @return enumId
     */
    @Override
    public int getEnumId() {
        return this.enumId;
    }

    /**
     * 设置
     * @param enumId
     */
    @Override
    public void setEnumId(int enumId) {
        this.enumId = enumId;
    }

    /**
     * 获取
     * @return guid
     */
    @Override
    public GUID getGuid() {
        return this.guid;
    }

    /**
     * 设置
     * @param guid
     */
    @Override
    public void setGuid(GUID guid) {
        this.guid = guid;
    }

    /**
     * 获取
     * @return createTime
     */
    @Override
    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    /**
     * 设置
     * @param createTime
     */
    @Override
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取
     * @return updateTime
     */
    @Override
    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }

    /**
     * 设置
     * @param updateTime
     */
    @Override
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取
     * @return name
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * 设置
     * @param name
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取
     * @return namespaceNodeMeta
     */
    @Override
    public NamespaceNodeMeta getNamespaceNodeMeta() {
        return this.namespaceNodeMeta;
    }

    /**
     * 设置
     * @param namespaceNodeMeta
     */
    @Override
    public void setNamespaceNodeMeta( NamespaceNodeMeta namespaceNodeMeta ) {
        this.namespaceNodeMeta = namespaceNodeMeta;
    }

    /**
     * 获取
     * @return nodeCommonData
     */
    @Override
    public NodeCommonData getNodeCommonData() {
        return this.nodeCommonData;
    }

    /**
     * 设置
     * @param nodeCommonData
     */
    @Override
    public void setNodeCommonData( NodeCommonData nodeCommonData ) {
        this.nodeCommonData = nodeCommonData;
    }

    @Override
    public Map<String,RegistryTreeNode> getContents() {
        return this.contents;
    }

    @Override
    public List<GUID> getContentGuids() {
        return this.contentGuids;
    }

    @Override
    public void setContentGuids(List<GUID> contentGuids) {
        this.contentGuids = contentGuids;
        HashMap<String, RegistryTreeNode> nodeHashMap = new HashMap<>();
        for(GUID guid : contentGuids){
            RegistryTreeNode registryTreeNode = this.registry.get(guid);
            nodeHashMap.put(registryTreeNode.getName(),registryTreeNode);
        }
        this.contents = nodeHashMap;
    }

    @Override
    public List<RegistryTreeNode> listItem() {
        ArrayList<RegistryTreeNode> registryTreeNodes = new ArrayList<>();
        registryTreeNodes.addAll(this.contents.values());
        return registryTreeNodes;
    }

    @Override
    public void put(String key,RegistryTreeNode val) {
        if (contents.get(key) != null){
            throw new RuntimeException("key is exist!!!");
        }
        this.contents.put(key,val);
        this.registry.insertRegistryTreeNode(this.guid,val.getGuid());
    }

    @Override
    public void remove(String key) {
        RegistryTreeNode registryTreeNode = this.contents.get(key);
        this.registry.remove(registryTreeNode.getGuid());
        this.contents.remove(key);
    }

    @Override
    public DistributedRegistry getRegistry() {
        return this.registry;
    }

    @Override
    public boolean containsKey(String key) {
        return this.contents.containsKey(key);
    }

    @Override
    public ConfigNode getConfigNode(String key) {
       return (ConfigNode) this.contents.get(key);
    }

    @Override
    public NamespaceNode getNamespaceNode(String key) {
        return (NamespaceNode) this.contents.get(key);
    }

    @Override
    public int size() {
        return this.contents.size();
    }

    @Override
    public boolean isEmpty() {
        return this.contents.isEmpty();
    }

    @Override
    public Set<String> keySet() {
        return this.contents.keySet();
    }

    @Override
    public  Set<Map.Entry<String,RegistryTreeNode>> entrySet() {
        return this.contents.entrySet();
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
