package Pinecone.Framework.Util.Summer.prototype;

import Pinecone.Framework.Util.JSON.JSONObject;

import javax.servlet.ServletException;
import java.io.IOException;

public interface JSONBasedModel {
    void beforeDispatch() throws IOException, ServletException;

    void dispatch() throws IOException, ServletException ;

    void afterDispatch() throws IOException, ServletException;

    JSONObject getPageData();

    String toJSONString();

    String getModelCommand();

    void render() throws ServletException, IOException ;
}
