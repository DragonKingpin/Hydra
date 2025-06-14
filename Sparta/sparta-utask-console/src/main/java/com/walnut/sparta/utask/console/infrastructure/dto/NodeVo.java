package com.walnut.sparta.utask.console.infrastructure.dto;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;

import java.util.List;


public class NodeVo implements Pinenut {
    protected String name;
    protected String guid;
    protected String type;
    protected List<NodeVo> children;



    public NodeVo(String name, String guid, String type, List<NodeVo> children) {
        this.name = name;
        this.guid = guid;
        this.type = type;
        this.children = children;
    }


    public String getName() {
        return name;
    }
    public String getGuid() {
        return guid;
    }
    public String getType() {
        return type;
    }
    public List<NodeVo> getChildren() {
        return children;
    }

    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }
}
