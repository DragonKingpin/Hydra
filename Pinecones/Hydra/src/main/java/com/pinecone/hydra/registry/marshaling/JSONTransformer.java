package com.pinecone.hydra.registry.marshaling;

import java.util.List;
import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONArray;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.registry.KOMRegistry;
import com.pinecone.hydra.registry.entity.ElementNode;
import com.pinecone.hydra.registry.entity.GenericNamespace;
import com.pinecone.hydra.registry.entity.GenericProperties;
import com.pinecone.hydra.registry.entity.GenericTextFile;
import com.pinecone.hydra.registry.entity.GenericTextValue;
import com.pinecone.hydra.registry.entity.Namespace;
import com.pinecone.hydra.registry.entity.Properties;
import com.pinecone.hydra.registry.entity.TextFile;
import com.pinecone.hydra.registry.entity.TextValueTypes;

public class JSONTransformer implements RegistryTransformer {
    protected KOMRegistry registry;

    public JSONTransformer( KOMRegistry registry ) {
        this.registry = registry;
    }

    @Override
    public ElementNode decode( Object val ) {
        return null;
    }

    @Override
    public Object encode( ElementNode node ) {
        if( node.evinceNamespace() != null ) {
            return node.evinceNamespace().toJSONObject();
        }
        else if( node.evinceProperties() != null ) {
            return node.evinceProperties().toJSONObject();
        }
        else if( node.evinceTextFile() != null ) {
            return node.evinceTextFile().toJSON();
        }
        return null;
    }

    protected boolean isPropertiesFormat( Map jo ) {
        boolean b = false;
        for( Object o : jo.entrySet() ) {
            Map.Entry kv = (Map.Entry) o;
            if( kv.getValue() instanceof Map ) {
                b = true;
            }
            if( kv.getValue() instanceof List ) {
                b = true;
            }
        }
        return b;
    }

    protected boolean isPropertiesFormat( List jo ) {
        boolean b = false;
        for( Object o : jo ) {
            if( o instanceof Map ) {
                b = true;
            }
            if( o instanceof List ) {
                b = true;
            }
        }
        return b;
    }

    public ElementNode decode(String szName, Object o, GUID parentGuid) {
        if (o instanceof Map) {
            return this.decodeJSONObject(szName, (Map) o, parentGuid);
        }
        else if (o instanceof List) {
            return this.decodeJSONArray(szName, (List) o, parentGuid);
        }

        // Handling text file as a leaf node
        TextFile file = new GenericTextFile(this.registry);
        file.setName(szName);
        this.registry.put(file);
        file.put(new GenericTextValue(file.getGuid(), o.toString(), TextValueTypes.queryType(o)));
        this.registry.affirmOwnedNode(parentGuid, file.getGuid());  // Correctly associate parent
        return file;
    }

    public ElementNode decodeJSONObject(String szName, Map jo, GUID parentGuid) {
        boolean isNamespace = this.isPropertiesFormat(jo);
        ElementNode elementNode;
        GUID currentGuid;

        if (isNamespace) {
            Namespace ns = new GenericNamespace(this.registry);
            ns.setName(szName);
            currentGuid = this.registry.put(ns);  // Save the new node and get its GUID
            this.registry.affirmOwnedNode(parentGuid, currentGuid);  // Associate with parent

            // Recursively decode children
            for (Object o : jo.entrySet()) {
                Map.Entry kv = (Map.Entry) o;
                ElementNode childNode = this.decode(kv.getKey().toString(), kv.getValue(), currentGuid);
                this.registry.put(childNode);
            }

            elementNode = ns;
        } else {
            Properties pro = new GenericProperties(this.registry);
            pro.setName(szName);
            currentGuid = this.registry.put(pro);
            this.registry.affirmOwnedNode(parentGuid, currentGuid);

            // Add properties as key-value pairs
            for (Object o : jo.entrySet()) {
                Map.Entry kv = (Map.Entry) o;
                pro.put(kv.getKey().toString(), kv.getValue());
            }

            this.registry.put(pro);
            elementNode = pro;
        }

        return elementNode;
    }

    public ElementNode decodeJSONArray(String szName, List ja, GUID parentGuid) {
        boolean isNamespace = this.isPropertiesFormat(ja);
        ElementNode elementNode;
        GUID currentGuid;

        if (isNamespace) {
            Namespace ns = new GenericNamespace(this.registry);
            ns.setName(szName);
            currentGuid = this.registry.put(ns);
            this.registry.affirmOwnedNode(parentGuid, currentGuid);

            // Recursively decode children
            int i = 0;
            for (Object o : ja) {
                ElementNode childNode = this.decode(Integer.toString(i), o, currentGuid);
                this.registry.put(childNode);
                ++i;
            }

            elementNode = ns;
        } else {
            Properties pro = new GenericProperties(this.registry);
            pro.setName(szName);
            currentGuid = this.registry.put(pro);
            this.registry.affirmOwnedNode(parentGuid, currentGuid);

            // Add list elements as properties
            int i = 0;
            for (Object o : ja) {
                pro.put(Integer.toString(i), o);
                ++i;
            }

            this.registry.put(pro);
            elementNode = pro;
        }

        return elementNode;
    }
}
