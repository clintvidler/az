package az.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}

@RestController
class TaskController {

    private final TaskRepository repository;

    TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/tasks")
    public List<TaskResponse> list() {
        return this.repository.findAll()
                .stream()
                .map(task -> new TaskResponse(
                        task.id(),
                        task.title(),
                        task.user_id()
                ))
                .toList();
    }
}

interface TaskRepository extends ListCrudRepository<Task, Long> {
}

@Configuration
class SecurityConfiguration {

    @Bean
    Customizer <HttpSecurity> httpSecurityCustomizer() {
        return http -> http
                // login via ott then visit http://localhost:8080/webauthn/register to make it into passkey as well
                .webAuthn(w -> w
                        .rpName("azdo")
                        .rpId("localhost")
                        .allowedOrigins("http://localhost:8080")
                )
                .oneTimeTokenLogin(c -> c.tokenGenerationSuccessHandler((request, response, oneTimeToken) -> {
                    response.getWriter().println("OTT generated! Check your console.");
                    response.setContentType(MediaType.TEXT_PLAIN_VALUE);
                    System.out.println("👉 Passwordless Login Link: " +
                            "http://localhost:8080/login/ott?token=" + oneTimeToken.getTokenValue());
                }));
    }

    @Bean
    JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }
}

@RestController
class MeController {
    @GetMapping("/")
    Map<String, String> me(Principal principal) {
        return Map.of("user", principal.getName());
    }
}
