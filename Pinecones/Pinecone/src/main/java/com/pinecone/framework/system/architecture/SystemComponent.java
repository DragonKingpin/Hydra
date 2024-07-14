package com.pinecone.framework.system.architecture;

import com.pinecone.framework.system.RuntimeSystem;

public interface SystemComponent extends Component {
    RuntimeSystem getSystem();
}
