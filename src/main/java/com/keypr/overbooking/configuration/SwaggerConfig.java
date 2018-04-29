package com.keypr.overbooking.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
 *
 * Swagger configuration
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.keypr.overbooking.rest"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Overbooking REST API",
                "Use /book for booking. Use /configure to save settings",
                "API 0",
                "",
                new Contact("Kirill Bazarov", "www.keypr.com", "es.kelevra@gmail.com"),
                "Apache License 2.0", "https://www.apache.org/licenses/LICENSE-2.0", Collections.emptyList());
    }

}
