package com.pinecone.hydra.registry.marshaling;

import com.pinecone.hydra.registry.KOMRegistry;
import com.pinecone.hydra.registry.entity.Attributes;
import com.pinecone.hydra.registry.entity.ElementNode;
import com.pinecone.hydra.registry.entity.Namespace;
import com.pinecone.hydra.registry.entity.Properties;
import com.pinecone.hydra.registry.entity.RegistryTreeNode;
import com.pinecone.hydra.registry.entity.TextFile;

import org.jsoup.nodes.Element;

public class RegistryDOMEncoder implements RegistryEncoder {
    protected KOMRegistry registry;

    public RegistryDOMEncoder( KOMRegistry registry ) {
        this.registry = registry;
    }

    @Override
    public Object encode( ElementNode node ) {
        if ( node.evinceNamespace() != null ) {
            return this.encodeNS(node.evinceNamespace() );
        }
        else if ( node.evinceProperties() != null ) {
            return this.encodeProperties(node.evinceProperties() );
        }
        else if ( node.evinceTextFile() != null ) {
            return this.encodeTextFile(node.evinceTextFile());
        }
        return null;
    }

    protected Element encodeNS( Namespace ns ) {
        Element element = new Element(ns.getName());
        Attributes attributes = ns.getAttributes();
        setDOMAttributes(element, attributes);

        for ( RegistryTreeNode child : ns.getChildren().values() ) {
            Object encodedChild = this.encode((ElementNode)child);
            if ( encodedChild instanceof Element ) {
                element.appendChild((Element) encodedChild);
            }
        }

        return element;
    }

    protected Element encodeProperties( Properties properties ) {
        Element element = new Element( properties.getName() );
        Attributes attributes = properties.getAttributes();
        setDOMAttributes(element, attributes);

        for ( String key : properties.keySet() ) {
            Element propertyElement = new Element(key);
            propertyElement.text( properties.get(key).getValue().toString() );
            element.appendChild( propertyElement );
        }

        return element;
    }

    protected Element encodeTextFile( TextFile textFile ) {
        Element element = new Element( textFile.getName() );
        Attributes attributes = textFile.getAttributes();
        setDOMAttributes( element, attributes );

        element.append( textFile.get().getValue() );

        return element;
    }

    private void setDOMAttributes( Element element, Attributes attributes ) {
        for ( String key : attributes.keySet() ) {
            element.attr(key, attributes.get(key));
        }
    }
}
