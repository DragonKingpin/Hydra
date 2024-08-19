package com.walnut.sparta.system;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.framework.util.json.JSON;
import com.pinecone.hydra.service.ServiceTreeMapper;
import com.pinecone.hydra.unit.udsn.DistrubuteScopeTreeDataManipinate;
import com.pinecone.ulf.util.id.GUID72;
import com.walnut.sparta.pojo.ApplicationNodeInformation;
import com.walnut.sparta.pojo.ClassifNodeInformation;
import com.walnut.sparta.pojo.ServiceNodeInformation;
import com.walnut.sparta.pojo.DistributedScopeTree;
import com.walnut.sparta.service.SystemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping( "/system" )
public class SystemController {
    //    @GetMapping( "/undefined" )
//    public String undefined() {
//        return "Hello, hi, good afternoon! This is undefined specking!";
//    }

}
