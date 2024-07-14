package com.pinecone.summer.http;

import com.pinecone.framework.util.json.JSONObject;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface HttpEntityParser {
    Object valueSafety( Object value );

    JSONObject parseQueryString  (String szQueryString, boolean bSafe );

    JSONObject parseFormData     (HttpServletRequest request, boolean bSafe );

    JSONObject requestMapJsonify ( HttpServletRequest request, boolean bSafe );

    JSONObject siftPostFromParameterMap( HttpServletRequest request, boolean bSafe );

    Map<String, Cookie > cookiesMapify ( Map<String, Cookie > map, HttpServletRequest request );
}
