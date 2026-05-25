package com.walnut.odin.task.mapper;

import com.pinecone.framework.system.construction.Structure;

import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.task.ibatis.hydranium.TaskMappingDriver;
import com.pinecone.hydra.task.kom.source.TaskMasterManipulator;
import com.walnut.odin.specific.mapper.TaskSpecificMapper;
import com.walnut.odin.project.mapper.TaskProjectMapper;
import com.walnut.odin.specific.source.TaskSpecificManipulator;
import com.walnut.odin.project.source.TaskProjectManipulator;
import com.walnut.odin.task.source.ScheduleManipulator;
import com.walnut.odin.task.source.CategoryMappingManipulator;
import com.walnut.odin.task.source.CategoryTypeManipulator;
import com.walnut.odin.task.source.RavenTaskMasterManipulator;
import com.walnut.odin.task.source.TaskCategoryManipulator;
import com.walnut.odin.task.source.TaskProcessorManipulator;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class RavenTaskMasterManipulatorImpl implements RavenTaskMasterManipulator {

    protected KOISkeletonMasterManipulator skeletonMasterManipulator;

    protected TaskMappingDriver            taskMappingDriver;

    protected TaskMasterManipulator        taskMasterManipulator;

    @Resource
    @Structure( type = CategoryTypeMapper.class )
    protected CategoryTypeManipulator categoryTypeManipulator;

    @Resource
    @Structure( type = TaskCategoryMapper.class )
    protected TaskCategoryManipulator taskCategoryManipulator;

    @Resource
    @Structure( type = CategoryMappingMapper.class )
    protected CategoryMappingManipulator categoryMappingManipulator;

    @Resource
    @Structure( type = TaskProcessorMapper.class )
    protected TaskProcessorManipulator taskProcessorManipulator;

    @Resource
    @Structure( type = TaskProjectMapper.class )
    protected TaskProjectManipulator taskProjectManipulator;

    @Resource
    @Structure( type = TaskSpecificMapper.class )
    protected TaskSpecificManipulator taskSpecificManipulator;

    protected ScheduleManipulator    scheduleManipulator;

    public RavenTaskMasterManipulatorImpl( KOIMappingDriver driver, TaskMappingDriver taskMappingDriver ) {
        driver.autoConstruct( RavenTaskMasterManipulatorImpl.class, Map.of(), this );
        this.taskMappingDriver         = taskMappingDriver;
        this.taskMasterManipulator     = (TaskMasterManipulator)taskMappingDriver.getMasterManipulator();
        this.skeletonMasterManipulator = this.taskMasterManipulator.getSkeletonMasterManipulator();

        this.scheduleManipulator       = new ScheduleManipulatorImpl( driver );
    }

    @Override
    public TaskMasterManipulator getTaskMasterManipulator() {
        return this.taskMasterManipulator;
    }

    @Override
    public TaskMappingDriver getTaskMappingDriver() {
        return this.taskMappingDriver;
    }

    @Override
    public KOISkeletonMasterManipulator getSkeletonMasterManipulator() {
        return this.skeletonMasterManipulator;
    }

    @Override
    public CategoryTypeManipulator getCategoryTypeManipulator() {
        return this.categoryTypeManipulator;
    }

    @Override
    public TaskCategoryManipulator getTaskCategoryManipulator() {
        return this.taskCategoryManipulator;
    }

    @Override
    public CategoryMappingManipulator getCategoryMappingManipulator() {
        return this.categoryMappingManipulator;
    }

    @Override
    public TaskProcessorManipulator getTaskProcessorManipulator() {
        return this.taskProcessorManipulator;
    }

    @Override
    public ScheduleManipulator getScheduleManipulator() {
        return this.scheduleManipulator;
    }

    @Override
    public TaskProjectManipulator getTaskProjectManipulator() {
        return this.taskProjectManipulator;
    }

    @Override
    public TaskSpecificManipulator getTaskSpecificManipulator() {
        return this.taskSpecificManipulator;
    }
}
