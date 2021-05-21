package com.china.demo.example.domain.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DomainValidateService {

    @Autowired
    private LocalValidatorFactoryBean localValidatorFactoryBean;

    @Autowired
    private MessageSource messageSource;

    public <T> void validateOrThrowBusinessException(T object, ExceptionKey exceptionKey) {

        Set<ConstraintViolation<T>> violations = localValidatorFactoryBean.getValidator()
                .validate(object);

        if (violations.size() > 0) {
            List<String> exceptionMessages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            throw new BusinessException(
                    exceptionKey,
                    messageSource.getMessage("common.invalid_property", null, Locale.getDefault()),
                    exceptionMessages);
        }
    }

}
