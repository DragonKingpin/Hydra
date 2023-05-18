package Pinecone.Framework.System.Hometype;

import Pinecone.Framework.System.Prototype.Reactor;

public interface StereotypicReactor extends Reactor {
    Class<?> getStereotype();

    void     setStereotype( Class<?> stereotype );
}
