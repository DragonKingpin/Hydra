package Pinecone.Framework.Util.Math;

import java.util.*;

public class Vectorizer<T> {
    private Vector<Vector<T > >      tokenArrays;

    private HashSet<T >              unionTokenSet;

    private Vector<Vector<Double > > tokenVectors;

    private Vectorizer() {
        this.unionTokenSet = new HashSet<>();
        this.tokenArrays   = new Vector<>();
        this.tokenVectors  = new Vector<>();
    }

    public Vectorizer( Vector<Vector<T> >tokenArrays ){
        this();
        this.apply( tokenArrays );
    }

    private void apply( Vector<Vector<T> >tokenArrays ) {
        this.tokenArrays = tokenArrays;

        for ( int i = 0; i < this.tokenArrays.size(); i++ ) {
            this.unionTokenSet.addAll( tokenArrays.get(i) );

            this.tokenVectors.add( new Vector<>() );
        }

        this.analysis();
    }

    private Vector<Vector<T> > singlify( Vector<T> tokenArrayA, Vector<T> tokenArrayB ) {
        Vector<Vector<T> > single = new Vector<>();
        single.add( tokenArrayA );
        single.add( tokenArrayB );
        return single;
    }

    public Vectorizer( Vector<T> tokenArrayA, Vector<T> tokenArrayB ){
        this();
        this.apply(  this.singlify( tokenArrayA, tokenArrayB ) );
    }

    public Vector<Vector<Double > > getResult(){
        return this.tokenVectors;
    }


    private void tokenMapify( Vector<T > proto, Map<T, Double > map ) {
        for ( T item : proto ){
            double tempInt = 0.0;
            if( map.containsKey(item) ){
                tempInt = map.get(item);
            }else {
                map.put(item,0.0);
            }
            map.replace(item,++tempInt);
        }
    }

    private void analysis(){
        ArrayList<HashMap<T,Double > > tokenMaps = new ArrayList<>();

        for ( int i = 0; i < this.tokenArrays.size(); i++ ) {
            tokenMaps.add( new HashMap<>() );
            this.tokenMapify( this.tokenArrays.get(i), tokenMaps.get(i) );
        }

        for( T item : this.unionTokenSet ){
            for ( int i = 0; i < tokenMaps.size(); i++ ) {
                if( !tokenMaps.get(i).containsKey(item) ){
                    tokenMaps.get(i).put( item,0.0 );
                }
            }

            for ( int j = 0; j < this.tokenVectors.size(); j++ ) {
                this.tokenVectors.get(j).add( tokenMaps.get(j).get(item) );
            }
        }


    }
}
