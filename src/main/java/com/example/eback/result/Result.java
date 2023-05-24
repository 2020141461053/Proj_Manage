package com.example.eback.result;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author PBW
 * @date 1/19
 */
@Data
@EqualsAndHashCode
public class Result {
    private int code;

    private String message;

    private Object result;

    Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.result = data;
    }
}
