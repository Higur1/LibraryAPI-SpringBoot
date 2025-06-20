package io.github.spring.libraryapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Library-api",
                version = "v1",
                contact = @Contact(
                        name = "Higor do Amaral Hungria",
                        email = "higor.hungria466@gmail.com",
                        url = "https://www.linkedin.com/in/higorhungria/"
                )
        )
)
public class OpenAPIConfiguration {
}
