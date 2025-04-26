package com.walnut.odin.task.service;

import java.util.List;

import com.pinecone.framework.system.NonNull;
import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.UniformTaskInstrument;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.dto.CategoryTag;
import com.walnut.odin.task.source.CategoryMappingManipulator;
import com.walnut.odin.task.source.CategoryTypeManipulator;
import com.walnut.odin.task.source.RavenTaskMasterManipulator;
import com.walnut.odin.task.source.TaskCategoryManipulator;
import com.walnut.odin.task.system.TaskPathInvalidException;

public class RavenCategoryService implements CategoryService {

    private RavenTaskMasterManipulator ravenTaskMasterManipulator;

    private CategoryTypeManipulator    categoryTypeManipulator;

    private TaskCategoryManipulator    taskCategoryManipulator;

    private CategoryMappingManipulator categoryMappingManipulator;

    private CentralizedTaskInstrument  centralizedTaskInstrument;

    private UniformTaskInstrument      uniformTaskInstrument;

    public RavenCategoryService( CentralizedTaskInstrument instrument ) {
        this.ravenTaskMasterManipulator = instrument.getRavenTaskMasterManipulator();
        this.categoryTypeManipulator    = this.ravenTaskMasterManipulator.getCategoryTypeManipulator();
        this.taskCategoryManipulator    = this.ravenTaskMasterManipulator.getTaskCategoryManipulator();
        this.categoryMappingManipulator = this.ravenTaskMasterManipulator.getCategoryMappingManipulator();
        this.centralizedTaskInstrument  = instrument;
        this.uniformTaskInstrument      = this.centralizedTaskInstrument.getUniformTaskInstrument();
    }




    @Override
    public void addCategoryTag ( CategoryTag categoryTag ) {
        this.categoryMappingManipulator.insert( categoryTag );
    }

    @Override
    public void addCategoryTag ( String taskTreePath, CategoryTag categoryTag ) throws TaskPathInvalidException, IllegalArgumentException {
        GUID guid = this.centralizedTaskInstrument.assertTaskGUIDByPath( taskTreePath );
        categoryTag.setTaskGuid( guid );
        this.categoryMappingManipulator.insert( categoryTag );
    }

    @Override
    public CategoryTag setCategoryTag ( String taskTreePath, CategoryTag categoryTag ) throws TaskPathInvalidException, IllegalArgumentException {
        GUID guid = this.centralizedTaskInstrument.assertTaskGUIDByPath( taskTreePath );
        CategoryTag tag = this.queryOwnedTag( guid, categoryTag.getCategoryType(), categoryTag.getCategoryName() );
        if ( tag != null ) {
            return tag;
        }

        categoryTag.setTaskGuid( guid );
        this.categoryMappingManipulator.insert( categoryTag );
        return categoryTag;
    }

    @Override
    public void updateCategoryTag ( CategoryTag categoryTag ) {
        this.categoryMappingManipulator.update( categoryTag );
    }

    @Override
    public CategoryTag queryOwnedTag( GUID taskGuid, String type, String name ) {
        return this.categoryMappingManipulator.queryOwnedTag( taskGuid, type, name );
    }

    @Override
    public List<CategoryTag> queryCategoryTag ( GUID taskGuid ) {
        return this.categoryMappingManipulator.queryByTaskGuid( taskGuid );
    }

    @Override
    public List<CategoryTag> queryCategoryTag( String taskTreePath ) {
        GUID guid = this.uniformTaskInstrument.queryGUIDByPath( taskTreePath );
        if ( guid == null ) {
            return null;
        }
        return this.queryCategoryTag( guid );
    }

    @Override
    public long countCategoryTag( String type, String name ) {
        return this.categoryMappingManipulator.countTag( type, name );
    }

    @Override
    public List<CategoryTag> queryCategoryTag ( String type, String name, long offset, long pageSize ) {
        return this.categoryMappingManipulator.queryTag( type, name, offset, pageSize );
    }

    @Override
    public long countCategoryTagsByName( String name ) {
        return this.categoryMappingManipulator.countTagsByName( name );
    }

    @Override
    public List<CategoryTag> fetchCategoryTagByName ( String name, long offset, long pageSize ) {
        return this.categoryMappingManipulator.fetchByName( name, offset, pageSize );
    }

    @Override
    public void purgeCategoryTag( @Nullable GUID taskGuid, @Nullable String type, @Nullable String name ) {
        this.categoryMappingManipulator.purge( taskGuid, type, name );
    }

    @Override
    public void purgeCategoryTag( @NonNull String name ) {
        this.categoryMappingManipulator.purgeByName( name );
    }

    @Override
    public void purgeCategoryTag( @NonNull GUID taskGuid ) {
        this.categoryMappingManipulator.purgeByTaskGuid( taskGuid );
    }

    @Override
    public void removeCategoryTag( @NonNull GUID taskGuid, @NonNull String type, @NonNull String name ) {
        this.categoryMappingManipulator.remove( taskGuid, type, name );
    }

    @Override
    public void eraseCategoryTag( @NonNull String taskTreePath, @Nullable String type, @Nullable String name ) throws TaskPathInvalidException, IllegalArgumentException {
        GUID guid = this.centralizedTaskInstrument.assertTaskGUIDByPath( taskTreePath );
        this.categoryMappingManipulator.remove( guid, type, name );
    }

}
