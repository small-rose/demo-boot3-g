package com.small.rose.demo.config.knife4j;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Project: demo-boot3-g
 * @Author: 张小菜
 * @Description: [ Knife4jConfig ] 说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/12/8 周一 22:08
 * @Version: v1.0
 * Knife4j UI: http://localhost:8080/doc.html
 * Swagger UI: http://localhost:8080/swagger-ui.html
 * OpenAPI文档: http://localhost:8080/v3/api-docs
 */
@Configuration
public class Knife4jConfig {

    /**
     * 创建自定义OpenAPI配置
     * 该方法配置了API文档的基本信息，包括标题、版本、描述、联系人和许可证等
     * @return OpenAPI 返回配置好的OpenAPI对象
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Demo API接口文档")  // 设置API文档标题
                        .version("1.0")           // 设置API版本号
                        .description("Spring Boot 3.5.8 整合 Knife4j API文档")  // 设置API描述
                        .contact(new Contact()    // 设置联系人信息
                                .name("张小菜")  // 联系人姓名
                                .email("your-email@example.com")  // 联系人邮箱
                                .url("https://your-website.com"))  // 联系人网址
                        .license(new License()   // 设置许可证信息
                                .name("Apache 2.0")  // 许可证名称
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")));  // 许可证链接
    }

    /**
     * 创建公共API分组配置
     * 该方法配置了公开访问的API接口分组，匹配以"/public/"开头的所有路径
     * @return GroupedOpenApi 返回配置好的公共API分组对象
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")  // 设置分组名称为"public"
                .pathsToMatch("/public/**")  // 匹配所有以"/public/"开头的路径
                .build();
    }

    /**
     * 创建管理API分组配置
     * 该方法配置了需要管理员权限的API接口分组，匹配以"/admin/"开头的所有路径
     * @return GroupedOpenApi 返回配置好的管理API分组对象
     */
    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("admin")  // 设置分组名称为"admin"
                .pathsToMatch("/admin/**")  // 匹配所有以"/admin/"开头的路径
                .build();
    }
}
