package com.hangangnow.mainserver.config.swagger;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket ShopApi() {
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hangangnow.mainserver.api.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(this.apiInfo());

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("HangangNOW API DOCS")
                .description("HangangNOW REST API")
                .version("1.0")
                .build();
    }


    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

}
