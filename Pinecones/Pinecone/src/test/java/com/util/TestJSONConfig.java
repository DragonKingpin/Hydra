package com.util;

import com.pinecone.Pinecone;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.config.JSONConfig;
import com.pinecone.framework.util.json.*;

import java.nio.file.Path;

public class TestJSONConfig {
    public static void test_JC1( )  {
        JSONConfig json = new JSONConfig( new JSONMaptron( "{ self:selfV, num:1234, sch:{ n: name, ssch: { n:n1  } } }" )  );
        json.addGlobalScope(  new JSONMaptron("{ satan: 'Satan', jesus: 'Jesus', obj:{ k=sss, f=sxf } }")  );
        json.addGlobalScope(  new JSONMaptron("{ f1: 'Satan', f2: 'Jesus', f3:{ k=fsss, f=s13xf } }")  );

        Debug.trace( json.optJSONObject( "f3" ), json.opt( "num" ), json.optJSONObject( "sch" ) );
        Debug.trace( json );

        JSONConfig sch = json.getChild( "sch" );
        Debug.trace( sch, sch.optJSONObject( "f3" ), sch.opt( "satan" ), sch.optJSONObject( "ssch" ) );

        JSONConfig ssch = json.getChild( "ssch" );
        Debug.trace( ssch, ssch.opt( "f2" ), sch.opt( "obj" ), sch.opt( "n" ) );

    }

    public static void test_JC( )  {
        JPlusContext context = new JPlusContext();
        context.addParentPath(Path.of("E:/MyFiles/CodeScript/Project/Hazelnut/Sauron/Saurons/Pinecone/src/test/java"));
        context.setOverriddenAffinity( true );
        context.addGlobalScope( new JSONMaptron("{ satan: 'Satan', jesus: 'Jesus' }") );
        //context.addGlobalScope( new JSONMaptron("{ this: { key:'TakeOver' } }") );

        JSONObject obj = (JSONObject) JPlus.parse(
                " { ro = 'root', next = { p = 'parent', po1: { kp:true, int = 9 }, pa:[9,9.01,6]," +
                        "next : { #extends super.po1, int = 7, str: &this.int,  end:xxxx, obj:{a:1, h:&this.a}, obj2:{/*#extends super.obj*/ h:&super.obj} ,inc:#include \"./com/util/inc.jplus\" /**/ }, " +
                        "arr:[ #extends 'super.pa', 1, &'this[1]', null, 'fuck' ]/**/ } }", context ) ;

        Debug.echo( obj.toJSONStringI(4) );




    }

    public static void test_Dictionary( )  {
        JSONObject object = new JSONMaptron( "{ satan: 'Satan', jesus: 'Jesus' }" );
        JSONDictium dictium = object;

        for ( Object o : dictium.entrySet() ) {
            Debug.trace( o.toString() );
        }

        JSONArray array = new JSONArraytron( "[0,1,2,3,4,5,6,7,8,9]" );
        dictium = array;

        for ( Object o : dictium.entrySet() ) {
            Debug.trace( o );
        }

        Debug.trace( dictium.optInt( "31s" ) );

    }

    public static void main( String[] args ) throws Exception {
        //String szJson = FileUtils.readAll("J:/120KWordsPhonetics.json5");
        Pinecone.init( (Object...cfg )->{

            //TestUnits.testUniScopeMap();


            //TestJSONConfig.test_JC1();
            //TestJSONConfig.test_Dictionary();
            //Debug.trace( ( new URI( "/ssss" ) ) );


            return 0;
        }, (Object[]) args );
    }
}
