package com.c2ray.idea.plugin.sqllog.protocol;

public enum SqlLogProtocolStatusEnum {

    INIT(100),
    CRUD(200),
    TERMINATE(500),
    ;

    private final Integer code;

    SqlLogProtocolStatusEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
