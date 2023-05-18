package Pinecone.Framework.Util.Summer.MultipartFile;

import Pinecone.Framework.Util.Summer.http.HttpHeaders;
import Pinecone.Framework.Util.Summer.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;

public interface MultipartHttpServletRequest extends HttpServletRequest, MultipartRequest {
    HttpMethod getRequestMethod();

    HttpHeaders getRequestHeaders();

    HttpHeaders getMultipartHeaders(String var1);
}
