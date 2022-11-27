package dev.forum.forum.config;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class SampleDataConfig {

    @Bean
    Faker faker() {
        return new Faker(new Locale("en-US"));
    }
}
