package com.desafiobackend.gestoreventosapi.config;

import io.swagger.models.auth.In;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/api.*"))
                .build()
                .securitySchemes(Collections.singletonList(new ApiKey("Token Access", HttpHeaders.AUTHORIZATION, In.HEADER.name())))
                .securityContexts(Collections.singletonList(securityContext()))
                .globalResponseMessage(RequestMethod.GET, responseMessages())
                .globalResponseMessage(RequestMethod.POST, responseMessages())
                .globalResponseMessage(RequestMethod.PUT, responseMessages())
                .globalResponseMessage(RequestMethod.DELETE, responseMessages())
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Gestor de Eventos API")
                .description("um exemplo de api que gerencia eventos")
                .version("1.0.0")
                .license("Apache License Version 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                        .contact(new Contact("Jorge Fernando Brito", "www.linkedin.com/in/jorge-fernando-brito-profile/", "jorge.fernando.brito@gmail.com"))
                        .build();
    }


    private List<ResponseMessage> responseMessages()
    {
        return new ArrayList<>() {{
            add(new ResponseMessageBuilder()
                    .code(200)
                    .message("OK")
                    .build());
            add(new ResponseMessageBuilder()
                    .code(201)
                    .message("Recurso criado com sucesso")
                    .build());
            add(new ResponseMessageBuilder()
                    .code(204)
                    .message("Resposta sem conteúdo")
                    .build());
            add(new ResponseMessageBuilder()
                    .code(401)
                    .message("Acesso bloqueado por regras de restrição de perfil de acesso")
                    .build());
            add(new ResponseMessageBuilder()
                    .code(403)
                    .message("Acesso negado, necessário estar autenticado para acessar o recurso")
                    .build());
            add(new ResponseMessageBuilder()
                    .code(409)
                    .message("Conflito ao tentar criar ou atualizar um recurso")
                    .build());
            add(new ResponseMessageBuilder()
                    .code(500)
                    .message("Erro ao efetuar a operação")
                    .responseModel(new ModelRef("Error"))
                    .build());

        }};
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.ant("/events/**"))
                .forPaths(PathSelectors.ant("/users/**"))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("ADMIN", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(
                new SecurityReference("Token Access", authorizationScopes));
    }
}
