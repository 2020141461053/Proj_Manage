package com.example.eback.exception;

import com.example.eback.result.Result;
import com.example.eback.result.ResultFactory;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.security.auth.login.LoginException;

/**
 * Global exception handler.
 *
 * @author PBW
 * @date 1/19
 */
@Log4j2
@ControllerAdvice
@ResponseBody
public class DefaultExceptionHandler {
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result exceptionHandler1(Exception e) {
        String message = "传入了错误的参数";
        return ResultFactory.buildFailResult(message);
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public Result exceptionHandler2(Exception e) {
        String message = "权限认证失败";

        return ResultFactory.buildFailResult(message);
    }

    @ExceptionHandler(value = HttpMediaTypeNotAcceptableException.class)
    public void exceptionHandler3(Exception e) {
        log.warn("HttpMediaTypeNotAcceptableException");
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public Result exceptionHandler4(Exception e) {
        String message = "error";
        return ResultFactory.buildFailResult(message);
    }

    @ExceptionHandler(value = LoginException.class)
    public Result exceptionHandler5(Exception e) {
        String message = "没有登录";
        return ResultFactory.buildFailResult(message);
    }

    @ExceptionHandler(value = NullPointerException.class)
    public Result exceptionHandler(Exception e) {
        String message = "找不到数据" + e.getMessage();
        return ResultFactory.buildFailResult(message);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public Result exceptionHandler6(Exception e) {
        String message = "参数不全";
        return ResultFactory.buildFailResult(message);
    }
}
