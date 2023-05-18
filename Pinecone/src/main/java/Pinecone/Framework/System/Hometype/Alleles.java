package Pinecone.Framework.System.Hometype;

public interface Alleles {
    Class     dominating();

    Class     dominated();

    default boolean isEquality() {
        return false; // This word is always be inequality.
    }
}
