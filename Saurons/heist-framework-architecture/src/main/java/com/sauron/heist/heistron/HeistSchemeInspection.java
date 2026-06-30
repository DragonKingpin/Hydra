package com.sauron.heist.heistron;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.prototype.Pinenut;
import com.sauron.heist.heistron.scheme.HeistSchemeRenderResult;

import java.util.List;

public interface HeistSchemeInspection extends Pinenut {

    HeistSchemeRenderResult previewInstanceConfigByName( @Nullable String name, boolean recursive );

    HeistSchemeRenderResult previewInstanceConfigByPath( List<String> names, boolean recursive );

}
