package com.tncalculator.tncalculatorapi.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info =@io.swagger.v3.oas.annotations.info.Info(
                title = "TNCalculator API",
                version = "${api.version}",
                contact = @Contact(
                        name = "Franklin Castillo", email = "franklincastillo18@gmail.com", url = "https://about.me/fcastillo18"
                ),
                license = @License(
                        name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"
                ),
                termsOfService = "${tos.uri}",
                description = "${api.description}"
        )
//        servers = @Server(
//                url = "${api.server.url}",
//                description = "Ready"
//        )
)
@Configuration
@io.swagger.v3.oas.annotations.security.SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
) //another way of adding jwt support to the swagger ui
public class SpringdocConfig {

    @Bean
    public OpenAPI baseOpenAPI() {

        return new OpenAPI()
                .info(new Info().title("Spring Doc").version("1.0.0").description("Spring doc"));
    }
}