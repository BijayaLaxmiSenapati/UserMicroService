package com.bridgelabz.fundoo.user.configurations;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;

import com.google.common.base.Predicate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

public class SwaggerConfig {
	
	/*private Predicate<String> postPaths() {
		return or(regex("/fundoo/*"), regex("/notes/*"));
	}*/
	
//	private Predicate<String> postPaths1() {
//		return or(regex(".*fundoo.*"));
//	}
//	
//	private Predicate<String> postPaths2() {
//		return or(regex(".*notes.*"));
//	}

	/*@Bean
	public Docket api() {                
	    return new Docket(DocumentationType.SWAGGER_2)          
	      .select()
	      .apis(RequestHandlerSelectors.any())
//	      .paths(PathSelectors.ant("/notes/*"))
	      .paths(PathSelectors.any())
	      //.paths(postPaths2())
	      //.paths(postPaths1())      
	      .build();		
	}*/
	 
//	@SuppressWarnings("deprecation")
//	private ApiInfo apiInfo() {
//		return new ApiInfoBuilder().title("LoginAndRegistrationUsingFundooNotes")
//				.description("Notes Taking using Spring Boot ANd MongoDB ").contact("dharaparanjape.1007@gmail.com")
//				.version("1.0").build();
//	}
	
	/*@SuppressWarnings("deprecation")
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("LoginAndRegistrationUsingFundooNotes")
				.description("Notes Taking using Spring Boot ANd MongoDB ").contact("dharaparanjape.1007@gmail.com")
				.version("1.0").build();
	}*/

	@Bean
	public Docket postsApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("public-api")
				.apiInfo(apiInfo()).select().paths(postPaths()).build();
	}

	private Predicate<String> postPaths() {
		return or(regex("/api/posts.*"), regex("/api/javainuse.*"));
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("JavaInUse API")
				.description("JavaInUse API reference for developers")
				.termsOfServiceUrl("http://javainuse.com")
				.contact("javainuse@gmail.com").license("JavaInUse License") 
				.licenseUrl("javainuse@gmail.com").version("1.0").build();
	}
	
	
}