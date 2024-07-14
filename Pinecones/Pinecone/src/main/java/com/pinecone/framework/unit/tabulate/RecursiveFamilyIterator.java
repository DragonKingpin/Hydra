package com.pinecone.framework.unit.tabulate;

import com.pinecone.framework.unit.KeyValue;
import com.pinecone.framework.util.StringUtils;

import java.util.Map;
import java.util.Iterator;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.NoSuchElementException;

public class RecursiveFamilyIterator<V > implements FamilyIterator<V > {
    private Deque<Iterator<? > >         mIterStack;
    private Deque<Integer >              mIndexStack;
    private Deque<Object >               mParentKeyStack;
    private Deque<DummyFamilyNode >      mParentStack;
    private DummyFamilyNode              mNextNode;
    private boolean                      mbIncludeCollection;

    protected RecursiveFamilyIterator( boolean bIncludeCollection ) {
        this.mbIncludeCollection = bIncludeCollection;
        this.mIterStack          = new ArrayDeque<>();
        this.mIndexStack         = new ArrayDeque<>();
        this.mParentStack        = new ArrayDeque<>();
        this.mParentKeyStack     = new ArrayDeque<>();
        this.mIndexStack.push(0);
        this.mParentStack.push( new DummyFamilyNode( null, null ) ); // Deque don't accept null, using dummy.
        this.mParentKeyStack.push( "" );
    }

    public RecursiveFamilyIterator( Map<?, V > map, boolean bIncludeCollection ) {
        this( bIncludeCollection );
        this.mIterStack.push(map.entrySet().iterator());
        this.advance();
    }

    public RecursiveFamilyIterator( Map<?, V > map ) {
        this( map, true );
    }

    public RecursiveFamilyIterator( Collection<?> collection ) {
        this( true );
        this.mIterStack.push( collection.iterator() );
        this.advance();
    }

    @SuppressWarnings( "unchecked" )
    private void advance() {
        Object selfKey   = null;
        Object parentKey = null;
        this.mNextNode   = null;

        while ( !this.mIterStack.isEmpty() ) {
            Iterator<?>   iterator = this.mIterStack.peek();
            DummyFamilyNode parent = this.mParentStack.peek();
            parentKey = this.mParentKeyStack.peek();
            if( StringUtils.isEmpty( parentKey ) ) {
                parentKey = null;
            }
            if( parent != null ) {
                if( parent.getEntry() == null ) {
                    parent  = null;
                    selfKey = null;
                }
                else {
                    selfKey = parent.getEntry().getKey();
                }
            }

            if ( iterator.hasNext() ) {
                Object next = iterator.next();
                if ( next instanceof Map.Entry ) {
                    Map.Entry<?, ?> entry = (Map.Entry<?, ?>) next;
                    Object value = entry.getValue();
                    if ( value instanceof Map ) {
                        this.mIterStack.push(((Map<?, ?>) value).entrySet().iterator());
                        this.mIndexStack.push(0);

                        Object thisKey = entry.getKey();
                        this.mParentKeyStack.push( thisKey );
                        this.mParentStack.push( new DummyFamilyNode( parent, parentKey, thisKey, (V)entry.getValue() ) );
                    }
                    else if ( value instanceof Collection && this.mbIncludeCollection ) {
                        this.mIterStack.push(((Collection<?>) value).iterator());
                        this.mIndexStack.push(0);

                        Object thisKey = entry.getKey();
                        this.mParentKeyStack.push( thisKey );
                        this.mParentStack.push( new DummyFamilyNode( parent, parentKey, thisKey, (V)entry.getValue() ) );
                    }
                    else {
                        this.updateNextNodeCursor( parent, selfKey, entry.getKey(), (V)value );
                        this.updateIndex();
                        break;
                    }
                }
                else if ( next instanceof Map ) {
                    Object thisKey = this.mIndexStack.getFirst();
                    this.mParentKeyStack.push( thisKey );
                    this.mParentStack.push(  new DummyFamilyNode( parent, parentKey, thisKey, (V)next ) );
                    this.mIterStack.push(((Map<?, ?>) next).entrySet().iterator());
                    this.mIndexStack.push(0);
                }
                else if ( next instanceof Collection && this.mbIncludeCollection ) {
                    Object thisKey = this.mIndexStack.getFirst();
                    this.mParentKeyStack.push( thisKey );
                    this.mParentStack.push(  new DummyFamilyNode( parent, parentKey, thisKey, (V)next ) );
                    this.mIterStack.push(((Collection<?>) next).iterator());
                    this.mIndexStack.push(0);
                }
                else {
                    this.updateNextNodeCursor( parent, selfKey, this.mIndexStack.peek(), (V)next );
                    this.updateIndex();
                    break;
                }
            }
            else {
                this.mIterStack.pop();
                this.mIndexStack.pop();
                this.mParentStack.pop();
                this.mParentKeyStack.pop();
                this.updateIndex();
            }
        }
    }

    protected void updateNextNodeCursor( UnitFamilyNode<Object, V > parent, Object selfKey, Object key, V value ) {
        if ( this.mNextNode == null ) {
            this.mNextNode = new DummyFamilyNode( parent, selfKey, key, value );
        }
        else {
            this.mNextNode.apply( parent, selfKey, key, value );
        }
    }

    protected void updateIndex() {
        if ( !this.mIndexStack.isEmpty() ) {
            int currentIndex = this.mIndexStack.pop();
            this.mIndexStack.push(currentIndex + 1);
        }
    }

    @Override
    public boolean hasNext() {
        return this.mNextNode != null;
    }

    @Override
    public UnitFamilyNode<Object, V > next() {
        if ( this.mNextNode == null ) {
            throw new NoSuchElementException();
        }
        DummyFamilyNode result = this.mNextNode;
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

    class DummyFamilyNode implements UnitFamilyNode<Object, V > {
        UnitFamilyNode<Object, V > parent;
        Object                     selfKey;
        DummyEntry                 entry;

        public DummyFamilyNode( UnitFamilyNode<Object, V > parent, Object selfKey ) {
            this.entry   = null;
            this.parent  = parent;
            this.selfKey = selfKey;
        }

        public DummyFamilyNode( UnitFamilyNode<Object, V > parent, Object selfKey, Object entryKey, V entryValue ) {
            this.entry   = new DummyEntry( entryKey, entryValue );
            this.parent  = parent;
            this.selfKey = selfKey;
        }

        public void setKey( Object key ) {
            this.entry.setKey( key );
        }

        public void apply( UnitFamilyNode<Object, V > parent, Object selfKey, Object key, V value ) {
            this.parent  = parent;
            this.selfKey = selfKey;
            this.entry.setKey( key );
            this.entry.setValue( value );
        }

        @Override
        public UnitFamilyNode<Object, V > parent() {
            return this.parent;
        }

        @Override
        public Object getSelfKey() {
            return this.selfKey;
        }

        @Override
        public Map.Entry<Object, V > getEntry() {
            return this.entry;
        }

        @Override
        public String toString() {
            return this.toJSONString();
        }
    }
}