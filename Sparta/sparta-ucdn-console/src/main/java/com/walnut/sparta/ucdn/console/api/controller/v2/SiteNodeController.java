package com.walnut.sparta.ucdn.console.api.controller.v2;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.ServiceInstance;
import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.service.kom.entity.ServiceElement;
import com.pinecone.hydra.service.registry.ServiceLifecycleIface;
import com.pinecone.hydra.service.registry.dto.RegisterServiceDTO;
import com.pinecone.hydra.storage.bucket.BucketInstrument;
import com.pinecone.hydra.storage.bucket.entity.GenericSiteNode;
import com.pinecone.hydra.storage.bucket.entity.SiteNode;
import com.pinecone.hydra.uma.DuplexAppointClient;
import com.pinecone.ulf.util.guid.GUIDs;
import com.walnut.sparta.ucdn.console.api.response.BasicResultResponse;
import com.walnut.sparta.ucdn.console.infrastructure.dto.SiteNodeDTO;
import com.walnut.sparta.ucdn.console.umc.wolf.WolfRPCManage;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequestMapping( "/api/v2/ucdn/siteNode" )
@CrossOrigin
public class SiteNodeController {
    @Resource
    private BucketInstrument bucketInstrument;

    @Resource
    private WolfRPCManage     wolfRPCManage;

    @Resource
    private ServicesInstrument primaryService;

    @GetMapping("/query/siteNode/siteGuid")
    public String querySiteNodeBySiteGuid(@RequestParam("siteGuid") String siteGuid){
        ServiceLifecycleIface lifecycleIFace = this.wolfRPCManage.getLifecycleIFace();
        List<SiteNode> siteNodes = this.bucketInstrument.querySiteNodeBySiteGuid(GUIDs.GUID72( siteGuid ));
        for( SiteNode siteNode : siteNodes ){
            if( lifecycleIFace.hasOwnedServiceByServiceId( siteNode.getRelatedService().toString() ) ){
                siteNode.setState( 1 );
            }else {
                siteNode.setState( 0 );
            }
        }
        return BasicResultResponse.success(siteNodes).toJSONString();
    }

    @DeleteMapping("/remove/siteNodeGuid")
    public BasicResultResponse<String> removeSiteNode( @RequestParam("siteNodeGuid") String siteNodeGuid ){
        ServiceLifecycleIface lifecycleIFace = this.wolfRPCManage.getLifecycleIFace();
        SiteNode siteNode = this.bucketInstrument.querySiteNode(GUIDs.GUID72(siteNodeGuid));
        lifecycleIFace.deregisterServiceByServiceId( siteNode.getRelatedService().toString() );
        this.bucketInstrument.removeSiteNode( GUIDs.GUID72( siteNodeGuid ) );
        return BasicResultResponse.success();
    }

    @PostMapping("/create/siteNode")
    public BasicResultResponse<String> createSiteNode(@RequestBody SiteNodeDTO dto){
        GenericSiteNode siteNode = new GenericSiteNode();
        siteNode.setSiteGuid( GUIDs.GUID72( dto.getSiteGuid() ) );
        siteNode.setNodeName( dto.getNodeName() );
        siteNode.setRelatedService( GUIDs.GUID72( dto.getRelatedService() ) );
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

    @GetMapping("/fetch/allService")
    public String fetchAllService(){
        List<ServiceElement> serviceElements = this.primaryService.fetchAllService();
        return BasicResultResponse.success(serviceElements).toJSONString();
    }

    @PostMapping("/test/registerService")
    public BasicResultResponse<String> testRegisterService( @RequestBody RegisterServiceDTO dto ){
        this.wolfRPCManage.getLifecycleIFace().registerService( dto );
        Debug.trace( "是否存在" + this.wolfRPCManage.getLifecycleIFace().hasOwnedServiceByServiceId( dto.getServiceId() ) );
        return BasicResultResponse.success();
    }
}
