package com.pinecone.summer.multiparts.support;

import com.pinecone.framework.unit.LinkedMultiValueMap;
import com.pinecone.framework.unit.MultiValueMap;
import com.pinecone.summer.multiparts.MultipartFile;
import com.pinecone.summer.multiparts.MultipartHttpServletRequest;
import com.pinecone.summer.http.HttpHeaders;
import com.pinecone.summer.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

public abstract class AbstractMultipartHttpServletRequest extends HttpServletRequestWrapper implements MultipartHttpServletRequest {
    private MultiValueMap<String, MultipartFile> multipartFiles;

    protected AbstractMultipartHttpServletRequest(HttpServletRequest request) {
        super(request);
    }

    public HttpServletRequest getRequest() {
        return (HttpServletRequest)super.getRequest();
    }

    public HttpMethod getRequestMethod() {
        return HttpMethod.valueOf(this.getRequest().getMethod());
    }

    public HttpHeaders getRequestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        Enumeration headerNames = this.getHeaderNames();

        while(headerNames.hasMoreElements()) {
            String headerName = (String)headerNames.nextElement();
            headers.put(headerName, Collections.list(this.getHeaders(headerName)));
        }

        return headers;
    }

    public Iterator<String> getFileNames() {
        return this.getMultipartFiles().keySet().iterator();
    }

    public MultipartFile getFile(String name) {
        return (MultipartFile)this.getMultipartFiles().getFirst(name);
    }

    public List<MultipartFile> getFiles(String name) {
        List<MultipartFile> multipartFiles = (List<MultipartFile>)this.getMultipartFiles().get(name);
        return multipartFiles != null ? multipartFiles : Collections.emptyList();
    }

    public Map<String, MultipartFile> getFileMap() {
        return this.getMultipartFiles().toSingleValueMap();
    }

    public MultiValueMap<String, MultipartFile> getMultiFileMap() {
        return this.getMultipartFiles();
    }

    protected final void setMultipartFiles(MultiValueMap<String, MultipartFile> multipartFiles) {
        this.multipartFiles = new LinkedMultiValueMap<>(Collections.unmodifiableMap(multipartFiles));
    }

    protected MultiValueMap<String, MultipartFile> getMultipartFiles() {
        if (this.multipartFiles == null) {
            this.initializeMultipart();
        }

        return this.multipartFiles;
    }

    protected void initializeMultipart() {
        throw new IllegalStateException("Multipart request not initialized");
    }
}
