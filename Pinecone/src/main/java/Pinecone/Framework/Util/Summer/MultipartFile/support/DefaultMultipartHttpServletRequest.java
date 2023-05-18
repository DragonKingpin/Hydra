package Pinecone.Framework.Util.Summer.MultipartFile.support;

import Pinecone.Framework.Unit.MultiValueMap;
import Pinecone.Framework.Util.Summer.MultipartFile.MultipartFile;
import Pinecone.Framework.Util.Summer.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class DefaultMultipartHttpServletRequest extends AbstractMultipartHttpServletRequest {
    private static final String CONTENT_TYPE = "Content-Type";
    private Map<String, String[]> multipartParameters;
    private Map<String, String> multipartParameterContentTypes;

    public DefaultMultipartHttpServletRequest(HttpServletRequest request, MultiValueMap<String, MultipartFile> mpFiles, Map<String, String[]> mpParams, Map<String, String> mpParamContentTypes) {
        super(request);
        this.setMultipartFiles(mpFiles);
        this.setMultipartParameters(mpParams);
        this.setMultipartParameterContentTypes(mpParamContentTypes);
    }

    public DefaultMultipartHttpServletRequest(HttpServletRequest request) {
        super(request);
    }

    public String getParameter(String name) {
        String[] values = (String[])this.getMultipartParameters().get(name);
        if (values != null) {
            return values.length > 0 ? values[0] : null;
        } else {
            return super.getParameter(name);
        }
    }

    public String[] getParameterValues(String name) {
        String[] values = (String[])this.getMultipartParameters().get(name);
        return values != null ? values : super.getParameterValues(name);
    }

    public Enumeration<String> getParameterNames() {
        Map<String, String[]> multipartParameters = this.getMultipartParameters();
        if (multipartParameters.isEmpty()) {
            return super.getParameterNames();
        } else {
            Set<String> paramNames = new LinkedHashSet<>();
            Enumeration paramEnum = super.getParameterNames();

            while(paramEnum.hasMoreElements()) {
                paramNames.add((String) paramEnum.nextElement());
            }

            paramNames.addAll(multipartParameters.keySet());
            return Collections.enumeration(paramNames);
        }
    }

    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> multipartParameters = this.getMultipartParameters();
        if (multipartParameters.isEmpty()) {
            return super.getParameterMap();
        } else {
            Map<String, String[]> paramMap = new LinkedHashMap<>();
            paramMap.putAll(super.getParameterMap());
            paramMap.putAll(multipartParameters);
            return paramMap;
        }
    }

    public String getMultipartContentType(String paramOrFileName) {
        MultipartFile file = this.getFile(paramOrFileName);
        return file != null ? file.getContentType() : (String)this.getMultipartParameterContentTypes().get(paramOrFileName);
    }

    public HttpHeaders getMultipartHeaders(String paramOrFileName) {
        String contentType = this.getMultipartContentType(paramOrFileName);
        if (contentType != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", contentType);
            return headers;
        } else {
            return null;
        }
    }

    protected final void setMultipartParameters(Map<String, String[]> multipartParameters) {
        this.multipartParameters = multipartParameters;
    }

    protected Map<String, String[]> getMultipartParameters() {
        if (this.multipartParameters == null) {
            this.initializeMultipart();
        }

        return this.multipartParameters;
    }

    protected final void setMultipartParameterContentTypes(Map<String, String> multipartParameterContentTypes) {
        this.multipartParameterContentTypes = multipartParameterContentTypes;
    }

    protected Map<String, String> getMultipartParameterContentTypes() {
        if (this.multipartParameterContentTypes == null) {
            this.initializeMultipart();
        }

        return this.multipartParameterContentTypes;
    }
}
