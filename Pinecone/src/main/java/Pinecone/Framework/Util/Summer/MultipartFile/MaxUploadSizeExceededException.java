package Pinecone.Framework.Util.Summer.MultipartFile;

public class MaxUploadSizeExceededException extends MultipartException {
    private final long maxUploadSize;

    public MaxUploadSizeExceededException(long maxUploadSize) {
        this(maxUploadSize, (Throwable)null);
    }

    public MaxUploadSizeExceededException(long maxUploadSize, Throwable ex) {
        super("Maximum upload size of " + maxUploadSize + " bytes exceeded", ex);
        this.maxUploadSize = maxUploadSize;
    }

    public long getMaxUploadSize() {
        return this.maxUploadSize;
    }
}
