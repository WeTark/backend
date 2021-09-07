package com.wetark.main.config;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private final String AUTH_HEADER;

    public SwaggerConfig(@Value("${auth.header:Authorization}") String authHeader) {
        this.AUTH_HEADER = authHeader;
    }

    @Bean
    public Docket customSwaggerConfig() {
        Contact contact = new Contact("WeTark", null, null);
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfoBuilder())
                .securitySchemes(Lists.newArrayList(new ApiKey("JWT", AUTH_HEADER, "header")))
                .securityContexts(Lists.newArrayList(securityContext()));
    }

    private ApiInfo apiInfoBuilder(){
        return new ApiInfoBuilder()
                .title("WeTark")
                .description("Backend application for authorization for weTark")
                .version("0.0.1")
                .build();
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex(".*"))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = new AuthorizationScope("global", "accessEverything");
        return Lists.newArrayList(new SecurityReference("JWT", authorizationScopes));
    }
}
