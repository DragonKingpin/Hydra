package com.walnut.sparta.utask.console.server;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.StringUtils;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.KernelObjectConstants;
import com.pinecone.hydra.task.kom.entity.ElementNode;
import com.pinecone.hydra.task.kom.entity.GenericNamespace;
import com.pinecone.hydra.task.kom.entity.GenericTaskElement;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.ulf.util.guid.GUIDs;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.sparta.utask.console.infrastructure.UTaskConstants;
import com.walnut.sparta.utask.console.infrastructure.dto.NodeVo;
import com.walnut.sparta.utask.console.infrastructure.dto.TaskChildDto;
import com.walnut.sparta.utask.console.infrastructure.dto.TaskDto;
import com.walnut.sparta.utask.console.response.BasicResultResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
public class TaskInstrumentServiceImpl implements TaskInstrumentService {

    @Resource
    protected CentralizedTaskInstrument primaryTask;


    @Override
    public String addTask(TaskDto taskDto) {
        GenericTaskElement taskElement = taskDto.toEntity(primaryTask);

        return BasicResultResponse.success(primaryTask.put(taskElement)).toJSONString();
    }

    @Override
    public String addTaskChild(TaskChildDto taskChildDto) {

        GenericTaskElement taskElement = taskChildDto.toEntity(primaryTask);

        String parentPath = taskChildDto.getParentPath();

        if (parentPath == null || parentPath.isEmpty()) {
            primaryTask.affirmNamespace(UTaskConstants.TASK_PATH_ROOT).addChild(taskElement);
            return BasicResultResponse.success(primaryTask.queryElement(UTaskConstants.TASK_PATH_ROOT+ KernelObjectConstants.FullNameSeparator + taskElement.getName())).toJSONString();
        }


        if (Objects.equals(primaryTask.get(primaryTask.queryGUIDByPath(parentPath)).getMetaType(), "Namespace")) {
            primaryTask.affirmNamespace(parentPath).addChild(taskElement);
        } else {
            parentPath = parentPath.substring(0, parentPath.lastIndexOf(KernelObjectConstants.FullNameSeparator));
            primaryTask.affirmNamespace(parentPath).addChild(taskElement);

        }
        return BasicResultResponse.success(primaryTask.queryElement(parentPath + KernelObjectConstants.FullNameSeparator + taskElement.getName())).toJSONString();
    }

    @Override
    public String addTaskNamespaceChild( String path, String parentPath ) {


        if ( StringUtils.isBlank(parentPath) || parentPath.equalsIgnoreCase("root")) {
            GenericNamespace taskNamespace = new GenericNamespace();
            taskNamespace.setName(path);
            primaryTask.affirmNamespace(UTaskConstants.TASK_PATH_ROOT).addChild(taskNamespace);
            return BasicResultResponse.success(
                    primaryTask.queryElement(UTaskConstants.TASK_PATH_ROOT+ KernelObjectConstants.FullNameSeparator+taskNamespace.getName())
            ).toJSONString();
        } else {
            GenericNamespace taskNamespace = new GenericNamespace();
            taskNamespace.setName(path);
            String cachePath = primaryTask.getPath(primaryTask.queryGUIDByPath(parentPath));
            primaryTask.affirmNamespace(parentPath).addChild(taskNamespace);
            String fullPath = cachePath + KernelObjectConstants.FullNameSeparator + path;
            return BasicResultResponse.success(primaryTask.queryElement(fullPath)).toJSONString();

        }
    }

    @Override
    public String queryTaskGuidInfo( String guid ) {

        GUID guid1 = GUIDs.GUID128( guid );
        TreeNode treeNode = primaryTask.get(guid1);
        if (treeNode instanceof TaskElement) {

            return BasicResultResponse.success((GenericTaskElement) primaryTask.get(guid1)).toJSONString();

        }
        else{
            Collection<TreeNode> taskNamespaceChildren = primaryTask.getChildren(guid1);
            List<ElementNode> taskNamespaceChildrenList = new ArrayList<>();
            for( TreeNode treeNamespaceChildrenNode : taskNamespaceChildren) {

                if (treeNamespaceChildrenNode instanceof TaskElement) {
                    GenericTaskElement taskElement = ( GenericTaskElement ) primaryTask.get(treeNamespaceChildrenNode.getGuid());
                    taskNamespaceChildrenList.add(taskElement);
                } else {
                    GenericNamespace taskNamespace = ( GenericNamespace ) primaryTask.get(treeNamespaceChildrenNode.getGuid());
                    taskNamespaceChildrenList.add(taskNamespace);
                }
            }
            return BasicResultResponse.success(taskNamespaceChildrenList).toJSONString();
        }
    }

