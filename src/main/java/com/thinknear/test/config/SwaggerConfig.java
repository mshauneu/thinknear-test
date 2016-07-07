package com.thinknear.test.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.TypeMismatchException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.thinknear.test.exception.ExceptionMessage;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author <a href="mailto:m.shauneu@gmail.com">Mike Shauneu</a>
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket multipartApi() {

        Docket docket = new Docket(DocumentationType.SWAGGER_2);

        List<ResponseMessage> responseMessageList = new ArrayList<>();

        addDefaultResponseMessages(responseMessageList, HttpStatus.NOT_FOUND, new Class<?>[] {
                NoSuchRequestHandlingMethodException.class,
                NoHandlerFoundException.class
        });

        addDefaultResponseMessages(responseMessageList, HttpStatus.METHOD_NOT_ALLOWED, new Class<?>[] {
                HttpRequestMethodNotSupportedException.class
        });

        addDefaultResponseMessages(responseMessageList, HttpStatus.UNSUPPORTED_MEDIA_TYPE, new Class<?>[] {
                HttpMediaTypeNotSupportedException.class
        });

        addDefaultResponseMessages(responseMessageList, HttpStatus.NOT_ACCEPTABLE, new Class<?>[] {
                HttpMediaTypeNotAcceptableException.class
        });

        addDefaultResponseMessages(responseMessageList, HttpStatus.BAD_REQUEST, new Class<?>[] {
                MissingServletRequestParameterException.class,
                ServletRequestBindingException.class,
                TypeMismatchException.class,
                MethodArgumentNotValidException.class,
                HttpMessageNotReadableException.class,
                MissingServletRequestPartException.class,
                BindException.class
        });

        addDefaultResponseMessages(responseMessageList, HttpStatus.INTERNAL_SERVER_ERROR, new Class<?>[] {
                Exception.class
        });

        for (RequestMethod requestMethod : RequestMethod.values()) {
            docket.globalResponseMessage(requestMethod, responseMessageList);
        }

        return docket.apiInfo(apiInfo()).select().build();
    }

    private void addDefaultResponseMessages(
    		List<ResponseMessage> responseMessageList, HttpStatus status, Class<?>[] exceptionClasses) {

        String classNames = Arrays.stream(exceptionClasses).map(c -> c.getSimpleName()).collect(Collectors.joining(",<br>"));
        responseMessageList.add(new ResponseMessage(
                    status.value(), classNames,
                    new ModelRef(ExceptionMessage.class.getSimpleName()),
                    new LinkedHashMap<>()
                ));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Thinknear Test API")
                .description("REST API to for a system that assigns students to classes")
                .termsOfServiceUrl("https://www.linkedin.com/in/mshavnev")
                .contact(new Contact("Mike Shauneu", "https://www.linkedin.com/in/mshavnev", "m.shauneu@gmail.com"))
                .version("2")
                .build();
    }
}
