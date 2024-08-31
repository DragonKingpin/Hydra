package com.walnut.sparta;



import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
// Dummy boot for spring to found the main classpath.
public class SpartaBoot {
    //    @PostConstruct
//    public void init() {
//        try {
//            ShadowBoot.shadow.vitalize();
//        }
//        catch ( Exception e ) {
//            ShadowBoot.shadow.console().cerr( e.getMessage() );
//        }
//    }
//    public static void main(String[] args) {
//        SpringApplication.run(SpartaBoot.class, args);
//    }
}
