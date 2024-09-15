package com.pinecone.hydra.scenario.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.scenario.entity.GenericScenarioCommonData;
import com.pinecone.hydra.scenario.entity.ScenarioCommonData;
import com.pinecone.hydra.scenario.source.ScenarioCommonDataManipulator;
import com.pinecone.hydra.unit.udsn.source.ScopeOwnerManipulator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface ScenarioCommonDataMapper extends ScenarioCommonDataManipulator {
    @Insert("INSERT INTO `hydra_scenario_commom_data` (`guid`, `create_time`, `update_time`) VALUES (#{guid},#{createTime},#{updateTime})")
    void insert(ScenarioCommonData scenarioCommonData);

    @Delete("DELETE FROM `hydra_scenario_commom_data` WHERE `guid`=#{guid}")
    void remove(GUID guid);

    @Select("SELECT `id`, `guid`, `create_time`, `update_time` FROM `hydra_scenario_commom_data` WHERE `guid`=#{guid}")
    GenericScenarioCommonData getScenarioCommonData(GUID guid);

    void update(ScenarioCommonData scenarioCommonData);
}
