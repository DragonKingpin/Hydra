package com.walnut.sparta.system;

import com.walnut.sparta.entity.node;
import com.walnut.sparta.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/save_node")
    public String save_node(@RequestBody node node){
        systemService.save_node(node);
        return "保存节点成功";
    }

    /**
     * 删除节点
     * @param uuid 节点唯一标识
     * @return 返回删除情况
     */
    @DeleteMapping("/delete_node/{uuid}")
    public String delete_node(@PathVariable("uuid") String uuid){
        return systemService.delete_node(uuid);
    }
}
