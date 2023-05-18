package Pinecone.Framework.System.Hometype;

import Pinecone.Framework.System.Prototype.Pinenut;

public interface Homotype extends Pinenut, Assimilable, Alleles {
    Class    conjugated();

    boolean  isHomogeneity( Object that );

    default  boolean  isHomotypic() {
        return false;
    }
}
