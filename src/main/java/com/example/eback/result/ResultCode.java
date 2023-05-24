package com.example.eback.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author PBW
 * @date 1/19
 */
@AllArgsConstructor
@Getter
public enum ResultCode {
    SUCCESS(200),
    FAIL(400),
    UNAUTHORIZED(401),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    int code;

}
