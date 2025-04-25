package com.walnut.odin.task;

import com.pinecone.framework.system.executum.Processum;

import com.pinecone.hydra.system.identifier.KOPathResolver;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.system.ko.kom.ArchKOMTree;
import com.pinecone.hydra.system.ko.kom.KOMInstrument;
import com.pinecone.hydra.task.kom.UniformTaskInstrument;
import com.pinecone.radium.Radium;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.ulf.util.guid.GUIDs;
import com.walnut.odin.category.KernelCategoryManager;
import com.walnut.odin.category.entity.CategoryTag;
import com.walnut.odin.category.entity.KernelCategory;
import com.walnut.odin.category.entity.TaskCategory;
import com.walnut.odin.category.source.CategoryTagManipulator;
import com.walnut.odin.category.source.KernelCategoryManipulator;
import com.walnut.odin.category.source.CategoryMasterManipulator;
import com.walnut.odin.category.source.TaskCategoryManipulator;
import com.walnut.odin.mapper.KernelCategoryMappingDriver;

public class RavenTaskInstrument extends ArchKOMTree implements com.walnut.odin.category.KernelCategoryManager {
    protected KernelCategoryManipulator kernelCategoryManipulator;

    protected CategoryMasterManipulator categoryMasterManipulator;

    protected TaskCategoryManipulator taskCategoryManipulator;

    protected CategoryTagManipulator categoryTagMasterManipulator;

    protected UniformTaskInstrument uniformTaskInstrument;

    public RavenTaskInstrument(Processum superiorProcess, KOIMasterManipulator masterManipulator, KOMInstrument parent, String name) {
        super(superiorProcess, masterManipulator, KernelCategoryConfig,parent, name);
        this.categoryMasterManipulator = (CategoryMasterManipulator) masterManipulator;
        this.pathResolver          = new KOPathResolver( this.kernelObjectConfig );
        this.guidAllocator         = GUIDs.newGuidAllocator();
        this.kernelCategoryManipulator = this.categoryMasterManipulator.getKernelCategoryManipulator();
/*        this.taskCategoryManipulator = this.kernelCategoryMasterManipulator.getTaskCategoryManipulator();
        this.categoryTagMasterManipulator = this.kernelCategoryMasterManipulator.getCategoryTagManipulator();*/


        Radium sys = (Radium) this.getSuperiorProcess().getSystem();
        KOIMappingDriver koiMappingDriver = new KernelCategoryMappingDriver(
                this.getSuperiorProcess(), (IbatisClient)sys.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), sys.getDispenserCenter()
        );

        this.uniformTaskInstrument = new UniformTaskInstrument(koiMappingDriver);
    }

    public RavenTaskInstrument( Processum superiorProcess, KOIMasterManipulator masterManipulator ) {
        this( superiorProcess, masterManipulator, null, com.walnut.odin.category.KernelCategoryManager.class.getSimpleName() );
    }

    public RavenTaskInstrument( KOIMappingDriver driver, KernelCategoryManager parent, String name ){
        this( driver.getSuperiorProcess(), driver.getMasterManipulator(), parent, name );
    }

    public RavenTaskInstrument( KOIMappingDriver driver ) {
        this( driver.getSuperiorProcess(), driver.getMasterManipulator() );
    }

    @Override
    public Object queryEntityHandleByNS(String path, String szBadSep, String szTargetSep) {
        return null;
    }

    @Override
    public void insert(KernelCategory kernelCategory) {
        this.kernelCategoryManipulator.insert(kernelCategory);
    }

    @Override
    public void remove(String kernelCategoryName) {
        this.kernelCategoryManipulator.remove(kernelCategoryName);}

    @Override
    public void update(KernelCategory kernelCategory) {
        this.kernelCategoryManipulator.update(kernelCategory);}
    @Override
    public KernelCategory query(String kernelCategoryName) {
        return kernelCategoryManipulator.queryKernelCategory(kernelCategoryName);
    }

    @Override
    public void insertTaskCategory(TaskCategory taskCategory) {
        this.taskCategoryManipulator.insert(taskCategory);
    }


    @Override
    public void insertCategoryTag(CategoryTag categoryTag) {
        this.categoryTagMasterManipulator.insert(categoryTag);
    }
}
