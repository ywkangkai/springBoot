package com.weaver.accurate.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SpringDocConfig {
    /**
     * Springdoc服务地址配置，用于多负载部署时使用域名访问
     */
    @Value("${springdoc.server.host}")
    String springdocServerHost;

    /**
     * 返回分组Open API信息，指定扫描指定包下的RestController
     * @return
     */
    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("ITP-Base-Backend")
                //指定此package下的接口显示在接口文档中
                .packagesToScan("com.example.travel_project.controller")
                .build();
    }

    /**
     * 返回Open API信息，指定基础地址
     * @return
     */
    @Bean
    public OpenAPI openAPI() {
        Components components = new Components();
        //添加服务地址
        Server server = new Server();
        server.setUrl(springdocServerHost);

        return new OpenAPI()
                .components(components)
                .info(apiInfo())
                .addServersItem(server);
    }

    /**
     * API信息
     * @return
     */
    private Info apiInfo() {
        Contact contact = new Contact();
        return new Info()
                .title("基础接口文档")
                .version("1.0")
                .contact(contact)
                .description("基础接口文档");
    }
}