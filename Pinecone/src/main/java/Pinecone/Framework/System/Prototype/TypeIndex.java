package Pinecone.Framework.System.Prototype;


/**
 *  Pinecone For Java TypeIndex [ Runtime Smart Prototype Identity ]
 *  Copyright © 2008 - 2024 Bean Nuts Foundation ( DR.Undefined ) All rights reserved. [Mr.A.R.B / WJH]
 *  Tip:
 *  *****************************************************************************************
 *  Author: undefined
 *  Last Modified Date: 2021-03-13
 *  *****************************************************************************************
 *  For name: It's simple name of `class`.
 *  Full name should be considered as Namespace(PackageName) + SimpleName
 *  *****************************************************************************************
 */
public class TypeIndex {
    private Class<?> mAntetype        = null;

    private Class<?> mParent          = null;

    private Object   mThis            = null;

    private Object   mNext            = null;

    public TypeIndex( Object that ) {
        this.mParent   = that.getClass().getSuperclass();
        this.mThis     = that;
        this.mAntetype = that.getClass();
    }

    public TypeIndex setNext( Object that ){
        this.mNext = that;
        return this;
    }

    public Object getNext(){
        return this.mNext;
    }

    public TypeIndex prototype(){
        return this;
    }

    public Object proto(){
        return this.mThis;
    }

    public Class<?> parent(){
        return this.mParent;
    }

    public Class<?> antetype() {
        return this.mAntetype;
    }

    public String namespace()     {
        return Prototype.namespace( this.mAntetype );
    }

    public String name()     {
        return this.mAntetype.getSimpleName();
    }

    public String typeName(){
        return this.mAntetype.getName();
    }

    @Override
    public String toString(){
        return this.typeName();
    }

    @Override
    public boolean equals( Object that ) {
        if( that instanceof TypeIndex ){
            TypeIndex realThat = (TypeIndex)that;
            return this.mAntetype.equals( realThat.mAntetype ) ;
        }
        return false;
    }

}
