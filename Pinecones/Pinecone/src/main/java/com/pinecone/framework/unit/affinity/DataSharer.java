package com.pinecone.framework.unit.affinity;

import com.pinecone.framework.system.prototype.ObjectiveBean;
import com.pinecone.framework.system.prototype.Objectom;
import com.pinecone.framework.system.prototype.Pinenut;

public interface DataSharer extends Pinenut {
    Object share ( Objectom that, boolean ignoreIfNoSetter ) ;

    default Object share( Objectom that ) {
        return this.share( that, true );
    }

    default Object shareFromBean( Object that ) {
        return this.share( new ObjectiveBean( that ) );
    }

    default Object share( Object that ) {
        return this.share( Objectom.wrap( that ) );
    }


    static Objectom warp( Object target, boolean isBean ) {
        if( target instanceof Objectom ) {
            return (Objectom)target;
        }
        else {
            if( isBean ) {
                return new ObjectiveBean( target );
            }
            else {
                return Objectom.wrap( target );
            }
        }
    }

    static Object share( Objectom target, Objectom shared, boolean ignoreIfNoSetter ) {
        for ( Object key : shared.keys() ) {
            try{
                target.set( key, shared.get( key ) );
            }
            catch ( IllegalArgumentException e ) {
                if( !ignoreIfNoSetter ) {
                    throw e;
                }
            }
        }
        return target.prototype().proto();
    }

    static Object share( Objectom target, Objectom shared ) {
        return DataSharer.share( target, shared, true );
    }
}
