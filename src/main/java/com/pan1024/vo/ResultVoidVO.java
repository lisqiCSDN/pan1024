package com.pan1024.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResultVoidVO {

    private String code;

    private String status;

    private String msg;

    public ResultVoidVO() {
        init("0", "0", "error");
    }

    public ResultVoidVO init(String code, String status, String msg) {
        this.code = code;
        this.status = status;
        this.msg = msg;
        return this;
    }

    public ResultVoidVO success(String msg) {
        return init("1", "200", msg);
    }

    public ResultVoidVO successCode(String code) {
        return init(code, "200", "success");
    }

    public ResultVoidVO success() {
        return success("success");
    }

    public ResultVoidVO fail(String code, String msg) {
        return init(code, "0", msg);
    }

}
