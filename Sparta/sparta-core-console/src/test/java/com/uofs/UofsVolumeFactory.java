package com.uofs;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.KernelVolumeConfig;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.storage.volume.block.SimpleVolume;
import com.pinecone.hydra.storage.volume.block.SpannedVolume;
import com.pinecone.hydra.storage.volume.block.StripedVolume;
import com.pinecone.hydra.storage.volume.config.TitanVolumeDefaults;
import com.pinecone.hydra.storage.volume.config.TitanVolumeSchema;
import com.pinecone.hydra.storage.volume.core.ArchVolume;
import com.pinecone.hydra.storage.volume.core.VolumeAllocationMode;
import com.pinecone.hydra.storage.volume.core.VolumeExtent;
import com.pinecone.hydra.storage.volume.core.VolumeExtentRole;
import com.pinecone.hydra.storage.volume.core.VolumePhysical;
import com.pinecone.hydra.storage.volume.core.VolumePhysicalStatus;
import com.pinecone.hydra.storage.volume.core.VolumePhysicalType;
import com.pinecone.hydra.storage.volume.core.VolumeRecord;
import com.pinecone.hydra.storage.volume.io.LocalFilePhysicalAccessor;
import com.pinecone.hydra.storage.volume.io.LocalObjectDirectoryPhysicalAccessor;
import com.pinecone.hydra.storage.volume.io.PhysicalAccessor;
import com.pinecone.ulf.util.guid.GUIDs;

import java.io.File;
import java.util.Arrays;

final class UofsVolumeFactory {
    private static final long DEFAULT_CAPACITY = 100L * UofsSmokePaths.MB;

    private final UofsSmokeContext mContext;

    UofsVolumeFactory( UofsSmokeContext context ) {
        this.mContext = context;
    }

    UofsVolumeFixture directObjectSimple( String caseName, String seed ) throws Exception {
        GUID volumeGuid = this.guid( seed, "201" );
        GUID physicalGuid = this.guid( seed, "101" );
        this.cleanup( seed );

        File objectRoot = new File( this.mContext.root + "\\" + caseName + "\\object" );
        PhysicalAccessor physical = new LocalObjectDirectoryPhysicalAccessor(
                physicalGuid,
                caseName + "-object",
                objectRoot.toPath(),
                DEFAULT_CAPACITY,
                new KernelVolumeConfig()
        );

        UniformVolumeManager manager = this.newManager();
        manager.registerPhysical( physical );
        manager.persistPhysical( this.physicalRecord( physical, objectRoot.getPath(), VolumePhysicalType.LOCAL_DIR ) );
        manager.createSimpleVolume(
                volumeGuid,
                caseName + "-object-simple",
                VolumeExtent.forPhysical( this.guid( seed, "301" ), null, physical, 0L, DEFAULT_CAPACITY, 0, VolumeExtentRole.SIMPLE_BACKING )
        );

        return new UofsVolumeFixture( caseName, volumeGuid, manager, objectRoot );
    }

    UofsVolumeFixture blockSimple( String caseName, String seed, VolumeAllocationMode allocationMode ) throws Exception {
        GUID volumeGuid = this.guid( seed, "201" );
        GUID physicalGuid = this.guid( seed, "101" );
        this.cleanup( seed );

        File physicalFile = new File( this.mContext.root + "\\" + caseName + "\\" + TitanVolumeSchema.BlockBackingFileName );
        this.deleteFile( physicalFile );
        PhysicalAccessor physical = new LocalFilePhysicalAccessor(
                physicalGuid,
                caseName + "-block",
                physicalFile.toPath(),
                DEFAULT_CAPACITY,
                allocationMode,
                UofsSmokePaths.ALLOCATION_UNIT
        );

        UniformVolumeManager manager = this.newManager();
        manager.registerPhysical( physical );
        manager.persistPhysical( this.physicalRecord( physical, physicalFile.getPath(), VolumePhysicalType.LOCAL_FILE ) );
        SimpleVolume volume = manager.createSimpleVolume(
                volumeGuid,
                caseName + "-block-simple",
                VolumeExtent.forPhysical( this.guid( seed, "301" ), null, physical, 0L, DEFAULT_CAPACITY, 0, VolumeExtentRole.SIMPLE_BACKING )
        );
        ( (ArchVolume) volume ).setAllocationMode( allocationMode );
        ( (ArchVolume) volume ).setAllocationUnit( UofsSmokePaths.ALLOCATION_UNIT );
        this.mContext.mappers.volumeMapper.update( VolumeRecord.fromVolume( volume ) );

        UofsVolumeFixture fixture = new UofsVolumeFixture( caseName, volumeGuid, manager, null );
        fixture.addPhysicalFile( physicalFile );
        return fixture;
    }

