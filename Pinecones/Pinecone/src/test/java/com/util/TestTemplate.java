package com.util;

import com.pinecone.Pinecone;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JPlus;
import com.pinecone.framework.util.json.JPlusContext;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.template.TemplateParser;

import java.nio.file.Path;

public class TestTemplate {
    public static void test_UTL( )  {
        TemplateParser templateParser = new TemplateParser( "122 ${arr[1].f['k']} ${ key[ key['g'].c ] } sdd", ( new JSONMaptron( "{ g1:'k', key:{  g:{ c:'k' },k:'1xxxx2' }, arr:[1,{f:{k:'sss'}},3] }" ) ).getMap() );
        Debug.trace( templateParser.eval() );
    }

    public static void test_JPlus( )  {
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

    public static void main( String[] args ) throws Exception {
        //String szJson = FileUtils.readAll("J:/120KWordsPhonetics.json5");
        Pinecone.init( (Object...cfg )->{

            //TestUnits.testUniScopeMap();


            //TestTemplate.test_UTL();
            TestTemplate.test_JPlus();


            return 0;
        }, (Object[]) args );
    }
}