package com.pinecone.summer.prototype;

import com.pinecone.framework.util.json.JSONObject;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.reflect.Method;

public interface Pageson extends Wizard {
    JSONObject getPageData();

    String toJSONString();

    String getModelCommand();

    void setRenderum( Method fnRenderum );

    void render() throws ServletException, IOException;

    void setEnchanterRole( boolean bRole );

    boolean isEnchanter();
}
