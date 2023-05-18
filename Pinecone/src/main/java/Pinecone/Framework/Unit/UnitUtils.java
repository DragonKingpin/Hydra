package Pinecone.Framework.Unit;

import java.util.ArrayList;
import java.util.List;

public abstract class UnitUtils {
    public static <T> List<T> spawnExtendParent( List<T> parent ) {
        return UnitUtils.spawnExtendParent( parent, ArrayList.class );
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> spawnExtendParent( List<T> parent, Class<?> basic ) {
        List<T> subList = null;
        try{
            subList = parent.getClass().newInstance();
        }
        catch ( IllegalAccessException | InstantiationException e ) {
            try{
                subList = (List<T>) basic.newInstance();
            }
            catch ( IllegalAccessException | InstantiationException e1 ) {
                throw new IllegalArgumentException( "Illegal 'basic' class given.", e1 );
            }
        }
        return subList;
    }
}
