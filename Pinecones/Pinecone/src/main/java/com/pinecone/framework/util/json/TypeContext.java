package com.pinecone.framework.util.json;

import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.Deque;

public final class TypeContext {

    static final class Frame {
        final Type mContainerType;
        final Type mElementType;

        Frame( Type containerType, Type elementType ) {
            this.mContainerType = containerType;
            this.mElementType   = elementType;
        }
    }

    private final Deque<Frame> mStack = new ArrayDeque<>();

    public void push( Type containerType, Type elementType ) {
        this.mStack.push( new Frame( containerType, elementType ) );
    }

    public void pop() {
        if ( !this.mStack.isEmpty() ) {
            this.mStack.pop();
        }
    }

    public Frame peek() {
        return this.mStack.peek();
    }

    public boolean isEmpty() {
        return this.mStack.isEmpty();
    }
}