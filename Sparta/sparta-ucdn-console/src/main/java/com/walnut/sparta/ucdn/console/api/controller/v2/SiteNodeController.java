package com.walnut.sparta.ucdn.console.api.controller.v2;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.service.kom.entity.ServiceElement;
import com.pinecone.hydra.service.registry.ServiceLifecycleIface;
import com.pinecone.hydra.service.registry.dto.RegisterServiceDTO;
import com.pinecone.hydra.storage.bucket.BucketInstrument;
import com.pinecone.hydra.storage.bucket.entity.GenericSiteNode;
import com.pinecone.hydra.storage.bucket.entity.SiteNode;
import com.pinecone.ulf.util.guid.GUIDs;
import com.walnut.redstone.response.BasicResultResponse;
import com.walnut.sparta.ucdn.console.infrastructure.dto.SiteNodeDTO;
import com.walnut.sparta.ucdn.console.infrastructure.vo.SiteNodeVO;
import com.walnut.sparta.ucdn.console.infrastructure.service.UCDNServiceManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping( "/api/v2/ucdn/siteNode" )
@CrossOrigin
public class SiteNodeController {
    @Resource
    private BucketInstrument bucketInstrument;

    @Resource
    private UCDNServiceManager UCDNServiceManager;

    @Resource
    private ServicesInstrument primaryService;

    @GetMapping("/query/siteGuid")
    public String querySiteNodeBySiteGuid(@RequestParam("siteGuid") String siteGuid){
        ServiceLifecycleIface lifecycleIface = this.UCDNServiceManager.getLifecycleIface();
        ArrayList<SiteNodeVO> siteNodeVOS = new ArrayList<>();
        List<SiteNode> siteNodes = this.bucketInstrument.querySiteNodeBySiteGuid(GUIDs.GUID72( siteGuid ));
        for( SiteNode siteNode : siteNodes ){
            if( lifecycleIface.hasOwnedServiceByServiceId( siteNode.getRelatedService().toString() ) ){
                siteNode.setState( 1 );
            }else {
                siteNode.setState( 0 );
            }
            SiteNodeVO siteNodeVO = new SiteNodeVO(siteNode);
            siteNodeVO.setRelatedServicePath( this.primaryService.getPath( siteNode.getRelatedService() ) );
            siteNodeVOS.add( siteNodeVO );
        }
        return BasicResultResponse.success(siteNodeVOS).toJSONString();
    }

    @DeleteMapping("/remove/siteNodeGuid")
    public BasicResultResponse<String> removeSiteNode( @RequestParam("siteNodeGuid") String siteNodeGuid ){
        ServiceLifecycleIface lifecycleIface = this.UCDNServiceManager.getLifecycleIface();
        SiteNode siteNode = this.bucketInstrument.querySiteNode(GUIDs.GUID72(siteNodeGuid));
        lifecycleIface.deregisterServiceByServiceId( siteNode.getRelatedService().toString() );
        this.bucketInstrument.removeSiteNode( GUIDs.GUID72( siteNodeGuid ) );
        return BasicResultResponse.success();
    }

    @PostMapping("/create")
    public BasicResultResponse<String> createSiteNode(@RequestBody SiteNodeDTO dto){
        GenericSiteNode siteNode = new GenericSiteNode();
        siteNode.setSiteGuid( GUIDs.GUID72( dto.getSiteGuid() ) );
        siteNode.setNodeName( dto.getNodeName() );
        siteNode.setRelatedService( GUIDs.GUID72( dto.getRelatedService() ) );
        GUID guid = this.bucketInstrument.createSiteNode(siteNode);
        return BasicResultResponse.success(guid.toString());
    }

    @PostMapping("/update")
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
        this.UCDNServiceManager.getLifecycleIface().registerService( dto );
        Debug.trace( "是否存在" + this.UCDNServiceManager.getLifecycleIface().hasOwnedServiceByServiceId( dto.getServiceId() ) );
        return BasicResultResponse.success();
    }
}
