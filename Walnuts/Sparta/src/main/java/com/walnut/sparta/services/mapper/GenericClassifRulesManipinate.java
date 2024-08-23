package com.walnut.sparta.services.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericClassificationRules;
import com.pinecone.hydra.service.tree.source.ClassifRulesManipulator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface GenericClassifRulesManipinate extends ClassifRulesManipulator {
    @Insert("INSERT INTO `hydra_classif_rules` (`guid`, `scope`, `name`, `description`) VALUES (#{UUID},#{scope},#{name},#{description})")
    void saveClassifRules(GenericClassificationRules classificationRules);
    @Delete("DELETE FROM `hydra_classif_rules` WHERE `guid`=#{UUID}")
    void deleteClassifRules(@Param("UUID")GUID UUID);
    @Select("SELECT `id`, `guid` AS UUID, `scope`, `name`, `description` FROM `hydra_classif_rules` WHERE `guid`=#{UUID}")
    GenericClassificationRules selectClassifRules(@Param("UUID")GUID UUID);
    void updateClassifRules(GenericClassificationRules classificationRules);
}
