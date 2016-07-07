package com.thinknear.test.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;


/**
 *
 * @author <a href="mailto:m.shauneu@gmail.com">Mike Shauneu</a>
 */
@ControllerAdvice
public class ExceptionAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

    @ExceptionHandler(value = {
            CourseAlreadyExistsException.class,
            StudentAlreadyExistsException.class,
            StudentAlreadyEnrolledException.class
    })
    public ResponseEntity<ExceptionMessage> handleConflict(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        return handleExceptionInternal(ex, status, request);
    }
    
    @ExceptionHandler(value = {
            NoSuchRequestHandlingMethodException.class,
            NoHandlerFoundException.class,
            CourseNotFoundException.class,
            StudentNotFoundException.class,
            StudentWasNotEnrolledException.class
    })
    public ResponseEntity<ExceptionMessage> handleNotFound(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return handleExceptionInternal(ex, status, request);
    }

    @ExceptionHandler(value = {
            HttpRequestMethodNotSupportedException.class
    })
    public ResponseEntity<ExceptionMessage> handleMethodNotAllowed(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
        return handleExceptionInternal(ex, status, request);
    }

    @ExceptionHandler(value = {
            HttpMediaTypeNotSupportedException.class
    })
    public ResponseEntity<ExceptionMessage> handleUnsupportedMediaType(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
        return handleExceptionInternal(ex, status, request);
    }

    @ExceptionHandler(value = {
            HttpMediaTypeNotAcceptableException.class
    })
    public ResponseEntity<ExceptionMessage> handleNotAcceptable(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
        return handleExceptionInternal(ex, status, request);
    }

    @ExceptionHandler(value = {
            MissingServletRequestParameterException.class,
            ServletRequestBindingException.class,
            TypeMismatchException.class,
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestPartException.class,
            BindException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ExceptionMessage> handleBadRequest(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return handleExceptionInternal(ex, status, request);
    }

    @ExceptionHandler(value = {
            Exception.class
    })
    public ResponseEntity<ExceptionMessage> handleInternalServerError(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(ex, status, request);
    }


    protected ResponseEntity<ExceptionMessage> handleExceptionInternal(
            Exception exception, HttpStatus status, WebRequest request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setStatus(status.value());
        exceptionMessage.setError(status.getReasonPhrase());

        if (exception != null) {
            exceptionMessage.setReason(exception.getClass().getSimpleName());
            exceptionMessage.setMessage(exception.getMessage());

            LOGGER.error("Error: ", exception);
        }

        return new ResponseEntity<>(exceptionMessage, headers, status);
    }

}
