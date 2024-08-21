package com.walnut.sparta.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.GenericClassificationRules;
import com.pinecone.hydra.unit.udsn.ClassifRulesManipinate;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface GenericClassifRulesManipinate extends ClassifRulesManipinate {
    @Insert("INSERT INTO `hydra_classif_rules` (uuid, scope, name, description) VALUES (#{UUID},#{scope},#{name},#{description})")
    void saveClassifRules(GenericClassificationRules classificationRules);
    @Delete("DELETE FROM `hydra_classif_rules` WHERE `uuid`=#{UUID}")
    void deleteClassifRules(@Param("UUID")GUID UUID);
    @Select("SELECT `id`, `uuid`, `scope`, `name`, `description` FROM `hydra_classif_rules` WHERE `uuid`=#{UUID}")
    GenericClassificationRules selectClassifRules(@Param("UUID")GUID UUID);
    void updateClassifRules(GenericClassificationRules classificationRules);
}
