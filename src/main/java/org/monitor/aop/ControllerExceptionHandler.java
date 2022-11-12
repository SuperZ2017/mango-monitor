package org.monitor.aop;

import lombok.extern.slf4j.Slf4j;
import org.monitor.entity.Result;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {


    @ExceptionHandler(IllegalArgumentException.class)
    public Result handlerIllegalArgumentException(Throwable e) {
        log.error("IllegalArgumentException", e);
        return Result.error(e.getMessage());
    }


    @ExceptionHandler(RuntimeException.class)
    public Result handlerRuntimeException(Throwable e) {
        log.error("RuntimeException", e);
        return Result.error(e.getMessage());
    }


    @ExceptionHandler(Throwable.class)
    public Result handleOtherException(Throwable e) {
        log.error("Throwable", e);
        return Result.error(e.getMessage());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException", e);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return Result.error(collect.toString());
    }
}
