package Pinecone;
import Pinecone.Framework.Debug.Debug;
import Pinecone.Framework.System.Framework;
import Pinecone.Framework.System.Functions.*;
import Pinecone.Framework.System.IO.Console;
import Pinecone.Framework.System.Prototype.Prototype;
import Pinecone.Framework.System.util.PackageUtils;
import Pinecone.Framework.Unit.UnitUtils;
import Pinecone.Framework.Util.File.FileUtils;
import Pinecone.Framework.Util.JSON.*;
import Pinecone.Framework.Util.JSON.Hometype.JSONReactor;
import Pinecone.Framework.Util.JSON.Hometype.JavaifyReactor;
import Pinecone.Framework.Util.RDB.MySQL.MySQLExecutor;
import Pinecone.Framework.Util.RDB.MySQL.MySQLHost;
import Pinecone.Framework.Util.Random.SeniorRandom;
import Pinecone.Framework.Util.Summer.http.HttpURLParser;
//import opennlp.tools.ml.maxent.DataStream;
//import org.glassfish.jersey.server.internal.scanning.FilesScanner;
//import sun.misc.FloatingDecimal;

import javax.xml.stream.events.Characters;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.FileSystem;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

/**
 *  Pinecone Framework For Java (= Bean Nuts Pinecone Pinecone Java )
 *  Copyright © 2008 - 2024 Bean Nuts Foundation ( DR.Undefined ) All rights reserved. [Mr.A.R.B / WJH]
 *  Open Source licensed under the GPL. No Commercial Use.
 *  Tip:
 *  *****************************************************************************************
 *  Other information about this framework, such as papers, patents, etc -> http://www.rednest.cn
 *  Warning: This source code is protected by copyright law and international treaties.
 *  *****************************************************************************************
 *  Code by Mr.A.R.B / WJH [ www.nutgit.com/ www.xbean.net / www.rednest.cn ]
 *  Include Almond, C/CPP, JAVA, PHP, Python, JavaScript, ActionScript, VB[Basic], E, Common Lisp
 *  *****************************************************************************************
 *  ;) Hope you enjoy this
 */

class ThreadImpl extends Thread {

    public Object lock;

    public ThreadImpl(Object lock) {
        this.lock = lock;
    }

    //  重写线程的run方法，线程的业务执行逻辑
    @Override
    public void run() {

        synchronized (lock) {
            for (int i = 0; i < 3; i++) {
                System.out.println(i);
            }

            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 3; i < 5; i++) {
                System.out.println(i);
            }

            lock.notifyAll();
        }
    }

    public void no(){
        synchronized (lock) {
            lock.notifyAll();
        }
    }
}

public class Pinecone {
    public static final long    VER_PINE        =  202406L;
    public static final String  VERSION         = "3.3.1";
    public static final String  RELEASE_DATE    = "2024/06/06";
    public static final String  ROOT_SERVER     = "http://www.rednest.cn/";
    public static final String  MY_PROGRAM_NAME = "Pinecone";              // Define your custom program name.
    public static final String  CONTACT_INFO    = "E-Mail:arb#rednest.cn"; // Giving your contact information, if this program interrupt abnormally.
    public static final boolean S_DEBUG_MODE    = true;
    public static final int FLOAT_ACCURACY     = 32;
    public static final int COMMON_ACCURACY_LIMIT = 10000;




