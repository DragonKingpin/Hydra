package com.walnut.sparta.services.pojo;

import java.net.URI;
import java.net.URISyntaxException;

public class pathURI {
    private URI pathURI;

    public pathURI() {
    }

    public pathURI(URI pathURI) {
        this.pathURI = pathURI;
    }

    public pathURI(String uri){
        this.pathURI = URI.create(uri);
    }

    public String getClassName(){
        String schemeSpecificPart = this.pathURI.getSchemeSpecificPart();
        if (schemeSpecificPart.startsWith("java-service//")) {
            return schemeSpecificPart.substring("java-service//".length());
        }
        return null;
    }

    /**
     * 获取
     * @return pathURI
     */
    public URI getPathURI() {
        return pathURI;
    }

    /**
     * 设置
     * @param pathURI
     */
    public void setPathURI(URI pathURI) {
        this.pathURI = pathURI;
    }

    public String toString() {
        return "pathURI{pathURI = " + pathURI + "}";
    }
}