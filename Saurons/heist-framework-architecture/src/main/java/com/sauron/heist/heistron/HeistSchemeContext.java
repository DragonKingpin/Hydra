package com.sauron.heist.heistron;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.unit.MultiScopeMap;
import com.pinecone.framework.util.config.JSONConfig;

import java.util.Map;

public interface HeistSchemeContext extends Pinenut {

    JSONConfig getProtoConfig();

    JSONConfig getTemplateHeistSchemeConfig();

    JSONConfig getLocalHeistsConfigList();

    MultiScopeMap<String, Object > getGlobalConfigScope();

    Map<String, Object > getRootConfig();

}
