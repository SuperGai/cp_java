package com.hh.appraisal.springboot.core.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.google.common.base.Predicate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;
import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Knife4j Swagger 配置
 * @author https://doc.xiaominfo.com/knife4j/documentation/description.html
 * @createTime 2021/05/24
 */
@Configuration
@EnableSwagger2WebMvc
public class SwaggerKnife4jConfig {

    /**
     * 默认的 API Group
     */
    private static final String DEFAULT_GROUP = "默认组";

    @Value("${swagger.title}")
    private String title;

    @Value("${swagger.description}")
    private String description;

    @Value("${swagger.version}")
    private String version;

    @Value("${swagger.termsOfServiceUrl}")
    private String termsOfServiceUrl;

    @Value("${swagger.contact.name}")
    private String name;

    @Value("${swagger.contact.url}")
    private String url;

    @Value("${swagger.contact.email}")
    private String email;

    @Value("${swagger.license}")
    private String license;

    @Value("${swagger.licenseUrl}")
    private String licenseUrl;

    /**
     * API Config
     * @return
     */
    @Bean(value = "swaggerApi")
    public Docket swaggerApi() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())

                // 分组名称
                .groupName(DEFAULT_GROUP)

                .select()
                // 指定 扫描方式
//                .apis(RequestHandlerSelectors.basePackage("com.hh.appraisal.springboot.controller"))
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())

                .build();
        return docket;
    }

    /**
     * API INFO
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .version(version)
                .termsOfServiceUrl(termsOfServiceUrl)
                .contact(new Contact(name, url, email)) // 作者
                .license(license)
                .licenseUrl(licenseUrl)
                .build();
    }
}
