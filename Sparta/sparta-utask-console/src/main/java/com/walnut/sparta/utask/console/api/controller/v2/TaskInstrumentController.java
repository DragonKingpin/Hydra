package com.walnut.sparta.utask.console.api.controller.v2;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.entity.GenericNamespace;
import com.pinecone.hydra.task.kom.entity.GenericTaskElement;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.ulf.util.guid.GUIDs;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.sparta.utask.console.infrastructure.dto.TaskChildDto;
import com.walnut.sparta.utask.console.infrastructure.dto.TaskDto;
import com.walnut.sparta.utask.console.response.BasicResultResponse;
import com.walnut.sparta.utask.console.server.TaskInstrumentService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;


@RestController
@RequestMapping( "/api/v2/task")
@CrossOrigin
public class TaskInstrumentController implements Pinenut {

    @Resource
    protected CentralizedTaskInstrument primaryTask;

    @Resource
    protected TaskInstrumentService taskInstrumentService;

    @PostMapping("add/task")
    public String addTask( @RequestBody TaskDto taskDto ) {

       return taskInstrumentService.addTask(taskDto);

    }

    //添加任务子节点
    @PutMapping("add/task/child")
    public String addTaskChild( @RequestBody TaskChildDto taskChildDto ) {

       return taskInstrumentService.addTaskChild(taskChildDto);

    }

    //添加命名空间
    @PutMapping("add/task/namespace/child")
    public String addTaskNamespaceChild(
            @RequestParam("path") String path,
            @RequestParam("parentPath") String parentPath) {

      return  taskInstrumentService.addTaskNamespaceChild(path, parentPath);
    }


    //查找任务元素信息
    @GetMapping("query/task/guid")
    public String queryTaskGuid( String szGuid ) {

        GUID guid = GUIDs.GUID128(szGuid);

        return  BasicResultResponse.success((GenericTaskElement)primaryTask.get(guid)).toJSONString();
    }

    @GetMapping("query/guid/info")
    public String queryTaskGuidInfo( String guid ) {

        return taskInstrumentService.queryTaskGuidInfo( guid );

    }

    //添加主命名空间
    @PutMapping("add/root/task/path")
    public void addTaskRootPath( String path ) {

        GenericNamespace  namespace = new GenericNamespace();

        namespace.setName(path);

        primaryTask.put(namespace);

    }

    //查找路径下的子节点所有信息(预热根目录下俩级)
    @GetMapping("query/task/path")
    public String queryTaskPath(String path) {

       return taskInstrumentService.queryTaskPath( path );

    }

    // 递归构建子节点树

    @DeleteMapping("remove/task")
    public void removeTask( String path ) {

        primaryTask.remove(path);
    }

    @DeleteMapping("remove/task/all/child")
    public void removeTaskChild( String path ) {

        GUID guid = primaryTask.queryGUIDByPath(path);
        Collection<TreeNode> taskTreeNodes = primaryTask.getChildren(guid);
        for (TreeNode treeNode : taskTreeNodes) {
            primaryTask.remove(treeNode.getGuid());
        }

        primaryTask.remove(path);
    }

    @PutMapping("update/taskElement")
    public String updateTask(@RequestParam("path") String path, @RequestBody TaskChildDto taskChildDto) {

       return taskInstrumentService.updateTask(path, taskChildDto);

    }


    @RequestMapping( "update/taskNamespace")
    public String updateTaskNamespace(@RequestParam  ("path") String path, @RequestParam  ("name") String name) {

       return taskInstrumentService.updateTaskNamespace( path, name );

    }

}
