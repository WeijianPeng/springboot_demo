package com.china.demo.example.domain.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BusinessException extends RuntimeException {
    private ExceptionKey exceptionKey;
    private List<String> exceptionMessageList = new ArrayList<>();


    public BusinessException(ExceptionKey exKey, List<String> messageList) {
        this.exceptionKey = exKey;
        this.exceptionMessageList = messageList;
    }

    public BusinessException(ExceptionKey exKey, String message) {
        super(message);
        this.exceptionKey = exKey;
    }

    public BusinessException(ExceptionKey exKey, String message, Throwable cause) {
        super(message, cause);
        this.exceptionKey = exKey;
    }

    public BusinessException(ExceptionKey exKey, String message, List<String> messageList) {
        super(message);
        this.exceptionKey = exKey;
        this.exceptionMessageList = messageList;
    }

    public BusinessException(ExceptionKey exKey, String message, List<String> messageList, Throwable cause) {
        super(message, cause);
        this.exceptionKey = exKey;
        this.exceptionMessageList = messageList;
    }
}
