package com.walnut.sparta.system;

import com.walnut.sparta.entity.Node;
import com.walnut.sparta.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping( "/system" )
public class SystemController {
    //    @GetMapping( "/undefined" )
//    public String undefined() {
//        return "Hello, hi, good afternoon! This is undefined specking!";
//    }
    @Autowired
    private SystemService systemService;
    //node类的CRUD操作

    /**
     * 保存节点信息
     * @param node 节点信息
     * @return 返回保存情况
     */
    @PostMapping("/saveNode")
    public String saveNode(@RequestBody Node node){
        systemService.saveNode(node);
        return "保存节点成功";
    }

    /**
     * 删除节点
     * @param uuid 节点唯一标识
     * @return 返回删除情况
     */
    @DeleteMapping("/deleteNode/{UUID}")
    public String delete_node(@PathVariable("UUID") String uuid){
        return systemService.deleteNode(uuid);
    }
}
