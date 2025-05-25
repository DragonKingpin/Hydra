package com.pinecone.hydra.storage.bucket;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.bucket.entity.Bucket;
import com.pinecone.hydra.storage.bucket.entity.Site;
import com.pinecone.hydra.storage.bucket.entity.SiteNode;
import com.pinecone.hydra.storage.bucket.source.BucketManipulator;
import com.pinecone.hydra.storage.bucket.source.BucketMasterManipulator;
import com.pinecone.hydra.storage.bucket.source.SiteManipulator;
import com.pinecone.hydra.storage.bucket.source.SiteNodeManipulator;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.system.Hydrogen;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.ulf.util.guid.i64.GenericGuidAllocator;

import java.time.LocalDateTime;
import java.util.List;

public class TitanBucketInstrument implements BucketInstrument {
    protected Hydrogen                  hydrogen;

    protected BucketMasterManipulator   masterManipulator;

    protected BucketManipulator         bucketManipulator;

    protected SiteManipulator           siteManipulator;

    protected SiteNodeManipulator       siteNodeManipulator;

    protected GuidAllocator             guidAllocator;

    public TitanBucketInstrument(Hydrogen hydrogen, KOIMasterManipulator masterManipulator, String name ){
        this.hydrogen               = hydrogen;
        this.masterManipulator      = (BucketMasterManipulator) masterManipulator;
        this.guidAllocator          = new GenericGuidAllocator();

        this.bucketManipulator      = this.masterManipulator.getBucketManipulator();
        this.siteManipulator        = this.masterManipulator.getSiteManipulator();
        this.siteNodeManipulator    = this.masterManipulator.getSiteNodeManipulator();
    }

    public TitanBucketInstrument( Hydrogen hydrogen, KOIMasterManipulator masterManipulator ){
        this( hydrogen, masterManipulator, KOMFileSystem.class.getSimpleName() );
    }

    public TitanBucketInstrument(KOIMappingDriver driver ) {
        this(
                driver.getSystem(),
                driver.getMasterManipulator()
        );
    }

    @Override
    public GUID createBucket(Bucket bucket) {
        GUID guid = this.guidAllocator.nextGUID();
        bucket.setBucketGuid( guid );
        this.bucketManipulator.insert( bucket );
        return guid;
    }

    @Override
    public void removeBucket(GUID bucketGuid) {
        this.bucketManipulator.remove( bucketGuid );
    }

    @Override
    public void removeBucketByAccountAndBucketName(GUID accountGuid, String bucketName) {

    }

    @Override
    public Bucket queryBucketByBucketGuid(GUID bucketGuid) {
        return this.bucketManipulator.queryBucketByBucketGuid( bucketGuid );
    }

    @Override
    public List<Bucket> queryBucketsByUserGuid(GUID userGuid) {
        return this.bucketManipulator.queryBucketsByUserGuid( userGuid );
    }

    @Override
    public SiteManipulator getSiteManipulator() {
        return this.siteManipulator;
    }

    @Override
    public GUID createSite(Site site) {
        GUID guid = this.guidAllocator.nextGUID();
        site.setSiteGuid(guid);
        site.setCreateTime(LocalDateTime.now());
        this.siteManipulator.insert(site);
        return guid;
    }

    @Override
    public void removeSite(GUID siteGuid) {
        this.siteManipulator.remove(siteGuid);
    }

    @Override
    public void removeSite(String siteName) {
        this.siteManipulator.removeByName( siteName );
    }

    @Override
    public Site querySite(GUID siteGuid) {
        return this.siteManipulator.querySite(siteGuid);
    }

    @Override
    public List<Site> listSite() {
        return this.siteManipulator.listSite();
    }

    @Override
    public GUID createSiteNode(SiteNode siteNode) {
        if ( siteNode.getNodeGuid() == null ){
            siteNode.setNodeGuid( this.guidAllocator.nextGUID() );
        }

        this.siteNodeManipulator.insert( siteNode );
        return siteNode.getNodeGuid();
    }

    @Override
    public void removeSiteNode(GUID siteNodeGuid) {
        this.siteNodeManipulator.remove( siteNodeGuid );
    }

    @Override
    public SiteNode querySiteNode(GUID siteNodeGuid) {
        return this.siteNodeManipulator.querySiteNode( siteNodeGuid );
    }

    @Override
    public List<SiteNode> querySiteNodeBySiteGuid( GUID siteGuid ) {
        return this.siteNodeManipulator.querySiteNodeBySiteGuid( siteGuid );
    }

    @Override
    public void updateSiteNode(SiteNode siteNode) {
        this.siteNodeManipulator.update( siteNode );
    }

}
