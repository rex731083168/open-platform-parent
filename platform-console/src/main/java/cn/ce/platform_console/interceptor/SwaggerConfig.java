package cn.ce.platform_console.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
* @Description : swagger配置配置
* @Author : makangwei
* @Date : 2017年9月6日
*/
@WebAppConfiguration
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfig {
	
    /**
     * Every Docket bean is picked up by the swagger-mvc framework - allowing for multiple
     * swagger groups i.e. same code base multiple swagger resource listings.
     */
    @Bean
    public Docket customDocket(){
        return new Docket(DocumentationType.SWAGGER_2);

//此处还有很多其他选项，包括路径过滤，api说明等
    }
}
