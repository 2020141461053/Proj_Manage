package com.example.eback.constans;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TnDataCode {

    IS_EXISTS(-1,"有次T+n离线数据"),
    SAVE_SUCCESS(0,"离线数据成功"),
    NOT_EXISTS(1,"有次T+n离线数据");
    int code;
    String msg;

}
