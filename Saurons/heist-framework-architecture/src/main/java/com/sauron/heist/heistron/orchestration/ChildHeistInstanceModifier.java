package com.sauron.heist.heistron.orchestration;

import com.pinecone.framework.system.prototype.Pinenut;
import com.sauron.heist.heistron.CascadeHeist;

public interface ChildHeistInstanceModifier extends Pinenut {

    void modify( CascadeHeist heistum ) ;

}
