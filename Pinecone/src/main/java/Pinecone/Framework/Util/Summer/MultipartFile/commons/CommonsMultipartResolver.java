package Pinecone.Framework.Util.Summer.MultipartFile.commons;

import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import Pinecone.Framework.System.util.Assert;
import Pinecone.Framework.Util.Summer.MultipartFile.MaxUploadSizeExceededException;
import Pinecone.Framework.Util.Summer.MultipartFile.MultipartException;
import Pinecone.Framework.Util.Summer.MultipartFile.MultipartHttpServletRequest;
import Pinecone.Framework.Util.Summer.MultipartFile.MultipartResolver;
import Pinecone.Framework.Util.Summer.MultipartFile.support.DefaultMultipartHttpServletRequest;
import Pinecone.Framework.Util.Summer.context.ServletContextAware;
import Pinecone.Framework.Util.Summer.util.WebUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


public class CommonsMultipartResolver extends CommonsFileUploadSupport implements MultipartResolver, ServletContextAware {
    private boolean resolveLazily;

    public CommonsMultipartResolver() {
        this.resolveLazily = false;
    }

    public CommonsMultipartResolver(ServletContext servletContext) {
        this();
        this.setServletContext(servletContext);
    }

    public void setResolveLazily(boolean resolveLazily) {
        this.resolveLazily = resolveLazily;
    }

    protected FileUpload newFileUpload(FileItemFactory fileItemFactory) {
        return new ServletFileUpload(fileItemFactory);
    }

    public void setServletContext(ServletContext servletContext) {
        if (!this.isUploadTempDirSpecified()) {
            this.getFileItemFactory().setRepository(WebUtils.getTempDir(servletContext));
        }

    }

    public boolean isMultipart(HttpServletRequest request) {
        return request != null && ServletFileUpload.isMultipartContent(request);
    }

    public MultipartHttpServletRequest resolveMultipart(final HttpServletRequest request) throws MultipartException {
        Assert.notNull(request, "Request must not be null");
        if (this.resolveLazily) {
            return new DefaultMultipartHttpServletRequest(request) {
                protected void initializeMultipart() {
                    MultipartParsingResult parsingResult = CommonsMultipartResolver.this.parseRequest(request);
                    this.setMultipartFiles(parsingResult.getMultipartFiles());
                    this.setMultipartParameters(parsingResult.getMultipartParameters());
                    this.setMultipartParameterContentTypes(parsingResult.getMultipartParameterContentTypes());
                }
            };
        } else {
            MultipartParsingResult parsingResult = this.parseRequest(request);
            return new DefaultMultipartHttpServletRequest(request, parsingResult.getMultipartFiles(), parsingResult.getMultipartParameters(), parsingResult.getMultipartParameterContentTypes());
        }
    }

    protected MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
        String encoding = this.determineEncoding(request);
        FileUpload fileUpload = this.prepareFileUpload(encoding);

        try {
            List<FileItem> fileItems = ((ServletFileUpload)fileUpload).parseRequest(request);
            return this.parseFileItems(fileItems, encoding);
        } catch (SizeLimitExceededException var5) {
            throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), var5);
        } catch (FileUploadException var6) {
            throw new MultipartException("Could not parse multipart servlet request", var6);
        }
    }

    protected String determineEncoding(HttpServletRequest request) {
        String encoding = request.getCharacterEncoding();
        if (encoding == null) {
            encoding = this.getDefaultEncoding();
        }

        return encoding;
    }

    public void cleanupMultipart(MultipartHttpServletRequest request) {
        if (request != null) {
            try {
                this.cleanupFileItems(request.getMultiFileMap());
            } catch (Throwable var3) {
                System.err.println("Failed to perform multipart cleanup for servlet request");
            }
        }

    }
}
