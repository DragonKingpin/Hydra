package com.sauron.heist.heistron;

import java.util.Map;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.unit.MultiScopeMap;
import com.pinecone.framework.util.config.JSONConfig;

public interface HeistScheme extends Pinenut {

    JSONConfig getInstanceConfigByName( String name );

    /**
     * getInstanceConfigByName
     * @param name ( Child instance name, which will extents the parent scope, and get its instance config of this child. )
     *             ( The `null` is the current scope, [this] )
     * @param bRecursive ( Override all object and list, if that key which its child doesnt`t had. )
     * @return Instance Config
     */
    JSONConfig getInstanceConfigByName( @Nullable String name, boolean bRecursive );

    void overrideSegment ( Map<String, Object > parentProto, Map<String, Object > instance );


    HeistScheme reinterpret( JSONConfig that );

    MultiScopeMap<String, Object > getHeistScope();

    JSONConfig getProtoConfig();

    Heistgram getHeistgram();

    Heistum getParentHeist();

    JSONConfig getTemplateHeistSchemeConfig();


}
