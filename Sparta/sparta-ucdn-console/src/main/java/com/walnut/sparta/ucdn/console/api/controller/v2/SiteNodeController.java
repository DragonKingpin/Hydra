package com.walnut.sparta.ucdn.console.api.controller.v2;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.bucket.BucketInstrument;
import com.pinecone.hydra.storage.bucket.entity.GenericSiteNode;
import com.pinecone.hydra.storage.bucket.entity.SiteNode;
import com.pinecone.ulf.util.guid.GUIDs;
import com.walnut.sparta.ucdn.console.api.response.BasicResultResponse;
import com.walnut.sparta.ucdn.console.infrastructure.dto.SiteNodeDTO;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping( "/api/v2/ucdn/siteNode" )
@CrossOrigin
public class SiteNodeController {
    @Resource
    private BucketInstrument bucketInstrument;

    @GetMapping("/query/siteNode/siteGuid")
    public String querySiteNodeBySiteGuid(@RequestParam("siteGuid") String siteGuid){
        List<SiteNode> siteNodes = this.bucketInstrument.querySiteNodeBySiteGuid(GUIDs.GUID72( siteGuid ));
        return BasicResultResponse.success(siteNodes).toJSONString();
    }

    @DeleteMapping("/remove/siteNodeGuid")
    public BasicResultResponse<String> removeSiteNode( @RequestParam("siteNodeGuid") String siteNodeGuid ){
        this.bucketInstrument.removeSiteNode( GUIDs.GUID72( siteNodeGuid ) );
        return BasicResultResponse.success();
    }

    @PostMapping("/create/siteNode")
    public BasicResultResponse<String> createSiteNode(@RequestBody SiteNodeDTO dto){
        GenericSiteNode siteNode = new GenericSiteNode();
        siteNode.setSiteGuid( GUIDs.GUID72( dto.getSiteGuid() ) );
        siteNode.setNodeName( dto.getNodeName() );
        GUID guid = this.bucketInstrument.createSiteNode(siteNode);
        return BasicResultResponse.success(guid.toString());
    }

    @PostMapping("/update/siteNode")
    public BasicResultResponse<String> updateSiteNode( @RequestBody SiteNodeDTO dto ){
        GenericSiteNode siteNode = new GenericSiteNode();
        siteNode.setNodeName( dto.getNodeName() );
        siteNode.setNodeGuid( GUIDs.GUID72( dto.getNodeGuid() ) );
        siteNode.setState( dto.getState() );
        siteNode.setIsEnabled( dto.getIsEnabled() );
        siteNode.setSiteGuid( GUIDs.GUID72( dto.getSiteGuid() ) );
        this.bucketInstrument.updateSiteNode( siteNode );
        return BasicResultResponse.success();
    }
}
