package com.walnut.odin.task.mapper;

import com.walnut.odin.conduct.entity.InstanceEvent;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface InstanceEventMapper {

    void insert( InstanceEvent instanceEvent );
}
