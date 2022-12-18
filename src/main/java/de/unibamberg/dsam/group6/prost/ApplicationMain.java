package de.unibamberg.dsam.group6.prost;

import com.github.bufferings.thymeleaf.extras.nl2br.dialect.Nl2brDialect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

@SpringBootApplication
public class ApplicationMain {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationMain.class, args);
    }

    @Bean
    public Nl2brDialect dialect() {
        return new Nl2brDialect();
    }

    @Bean
    public Java8TimeDialect timeDialect() {
        return new Java8TimeDialect();
    }
}
