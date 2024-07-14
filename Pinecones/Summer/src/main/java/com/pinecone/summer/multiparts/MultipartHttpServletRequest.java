package com.pinecone.summer.multiparts;

import com.pinecone.summer.http.HttpHeaders;
import com.pinecone.summer.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;

public interface MultipartHttpServletRequest extends HttpServletRequest, MultipartRequest {
    HttpMethod getRequestMethod();

    HttpHeaders getRequestHeaders();

    HttpHeaders getMultipartHeaders(String var1);
}