    public static void main(String[] args) throws Exception {
        //String szJson = FileUtils.readAll("J:/120KWordsPhonetics.json5");
        Framework.inti( (Object...cfg )->{

//            String packageName = "Predator.Wizard.Public.undefined";
//            // List<String> classNames = getClassName(packageName);
//            List<String> classNames = PackageUtils.fetchClassName( packageName );
//            if (classNames != null) {
//                for ( String className : classNames ) {
//                    className = className.substring( className.indexOf(packageName) );
//                    Class<?> pVoid = Thread.currentThread().getContextClassLoader().loadClass( className );
//                    Debug.trace(pVoid.getAnnotations());
//                }
//            }


//            Debug.trace( System.getProperty("user.dir") );
//
//            HostMatrix illuminationSystem = new HostMatrix("E:/MyFiles/CodeScript/Project/Hazelnut/Predator/Predator/src/Resources/","config.json5");
//
//            //Debug.trace( illuminationSystem.getSystemConfig() );

//            String szJson = FileUtils.readAll("J:/120KWordsPhonetics.json5");
//            JSONObject jsonShit = new JSONObject(szJson);


            ///predator_en_w_etymon_derived_linguae
            /*MySQLExecutor mysql = new MySQLExecutor( new MySQLHost( "localhost/predator", "root", "test", "UTF-8" ) );
            JSONArray tables = mysql.fetch( "SELECT * FROM predator_en_w_etymon_derived_linguae" );


            for ( Object obj : tables) {
                JSONObject row = (JSONObject)obj;

                row.put( "nation", new JSONArray() );
            }

            FileWriter fileWriter = new FileWriter( "M:/etymon_derived_linguae.json" );
            tables.write( fileWriter );
            fileWriter.close();*/


/*            MySQLExecutor mysql = new MySQLExecutor( new MySQLHost( "localhost/predator", "root", "test", "UTF-8" ) );
            JSONArray tables = mysql.fetch( "SELECT * FROM predator_mutual_words_frequency" );

            JSONObject jMap  = new JSONObject();
            for ( Object obj : tables) {
                JSONObject row = (JSONObject)obj;

                String szWord = row.optString( "en_word" );

                jMap.affirmArray( szWord ).put( row );
            }

            FileWriter fileWriter = new FileWriter( "M:/dv/mutual_words_frequency.json" );
            tables.write( fileWriter );
            fileWriter.close();*/







//            ArrayList<String > arrayList = new ArrayList<>();
//            for ( int i = 0; i < 1e7; i++ ) {
//                arrayList.add( new String( new char[4] ) );
//            }
//
//            System.out.println( arrayList.size() );
//
//            System.gc();
//            System.gc();
//            System.gc();
//            System.gc();
//

//            long nMem = (long)( (double)1 * 1024 * 1024 * 1024 );
//            byte[][] magnChars = new byte[8][];
//            magnChars[0]  = new byte[ (int) nMem  ];
//            magnChars[1] = new byte[ (int) nMem  ];
//            magnChars[2] = new byte[ (int) nMem  ];
//            magnChars[3] = new byte[ (int) nMem  ];
//
//
//            Debug.trace( nMem,"Done", Framework.getRunTime() );
//
//            Thread.sleep(1000000);






//            Shit shit = new Shit();
//
//            JSONObject jsonShit = new JSONObject("{ name:'shit',  'fuck':7, 'length': 1, 'array':[{name:'shit',length:1998}]}");
//            Debug.trace( jsonShit );
//
//            JSONArray jsonArray = jsonShit.optJSONArray( "array" );
//
//            shit = (Shit) ( new JavaifyReactor(  true,Shit.class ) ).javaify( jsonShit );
//            Debug.trace( shit );


            return 0;
        } );
    }
}

class Dick {
    public  String    mName ;
    public  long      mLength;
    public  int       emnus;

    public Dick( long      length ) {
        this.mLength = length;
    }
}

class Shit{
    public  String    mName ;
    public int        length;
    public JSONArray  array;

    public Shit(  ){

    }

//    public Object getName() {
//        return this.mName;
//    }

    public Object test( int i, Integer c, String sz ) {
        return sz + i + c;
    }

    public Object trial( Object... arg ){
        return arg;
    }


    public String toJSONString() {
        return JSONReactor.jsonify( this ).toString();
    }

    public String toString(){
        return JSONReactor.jsonify( this ).toString();
    }

}