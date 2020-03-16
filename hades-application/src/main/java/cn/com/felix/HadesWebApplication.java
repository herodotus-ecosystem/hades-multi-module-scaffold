package cn.com.felix;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.flowable.ui.modeler.conf.ApplicationConfiguration;
import org.flowable.ui.modeler.servlet.AppDispatcherServletConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@Import({
        ApplicationConfiguration.class,
        AppDispatcherServletConfiguration.class
})
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableCaching
@EnableAsync
@EnableEncryptableProperties
public class HadesWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(HadesWebApplication.class, args);
    }

}

