package com.pinecone.hydra.registry.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.registry.entity.ConfigNode;
import com.pinecone.hydra.registry.entity.GenericProperties;
import com.pinecone.hydra.registry.entity.GenericTextConfigNode;
import com.pinecone.hydra.registry.source.RegistryConfigNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface RegistryConfigNodeMapper extends RegistryConfigNodeManipulator {
    @Insert("INSERT INTO `hydra_registry_config_node` (`guid`, `data_affinity_guid`, `create_time`, `update_time`,`name`) VALUES (#{guid},#{dataAffinityGuid},#{createTime},#{updateTime},#{name})")
    void insert( ConfigNode configNode );

    @Delete("DELETE FROM `hydra_registry_config_node` WHERE `guid`=#{guid}")
    void remove( @Param("guid") GUID guid );

    @Select("SELECT `type` FROM `hydra_registry_nodes` WHERE `guid`=#{guid}")
    UOI getUOIByGUID( GUID guid );

    @Select("SELECT `id` AS `enumId`, `guid`, `data_affinity_guid` AS dataAffinityGuid, `create_time` AS createTime, `update_time` updateTime, `name` FROM `hydra_registry_config_node` WHERE `guid` = #{guid}")
    GenericProperties getPropertiesNode( GUID guid );

    @Select("SELECT `id` AS `enumId`, `guid`, `data_affinity_guid` AS dataAffinityGuid, `create_time` AS createTime, `update_time` updateTime, `name` FROM `hydra_registry_config_node` WHERE `guid`=#{guid}")
    GenericTextConfigNode getTextConfigNode( GUID guid );

    @Override
    default ConfigNode getConfigNode ( GUID guid ) {
        String objectName = this.getUOIByGUID(guid).getObjectName();
        if ( objectName.equals( GenericTextConfigNode.class.getName()) ){
            return this.getTextConfigNode(guid);
        }
        else if ( objectName.equals(GenericProperties.class.getName()) ){
            return this.getPropertiesNode(guid);
        }
        return null;
    }

    @Override
    default void update( ConfigNode configNode ) {
        if (configNode.getUpdateTime() != null){
            this.updateUpdateTime(configNode.getUpdateTime(),configNode.getGuid());
        }
        if (configNode.getName() != null){
            //updateName(configNode.getName(),configNode.getGuid());
        }
    }

    @Override
    @Select( "SELECT `guid` FROM `hydra_registry_config_node` WHERE `name` = #{name}" )
    List<GUID > getGuidsByName( String name );

    @Override
    @Select( "SELECT `guid` FROM `hydra_registry_config_node` WHERE `name` = #{name} AND `guid` = #{guid}" )
    List<GUID > getGuidsByNameID( @Param("name") String name, @Param("guid") GUID guid );

    @Update( "UPDATE `hydra_registry_config_node` SET `update_time` = #{updateTime} WHERE `guid` = #{guid}" )
    void updateUpdateTime(@Param("updateTime") LocalDateTime updateTime,@Param("guid") GUID guid);

    @Select( "SELECT `guid` FROM `hydra_registry_config_node`" )
    List<GUID > dumpGuid();

    @Update( "UPDATE `hydra_registry_config_node` SET `name` = #{name} WHERE `guid` = #{guid}" )
    void updateName( @Param("guid") GUID guid ,@Param("name") String name );

    @Select( "SELECT `data_affinity_guid` FROM `hydra_registry_config_node` WHERE `guid` = #{guid}" )
    GUID getDataAffinityGuid ( GUID guid );

    @Update( "UPDATE `hydra_registry_config_node` SET `data_affinity_guid` = #{affinityGuid} WHERE `guid` = #{guid}" )
    void setDataAffinityGuid( @Param("guid") GUID guid, @Param("affinityGuid") GUID affinityGuid );

}
