import com.pinecone.ulf.util.id.UidGenerator;
import com.pinecone.ulf.util.id.UUIDBuilder;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(locations = { "classpath:uid/default-uid-spring.xml" })
public class test {
    @Test
   public void test(){
       UidGenerator uidGenerator= UUIDBuilder.getBuilder();
       long uid = uidGenerator.getUID();
       System.out.println(uidGenerator.parseUID(uid));
   }
}
