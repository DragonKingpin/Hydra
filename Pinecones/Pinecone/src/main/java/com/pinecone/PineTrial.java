package com.pinecone;

import com.pinecone.framework.util.Debug;
//import opennlp.tools.ml.maxent.DataStream;
//import org.glassfish.jersey.server.internal.scanning.FilesScanner;
//import sun.misc.FloatingDecimal;
//import sun.nio.ch.WindowsSelectorImpl


class SS implements Runnable {
    public int i = 0;
    @Override
    public void run() {
        for ( int j = 0; j < 1e4; j++ ) {
            //++i;
            Debug.trace( Thread.currentThread().getName(), j );
        }
    }
}



public class PineTrial {

    public static boolean test(int n){
        if (n<2){
            return false;
        }
        int z = (int)Math.sqrt(n);
        for (int i = 2; i <= z; i++) {
            if (n%i == 0){
                return false;
            }
        }
        return true;
    }

    public static Integer pre(int n) {
        int temp = 0;
        while (n > 0) {
            temp = temp * 10 + (n % 10);
            n = n / 10;
        }
        return temp;
    }


    public static void main( String[] args ) throws Exception {
        //String szJson = FileUtils.readAll("J:/120KWordsPhonetics.json5");
        Pinecone.init( (Object...cfg )->{
//            for (int i = 11; i < 100_000_000_0; i++) {
//                if (i == pre(i) && test(i) ){
//                    System.out.println(i);
//                }
//            }






//            MySQLExecutor mysql = new MySQLExecutor( new MySQLHost( "localhost/predator", "root", "test", "UTF-8" ) );
            //JSONArray tables = mysql.fetch( "SELECT tM.en_word, tM.coca_rank FROM( SELECT tW.en_word, tF.coca_rank FROM predator_mutual_words AS tW LEFT JOIN predator_mutual_words_frequency AS tF ON tW.en_word = tF.en_word WHERE LENGTH(tW.en_word) = 3 ) AS tM WHERE tM.coca_rank IS NOT NULL AND tM.coca_rank <= 20000 AND tM.coca_rank != 0 ORDER BY tM.coca_rank;" );
//            JSONArray tables = mysql.fetch( "SELECT tM.en_word, tM.coca_rank FROM( SELECT tW.en_word, tF.coca_rank FROM predator_mutual_words AS tW LEFT JOIN predator_mutual_words_frequency AS tF ON tW.en_word = tF.en_word WHERE LENGTH(tW.en_word) >= 3 AND LENGTH(tW.en_word) <= 5 ) AS tM WHERE tM.coca_rank IS NOT NULL AND tM.coca_rank <= 15000 AND tM.coca_rank != 0 ORDER BY tM.coca_rank;" );
//
//            JSONArray words = new JSONArraytron();
//            for ( int i = 0; i < tables.size(); i++ ) {
//                String szWord = tables.optJSONObject(i).optString( "en_word" );
//                //if( szWord.charAt(0) >= 'a' ) {
//                    words.put( szWord );
//                //}
//            }
//
//            Debug.trace( words );



            SS runnable = new SS();

            Thread t1 = new Thread( runnable );
            Thread t2 = new Thread( runnable );


            t1.start();

            t2.start();

            Thread.sleep( 100 );

            Debug.trace( runnable.i );












//            Debug.trace( ( (Framework)Pinecone.sys().getTaskManager().summon(
//                    Framework.class.getName(),
//                    new Class<?>[]{ String[].class, PrimeSystem.class },
//                    (Object[]) new String[0], Pinecone.sys()
//            ) ).getName() );


//            ReentrantLock lock = new ReentrantLock();
//            Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
////                    for ( int i = 0;  i < 1e6;  i++ ) {
////                        lock.lock();
////                        Debug.trace( i );
////                        lock.unlock();
////                    }
//                    Thread thread2 = new Thread(()->{
//                        //ThreadGroup parentThreadGroup = Thread.currentThread().getThreadGroup().getParent();
//                       // Debug.trace( Thread.currentThread().getId(), parentThreadGroup. )
//                        Thread.currentThread().getThreadGroup().list();
//
//                    });
//                    thread2.start();
//                }
//            };
//
//            Thread thread1 = new Thread(runnable);
//            thread1.start();
//
//            Thread thread2 = new Thread(runnable);
//            thread2.start();




//            LinkedTreeMap<Integer, Integer > linkedTreeMap = new LinkedTreeMap<>();
//            LinkedHashSet<Integer > linkedTreeSet = new LinkedHashSet<>();
//            for ( int i = 0; i < 1e6; i++ ) {
//                int j = new Random().nextInt((int)1e6);
//                linkedTreeMap.put( j, i );
//                linkedTreeSet.add(j);
//            }
//
//
//
////            for ( Integer i : linkedTreeSet ) {
////                linkedTreeMap.remove(i);
////            }
//            Integer[] arr = linkedTreeSet.toArray( new Integer[0] );
//
//            int len = linkedTreeMap.size();
//            for ( int i = 0; i < len -20; i++ ) {
//                //linkedTreeMap.remove( arr[i] );
//                linkedTreeMap.removeFirst();
//            }
//
//
//            int i = 0;
//            for ( Object kv : linkedTreeMap.entrySet()  ) {
//                ++i;
//            }
//
//            Debug.trace( linkedTreeMap.size(), i, linkedTreeMap );




//            Thread.sleep( 100000 );


//            for ( Map.Entry<String, String> kv : treeMap ) {
//                Debug.trace( kv );
//            }



//            Debug.trace( JSON.parse( FileUtils.readAll("E:\\MyFiles\\CodeScript\\Project\\Hazelnut\\Sauron\\Saurons\\system\\setup\\PubChem.json5") ) );

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


//            Debug.trace( system.getProperty("user.dir") );
//
//            HostMatrix illuminationSystem = new HostMatrix("E:/MyFiles/CodeScript/Project/Hazelnut/Predator/Predator/src/Resources/","config.json5");
//
//            //Debug.trace( illuminationSystem.getSystemConfig() );

//            String szJson = FileUtils.readAll("J:/120KWordsPhonetics.json5");
//            JSONObject jsonShit = new JSONMaptron(szJson);


            ///predator_en_w_etymon_derived_linguae
            /*MySQLExecutor mysql = new MySQLExecutor( new MySQLHost( "localhost/predator", "root", "test", "UTF-8" ) );
            JSONArray tables = mysql.fetch( "SELECT * FROM predator_en_w_etymon_derived_linguae" );


            for ( Object obj : tables) {
                JSONObject row = (JSONObject)obj;

                row.put( "nation", new JSONArraytron() );
            }

            FileWriter fileWriter = new FileWriter( "M:/etymon_derived_linguae.json" );
            tables.write( fileWriter );
            fileWriter.close();*/


/*            MySQLExecutor mysql = new MySQLExecutor( new MySQLHost( "localhost/predator", "root", "test", "UTF-8" ) );
            JSONArray tables = mysql.fetch( "SELECT * FROM predator_mutual_words_frequency" );

            JSONObject jMap  = new JSONMaptron();
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
//            system.out.println( arrayList.size() );
//
//            system.gc();
//            system.gc();
//            system.gc();
//            system.gc();
//

//            long nMem = (long)( (double)1 * 1024 * 1024 * 1024 );
//            byte[][] magnChars = new byte[8][];
//            magnChars[0]  = new byte[ (int) nMem  ];
//            magnChars[1] = new byte[ (int) nMem  ];
//            magnChars[2] = new byte[ (int) nMem  ];
//            magnChars[3] = new byte[ (int) nMem  ];
//
//
//            Debug.trace( nMem,"Done", framework.getRunTime() );
//
//            Thread.sleep(1000000);





            return 0;
        }, (Object[]) args );
    }
}


