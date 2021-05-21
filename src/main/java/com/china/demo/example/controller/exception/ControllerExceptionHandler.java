package com.china.demo.example.controller.exception;


import com.china.demo.example.domain.exception.BusinessException;
import com.china.demo.example.domain.exception.ExceptionKey;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.Locale;

@Log
@RestControllerAdvice
public class ControllerExceptionHandler {
    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler
    public ResponseEntity handleException(Exception exception) {
        BusinessException biz = null;

        if (exception instanceof BusinessException) {
            biz = (BusinessException) exception;
        } else if (exception instanceof MissingRequestHeaderException) {
            MissingRequestHeaderException ex = (MissingRequestHeaderException) exception;
            biz = new BusinessException(
                    ExceptionKey.INVALID_REQUEST,
                    messageSource.getMessage("common.missing_http_header",
                            new String[]{ex.getHeaderName()}, Locale.getDefault()),
                    exception
            );
        } else if (exception instanceof HttpMessageNotReadableException) {
            HttpMessageNotReadableException ex = (HttpMessageNotReadableException) exception;
            biz = new BusinessException(
                    ExceptionKey.INVALID_REQUEST,
                    messageSource.getMessage("common.invalid_http_body",
                            new String[] {ex.getMessage()}, Locale.getDefault()),
                    exception
            );
        } else if (exception instanceof HttpRequestMethodNotSupportedException) {
            HttpRequestMethodNotSupportedException ex = (HttpRequestMethodNotSupportedException) exception;
            biz = new BusinessException(
                    ExceptionKey.INVALID_REQUEST,
                    messageSource.getMessage("common.invalid_http_method",
                            new String[] { ex.getMethod(), ex.getSupportedHttpMethods().toString() },
                            Locale.getDefault()),
                    exception
            );
        } else if (exception instanceof MethodArgumentTypeMismatchException) {
            MethodArgumentTypeMismatchException ex = (MethodArgumentTypeMismatchException) exception;
            biz = new BusinessException(
                    ExceptionKey.INVALID_REQUEST,
                    messageSource.getMessage("common.invalid_http_parameter",
                            new String[]{ex.getName()}, Locale.getDefault()),
                    exception
            );
        } else if (exception instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) exception;
            biz = new BusinessException(ExceptionKey.INVALID_REQUEST,
                    messageSource.getMessage("common.invalid_request",
                            null, Locale.getDefault()), Arrays.asList(ex.getMessage()),
                    exception
            );

        } else if (exception instanceof IllegalArgumentException) {
            IllegalArgumentException ex = (IllegalArgumentException) exception;
            biz = new BusinessException(
                    ExceptionKey.INVALID_REQUEST,
                    messageSource.getMessage("common.invalid_http_parameter",
                            new String[] { ex.getMessage() }, Locale.getDefault()),
                    exception
            );
        } else if (exception instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException ex = (MissingServletRequestParameterException) exception;
            biz = new BusinessException(
                    ExceptionKey.INVALID_REQUEST,
                    messageSource.getMessage("common.invalid_http_parameter",
                            new String[] { ex.getParameterName() }, Locale.getDefault()),
                    exception
            );

        } else if (exception instanceof HystrixRuntimeException) {
            HystrixRuntimeException ex = (HystrixRuntimeException) exception;
            Throwable fallbackException = ex.getFallbackException();
            if (fallbackException != null) {
                biz = findBusinessException(fallbackException);
            }
        }

        if (biz == null) {
            log.severe(exception.toString());
            biz = new BusinessException(
                    ExceptionKey.INTERNAL_SERVER_ERROR,
                    messageSource.getMessage("common.internal_server_error",
                            new String[] { exception.getMessage() },
                            Locale.getDefault()),
                    exception
            );
        }

        biz.printStackTrace();

        log.severe(
                String.format("BusinessException(%s): %s %s",
                        biz.getExceptionKey(),
                        biz.getMessage(),
                        biz.getExceptionMessageList())
        );

        return ResponseEntity
                .status(getHttpStatus(biz.getExceptionKey()))
                .body(toDTO(biz));
    }


    /**
     * this method is used to transfer the domain ExceptionKey to HttpStatus.
     * NEVER let domain know the HttpStatus.
     */
    private HttpStatus getHttpStatus(ExceptionKey exceptionKey) {
        switch (exceptionKey) {
            case NOT_FOUND:
                return HttpStatus.UNPROCESSABLE_ENTITY;
            case INVALID_REQUEST:
                return HttpStatus.BAD_REQUEST;
            case ENTITY_CONFLICT:
                return HttpStatus.CONFLICT;
            case INVALID_PROPERTY:
                return HttpStatus.UNPROCESSABLE_ENTITY;
            case RESOURCE_TYPE_NOT_MATCH:
                return HttpStatus.UNPROCESSABLE_ENTITY;
            case AUTH_METHOD_NOT_SUPPORTED:
                return HttpStatus.UNPROCESSABLE_ENTITY;
            case NOT_AUTHORIZED:
                return HttpStatus.FORBIDDEN;
            case NO_ACCESS:
                return HttpStatus.FORBIDDEN;
            case NOT_CAPABLE_GENERATE_SECRET_KEY:
                return HttpStatus.PRECONDITION_FAILED;
            case INVALID_STATUS_TRANSITION:
                return HttpStatus.NOT_ACCEPTABLE;
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    private BusinessException findBusinessException(Throwable cause) {
        int maxLevel = 5;
        Throwable current = cause;
        while (--maxLevel >= 0) {
            if (current instanceof BusinessException) {
                return (BusinessException) current;
            }
            current = current.getCause();
        }
        return null;
    }

    private ExceptionDTO toDTO(Exception ex) {
        ExceptionDTO dto;

        if (ex instanceof BusinessException) {
            BusinessException biz = (BusinessException) ex;

            dto = ExceptionDTO.builder()
                    .errorCode(biz.getExceptionKey().toString())
                    .errorMessage(biz.getMessage())
                    .errorDetails(biz.getExceptionMessageList())
                    .build();
        } else {
            dto = ExceptionDTO.builder()
                    .errorCode(ex.getClass().getSimpleName())
                    .errorMessage(ex.getMessage())
                    .build();
        }

        return dto;
    }


}