    UofsVolumeFixture blockSpanned( String caseName, String seed ) throws Exception {
        this.cleanup( seed );
        UniformVolumeManager manager = this.newManager();
        SimpleVolume left = this.childSimple( manager, caseName, seed, "101", "201", "301", 40L * UofsSmokePaths.MB );
        SimpleVolume right = this.childSimple( manager, caseName, seed, "102", "202", "302", 60L * UofsSmokePaths.MB );
        GUID spannedGuid = this.guid( seed, "901" );
        SpannedVolume spanned = manager.createSpannedVolume(
                spannedGuid,
                caseName + "-block-spanned",
                Arrays.asList(
                        VolumeExtent.forChild( this.guid( seed, "401" ), null, left, 0L, 40L * UofsSmokePaths.MB, 0, VolumeExtentRole.SPANNED_EXTENT ),
                        VolumeExtent.forChild( this.guid( seed, "402" ), null, right, 0L, 60L * UofsSmokePaths.MB, 1, VolumeExtentRole.SPANNED_EXTENT )
                )
        );
        this.mContext.mappers.volumeMapper.update( VolumeRecord.fromVolume( spanned ) );
        UofsVolumeFixture fixture = new UofsVolumeFixture( caseName, spannedGuid, manager, null );
        fixture.addPhysicalFile( new File( this.mContext.root + "\\" + caseName + "\\child-101.bin" ) );
        fixture.addPhysicalFile( new File( this.mContext.root + "\\" + caseName + "\\child-102.bin" ) );
        return fixture;
    }

    UofsVolumeFixture blockStriped( String caseName, String seed ) throws Exception {
        this.cleanup( seed );
        UniformVolumeManager manager = this.newManager();
        SimpleVolume left = this.childSimple( manager, caseName, seed, "101", "201", "301", 64L * UofsSmokePaths.MB );
        SimpleVolume right = this.childSimple( manager, caseName, seed, "102", "202", "302", 64L * UofsSmokePaths.MB );
        GUID stripedGuid = this.guid( seed, "901" );
        StripedVolume striped = manager.createStripedVolume(
                stripedGuid,
                caseName + "-block-striped",
                TitanVolumeDefaults.DefaultStripeUnit,
                Arrays.asList(
                        VolumeExtent.forChild( this.guid( seed, "401" ), null, left, 0L, 64L * UofsSmokePaths.MB, 0, VolumeExtentRole.STRIPED_MEMBER ),
                        VolumeExtent.forChild( this.guid( seed, "402" ), null, right, 0L, 64L * UofsSmokePaths.MB, 1, VolumeExtentRole.STRIPED_MEMBER )
                )
        );
        this.mContext.mappers.volumeMapper.update( VolumeRecord.fromVolume( striped ) );
        UofsVolumeFixture fixture = new UofsVolumeFixture( caseName, stripedGuid, manager, null );
        fixture.addPhysicalFile( new File( this.mContext.root + "\\" + caseName + "\\child-101.bin" ) );
        fixture.addPhysicalFile( new File( this.mContext.root + "\\" + caseName + "\\child-102.bin" ) );
        return fixture;
    }

    private SimpleVolume childSimple(
            UniformVolumeManager manager,
            String caseName,
            String seed,
            String physicalSuffix,
            String volumeSuffix,
            String extentSuffix,
            long capacity
    ) throws Exception {
        File physicalFile = new File( this.mContext.root + "\\" + caseName + "\\child-" + physicalSuffix + ".bin" );
        this.deleteFile( physicalFile );
        PhysicalAccessor physical = new LocalFilePhysicalAccessor(
                this.guid( seed, physicalSuffix ),
                caseName + "-child-" + physicalSuffix,
                physicalFile.toPath(),
                capacity,
                VolumeAllocationMode.THIN,
                UofsSmokePaths.ALLOCATION_UNIT
        );
        manager.registerPhysical( physical );
        manager.persistPhysical( this.physicalRecord( physical, physicalFile.getPath(), VolumePhysicalType.LOCAL_FILE ) );
        return manager.createSimpleVolume(
                this.guid( seed, volumeSuffix ),
                caseName + "-child-volume-" + volumeSuffix,
                VolumeExtent.forPhysical( this.guid( seed, extentSuffix ), null, physical, 0L, capacity, 0, VolumeExtentRole.SIMPLE_BACKING )
        );
    }

    private UniformVolumeManager newManager() {
        return new UniformVolumeManager( this.mContext.volumeMappingDriver, new KernelVolumeConfig() );
    }

    private VolumePhysical physicalRecord( PhysicalAccessor accessor, String rootPath, VolumePhysicalType physicalType ) {
        VolumePhysical physical = new VolumePhysical();
        physical.setGuid( accessor.getGuid() );
        physical.setName( accessor.getName() );
        physical.setPhysicalType( physicalType );
        physical.setStatus( VolumePhysicalStatus.READY );
        physical.setRootPath( rootPath );
        physical.setCapacityBytes( accessor.getCapacity() );
        physical.setUsedBytes( 0L );
        return physical;
    }

    private void cleanup( String seed ) {
        String[] suffixes = new String[] { "201", "202", "301", "302", "401", "402", "901" };
        for ( String suffix : suffixes ) {
            GUID guid = this.guid( seed, suffix );
            this.mContext.mappers.extentMapper.removeByParentGuid( guid );
            this.mContext.mappers.volumeMapper.remove( guid );
        }
        this.mContext.mappers.physicalMapper.remove( this.guid( seed, "101" ) );
        this.mContext.mappers.physicalMapper.remove( this.guid( seed, "102" ) );
    }

    private void deleteFile( File file ) {
        if ( file.exists() ) {
            file.delete();
        }
        File parent = file.getParentFile();
        if ( parent != null ) {
            parent.mkdirs();
        }
    }

    private GUID guid( String seed, String suffix ) {
        return GUIDs.GUID128( "01990000-0000-" + seed + "-8000-000000000" + suffix );
    }
}
