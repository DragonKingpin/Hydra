package Pinecone.Framework.Util.Summer;

public class TerminateSessionException extends RuntimeException {
    public TerminateSessionException() {
    }

    public TerminateSessionException(String message) {
        super(message);
    }

    public TerminateSessionException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

    public TerminateSessionException(Throwable rootCause) {
        super(rootCause);
    }

    public Throwable getRootCause() {
        return this.getCause();
    }
}
