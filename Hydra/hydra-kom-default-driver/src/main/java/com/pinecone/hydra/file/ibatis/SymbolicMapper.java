package com.pinecone.hydra.file.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.GenericSymbolic;
import com.pinecone.hydra.storage.file.entity.Symbolic;
import com.pinecone.hydra.storage.file.source.SymbolicManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface SymbolicMapper extends SymbolicManipulator {
    @Override
    default Symbolic getSymbolic( GUID guid, ElementNode element ) {
        return this.getSymbolicByGuid( guid );
    }

    @Insert("INSERT INTO `hydra_uofs_symbolic` (`guid`, `create_time`, `update_time`, `name`, `reparsed_point`) VALUES (#{guid}, #{createTime}, #{updateTime}, #{name}, #{reparsedPoint})")
    void insert( Symbolic symbolic );

    @Delete("DELETE FROM `hydra_uofs_symbolic` WHERE `guid` = #{guid}")
    void remove( GUID guid );

    @Select("SELECT `id` AS enumId, `guid`, `create_time` AS createTime, `update_time` AS updateTime, `name`, `reparsed_point` AS reparsedPoint FROM `hydra_uofs_symbolic` WHERE `guid` = #{guid}")
    GenericSymbolic getSymbolicByGuid( GUID guid );

    @Select("SELECT `id` AS enumId, `guid`, `create_time` AS createTime, `update_time` AS updateTime, `name`, `reparsed_point` AS reparsedPoint FROM `hydra_uofs_symbolic` WHERE `guid` = #{guid} AND `name` = #{nodeName}")
    GenericSymbolic getSymbolicByNameGuid( @Param("nodeName") String nodeName, @Param("guid") GUID guid );

    @Select("SELECT COUNT(*) FROM `hydra_uofs_symbolic` WHERE `guid` = #{guid} AND `name` = #{nodeName}")
    boolean isSymbolicMatchedByNameGuid( @Param("nodeName") String nodeName, @Param("guid") GUID guid );

    @Select("SELECT `guid` FROM `hydra_uofs_symbolic` WHERE `name` = #{name}")
    List<GUID> getGuidsByName( String name );

    @Select("SELECT `guid` FROM `hydra_uofs_symbolic` WHERE `name` = #{name} AND `guid` = #{guid}")
    List<GUID> getGuidsByNameID( @Param("name") String name, @Param("guid") GUID guid );
}
