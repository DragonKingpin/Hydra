package com.others;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.NonNull;
import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.config.JSONConfig;
import com.sauron.heist.heistron.CascadeHeist;
import com.sauron.heist.heistron.Crew;
import com.sauron.heist.heistron.HTTPIndexHeist;
import com.sauron.heist.heistron.Heist;
import com.sauron.heist.heistron.Heistgram;
import com.sauron.heist.heistron.Heistotron;
import com.pinecone.ulf.util.lang.GenericPreloadClassInspector;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.bytecode.annotation.Annotation;

class FakeHeist extends HTTPIndexHeist {
    public FakeHeist( Heistgram heistron ){
        super( heistron );
    }

    public FakeHeist( Heistgram heistron, JSONConfig joConfig ){
        super( heistron, joConfig );
    }

    public FakeHeist(Heistgram heistron, @Nullable CascadeHeist parent, @NonNull String szChildName ) {
        super( heistron, parent, szChildName );
    }

    @Override
    public Crew newCrew(int nCrewId ) {
        return null;
    }

    @Override
    public void toRavage(){
        super.toRavage();
    }

    @Override
    public void toStalk(){

    }
}

public class TestServgramRadium {
    public static void testJavassist() throws Exception{
        GenericPreloadClassInspector inspector = new GenericPreloadClassInspector( ClassPool.getDefault() );


        String className = "com.sauron.heist.heistron.Heistotron";
        Debug.trace( inspector.isImplementedDirectly( className, Heistgram.class ) );
        Debug.trace( inspector.isImplemented( className, com.pinecone.hydra.servgram.Servgram.class ) );

        Debug.trace( inspector.isExtendedDirectly( Heistgram.class.getName(), com.pinecone.hydra.servgram.Servgram.class ) );
        Debug.trace( inspector.isExtended( Heistgram.class.getName(), com.pinecone.framework.system.executum.Processum.class ) );

        Debug.trace( inspector.hasOwnAnnotations( Heistotron.class.getName(), new Class[]{ com.pinecone.hydra.servgram.Gram.class } ) );

        Debug.trace( inspector.isImplemented( com.others.FakeHeist.class.getName(), Heist.class /*com.pinecone.framework.system.prototype.Pinenut.class*/ ) );

        Debug.trace( inspector.isExtended( com.others.FakeHeist.class.getName(), Heist.class ) );

        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get(className);
        Debug.trace( ctClass.isFrozen() ) ;

        Annotation[] annotations = inspector.queryVisibleAnnotations( ctClass );
        Debug.echo( annotations[0].getMemberValue( "value" ) );

    }

    public static void main(String[] args) throws Exception {
        // String szJson = FileUtils.readAll("J:/120KWordsPhonetics.json5");
        Pinecone.init((Object... cfg) -> {

            // TestServgramRadium.testClassReader();
            TestServgramRadium.testJavassist();


            return 0;
        }, (Object[]) args);
    }
}
