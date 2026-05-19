package com.pinecone.hydra.storage.volume;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.block.BlockSimpleVolume;
import com.pinecone.hydra.storage.volume.block.BlockSpannedVolume;
import com.pinecone.hydra.storage.volume.block.BlockVolume;
import com.pinecone.hydra.storage.volume.block.SimpleVolume;
import com.pinecone.hydra.storage.volume.block.SpannedVolume;
import com.pinecone.hydra.storage.volume.block.StripedVolume;
import com.pinecone.hydra.storage.volume.block.TitanBlockSimpleVolume;
import com.pinecone.hydra.storage.volume.block.TitanBlockSpannedVolume;
import com.pinecone.hydra.storage.volume.block.TitanStripedVolume;
import com.pinecone.hydra.storage.volume.object.TitanObjectSimpleVolume;
import com.pinecone.hydra.storage.volume.object.TitanObjectSpannedVolume;
import com.pinecone.hydra.storage.volume.core.Volume;
import com.pinecone.hydra.storage.volume.core.BuiltinStorageSupportType;
import com.pinecone.hydra.storage.volume.core.ObjectMappedType;
import com.pinecone.hydra.storage.volume.core.StorageSupportDescriptor;
import com.pinecone.hydra.storage.volume.core.VolumeEvent;
import com.pinecone.hydra.storage.volume.core.VolumeExtent;
import com.pinecone.hydra.storage.volume.core.VolumeExtentRole;
import com.pinecone.hydra.storage.volume.core.VolumePhysical;
import com.pinecone.hydra.storage.volume.core.VolumePhysicalSupportTrait;
import com.pinecone.hydra.storage.volume.core.VolumePhysicalStatus;
import com.pinecone.hydra.storage.volume.core.VolumePhysicalType;
import com.pinecone.hydra.storage.volume.core.VolumeRecord;
import com.pinecone.hydra.storage.volume.core.VolumeStatus;
import com.pinecone.hydra.storage.volume.core.VolumeType;
import com.pinecone.hydra.storage.volume.core.ArchVolume;
import com.pinecone.hydra.storage.volume.core.VolumeAllocationMode;
import com.pinecone.hydra.storage.volume.core.VolumeMappingMode;
import com.pinecone.hydra.storage.volume.io.LocalFilePhysicalAccessor;
import com.pinecone.hydra.storage.volume.io.LocalObjectDirectoryPhysicalAccessor;
import com.pinecone.hydra.storage.volume.io.PhysicalAccessor;
import com.pinecone.hydra.storage.volume.object.ObjectSimpleVolume;
import com.pinecone.hydra.storage.volume.object.ObjectSpannedVolume;
import com.pinecone.hydra.storage.volume.source.VolumeExtentManipulator;
import com.pinecone.hydra.storage.volume.source.VolumeEventManipulator;
import com.pinecone.hydra.storage.volume.source.KernelStorageSupportTypeProvider;
import com.pinecone.hydra.storage.volume.source.StorageSupportTypeProvider;
import com.pinecone.hydra.storage.volume.source.VolumeManipulator;
import com.pinecone.hydra.storage.volume.source.VolumeMasterManipulator;
import com.pinecone.hydra.storage.volume.source.VolumePhysicalManipulator;
import com.pinecone.hydra.storage.volume.source.VolumePhysicalSupportTraitManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.ulf.util.guid.GUIDs;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class UniformVolumeManager implements VolumeManager {
    protected final Map<GUID, PhysicalAccessor> mPhysicalAccessors;
    protected final Map<GUID, Volume>           mVolumes;
    protected VolumeManipulator                 mVolumeManipulator;
    protected VolumePhysicalManipulator         mPhysicalManipulator;
    protected VolumePhysicalSupportTraitManipulator mPhysicalSupportTraitManipulator;
    protected StorageSupportTypeProvider          mStorageSupportTypeProvider;
    protected VolumeExtentManipulator           mExtentManipulator;
    protected VolumeEventManipulator            mEventManipulator;
    protected VolumeConfig                      mConfig;

    public UniformVolumeManager() {
        this.mPhysicalAccessors = new LinkedHashMap<>();
        this.mVolumes           = new LinkedHashMap<>();
        this.mConfig            = new KernelVolumeConfig();
        this.mStorageSupportTypeProvider = new KernelStorageSupportTypeProvider();
    }

    public UniformVolumeManager( VolumeConfig config ) {
        this();
        this.mConfig = config == null ? new KernelVolumeConfig() : config;
    }

    public UniformVolumeManager(
            VolumeManipulator volumeManipulator,
            VolumePhysicalManipulator physicalManipulator,
            VolumeExtentManipulator extentManipulator
    ) {
        this();
        this.mVolumeManipulator   = volumeManipulator;
        this.mPhysicalManipulator = physicalManipulator;
        this.mExtentManipulator   = extentManipulator;
    }

    public UniformVolumeManager(
            VolumeManipulator volumeManipulator,
            VolumePhysicalManipulator physicalManipulator,
            VolumeExtentManipulator extentManipulator,
            VolumeEventManipulator eventManipulator
    ) {
        this( volumeManipulator, physicalManipulator, extentManipulator );
        this.mEventManipulator = eventManipulator;
    }

    public UniformVolumeManager(
            VolumeManipulator volumeManipulator,
            VolumePhysicalManipulator physicalManipulator,
            VolumeExtentManipulator extentManipulator,
            VolumeConfig config
    ) {
        this( config );
        this.mVolumeManipulator   = volumeManipulator;
        this.mPhysicalManipulator = physicalManipulator;
        this.mExtentManipulator   = extentManipulator;
    }

    public UniformVolumeManager(
            VolumeManipulator volumeManipulator,
            VolumePhysicalManipulator physicalManipulator,
            VolumeExtentManipulator extentManipulator,
            VolumeEventManipulator eventManipulator,
            VolumeConfig config
    ) {
        this( volumeManipulator, physicalManipulator, extentManipulator, config );
        this.mEventManipulator = eventManipulator;
    }

    public UniformVolumeManager( KOIMappingDriver driver, VolumeConfig config ) {
        this( config );
        VolumeMasterManipulator masterManipulator = (VolumeMasterManipulator) driver.getMasterManipulator();
        this.mVolumeManipulator = masterManipulator.getVolumeManipulator();
        this.mPhysicalManipulator = masterManipulator.getPhysicalManipulator();
        this.mPhysicalSupportTraitManipulator = masterManipulator.getPhysicalSupportTraitManipulator();
        this.mExtentManipulator = masterManipulator.getExtentManipulator();
        this.mEventManipulator = masterManipulator.getEventManipulator();
    }

    @Override
    public VolumeConfig getConfig() {
        return this.mConfig;
    }

    public void setVolumeManipulator( VolumeManipulator volumeManipulator ) {
        this.mVolumeManipulator = volumeManipulator;
    }

    public void setPhysicalManipulator( VolumePhysicalManipulator physicalManipulator ) {
        this.mPhysicalManipulator = physicalManipulator;
    }

    public void setPhysicalSupportTraitManipulator( VolumePhysicalSupportTraitManipulator physicalSupportTraitManipulator ) {
        this.mPhysicalSupportTraitManipulator = physicalSupportTraitManipulator;
    }

    public void setStorageSupportTypeProvider( StorageSupportTypeProvider storageSupportTypeProvider ) {
        this.mStorageSupportTypeProvider = storageSupportTypeProvider == null
                ? new KernelStorageSupportTypeProvider()
                : storageSupportTypeProvider;
    }

    public void setExtentManipulator( VolumeExtentManipulator extentManipulator ) {
        this.mExtentManipulator = extentManipulator;
    }

    public void setEventManipulator( VolumeEventManipulator eventManipulator ) {
        this.mEventManipulator = eventManipulator;
    }

    @Override
    public void registerPhysical( PhysicalAccessor physicalAccessor ) {
        this.mPhysicalAccessors.put( physicalAccessor.getGuid(), physicalAccessor );
    }

    @Override
    public void persistPhysical( VolumePhysical physical ) {
        if ( this.mPhysicalManipulator == null ) {
            return;
        }
        if ( this.mPhysicalManipulator.get( physical.getGuid() ) == null ) {
            this.mPhysicalManipulator.insert( physical );
        }
        else {
            this.mPhysicalManipulator.update( physical );
        }
    }

    @Override
    public void registerVolume( Volume volume ) {
        this.mVolumes.put( volume.getGuid(), volume );
    }

    @Override
    public Optional<PhysicalAccessor> findPhysical( GUID guid ) {
        return Optional.ofNullable( this.mPhysicalAccessors.get( guid ) );
    }

    @Override
    public PhysicalAccessor loadPhysicalAccessor( GUID physicalGuid ) throws IOException {
        return this.loadPhysicalAccessor( physicalGuid, null );
    }

    @Override
    public PhysicalAccessor loadPhysicalAccessor(
            GUID physicalGuid,
            VolumeAllocationMode allocationMode,
            long allocationUnit
    ) throws IOException {
        VolumeRecord record = new VolumeRecord();
        record.setGuid( physicalGuid );
        record.setAllocationMode( allocationMode == null ? null : allocationMode.name() );
        record.setAllocationUnit( allocationUnit );
        return this.loadPhysicalAccessor( physicalGuid, record );
    }

    @Override
    public Optional<Volume> findVolume( GUID guid ) {
        return Optional.ofNullable( this.mVolumes.get( guid ) );
    }

    @Override
    public Volume affirmVolume( GUID guid ) {
        Volume volume = this.mVolumes.get( guid );
        if ( volume == null ) {
            throw new IllegalArgumentException( "Volume not found: " + guid );
        }
        return volume;
    }

    @Override
    public BlockSimpleVolume createBlockSimpleVolume( GUID guid, String name, VolumeExtent backingExtent ) {
        backingExtent.setParentGuid( guid );
        backingExtent.setRole( VolumeExtentRole.SIMPLE_BACKING );
        this.assertPhysicalType( backingExtent, VolumePhysicalType.BLOCK );
        this.checkPhysicalExtentAvailable( backingExtent );
        BlockSimpleVolume volume = new TitanBlockSimpleVolume( guid, name, backingExtent );
        this.persistVolume( volume );
        this.registerVolume( volume );
        this.emitVolumeEvent( volume, "VOLUME_CREATED", "SUCCESS", this.volumePayload( volume ) );
        return volume;
    }

    @Override
    public ObjectSimpleVolume createObjectSimpleVolume( GUID guid, String name, VolumeExtent backingExtent ) {
        backingExtent.setParentGuid( guid );
        backingExtent.setRole( VolumeExtentRole.SIMPLE_BACKING );
        this.assertPhysicalType( backingExtent, VolumePhysicalType.OBJECT );
        this.checkPhysicalExtentAvailable( backingExtent );
        ObjectSimpleVolume volume = new TitanObjectSimpleVolume( guid, name, backingExtent, this.mConfig );
        ( (ArchVolume) volume ).setObjectRoot(
                Paths.get( ( (LocalObjectDirectoryPhysicalAccessor) backingExtent.getPhysicalAccessor() ).getRootPath().toString() ).toString()
        );
        this.persistVolume( volume );
        this.registerVolume( volume );
        this.emitVolumeEvent( volume, "VOLUME_CREATED", "SUCCESS", this.volumePayload( volume ) );
        return volume;
    }

    @Override
    public BlockSpannedVolume createBlockSpannedVolume( GUID guid, String name, Iterable<VolumeExtent> extents ) {
        BlockSpannedVolume volume = new TitanBlockSpannedVolume( guid, name );
        for ( VolumeExtent extent : extents ) {
            extent.setParentGuid( guid );
            extent.setRole( VolumeExtentRole.SPANNED_EXTENT );
            this.assertBlockChild( extent );
            volume.addExtent( extent );
        }
        this.persistVolume( volume );
        this.registerVolume( volume );
        this.emitVolumeEvent( volume, "VOLUME_CREATED", "SUCCESS", this.volumePayload( volume ) );
        return volume;
    }

    @Override
    public ObjectSpannedVolume createObjectSpannedVolume( GUID guid, String name, Iterable<VolumeExtent> extents ) {
        ObjectSpannedVolume volume = new TitanObjectSpannedVolume( guid, name );
        for ( VolumeExtent extent : extents ) {
            extent.setParentGuid( guid );
            extent.setRole( VolumeExtentRole.SPANNED_EXTENT );
            this.assertObjectSimpleChild( extent );
            volume.addExtent( extent );
        }
        this.persistVolume( volume );
        this.registerVolume( volume );
        this.emitVolumeEvent( volume, "VOLUME_CREATED", "SUCCESS", this.volumePayload( volume ) );
        return volume;
    }

    @Override
    public StripedVolume createStripedVolume( GUID guid, String name, long stripeUnit, Iterable<VolumeExtent> members ) {
        StripedVolume volume = new TitanStripedVolume( guid, name, stripeUnit );
        for ( VolumeExtent extent : members ) {
            extent.setParentGuid( guid );
            extent.setRole( VolumeExtentRole.STRIPED_MEMBER );
            this.assertBlockChild( extent );
            volume.addMember( extent );
        }
        this.persistVolume( volume );
        this.registerVolume( volume );
        this.emitVolumeEvent( volume, "VOLUME_CREATED", "SUCCESS", this.volumePayload( volume ) );
        return volume;
    }

    @Override
    public int read( GUID volumeGuid, long position, ByteBuffer dst ) throws IOException {
        return this.loadBlockVolume( volumeGuid ).read( position, dst );
    }

    @Override
    public int write( GUID volumeGuid, long position, ByteBuffer src ) throws IOException {
        BlockVolume volume = this.loadBlockVolume( volumeGuid );
        int written = volume.write( position, src );
        this.syncCommittedBytes( volume );
        return written;
    }

    @Override
    public BlockVolume affirmBlockVolume( GUID guid ) {
        Volume volume = this.affirmVolume( guid );
        if ( !( volume instanceof BlockVolume ) ) {
            throw new IllegalArgumentException( "Volume is not block-addressable: " + guid );
        }
        return (BlockVolume) volume;
    }

    @Override
    public void refreshVolumeUsage( GUID volumeGuid ) throws IOException {
        Volume volume = this.loadVolume( volumeGuid );
        long committedBytes = this.syncCommittedBytes( volume );
        this.emitVolumeEvent(
                volume,
                "VOLUME_USAGE_REFRESHED",
                "SUCCESS",
                "{\"committedBytes\":" + committedBytes + ",\"logicalSize\":" + volume.getLogicalSize() + "}"
        );
    }

    @Override
    public void flush( GUID volumeGuid ) throws IOException {
        this.loadVolume( volumeGuid ).flush();
    }

    protected BlockVolume loadBlockVolume( GUID guid ) throws IOException {
        Volume volume = this.loadVolume( guid );
        if ( !( volume instanceof BlockVolume ) ) {
            throw new IllegalArgumentException( "Volume is not block-addressable: " + guid );
        }
        return (BlockVolume) volume;
    }

    @Override
    public Volume loadVolume( GUID guid ) throws IOException {
        Volume cachedVolume = this.mVolumes.get( guid );
        if ( cachedVolume != null ) {
            return cachedVolume;
        }
        if ( this.mVolumeManipulator == null || this.mExtentManipulator == null ) {
            throw new IllegalStateException( "Volume persistence manipulators are not configured" );
        }
        VolumeRecord record = this.mVolumeManipulator.get( guid );
        if ( record == null ) {
            throw new IllegalArgumentException( "Volume record not found: " + guid );
        }
        VolumeType volumeType = VolumeType.valueOf( record.getVolumeType() );
        List<VolumeExtent> extents = this.mExtentManipulator.listByParentGuid( guid );
        switch ( volumeType ) {
            case SIMPLE: {
                if ( extents.isEmpty() ) {
                    throw new IllegalStateException( "Simple volume has no backing extent: " + guid );
                }
                VolumeExtent extent = extents.get( 0 );
                extent.setPhysicalAccessor( this.loadPhysicalAccessor( extent.getPhysicalGuid(), record ) );
                SimpleVolume volume;
                if ( VolumeMappingMode.VOLUME_DIRECT_OBJECT.name().equals( record.getMappingMode() ) ) {
                    this.assertPhysicalType( extent, VolumePhysicalType.OBJECT );
                    volume = new TitanObjectSimpleVolume( record.getGuid(), record.getName(), extent, this.mConfig );
                }
                else {
                    this.assertPhysicalType( extent, VolumePhysicalType.BLOCK );
                    volume = new TitanBlockSimpleVolume( record.getGuid(), record.getName(), extent );
                }
                this.applyRecord( volume, record );
                this.registerVolume( volume );
                return volume;
            }
            case SPANNED: {
                SpannedVolume volume = VolumeMappingMode.VOLUME_DIRECT_OBJECT.name().equals( record.getMappingMode() )
                        ? new TitanObjectSpannedVolume( record.getGuid(), record.getName() )
                        : new TitanBlockSpannedVolume( record.getGuid(), record.getName() );
                this.applyRecord( volume, record );
                this.registerVolume( volume );
                for ( VolumeExtent extent : extents ) {
                    extent.setChildVolume( this.loadVolume( extent.getChildGuid() ) );
                    volume.addExtent( extent );
                }
                return volume;
            }
            case STRIPED: {
                long stripeUnit = record.getStripeUnit() == null ? 0L : record.getStripeUnit();
                StripedVolume volume = new TitanStripedVolume( record.getGuid(), record.getName(), stripeUnit );
                this.applyRecord( volume, record );
                this.registerVolume( volume );
                for ( VolumeExtent extent : extents ) {
                    extent.setChildVolume( this.loadVolume( extent.getChildGuid() ) );
                    volume.addMember( extent );
                }
                return volume;
            }
            default: {
                throw new IllegalArgumentException( "Unsupported volume type: " + volumeType );
            }
        }
    }

    @Override
    public List<Volume> listVolumes() throws IOException {
        if ( this.mVolumeManipulator == null ) {
            return new ArrayList<>( this.mVolumes.values() );
        }
        List<Volume> volumes = new ArrayList<>();
        for ( VolumeRecord record : this.mVolumeManipulator.listAll() ) {
            volumes.add( this.loadVolume( record.getGuid() ) );
        }
        return volumes;
    }

    @Override
    public long countVolumes(
            String name,
            VolumeType volumeType,
            VolumeMappingMode mappingMode,
            VolumeStatus status
    ) {
        this.requireVolumeManipulator();
        return this.mVolumeManipulator.count( name, volumeType, mappingMode, status );
    }

    @Override
    public List<VolumeRecord> listVolumeRecordPage(
            String name,
            VolumeType volumeType,
            VolumeMappingMode mappingMode,
            VolumeStatus status,
            int offset,
            int limit
    ) {
        this.requireVolumeManipulator();
        return this.mVolumeManipulator.listPage( name, volumeType, mappingMode, status, offset, limit );
    }

    @Override
    public VolumeRecord affirmVolumeRecord( GUID guid ) {
        this.requireVolumeManipulator();
        VolumeRecord record = this.mVolumeManipulator.get( guid );
        if ( record == null ) {
            throw new IllegalArgumentException( "Volume record not found: " + guid );
        }
        return record;
    }

    @Override
    public List<VolumeExtent> getExtentsByParentGuid( GUID parentGuid ) {
        this.requireExtentManipulator();
        return this.mExtentManipulator.listByParentGuid( parentGuid );
    }

    @Override
    public void retireVolume( GUID guid ) {
        this.requireVolumeManipulator();
        this.requireExtentManipulator();
        VolumeRecord record = this.affirmVolumeRecord( guid );
        record.setStatus( VolumeStatus.DELETED.name() );
        this.mVolumeManipulator.update( record );
        this.mExtentManipulator.removeByParentGuid( guid );
        Volume cachedVolume = this.mVolumes.get( guid );
        this.emitVolumeEvent( cachedVolume, "VOLUME_DELETED", "SUCCESS", "{\"guid\":\"" + guid + "\"}" );
        this.mVolumes.remove( guid );
    }

    @Override
    public long countPhysicals(
            String name,
            VolumePhysicalType physicalType,
            VolumePhysicalStatus status,
            GUID deviceGuid
    ) {
        this.requirePhysicalManipulator();
        return this.mPhysicalManipulator.count( name, physicalType, status, deviceGuid );
    }

    @Override
    public List<VolumePhysical> listPhysicalPage(
            String name,
            VolumePhysicalType physicalType,
            VolumePhysicalStatus status,
            GUID deviceGuid,
            int offset,
            int limit
    ) {
        this.requirePhysicalManipulator();
        return this.mPhysicalManipulator.listPage( name, physicalType, status, deviceGuid, offset, limit );
    }

    @Override
    public VolumePhysical affirmPhysicalRecord( GUID guid ) {
        this.requirePhysicalManipulator();
        VolumePhysical physical = this.mPhysicalManipulator.get( guid );
        if ( physical == null ) {
            throw new IllegalArgumentException( "Physical record not found: " + guid );
        }
        return physical;
    }

    @Override
    public long countPhysicalReferences( GUID physicalGuid ) {
        this.requireExtentManipulator();
        return this.mExtentManipulator.countByPhysicalGuid( physicalGuid );
    }

    @Override
    public long countVolumeChildReferences( GUID volumeGuid ) {
        this.requireExtentManipulator();
        return this.mExtentManipulator.countByChildGuid( volumeGuid );
    }

    @Override
    public List<StorageSupportDescriptor> listStorageSupportTypes() {
        return this.mStorageSupportTypeProvider.listBuiltinSupportTypes();
    }

    @Override
    public List<VolumePhysicalSupportTrait> listPhysicalSupportTraits() {
        if ( this.mPhysicalSupportTraitManipulator == null ) {
            return List.of();
        }
        return this.mPhysicalSupportTraitManipulator.listAll();
    }

    @Override
    public VolumePhysicalSupportTrait getPhysicalSupportTraitByPhysicalGuid( GUID physicalGuid ) {
        if ( this.mPhysicalSupportTraitManipulator == null || physicalGuid == null ) {
            return null;
        }
        return this.mPhysicalSupportTraitManipulator.getByPhysicalGuid( physicalGuid );
    }

    @Override
    public void persistPhysicalSupportTrait( VolumePhysicalSupportTrait trait ) {
        if ( this.mPhysicalSupportTraitManipulator == null ) {
            throw new IllegalStateException( "Volume physical support trait manipulator is not ready." );
        }
        if ( trait.getGuid() == null || this.mPhysicalSupportTraitManipulator.get( trait.getGuid() ) == null ) {
            this.mPhysicalSupportTraitManipulator.insert( trait );
            return;
        }
        this.mPhysicalSupportTraitManipulator.update( trait );
    }

    @Override
    public void persistVolume( Volume volume ) {
        if ( this.mVolumeManipulator == null ) {
            return;
        }
        VolumeRecord record = VolumeRecord.fromVolume( volume );
        if ( this.mVolumeManipulator.get( volume.getGuid() ) == null ) {
            this.mVolumeManipulator.insert( record );
        }
        else {
            this.mVolumeManipulator.update( record );
        }
        this.persistExtents( volume );
    }

    protected void applyRecord( Volume volume, VolumeRecord record ) {
        if ( !( volume instanceof ArchVolume ) ) {
            return;
        }
        ArchVolume archVolume = (ArchVolume) volume;
        if ( record.getMappingMode() != null ) {
            archVolume.setMappingMode( VolumeMappingMode.valueOf( record.getMappingMode() ) );
        }
        if ( record.getAllocationMode() != null ) {
            archVolume.setAllocationMode( VolumeAllocationMode.valueOf( record.getAllocationMode() ) );
        }
        archVolume.setObjectRoot( record.getObjectRoot() );
        archVolume.setCommittedBytes( record.getCommittedBytes() );
        archVolume.setAllocationUnit( record.getAllocationUnit() );
    }

    protected long syncCommittedBytes( Volume volume ) throws IOException {
        if ( !( volume instanceof ArchVolume ) ) {
            return volume.getCommittedBytes();
        }
        if ( volume instanceof SimpleVolume ) {
            return this.syncSimpleCommittedBytes( (SimpleVolume) volume );
        }
        if ( volume instanceof SpannedVolume ) {
            return this.syncCompositeCommittedBytes( volume, ( (SpannedVolume) volume ).getExtents() );
        }
        if ( volume instanceof StripedVolume ) {
            return this.syncCompositeCommittedBytes( volume, ( (StripedVolume) volume ).getMembers() );
        }
        return volume.getCommittedBytes();
    }

    protected long syncSimpleCommittedBytes( SimpleVolume volume ) throws IOException {
        if ( !( volume instanceof ArchVolume ) ) {
            return volume.getCommittedBytes();
        }
        ArchVolume archVolume = (ArchVolume) volume;
        VolumeExtent backingExtent = volume.getBackingExtent();
        PhysicalAccessor physicalAccessor = backingExtent == null ? null : backingExtent.getPhysicalAccessor();
        if ( physicalAccessor == null ) {
            return archVolume.getCommittedBytes();
        }
        long committedBytes = physicalAccessor.getCommittedBytes();
        archVolume.setCommittedBytes( committedBytes );
        VolumeAllocationMode beforeMode = archVolume.getAllocationMode();
        if (
                archVolume.getAllocationMode() == VolumeAllocationMode.THIN
                        && committedBytes >= archVolume.getLogicalSize()
        ) {
            archVolume.setAllocationMode( VolumeAllocationMode.THICK );
            this.emitThinPromotionEvent( archVolume, beforeMode, committedBytes );
        }
        this.persistPhysicalCommittedBytes( backingExtent.getPhysicalGuid(), committedBytes );
        this.persistVolumeRecord( archVolume );
        return committedBytes;
    }

    protected long syncCompositeCommittedBytes( Volume volume, Iterable<VolumeExtent> extents ) throws IOException {
        if ( !( volume instanceof ArchVolume ) ) {
            return volume.getCommittedBytes();
        }
        long committedBytes = 0L;
        for ( VolumeExtent extent : extents ) {
            Volume childVolume = extent.getChildVolume();
            if ( childVolume == null && extent.getChildGuid() != null ) {
                childVolume = this.loadVolume( extent.getChildGuid() );
                extent.setChildVolume( childVolume );
            }
            if ( childVolume != null ) {
                committedBytes += this.syncCommittedBytes( childVolume );
            }
        }
        ArchVolume archVolume = (ArchVolume) volume;
        archVolume.setCommittedBytes( committedBytes );
        VolumeAllocationMode beforeMode = archVolume.getAllocationMode();
        if (
                archVolume.getAllocationMode() == VolumeAllocationMode.THIN
                        && committedBytes >= archVolume.getLogicalSize()
        ) {
            archVolume.setAllocationMode( VolumeAllocationMode.THICK );
            this.emitThinPromotionEvent( archVolume, beforeMode, committedBytes );
        }
        this.persistVolumeRecord( archVolume );
        return committedBytes;
    }

    protected void persistVolumeRecord( Volume volume ) {
        if ( this.mVolumeManipulator == null ) {
            return;
        }
        VolumeRecord record = VolumeRecord.fromVolume( volume );
        if ( this.mVolumeManipulator.get( volume.getGuid() ) == null ) {
            this.mVolumeManipulator.insert( record );
        }
        else {
            this.mVolumeManipulator.update( record );
        }
    }

    protected void persistPhysicalCommittedBytes( GUID physicalGuid, long committedBytes ) {
        if ( this.mPhysicalManipulator == null || physicalGuid == null ) {
            return;
        }
        VolumePhysical physical = this.mPhysicalManipulator.get( physicalGuid );
        if ( physical == null ) {
            return;
        }
        physical.setCommittedBytes( committedBytes );
        this.mPhysicalManipulator.update( physical );
    }

    protected void persistExtents( Volume volume ) {
        if ( this.mExtentManipulator == null ) {
            return;
        }
        this.mExtentManipulator.removeByParentGuid( volume.getGuid() );
        if ( volume instanceof SimpleVolume ) {
            this.mExtentManipulator.insert( ( (SimpleVolume) volume ).getBackingExtent() );
        }
        else if ( volume instanceof SpannedVolume ) {
            for ( VolumeExtent extent : ( (SpannedVolume) volume ).getExtents() ) {
                this.mExtentManipulator.insert( extent );
            }
        }
        else if ( volume instanceof StripedVolume ) {
            for ( VolumeExtent extent : ( (StripedVolume) volume ).getMembers() ) {
                this.mExtentManipulator.insert( extent );
            }
        }
    }

    protected void emitThinPromotionEvent( Volume volume, VolumeAllocationMode beforeMode, long committedBytes ) {
        if ( beforeMode != VolumeAllocationMode.THIN || volume.getAllocationMode() != VolumeAllocationMode.THICK ) {
            return;
        }
        this.emitVolumeEvent(
                volume,
                "VOLUME_ALLOCATION_PROMOTED",
                "SUCCESS",
                "{"
                        + "\"fromMode\":\"THIN\","
                        + "\"toMode\":\"THICK\","
                        + "\"committedBytes\":" + committedBytes + ","
                        + "\"logicalSize\":" + volume.getLogicalSize() + ","
                        + "\"reason\":\"COMMITTED_REACHED_LOGICAL_SIZE\""
                        + "}"
        );
    }

    protected void emitVolumeEvent( Volume volume, String eventType, String eventStatus, String payload ) {
        if ( this.mEventManipulator == null || volume == null ) {
            return;
        }
        VolumeEvent event = new VolumeEvent();
        event.setGuid( GUIDs.GUID128( UUID.randomUUID().toString() ) );
        event.setVolumeGuid( volume.getGuid() );
        event.setEventType( eventType );
        event.setEventStatus( eventStatus );
        event.setEventPayload( payload );
        this.mEventManipulator.insert( event );
    }

    protected String volumePayload( Volume volume ) {
        return "{"
                + "\"volumeType\":\"" + volume.getVolumeType() + "\","
                + "\"mappingMode\":\"" + volume.getMappingMode() + "\","
                + "\"allocationMode\":\"" + volume.getAllocationMode() + "\","
                + "\"logicalSize\":" + volume.getLogicalSize() + ","
                + "\"committedBytes\":" + volume.getCommittedBytes()
                + "}";
    }

    protected void assertPhysicalType( VolumeExtent extent, VolumePhysicalType expectedType ) {
        if ( extent == null || extent.getPhysicalAccessor() == null ) {
            throw new IllegalArgumentException( "Volume extent requires physical backing" );
        }
        VolumePhysicalType actualType = extent.getPhysicalAccessor().getPhysicalType();
        if ( actualType != expectedType ) {
            throw new IllegalArgumentException(
                    "Volume extent physical type mismatch, expected " + expectedType + " but got " + actualType
            );
        }
    }

    protected void assertBlockChild( VolumeExtent extent ) {
        if ( extent == null || !( extent.getChildVolume() instanceof BlockVolume ) ) {
            throw new IllegalArgumentException( "Volume extent requires block child volume" );
        }
    }

    protected void assertObjectSimpleChild( VolumeExtent extent ) {
        if ( extent == null || !( extent.getChildVolume() instanceof ObjectSimpleVolume ) ) {
            throw new IllegalArgumentException( "Object spanned volume only accepts object simple child volumes" );
        }
    }

    protected PhysicalAccessor loadPhysicalAccessor( GUID physicalGuid, VolumeRecord volumeRecord ) throws IOException {
        PhysicalAccessor cachedAccessor = this.mPhysicalAccessors.get( physicalGuid );
        if ( cachedAccessor != null ) {
            return cachedAccessor;
        }
        if ( this.mPhysicalManipulator == null ) {
            throw new IllegalStateException( "Volume physical manipulator is not configured" );
        }
        VolumePhysical physical = this.mPhysicalManipulator.get( physicalGuid );
        if ( physical == null ) {
            throw new IllegalArgumentException( "Physical record not found: " + physicalGuid );
        }
        PhysicalAccessor accessor;
        VolumeAllocationMode allocationMode = volumeRecord != null && volumeRecord.getAllocationMode() != null
                ? VolumeAllocationMode.valueOf( volumeRecord.getAllocationMode() )
                : physical.getAllocationMode();
        if ( allocationMode == null ) {
            allocationMode = VolumeAllocationMode.THIN;
        }
        long allocationUnit = volumeRecord != null && volumeRecord.getAllocationUnit() > 0L
                ? volumeRecord.getAllocationUnit()
                : physical.getAllocationUnit();
        if ( allocationUnit <= 0L ) {
            allocationUnit = this.mConfig.getDefaultAllocationUnit();
        }
        this.validatePhysicalSupport( physical );
        if ( physical.getPhysicalType() == VolumePhysicalType.BLOCK ) {
            this.assertBuiltinSupportType( physical, BuiltinStorageSupportType.TITAN_BLOCK );
            accessor = new LocalFilePhysicalAccessor(
                    physical.getGuid(),
                    physical.getName(),
                    Paths.get( this.requireRootPath( physical ) ),
                    physical.getCapacityBytes(),
                    allocationMode,
                    allocationUnit
            );
        }
        else if ( physical.getPhysicalType() == VolumePhysicalType.OBJECT ) {
            this.assertObjectAccessorSupport( physical );
            accessor = new LocalObjectDirectoryPhysicalAccessor(
                    physical.getGuid(),
                    physical.getName(),
                    Paths.get( this.requireRootPath( physical ) ),
                    physical.getCapacityBytes(),
                    this.mConfig
            );
        }
        else {
            throw new UnsupportedStorageAccessorException(
                    "Physical type is registration-only and has no runtime accessor: " + physical.getPhysicalType()
            );
        }
        this.registerPhysical( accessor );
        return accessor;
    }

    protected void validatePhysicalSupport( VolumePhysical physical ) {
        if ( physical == null ) {
            throw new IllegalArgumentException( "Physical record is required." );
        }
        if ( physical.getPhysicalType() == null ) {
            throw new IllegalArgumentException( "Physical type is required: " + physical.getGuid() );
        }
        if ( this.isBlank( physical.getSupportType() ) ) {
            throw new IllegalArgumentException( "Physical supportType is required: " + physical.getGuid() );
        }
        Optional<BuiltinStorageSupportType> builtin = BuiltinStorageSupportType.find( physical.getSupportType() );
        if ( builtin.isPresent() ) {
            BuiltinStorageSupportType supportType = builtin.get();
            if ( supportType.getPhysicalType() != physical.getPhysicalType() ) {
                throw new IllegalArgumentException(
                        "Physical supportType " + supportType.name() + " requires physicalType "
                                + supportType.getPhysicalType() + " but got " + physical.getPhysicalType()
                );
            }
            if ( supportType.getObjectMappedType() != physical.getObjectMappedType() ) {
                throw new IllegalArgumentException(
                        "Physical supportType " + supportType.name() + " requires objectMappedType "
                                + supportType.getObjectMappedType() + " but got " + physical.getObjectMappedType()
                );
            }
            return;
        }
        VolumePhysicalSupportTrait trait = this.findPhysicalSupportTrait( physical );
        if ( trait == null ) {
            throw new IllegalArgumentException(
                    "Custom physical support type requires hydra_volume_physical_support_trait: " + physical.getSupportType()
            );
        }
        if ( !physical.getSupportType().equals( trait.getCode() ) ) {
            throw new IllegalArgumentException(
                    "Physical supportType must match trait code, supportType=" + physical.getSupportType()
                            + ", traitCode=" + trait.getCode()
            );
        }
        if ( physical.getPhysicalType() == VolumePhysicalType.OBJECT && physical.getObjectMappedType() == null ) {
            throw new IllegalArgumentException( "Object mapped type is required for OBJECT physical: " + physical.getGuid() );
        }
    }

    protected void assertBuiltinSupportType( VolumePhysical physical, BuiltinStorageSupportType expected ) {
        BuiltinStorageSupportType actual = BuiltinStorageSupportType.find( physical.getSupportType() )
                .orElseThrow( () -> new UnsupportedStorageAccessorException(
                        "Runtime accessor requires builtin supportType " + expected.name()
                                + " but got custom supportType " + physical.getSupportType()
                ) );
        if ( actual != expected ) {
            throw new UnsupportedStorageAccessorException(
                    "Runtime accessor requires supportType " + expected.name() + " but got " + actual.name()
            );
        }
    }

    protected void assertObjectAccessorSupport( VolumePhysical physical ) {
        BuiltinStorageSupportType supportType = BuiltinStorageSupportType.find( physical.getSupportType() )
                .orElseThrow( () -> new UnsupportedStorageAccessorException(
                        "Custom OBJECT supportType has no runtime accessor yet: " + physical.getSupportType()
                ) );
        if ( supportType != BuiltinStorageSupportType.TITAN_OBJECT ) {
            throw new UnsupportedStorageAccessorException(
                    "OBJECT supportType has no runtime accessor yet: " + supportType.name()
            );
        }
        if ( physical.getObjectMappedType() != ObjectMappedType.OBJECT_ADDRESSABLE ) {
            throw new IllegalArgumentException(
                    "Titan object physical requires OBJECT_ADDRESSABLE but got " + physical.getObjectMappedType()
            );
        }
    }

    protected VolumePhysicalSupportTrait findPhysicalSupportTrait( VolumePhysical physical ) {
        if ( this.mPhysicalSupportTraitManipulator == null || physical == null || physical.getGuid() == null ) {
            return null;
        }
        return this.mPhysicalSupportTraitManipulator.getByPhysicalGuid( physical.getGuid() );
    }

    protected boolean isBlank( String value ) {
        return value == null || value.trim().isEmpty();
    }

    protected String requireRootPath( VolumePhysical physical ) {
        if ( physical.getRootPath() == null || physical.getRootPath().isBlank() ) {
            throw new IllegalArgumentException(
                    "Physical rootPath is required for runtime accessor: " + physical.getGuid()
            );
        }
        return physical.getRootPath();
    }

    protected void requirePhysicalManipulator() {
        if ( this.mPhysicalManipulator == null ) {
            throw new IllegalStateException( "Volume physical manipulator is not configured" );
        }
    }

    protected void requireVolumeManipulator() {
        if ( this.mVolumeManipulator == null ) {
            throw new IllegalStateException( "Volume manipulator is not configured" );
        }
    }

    protected void requireExtentManipulator() {
        if ( this.mExtentManipulator == null ) {
            throw new IllegalStateException( "Volume extent manipulator is not configured" );
        }
    }

    protected void checkPhysicalExtentAvailable( VolumeExtent targetExtent ) {
        if ( this.mExtentManipulator == null || targetExtent.getPhysicalGuid() == null ) {
            return;
        }
        List<VolumeExtent> extents = this.mExtentManipulator.listByPhysicalGuid( targetExtent.getPhysicalGuid() );
        extents.sort( Comparator.comparingLong( VolumeExtent::getPhysicalOffset ) );
        long targetStart = targetExtent.getPhysicalOffset();
        long targetEnd = targetStart + targetExtent.getLength();
        for ( VolumeExtent extent : extents ) {
            long start = extent.getPhysicalOffset();
            long end = start + extent.getLength();
            if ( targetStart < end && targetEnd > start ) {
                throw new IllegalStateException(
                        "Physical extent overlaps: " + targetExtent.getPhysicalGuid() + " [" + targetStart + ", " + targetEnd + ")"
                );
            }
        }
    }
}


