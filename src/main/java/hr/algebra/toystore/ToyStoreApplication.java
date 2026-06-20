package hr.algebra.toystore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ToyStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToyStoreApplication.class, args);
    }
}
