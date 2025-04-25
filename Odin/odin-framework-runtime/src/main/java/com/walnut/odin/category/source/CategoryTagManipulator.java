package com.walnut.odin.category.source;


import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.category.entity.CategoryTag;

public interface CategoryTagManipulator extends Pinenut {

    void insert(CategoryTag categoryTag);

    CategoryTag queryCategoryTag(int categoryTagId);
}
