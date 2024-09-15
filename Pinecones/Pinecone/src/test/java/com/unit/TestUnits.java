package com.unit;
import com.pinecone.Pinecone;
import com.pinecone.framework.unit.*;
import com.pinecone.framework.unit.tabulate.*;
import com.pinecone.framework.unit.trie.TrieReparseNode;
import com.pinecone.framework.unit.trie.UniTrieMaptron;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JSONMaptron;

import java.util.*;

@SuppressWarnings( "unchecked" )
public class TestUnits {
    public static void testUniScopeMap() {
        UniScopeMaptron map2 = new UniScopeMaptron(new JSONMaptron("{c1:'kc1', c2:'kc2', c3:'kc3'}"));
        UniScopeMaptron map1 = new UniScopeMaptron(new JSONMaptron("{c1:'kb1', b2:'kb2', b3:'kb3'}"), map2);
        UniScopeMaptron map  = new UniScopeMaptron(true, map1);
        //map.put( "ka1", "a1" );


        for ( Object o: map.scopeEntrySet() ) {
            Map.Entry kv = (Map.Entry) o;
            Debug.trace( kv.getKey(), kv.getValue() );
        }


        Debug.trace( map.get("c1") );
        Debug.trace( map.get("b2") );
        Debug.trace( map.get("d3") );

        LinkedHashMap ls = new LinkedHashMap();
        ls.put( "g1", "gg" );

        map.elevate( ls );

        LinkedHashMap linkedHashMap = new LinkedHashMap();
        map.overrideTo( linkedHashMap );



        Debug.trace( linkedHashMap, map.isEmpty(), map.isScopeEmpty(), map.ancestors(), map.hasOwnProperty("g1"), map.hasOwnProperty("kc2") );
    }

    public static void testMultiScopeMap() {


        MultiScopeMaptron map4_0 = new MultiScopeMaptron(new JSONMaptron("{e1:'ke1', e2:'ke2'}"));

        MultiScopeMaptron map3_0 = new MultiScopeMaptron(new JSONMaptron("{d1:'kd1', c1:'kd1'}"));
        map3_0.addParent( new MultiScopeMaptron() );
        map3_0.addParent( ( new MultiScopeMaptron() ).addParent( map4_0 ) );

        MultiScopeMaptron map2_0 = new MultiScopeMaptron(new JSONMaptron("{c1:'kc1', c2:'kc2', c3:'kc3'}"), null, "jesus");
        MultiScopeMaptron map2_1 = new MultiScopeMaptron(new JSONMaptron("{c11:'kc11'}"));
        map2_1.addParent(map3_0);
        MultiScopeMaptron map1 = new MultiScopeMaptron(new JSONMaptron("{c1:'kb1', b2:'kb2', b3:'kb3'}"));
        map1.addParent( map2_0 ).addParent( map2_1 );
        MultiScopeMaptron map = new MultiScopeMaptron(true, null);

        map.addParent( map1 );
        map.put( "fuck", "me" );
        map.put( "fuck2", "this" );

        //map.put( "ka1", "a1" );

        Debug.trace( map.scopes() );


        for ( Object o: map.scopeEntrySet() ) {
            Map.Entry kv = (Map.Entry) o;
            Debug.trace( kv.getKey(), kv.getValue() );
        }



        Debug.trace( map.getAll( "c1" ), map.query( "c1","jesus" ) );


        Debug.trace( map.get("c1") );
        Debug.trace( map.get("b2") );
        Debug.trace( map.get("d3") );

        LinkedHashMap ls = new LinkedHashMap();
        ls.put( "g1", "gg" );

        map.elevate( ls );

        LinkedHashMap linkedHashMap = new LinkedHashMap();
        map.overrideTo( linkedHashMap );


        Debug.trace( linkedHashMap, map.isEmpty(), map.isScopeEmpty(), map.hasOwnProperty("g1"), map.hasOwnProperty("kc2") );
    }

    public static void testPrecedeMultiMap() {
        MultiScopeMaptron map1_0 = new MultiScopeMaptron(new JSONMaptron("{p1:'kp1', p2:'kp2'}"));

        PrecedeMultiScopeMap p = new PrecedeMultiMaptron();
        p.addParent( map1_0 );
        p.put( "this1", "this1" );
        p.put( "this" , "this is this" );

        MultiScopeMaptron mapKeyWord = new MultiScopeMaptron(new JSONMaptron("{this:'this is keyword', super:'super is keyword'}"));
        p.setPrecedeScope( mapKeyWord );

        Debug.trace( p, p.get( "p1" ), p.get( "this" ) );
    }

