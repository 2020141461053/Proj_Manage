package com.example.eback.exception;

public class RequestLimitException extends Exception {

    public RequestLimitException(){
        super("HTTP请求超出设定的限制");
    }

    public RequestLimitException(String message){
        super(message);
    }
}
