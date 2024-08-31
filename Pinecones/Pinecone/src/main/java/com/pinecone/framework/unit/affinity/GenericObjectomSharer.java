package com.pinecone.framework.unit.affinity;

import com.pinecone.framework.system.prototype.ObjectiveBean;
import com.pinecone.framework.system.prototype.Objectom;

public class GenericObjectomSharer implements DataSharer {
    protected Objectom  mWarped;

    public GenericObjectomSharer( Object target, boolean isBean ) {
        this.mWarped = DataSharer.warp( target, isBean );
    }

    @Override
    public Object share( Objectom that, boolean ignoreIfNoSetter ) {
        return DataSharer.share( this.mWarped, that, ignoreIfNoSetter );
    }

    @Override
    public Object share( Objectom that ) {
        return this.share( that, true );
    }

    @Override
    public Object shareFromBean( Object that ) {
        return this.share( new ObjectiveBean( that ) );
    }

    @Override
    public Object share( Object that ) {
        return this.share( Objectom.wrap( that ) );
    }

}
