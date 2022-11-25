package dev.forum.forum;

import dev.forum.forum.model.user.User;
import dev.forum.forum.model.user.UserRole;
import dev.forum.forum.repository.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;

@SpringBootApplication
public class ForumApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForumApplication.class, args);
    }


    @Bean
    CommandLineRunner commandLineRunner(UserRepo userRepo, PasswordEncoder passwordEncoder) {


        return args -> {
            User user1 = User.builder()
                    .username("janek")
                    .email("jan.kowalski@email.com")
                    .password(passwordEncoder.encode("password"))
                    .created(Instant.now())
                    .enabled(Boolean.TRUE)
                    .userRole(UserRole.USER)
                    .build();

            userRepo.save(user1);
        };
    }

}
