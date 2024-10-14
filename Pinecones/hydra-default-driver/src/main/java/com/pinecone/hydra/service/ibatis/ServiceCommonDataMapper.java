package com.pinecone.hydra.service.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.GenericNodeCommonData;
import com.pinecone.hydra.service.kom.source.CommonDataManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
@IbatisDataAccessObject
public interface ServiceCommonDataMapper extends CommonDataManipulator {
    @Insert("INSERT INTO `hydra_service_node_common_data` (`guid`, `scenario`, primary_impl_lang, extra_information, `level`, `description`) VALUES (#{guid},#{scenario},#{primaryImplLang},#{extraInformation},#{level},#{description})")
    void insert(GenericNodeCommonData nodeMetadata);
    @Delete("DELETE FROM `hydra_service_node_common_data` WHERE `guid`=#{guid}")
    void remove(@Param("guid")GUID guid);
    @Select("SELECT `id` AS `enumId`, `guid`, `scenario`, `primary_impl_lang` AS primaryImplLang, `extra_information` AS extraInformation, `level`, `description` FROM `hydra_service_node_common_data` WHERE `guid`=#{guid}")
    GenericNodeCommonData getNodeMetadata(@Param("guid")GUID guid);
    void update(GenericNodeCommonData nodeMetadata);
}
