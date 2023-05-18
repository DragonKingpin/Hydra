package Pinecone.Framework.Util.Summer;

public class NaughtyGenieInvokedException extends ReflectiveOperationException {
    public enum NaughtyGenieType {
        N_NAUGHTY,
        N_GHOST,
        N_HETEROGENEOUS,
        N_ILLEGAL
    }

    private NaughtyGenieType mNaughtyGenieType = NaughtyGenieType.N_NAUGHTY;

    public NaughtyGenieInvokedException() {
        super();
    }

    public NaughtyGenieInvokedException(String s) {
        super(s);
    }

    public NaughtyGenieInvokedException( String s, NaughtyGenieType naughtyGenieType ) {
        super(s);
        this.mNaughtyGenieType = naughtyGenieType;
    }

    public NaughtyGenieInvokedException( String s, NaughtyGenieType naughtyGenieType, Throwable cause ) {
        super( s, cause );
        this.mNaughtyGenieType = naughtyGenieType;
    }

    public NaughtyGenieType getType() {
        return this.mNaughtyGenieType;
    }
}
