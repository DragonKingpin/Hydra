import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(locations = { "classpath:uid/default-uid-spring.xml" })
public class test {
    @Test
    public void test(){

    }
}
