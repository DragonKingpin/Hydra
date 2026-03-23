package com.walnut.odin.task.mapper;

import com.walnut.odin.conduct.entity.InstanceExec;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InstanceExecMapper {

    void insert( InstanceExec instanceExec );

}
