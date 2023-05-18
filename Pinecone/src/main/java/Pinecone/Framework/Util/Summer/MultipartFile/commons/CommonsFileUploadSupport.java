package Pinecone.Framework.Util.Summer.MultipartFile.commons;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Pinecone.Framework.Debug.Debug;
import Pinecone.Framework.Util.Summer.MultipartFile.MultipartFile;
import Pinecone.Framework.Util.Summer.http.MediaType;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import Pinecone.Framework.Util.Summer.io.Resource;
import Pinecone.Framework.Unit.LinkedMultiValueMap;
import Pinecone.Framework.Unit.MultiValueMap;
import Pinecone.Framework.System.util.StringUtils;


public abstract class CommonsFileUploadSupport {
    private final DiskFileItemFactory fileItemFactory = this.newFileItemFactory();
    private final FileUpload fileUpload = this.newFileUpload(this.getFileItemFactory());
    private boolean uploadTempDirSpecified = false;

    public CommonsFileUploadSupport() {
    }

    public DiskFileItemFactory getFileItemFactory() {
        return this.fileItemFactory;
    }

    public FileUpload getFileUpload() {
        return this.fileUpload;
    }

    public void setMaxUploadSize(long maxUploadSize) {
        this.fileUpload.setSizeMax(maxUploadSize);
    }

    public void setSingleUploadSize(long maxUploadSize) {
        this.fileUpload.setFileSizeMax(maxUploadSize);
    }

    public void setMaxInMemorySize(int maxInMemorySize) {
        this.fileItemFactory.setSizeThreshold(maxInMemorySize);
    }

    public void setDefaultEncoding(String defaultEncoding) {
        this.fileUpload.setHeaderEncoding(defaultEncoding);
    }

    protected String getDefaultEncoding() {
        String encoding = this.getFileUpload().getHeaderEncoding();
        if (encoding == null) {
            encoding = "ISO-8859-1";
        }

        return encoding;
    }

    public void setUploadTempDir(Resource uploadTempDir) throws IOException {
        if (!uploadTempDir.exists() && !uploadTempDir.getFile().mkdirs()) {
            throw new IllegalArgumentException("Given uploadTempDir [" + uploadTempDir + "] could not be created");
        } else {
            this.fileItemFactory.setRepository(uploadTempDir.getFile());
            this.uploadTempDirSpecified = true;
        }
    }

    protected boolean isUploadTempDirSpecified() {
        return this.uploadTempDirSpecified;
    }

    protected DiskFileItemFactory newFileItemFactory() {
        return new DiskFileItemFactory();
    }

    protected abstract FileUpload newFileUpload(FileItemFactory var1);

    protected FileUpload prepareFileUpload(String encoding) {
        FileUpload fileUpload = this.getFileUpload();
        FileUpload actualFileUpload = fileUpload;
        if (encoding != null && !encoding.equals(fileUpload.getHeaderEncoding())) {
            actualFileUpload = this.newFileUpload(this.getFileItemFactory());
            actualFileUpload.setSizeMax(fileUpload.getSizeMax());
            actualFileUpload.setHeaderEncoding(encoding);
        }

        return actualFileUpload;
    }

    protected CommonsFileUploadSupport.MultipartParsingResult parseFileItems(List<FileItem> fileItems, String encoding) {
        MultiValueMap<String, MultipartFile> multipartFiles = new LinkedMultiValueMap();
        Map<String, String[]> multipartParameters = new HashMap();
        Map<String, String> multipartParameterContentTypes = new HashMap();
        Iterator var6 = fileItems.iterator();

        while(true) {
            while(var6.hasNext()) {
                FileItem fileItem = (FileItem)var6.next();
                if (fileItem.isFormField()) {
                    String partEncoding = this.determineEncoding(fileItem.getContentType(), encoding);
                    String value;
                    if (partEncoding != null) {
                        try {
                            value = fileItem.getString(partEncoding);
                        } catch (UnsupportedEncodingException var12) {
                            System.err.println("Could not decode multipart item '" + fileItem.getFieldName() + "' with encoding '" + partEncoding + "': using platform default");

                            value = fileItem.getString();
                        }
                    } else {
                        value = fileItem.getString();
                    }

                    String[] curParam = (String[])multipartParameters.get(fileItem.getFieldName());
                    if (curParam == null) {
                        multipartParameters.put(fileItem.getFieldName(), new String[]{value});
                    } else {
                        String[] newParam = StringUtils.addStringToArray(curParam, value);
                        multipartParameters.put(fileItem.getFieldName(), newParam);
                    }

                    multipartParameterContentTypes.put(fileItem.getFieldName(), fileItem.getContentType());
                } else {
                    CommonsMultipartFile file = new CommonsMultipartFile(fileItem);
                    multipartFiles.add(file.getName(), file);
                    Debug.trace(
                            "Found multipart file [" + file.getName() + "] of size " + file.getSize() + " bytes with original filename [" + file.getOriginalFilename() + "], stored " + file.getStorageDescription()
                    );
                }
            }

            return new CommonsFileUploadSupport.MultipartParsingResult(multipartFiles, multipartParameters, multipartParameterContentTypes);
        }
    }

    protected void cleanupFileItems(MultiValueMap<String, MultipartFile> multipartFiles) {
        Iterator var2 = multipartFiles.values().iterator();

        while(var2.hasNext()) {
            List<MultipartFile> files = (List)var2.next();
            Iterator var4 = files.iterator();

            while(var4.hasNext()) {
                MultipartFile file = (MultipartFile)var4.next();
                if (file instanceof CommonsMultipartFile) {
                    CommonsMultipartFile cmf = (CommonsMultipartFile)file;
                    cmf.getFileItem().delete();

                    Debug.trace("Cleaning up multipart file [" + cmf.getName() + "] with original filename [" + cmf.getOriginalFilename() + "], stored " + cmf.getStorageDescription());
                }
            }
        }

    }

    private String determineEncoding(String contentTypeHeader, String defaultEncoding) {
        if (!StringUtils.hasText(contentTypeHeader)) {
            return defaultEncoding;
        } else {
            MediaType contentType = MediaType.parseMediaType(contentTypeHeader);
            Charset charset = contentType.getCharSet();
            return charset != null ? charset.name() : defaultEncoding;
        }
    }

    protected static class MultipartParsingResult {
        private final MultiValueMap<String, MultipartFile> multipartFiles;
        private final Map<String, String[]> multipartParameters;
        private final Map<String, String> multipartParameterContentTypes;

        public MultipartParsingResult(MultiValueMap<String, MultipartFile> mpFiles, Map<String, String[]> mpParams, Map<String, String> mpParamContentTypes) {
            this.multipartFiles = mpFiles;
            this.multipartParameters = mpParams;
            this.multipartParameterContentTypes = mpParamContentTypes;
        }

        public MultiValueMap<String, MultipartFile> getMultipartFiles() {
            return this.multipartFiles;
        }

        public Map<String, String[]> getMultipartParameters() {
            return this.multipartParameters;
        }

        public Map<String, String> getMultipartParameterContentTypes() {
            return this.multipartParameterContentTypes;
        }
    }
}
