package com.pan1024.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResultOneVO<T> {

    private String code;

    private String status;

    private String msg;

    private T data;

    public ResultOneVO() {
        init("0", "0", "error", null);
    }

    public ResultOneVO<T> init(String code, String status, String msg, T data) {
        this.code = code;
        this.status = status;
        this.msg = msg;
        this.data = data;
        return this;
    }

    public ResultOneVO<T> success(String msg, T data) {
        return init("1", "200", msg, data);
    }

    public ResultOneVO<T> success(T data) {
        return success("success", data);
    }

    public ResultOneVO<T> fail(String code, String msg) {
        return init(code, "0", msg, null);
    }

}
