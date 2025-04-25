package com.walnut.odin.mapper;

import com.pinecone.framework.system.construction.Structure;

import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.walnut.odin.category.source.KernelCategoryManipulator;
import com.walnut.odin.category.source.CategoryMasterManipulator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
@Component
public class CategoryMasterManipulatorImpl implements CategoryMasterManipulator {
    @Resource
    @Structure( type = KernelCategoryMasterTreeManipulatorImpl.class )
    protected KOISkeletonMasterManipulator skeletonMasterManipulator;

   @Resource
    @Structure( type = KernelCategoryMapper.class )
    protected KernelCategoryManipulator kernelCategoryManipulator;

   /*  @Resource
    @Structure( type = TaskCategoryMapper.class )
    protected TaskCategoryManipulator taskCategoryManipulator;


    @Resource
    @Structure( type = CategoryTagMapper.class )
    protected CategoryTagManipulator categoryTagManipulator;*/

    public CategoryMasterManipulatorImpl() {
    }

    public CategoryMasterManipulatorImpl( KOIMappingDriver driver ) {
        driver.autoConstruct( CategoryMasterManipulatorImpl.class, Map.of(), this );
        this.skeletonMasterManipulator = new KernelCategoryMasterTreeManipulatorImpl( driver );
    }

    @Override
    public KOISkeletonMasterManipulator getSkeletonMasterManipulator() {
        return this.skeletonMasterManipulator;
    }

    @Override
    public KernelCategoryManipulator getKernelCategoryManipulator() {
        return this.kernelCategoryManipulator;
    }


/*    @Override
    public KernelCategoryManipulator getKernelCategoryManipulator() {
        return this.kernelCategoryManipulator;
    }*/

/*    @Override
    public TaskCategoryManipulator getTaskCategoryManipulator() {
        return this.taskCategoryManipulator;
    }

    @Override
    public CategoryTagManipulator getCategoryTagManipulator() {
        return this.categoryTagManipulator;
    }*/
}
