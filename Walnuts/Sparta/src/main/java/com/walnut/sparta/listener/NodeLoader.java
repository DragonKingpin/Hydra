//package com.walnut.sparta.listener;
//
//
//import com.pinecone.framework.util.json.JSON;
//import com.walnut.sparta.entity.ApplicationDescription;
//import com.walnut.sparta.entity.ClassificationNode;
//import com.walnut.sparta.entity.ClassificationRules;
//import com.walnut.sparta.entity.Node;
//import com.walnut.sparta.pojo.NodeInformation;
//import com.walnut.sparta.entity.NodeMetadata;
//import com.walnut.sparta.entity.ServiceDescription;
//import com.walnut.sparta.pojo.ServiceTree;
//import com.walnut.sparta.mapper.SystemMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
///**
// * 在服务启动时监听并加载所有的Node信息将其导入到服务树中
// */
//@Component
//public class NodeLoader implements ApplicationListener<ContextRefreshedEvent> {
//
//    private final SystemMapper systemMapper;
//    private final ServiceTree serviceTree;
//
//    @Autowired
//    public NodeLoader(SystemMapper systemMapper, ServiceTree serviceTree) {
//        this.systemMapper = systemMapper;
//        this.serviceTree = serviceTree;
//    }
//
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        loadNodesFromDatabase();
//    }
//
//    private void loadNodesFromDatabase() {
//       //获取所有节点和对应信息
//        List<ClassificationRules> classificationRules = systemMapper.selectAllClassificationRules();
//        List<NodeMetadata> nodeMetadata = systemMapper.selectAllNodeMetadata();
//        List<ClassificationNode> classificationNodes = systemMapper.selectAllClassificationNode();
//        List<ApplicationDescription> applicationDescriptions = systemMapper.selectAllApplicationDescription();
//        List<ServiceDescription> serviceDescriptions = systemMapper.selectAllServiceDescription();
//        List<Node> nodes = systemMapper.selectAllNode();
//        //将所有信息存入服务树中
//        for (Node node:nodes){
//            NodeInformation nodeInformation = new NodeInformation();
//            nodeInformation.setNode(node);
//            serviceTree.addNode(nodeInformation);
//        }
//        serviceTree.setClassificationNodeList(classificationNodes);
//        serviceTree.setServiceDescriptionList(serviceDescriptions);
//        serviceTree.setNodeMetadataList(nodeMetadata);
//        serviceTree.setApplicationDescriptionList(applicationDescriptions);
//        serviceTree.setClassificationRulesList(classificationRules);
//        String stringify = JSON.stringify(serviceTree);
//        System.out.println(stringify);
//    }
//}
