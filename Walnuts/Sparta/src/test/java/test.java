import com.pinecone.framework.util.Debug;

import com.pinecone.hydra.registry.entity.GenericConfNode;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(locations = { "classpath:uid/default-uid-spring.xml" })
public class test {
    @Test
    public void test(){
//       UidGenerator uidGenerator= UUIDBuilder.getBuilder();
//       for (int i=0;i<4;i++){
//           GUID guid72 = uidGenerator.getGUID72();
//           System.out.println(guid72.toString());
//       }

//       for (int i=0;i<6;i++){
//           long uid = uidGenerator.getUID();
//           System.out.println(uidGenerator.getUUID(uid));
//       }
//        String guid721 = uidGenerator.getGUID72();
//        System.out.println(guid721);
//        GUID72 guid72 = new GUID72();
//        Identification parse = guid72.parse(guid721);
//        System.out.println(parse.toString());
//        GUID72 guid72 = new GUID72();
//        Debug.trace(guid72);
//        GenericDynamicFactory genericDynamicFactory = new GenericDynamicFactory();
//        String s="f83ccfc-0002f9-0000-b4";
//        Object[] constructorArgs={s};
//        try {
//            Object GUID = genericDynamicFactory.loadInstance("com.walnut.sparta.pojo.ApplicationFunctionalNodeInformation", null,null );
//            Debug.trace(GUID.toString());
//        } catch (Exception e){
//            Debug.trace(e.toString());
//        }
//        pathURI pathURI = new pathURI("java-service:///com.walnut.sparta.services.pojo.ServiceFunctionalNodeMeta");
//        String className = pathURI.getClassName();
//        Debug.trace(className);


        GenericConfNode confNode = new GenericConfNode();
        Debug.trace(confNode.getClass().getName());

    }
}
