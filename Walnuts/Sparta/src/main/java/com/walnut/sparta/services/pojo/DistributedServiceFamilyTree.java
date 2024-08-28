package com.walnut.sparta.services.pojo;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.service.tree.nodes.ServiceNode;
import com.pinecone.hydra.service.tree.nodes.ServiceTreeNode;
import com.pinecone.hydra.service.tree.operator.MetaNodeOperator;
import com.pinecone.hydra.service.tree.operator.MetaNodeOperatorProxy;
import com.pinecone.hydra.service.tree.source.CommonDataManipulator;
import com.pinecone.hydra.service.tree.source.DefaultMetaNodeManipulator;
import com.pinecone.hydra.service.tree.source.ServiceFamilyTreeManipulator;
import com.pinecone.hydra.service.tree.source.ServiceNodeManipulator;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DistributedServiceFamilyTree implements ServiceFamilyTree{
    private DefaultMetaNodeManipulator      defaultMetaNodeManipulator;

    private ServiceNodeManipulator          serviceNodeManipulator;

    private ServiceFamilyTreeManipulator    serviceFamilyTreeManipulator;

    private ScopeTreeManipulator            scopeTreeManipulator;

    private CommonDataManipulator           commonDataManipulator;

    private MetaNodeOperatorProxy           metaNodeOperatorProxy;



    public DistributedServiceFamilyTree(DefaultMetaNodeManipulator defaultMetaNodeManipulator){
        this.defaultMetaNodeManipulator    = defaultMetaNodeManipulator;
        this.serviceNodeManipulator        = this.defaultMetaNodeManipulator.getServiceNodeManipulator();
        this.serviceFamilyTreeManipulator  = this.defaultMetaNodeManipulator.getServiceFamilyTreeManipulator();
        this.scopeTreeManipulator          = this.defaultMetaNodeManipulator.getScopeTreeManipulator();
        this.commonDataManipulator         = this.defaultMetaNodeManipulator.getCommonDataManipulator();
        this.metaNodeOperatorProxy         = new MetaNodeOperatorProxy(this.defaultMetaNodeManipulator);
    }



    public ServiceNode getServiceNode(GUID guid){
        GUIDDistributedScopeNode node = this.scopeTreeManipulator.getNode(guid);
        UOI type = node.getType();
        MetaNodeOperator operator = this.metaNodeOperatorProxy.getOperator(type.getObjectName());
        ServiceTreeNode serviceTreeNode = operator.get(guid);
        String[] nullVariablesPackages = this.getNullVariablesPackages(serviceTreeNode);
        if (nullVariablesPackages.length>0){

        }
        return null;
    }

    private Boolean containsKey(GUID key){
        GUID guid = this.serviceFamilyTreeManipulator.getParentByChildGUID(key);
        return guid != null;
    }

    private void inheritInfo(){

    }

    private String[] getNullVariablesPackages(ServiceTreeNode serviceNode) {
        if (serviceNode == null) return new String[0];
        Class<?> clazz = serviceNode.getClass();
        Field[] fields = clazz.getDeclaredFields();
        List<String> packageNames = new ArrayList<>();

        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(serviceNode);
                if (value == null) {
                    Class<?> fieldType = field.getType();
                    String packageName = fieldType.getPackage().getName();
                    packageNames.add(packageName);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("访问成员失败", e);
        }

        return packageNames.toArray(new String[0]);
    }
}
