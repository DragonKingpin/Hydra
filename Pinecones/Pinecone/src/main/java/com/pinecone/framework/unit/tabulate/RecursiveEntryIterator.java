package com.pinecone.framework.unit.tabulate;

import com.pinecone.framework.unit.KeyValue;

import java.util.Map;
import java.util.Iterator;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.NoSuchElementException;

public class RecursiveEntryIterator<V > implements Iterator<Map.Entry<?, V > > {
    private Deque<Iterator<? > > mIterStack;
    private Deque<Integer >      mIndexStack;
    private DummyEntry           mNextEntry;
    private boolean              mbIncludeCollection;

    protected RecursiveEntryIterator ( boolean bIncludeCollection ) {
        this.mbIncludeCollection = bIncludeCollection;
        this.mIterStack          = new ArrayDeque<>();
        this.mIndexStack         = new ArrayDeque<>();
        this.mIndexStack.push( 0 );
    }

    public RecursiveEntryIterator( Map<?, V > map, boolean bIncludeCollection ) {
        this( bIncludeCollection );
        this.mIterStack.push( map.entrySet().iterator() );
        this.advance();
    }

    public RecursiveEntryIterator( Map<?, V > map ) {
        this( map, true );
    }

    public RecursiveEntryIterator( Collection<V> collection ) {
        this( true );
        this.mIterStack.push( collection.iterator() );
        this.advance();
    }

    @SuppressWarnings( "unchecked" )
    private void advance() {
        this.mNextEntry = null;

        while ( !this.mIterStack.isEmpty() ) {
            Iterator<?> iterator = this.mIterStack.peek();

            if ( iterator.hasNext() ) {
                Object next = iterator.next();
                if ( next instanceof Map.Entry ) {
                    Map.Entry<?, V > entry = (Map.Entry<?, V >) next;
                    Object value = entry.getValue();
                    if ( value instanceof Map ) {
                        this.mIterStack.push( ((Map<?, V >) value).entrySet().iterator() );
                        this.mIndexStack.push(0);
                    }
                    else if ( value instanceof Collection && this.mbIncludeCollection ) {
                        this.mIterStack.push( ((Collection<?>) value).iterator() );
                        this.mIndexStack.push(0);
                    }
                    else {
                        //this.mNextEntry = new KeyValue<>( entry.getKey(), value );
                        this.updateNextEntryCursor( entry.getKey(), (V)value );
                        this.updateIndex();
                        break;
                    }
                }
                else if ( next instanceof Map ) {
                    this.mIterStack.push( ((Map<?, V>) next).entrySet().iterator() );
                    this.mIndexStack.push(0);
                }
                else if ( next instanceof Collection && this.mbIncludeCollection ) {
                    this.mIterStack.push(((Collection<V>) next).iterator());
                    this.mIndexStack.push(0);
                }
                else {
                    //this.mNextEntry = new KeyValue<>( this.mIndexStack.peek(), next );
                    this.updateNextEntryCursor( this.mIndexStack.peek(), (V)next );
                    this.updateIndex();
                    break;
                }
            }
            else {
                this.mIterStack.pop();
                this.mIndexStack.pop();
                this.updateIndex();
            }
        }
    }

    protected void updateNextEntryCursor( Object key, V value ) {
        if( this.mNextEntry == null ) {
            this.mNextEntry = new DummyEntry( key, value );
        }

        this.mNextEntry.apply( key, value );
    }

    protected void updateIndex() {
        if ( !this.mIndexStack.isEmpty() ) {
            int currentIndex = this.mIndexStack.pop();
            this.mIndexStack.push( currentIndex + 1 );
        }
    }

    @Override
    public boolean hasNext() {
        return this.mNextEntry != null;
    }

    @Override
    public Map.Entry<?, V > next() {
        if ( this.mNextEntry == null ) {
            throw new NoSuchElementException();
        }
        Map.Entry<?, V > result = this.mNextEntry;
        this.advance();
        return result;
    }

    class DummyEntry extends KeyValue<Object, V > {
        public DummyEntry( Object key, V value ) {
            super( key, value );
        }

        public void setKey( Object key ) {
            this.key = key;
        }

        public void apply( Object key, V value ) {
            this.key   = key;
            this.value = value;
        }
    }
}
