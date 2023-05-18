package Pinecone.Framework.Util.Summer.MultipartFile;

import Pinecone.Framework.Unit.MultiValueMap;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public interface MultipartRequest {
    Iterator<String> getFileNames();

    MultipartFile getFile(String szFileFieldName);

    List<MultipartFile> getFiles(String szName);

    Map<String, MultipartFile> getFileMap();

    MultiValueMap<String, MultipartFile> getMultiFileMap();

    String getMultipartContentType(String szParamOrFileName);
}