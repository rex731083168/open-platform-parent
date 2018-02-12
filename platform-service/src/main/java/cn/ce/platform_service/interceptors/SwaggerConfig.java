package cn.ce.platform_service.interceptors;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
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
//@ComponentScan(basePackageClasses = {ApisController.class }) //Instructs spring where to scan for API controllers
public class SwaggerConfig {
	
    /**
     * Every Docket bean is picked up by the swagger-mvc framework - allowing for multiple
     * swagger groups i.e. same code base multiple swagger resource listings.
     */
    @Bean
    public Docket customDocket(){
    	ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();  
    	ticketPar.name("ticket").description("user ticket")
    	.modelRef(new ModelRef("string")).parameterType("header")
    	.required(false).build();
    	pars.add(ticketPar.build());
 
        return new Docket(DocumentationType.SWAGGER_2)
        		.select()
        		.apis(RequestHandlerSelectors.any())  
                .build()  
                .globalOperationParameters(pars)  
                .apiInfo(apiInfo());
    }
    
    ApiInfo apiInfo() {  
    	return new ApiInfoBuilder()  
            .title("api swagger document")  
            .description("前后端联调swagger api 文档") 
            .version("2.1.5.5")  
            .build();
    }  
}
