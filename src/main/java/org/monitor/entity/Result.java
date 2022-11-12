package org.monitor.entity;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Result<T> {

    private T data;

    private Integer code;

    private String msg;


    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<>();
        result.setCode(HttpStatus.OK.value());
        result.setData(data);
        return result;
    }


    public static <T> Result<T> ok() {
        Result<T> result = new Result<>();
        result.setCode(HttpStatus.OK.value());
        return result;
    }


    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.setCode(HttpStatus.BAD_REQUEST.value());
        result.setMsg(msg);
        return result;
    }
}
