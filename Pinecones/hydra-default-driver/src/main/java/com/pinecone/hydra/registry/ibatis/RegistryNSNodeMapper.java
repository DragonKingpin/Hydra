package com.pinecone.hydra.registry.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.GenericNamespaceNode;
import com.pinecone.hydra.registry.entity.NamespaceNode;
import com.pinecone.hydra.registry.source.RegistryNSNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface RegistryNSNodeMapper extends RegistryNSNodeManipulator {
    @Override
    @Insert("INSERT INTO `hydra_registry_namespace` (`guid`, `create_time`, `name`, `update_time`) VALUES (#{guid},#{createTime},#{name},#{updateTime})")
    void insert(NamespaceNode namespace);

    @Override
    @Delete("DELETE FROM `hydra_registry_namespace` WHERE `guid`=#{guid}")
    void remove( GUID guid );

    @Override
    @Select( "SELECT COUNT(`id` AS `enumId`) FROM `hydra_registry_namespace` WHERE guid = #{guid}" )
    boolean isNamespaceNode( GUID guid );

    @Override
    @Select("SELECT `id` AS `enumId`, `guid`, `create_time` AS createTime, `name`, `update_time` AS updateTime FROM `hydra_registry_namespace` WHERE guid=#{guid}")
    GenericNamespaceNode getNamespaceMeta(GUID guid);

    @Override
    @Update("UPDATE `hydra_registry_namespace` SET `create_time`=#{createTime},`name`=#{name},`update_time`=#{updateTime} WHERE `guid`=#{guid}")
    void update(NamespaceNode namespace);

    @Override
    @Select("SELECT `guid` FROM `hydra_registry_namespace` WHERE `name`=#{name}")
    List<GUID > getGuidsByName(String name);

    @Override
    @Select( "SELECT `guid` FROM `hydra_registry_namespace` WHERE `name` = #{name} AND `guid` = #{guid}" )
    List<GUID > getGuidsByNameID( @Param( "name" ) String name, @Param( "guid" ) GUID guid );

    @Override
    @Select("SELECT `guid` FROM `hydra_registry_namespace`")
    List<GUID > dumpGuid();

    @Override
    @Update( "UPDATE `hydra_registry_namespace` SET `name` = #{name} WHERE `guid` = #{guid}" )
    void updateName( @Param("guid") GUID guid ,@Param("name") String name );
}
