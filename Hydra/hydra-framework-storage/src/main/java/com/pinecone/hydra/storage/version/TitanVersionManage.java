package com.pinecone.hydra.storage.version;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.version.entity.TitanVersion;
import com.pinecone.hydra.storage.version.entity.TitanVersionMapping;
import com.pinecone.hydra.storage.version.entity.VersionMapping;
import com.pinecone.hydra.storage.version.source.VersionManipulator;
import com.pinecone.hydra.storage.version.source.VersionMappingManipulator;
import com.pinecone.hydra.storage.version.source.VersionMasterManipulator;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.ulf.util.guid.GenericGuidAllocator;

import java.util.List;

public class TitanVersionManage implements VersionManage {
    protected Hydrarum                      hydrarum;

    protected GuidAllocator                 guidAllocator;

    protected VersionMasterManipulator      masterManipulator;

    protected VersionManipulator            versionManipulator;
    protected VersionMappingManipulator     versionMappingManipulator;

    public TitanVersionManage( Hydrarum hydrarum, KOIMasterManipulator masterManipulator, String name ){
        this.hydrarum                   = hydrarum;
        this.masterManipulator          = (VersionMasterManipulator) masterManipulator;
        this.guidAllocator              = new GenericGuidAllocator();
        this.versionManipulator         = this.masterManipulator.getVersionManipulator();
        this.versionMappingManipulator  = this.masterManipulator.getVersionMappingManipulator();
    }

    public TitanVersionManage( Hydrarum hydrarum, KOIMasterManipulator masterManipulator ){
        this( hydrarum, masterManipulator, VersionManage.class.getSimpleName() );
    }

    public TitanVersionManage( KOIMappingDriver driver ) {
        this(
                driver.getSystem(),
                driver.getMasterManipulator()
        );
    }


    @Override
    public void insert(TitanVersion version) {
        this.versionManipulator.insertObjectVersion( version);
    }

    @Override
    public void remove(String version, GUID fileGuid) {
        this.versionManipulator.removeObjectVersion( version, fileGuid );
    }

    @Override
    public boolean queryIsManage(GUID targetStorageObjectGuid) {
        return this.versionManipulator.queryIsManage( targetStorageObjectGuid );
    }

    @Override
    public GUID queryObjectGuid(String version, GUID fileGuid) {
        return this.versionManipulator.queryObjectGuid( version, fileGuid );
    }

    @Override
    public List<GUID> fetchVersions(GUID guid) {
        return  this.versionManipulator.fetchVersions( guid );
    }

    @Override
    public GUID getVersionFileByGuid(GUID fileGuid) {
        return this.versionManipulator.getVersionFileByGuid( fileGuid );
    }

    @Override
    public TitanVersion queryByTargetStorageObjectGuid(GUID targetStorageObjectGuid) {
        return this.versionManipulator.queryByTargetStorageObjectGuid( targetStorageObjectGuid );
    }

    @Override
    public boolean isExistEnableVersionMapping(GUID enableVersionGuid) {
        for (TitanVersionMapping versionMapping : this.versionMappingManipulator.queryAllVersionMapper())
            if (versionMapping.getEnableVersionGuid().equals(enableVersionGuid))
                return true;
        return false;
    }

    @Override
    public VersionMapping queryVersionMapping(GUID fileGuid) {
        for (TitanVersionMapping versionMapping : this.versionMappingManipulator.queryAllVersionMapper())
            if (versionMapping.getFileGuid().equals(fileGuid))
                return versionMapping;
        return null;
    }

    @Override
    public void UpdateVesionMapping(VersionMapping versionMapping) {
        this.versionMappingManipulator.update(versionMapping);
    }

    @Override
    public void insertVesionMapping(TitanVersionMapping versionMapping) {
        this.versionMappingManipulator.insert(versionMapping);
    }
}
