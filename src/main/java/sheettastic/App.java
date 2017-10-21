package sheettastic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Dave on 21/10/2017.
 */
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication( App.class);
        springApplication.run(args);
    }
}