    public static void testRecursiveEntryIterator() {
        Map<String, Object> map = new JSONMaptron( "{ k1:v1, k2:v2, k3: { k3_1:v3_1, k3_2:v3_2, li:[ 0,1,2,3, { lk1: vlk1, lk2:vlk2  } ] }, k3_4: v3_4  }" );
        //Map<String, Object> map = new JSONMaptron( "{ k1:v1, li:[ 0, { lk1: vlk1, lk2:vlk2  } ] }, k3_4: v3_4  }" );

        RecursiveFamilyIterator<Object> iterator = new RecursiveFamilyIterator<>( map, true );
        //RecursiveEntryIterator iterator = new RecursiveEntryIterator( map, true );

        TypedNamespaceFamilyEntryNameEncoder encoder = new TypedNamespaceFamilyEntryNameEncoder();
        while ( iterator.hasNext() ) {
            UnitFamilyNode node = iterator.next();
            //Debug.trace( node, node.parent(), node.namespacify( true ) );

            Debug.trace( node, node.parent(), encoder.encodeNS( node, true ).getFullName(), node.namespacify( true ) );

//            if( node.parent() != null ) {
//                Debug.trace( "K", node.parent().parent() );
//            }
        }
//        while ( iterator.hasNext() ) {
//            Map.Entry node = iterator.next();
//            Debug.trace( node );
//        }

        iterator = new RecursiveFamilyIterator<>( map, true );
        GenericCollectedEntryEncoder entryEncoder = new GenericCollectedEntryEncoder( iterator );
        Collection collection = entryEncoder.encode();

        Debug.trace( collection );
        Debug.trace( map );


        GenericCollectedEntryDecoder decoder = new GenericCollectedEntryDecoder<>();
        Map<String, Object> decoded = decoder.decode(collection);
        Debug.trace(decoded);

        iterator = new RecursiveFamilyIterator<>( map, true );
        entryEncoder = new GenericCollectedEntryEncoder( iterator );
        Map map1 = entryEncoder.regress();
        Debug.trace( map1 );


        decoded = decoder.evolve( map1 );
        Debug.trace(decoded);
    }


    public static void testMergeSharedList(){
        List<String> list1 = new ArrayList<>(List.of("a", "b", "c","d","e"));
        List<String> list2 = new ArrayList<>(List.of("X", "D", "F","X","Y"));
        System.out.println("list1: "+list1);
        System.out.println("list2: "+list2);

        SharedList<String> mergeList = SharedList.SharedListBuilder.merge(list1, list2);

        System.out.println("merge list1 and list2 : "+ mergeList);

        SharedList<String> slice = SharedList.SharedListBuilder.slice(2, 6, mergeList);
        System.out.println("slice mergeList from 2 to 6 : "+ slice);

        SharedList<String> merge2List = SharedList.SharedListBuilder.merge(list1, list2, slice);
        System.out.println("merge list1 and list2 and slice : "+ merge2List);
        System.out.println("merge2 get index 1: "+ merge2List.get(1));

        SharedList<String> subList = merge2List.subList(4, 5);
        System.out.println("merge2 subList from 4 to 5 :"+ subList);

        subList.set(1, "hello");
        System.out.println("sublist after set sublist index 1 to hello: "+subList);
        System.out.println("merge2 after set sublist index 1 to hello: "+merge2List);
    }

    public static void testTrieMap() {
        UniTrieMaptron<String, String> trieMap = new UniTrieMaptron<>();

        // 插入一些键值对
        trieMap.put("animals/dogs/breed1", "Breed One");
        trieMap.put("animals/dogs/breed2", "Breed Two");
        trieMap.put("animals/cats/breed1", "Breed Three");
        trieMap.put("animals/cats/breed2", "Breed Four");
        trieMap.put("animals/cats/breed3", "Breed Five");
        TrieReparseNode<String, String> objectObjectTrieReparseNode = new TrieReparseNode<>("animals/cats/breed1",trieMap);
        trieMap.putReference("animals/cats/breed4", objectObjectTrieReparseNode);
        // 获取值
        System.out.println("get(\"animals/dogs/breed1\"): " + trieMap.get("animals/dogs/breed1")); // 应该输出Breed One
        System.out.println("get(\"animals/cats/breed3\"): " + trieMap.get("animals/cats/breed4")); // 应该输出Breed Five

        // 检查是否存在键
        System.out.println("containsKey(\"animals/dogs/breed1\"): " + trieMap.containsKey("animals/dogs/breed1")); // 应该输出true
        System.out.println("containsKey(\"animals/dogs/breedX\"): " + trieMap.containsKey("animals/dogs/breedX")); // 应该输出false

        // 检查是否包含值
        System.out.println("containsValue(\"Breed One\"): " + trieMap.containsValue("Breed One")); // 应该输出true
        System.out.println("containsValue(\"Unknown Breed\"): " + trieMap.containsValue("Unknown Breed")); // 应该输出false

        // 移除键值对
        System.out.println("remove(\"animals/cats/breed3\"): " + trieMap.remove("animals/cats/breed3")); // 应该输出Breed Five
        System.out.println("size after removing breed3: " + trieMap.size()); // 应该输出4

        // 获取所有键
        Set<String> keys = trieMap.keySet();
        System.out.println("keys: " + keys);

        // 获取所有值
        Collection<String> values = trieMap.values();
        System.out.println("values: " + values);

        // 获取所有键值对
        Set<Map.Entry<String, String>> entries = trieMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }

        // 清空前缀树
        trieMap.clear();
        System.out.println("isEmpty after clearing: " + trieMap.isEmpty()); // 应该输出true
    }

    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{

            //TestUnits.testUniScopeMap();
            //TestUnits.testMultiScopeMap();
            //TestUnits.testPrecedeMultiMap();
            //TestUnits.testRecursiveEntryIterator();
            //TestUnits.testMergeSharedList();
            TestUnits.testTrieMap();

            return 0;
        }, (Object[]) args );
    }
}