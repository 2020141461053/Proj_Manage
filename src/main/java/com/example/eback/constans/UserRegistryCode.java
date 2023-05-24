package com.example.eback.constans;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRegistryCode {

    EMPTY_MSG(0, "用户名和密码不能为空"),
    REGISTRY_SUCCESS(1, "注册成功"),
    USER_EXISTS(2, "用户已存在"),
    UNKNOWN(99, "未知错误");

    int code;
    String msg;

}