    @Override
    public String updateTask( String path, TaskChildDto taskChildDto ) {
        Debug.trace(path);
        GUID guid = primaryTask.queryGUIDByPath(path);
        TreeNode treeNode = primaryTask.get(guid);
        GenericTaskElement taskElement = (GenericTaskElement) treeNode;
        taskElement.setName(taskChildDto.getName());
        taskElement.setType(taskChildDto.getTaskType());
        taskElement.setImagePath(taskChildDto.getImagePath());
        taskElement.setResourceType(taskChildDto.getResourceType());
        taskElement.setDeploymentMethod(taskChildDto.getDeploymentMethod());
        taskElement.setPriority(taskChildDto.getPriority());
        taskElement.setActuallyPriority(taskChildDto.getActuallyPriority());
        taskElement.setDryRun(taskChildDto.isDryRun());
        taskElement.setScheduleCycle(taskChildDto.getKernelScheduleCycle());
        taskElement.setScheduleType(taskChildDto.getKernelScheduleType());
        taskElement.setEnable(taskChildDto.isEnable());
        primaryTask.update(taskElement);
        String parentPath = path.contains(KernelObjectConstants.FullNameSeparator) ? path.substring(0, path.lastIndexOf(KernelObjectConstants.FullNameSeparator)) : "";
        String newPath = parentPath.isEmpty() ? taskChildDto.getName() : parentPath + KernelObjectConstants.FullNameSeparator + taskChildDto.getName();

        Debug.trace("updateTask newPath:" + newPath);
        return BasicResultResponse.success(primaryTask.queryElement(newPath)).toJSONString();
    }

    @Override
    public String updateTaskNamespace( String path, String name ) {
        GUID guid = primaryTask.queryGUIDByPath(path);
        TreeNode treeNode = primaryTask.get(guid);
        GenericNamespace taskNamespace = (GenericNamespace) treeNode ;
        taskNamespace.setName(name);
        primaryTask.update(taskNamespace);
        String parentPath = path.contains(KernelObjectConstants.FullNameSeparator) ? path.substring(0, path.lastIndexOf(KernelObjectConstants.FullNameSeparator)) : "";
        String newPath = parentPath.isEmpty() ? name : parentPath + KernelObjectConstants.FullNameSeparator + name;
        Debug.info("updateTaskNamespace newPath:" + newPath);
        Collection<TreeNode> taskTreeNodes = primaryTask.getChildren(guid);
        for (TreeNode treeNode1 : taskTreeNodes) {
            primaryTask.update(treeNode1);
            primaryTask.queryElement(newPath+KernelObjectConstants.FullNameSeparator+treeNode1.getName());
        }
        return BasicResultResponse.success(primaryTask.queryElement(newPath)).toJSONString();
    }

    @Override
    public String queryTaskPath(String path) {
        GUID guid = primaryTask.queryGUIDByPath(path);
        TreeNode rootNode = primaryTask.get(guid);
        NodeVo rootVo = buildNodeVo(rootNode);

        return BasicResultResponse.success(rootVo).toJSONString();
    }

    private NodeVo buildNodeVo(TreeNode node) {
        List<NodeVo> childNodes = buildChildNodes(node.getGuid());
        return new NodeVo(
                node.getName(),
                node.getGuid().toString(),
                node.getMetaType(),
                childNodes
        );
    }

    private List<NodeVo> buildChildNodes(GUID parentGuid) {
        Collection<TreeNode> children = primaryTask.getChildren(parentGuid);
        List<NodeVo> childVos = new ArrayList<>();
        for (TreeNode child : children) {
            NodeVo childVo = buildNodeVo(child);
            childVos.add(childVo);
        }
        return childVos;
    }
}