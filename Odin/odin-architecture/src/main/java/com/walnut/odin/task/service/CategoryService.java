package com.walnut.odin.task.service;

import java.util.List;

import com.pinecone.framework.system.NonNull;
import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.task.dto.CategoryTag;
import com.walnut.odin.task.system.TaskPathInvalidException;

public interface CategoryService extends Pinenut {

    void addCategoryTag ( CategoryTag categoryTag );

    void addCategoryTag ( String taskTreePath, CategoryTag categoryTag ) throws TaskPathInvalidException, IllegalArgumentException;

    CategoryTag setCategoryTag ( String taskTreePath, CategoryTag categoryTag ) throws TaskPathInvalidException, IllegalArgumentException;

    void updateCategoryTag ( CategoryTag categoryTag );

    CategoryTag queryOwnedTag( GUID taskGuid, String type, String name );

    List<CategoryTag> queryCategoryTag ( GUID taskGuid );

    List<CategoryTag> queryCategoryTag ( String taskTreePath );

    long countCategoryTag( String type, String name );

    List<CategoryTag> queryCategoryTag ( String type, String name, long offset, long pageSize );

    long countCategoryTagsByName( String name );

    List<CategoryTag> fetchCategoryTagByName ( String name, long offset, long pageSize );

    void purgeCategoryTag( @Nullable GUID taskGuid, @Nullable String type, @Nullable String name );

    void purgeCategoryTag( @NonNull String name );

    void purgeCategoryTag( @NonNull GUID taskGuid );

    void removeCategoryTag( @NonNull GUID taskGuid, @NonNull String type, @NonNull String name );

    void eraseCategoryTag( @NonNull String taskTreePath, @Nullable String type, @Nullable String name ) throws TaskPathInvalidException, IllegalArgumentException;

}
