package Pinecone.Framework.Util.JSON.Hometype;

import Pinecone.Framework.System.Functions.Executable;
import Pinecone.Framework.System.Functions.FunctionTraits;
import Pinecone.Framework.System.Functions.Invokable;
import Pinecone.Framework.System.Hometype.ChiralPair;
import Pinecone.Framework.System.Prototype.Prototype;
import Pinecone.Framework.System.Prototype.TypeIndex;
import Pinecone.Framework.Util.JSON.JSON;
import Pinecone.Framework.Util.JSON.JSONArray;
import Pinecone.Framework.Util.JSON.JSONObject;


public class JSONType implements ChiralPair, Invokable {
    private Object   mJsonProto;

    private Object   mJavaObject;

    private void assertSelf( Object that ) {
        if( that instanceof JSONType ){
            throw new IllegalArgumentException( "The snake that eats its own tail, forever and ever." );
        }
    }

    private void accept( Object that ) {
        this.mJsonProto  = that;
        this.mJavaObject = that;
    }

    public JSONType ( JSONType that ){
        this.mJsonProto  = that.mJsonProto;
        this.mJavaObject = that.mJavaObject;
    }

    public JSONType ( Number that ){
        this.accept( that );
    }

    public JSONType ( Boolean that ){
        this.accept( that );
    }

    public JSONType ( String that ){
        this.accept( that );
    }

    public JSONType ( JSONObject that ){
        this.accept( that );
    }

    public JSONType ( JSONArray that ){
        this.accept( that );
    }

    public JSONType ( Executable that ) {
        this.accept( that );
    }

    public JSONType ( Object that ) {
        this.assimilate( that );
    }

    @Override
    public Class conjugated() {
        return this.mJavaObject.getClass();
    }

    @Override
    public boolean isHomogeneity( Object that ) {
        return  JSONReactor.trialHomogeneity( that );
    }

    @Override
    public boolean isHomotypic() {
        return this.mJavaObject == this.mJsonProto;
    }

    @Override
    public Object assimilate( Object that ) {
        if( that instanceof JSONType ){
            this.mJsonProto  = ( (JSONType)that ).mJsonProto;
            this.mJavaObject = ( (JSONType)that ).mJavaObject;
        }
        else if( this.isHomogeneity( that ) ){
            this.accept( that );
        }
        else {
            this.mJsonProto  = JSONReactor.jsonify( that );
            this.mJavaObject = that;
        }
        return this;
    }

    @Override
    public Class dominating() {
        return JSONType.class;
    }

    @Override
    public Class dominated() {
        return this.conjugated();
    }

    @Override
    public TypeIndex prototype() {
        return Prototype.typeid( this );
    }

    @Override
    public String prototypeName() {
        return this.getClass().getSimpleName();
    }


    @Override
    public Object getLeft(){
        return this.mJsonProto;
    }

    @Override
    public Object getRight(){
        return this.mJavaObject;
    }


    @Override
    public Object invoke( Object... obj ) throws Exception {
        if( this.mJsonProto instanceof Executable ) {
            return FunctionTraits.invoke( (Executable) this.mJsonProto, obj );
        }
        throw new IllegalStateException( "Json prototype is not function or executor." );
    }



    @Override
    public String toJSONString() {
        return JSON.stringify( this.mJsonProto );
    }

    @Override
    public String toString() {
        return this.mJsonProto.toString();
    }

    @Override
    public int hashCode(){
        return this.mJsonProto.hashCode();
    }

    @Override
    public boolean equals( Object that ){
        if( that instanceof JSONType ){
            return this.mJsonProto.equals( ( (JSONType) that ).mJsonProto );
        }
        return false;
    }
}
