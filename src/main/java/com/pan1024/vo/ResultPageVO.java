package com.pan1024.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ResultPageVO<T> {

    private String code;

    private String status;

    private String msg;

    private long total;

    private List<T> data;

    public ResultPageVO() {
        init("0", "0", "error", null, 0);
    }

    public ResultPageVO<T> init(String code, String status, String msg, List<T> data, long total) {
        this.code = code;
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.total = total;
        return this;
    }

    public ResultPageVO<T> success(String msg, List<T> data, long total) {
        return init("1", "200", msg, data, total);
    }

    public ResultPageVO<T> success(List<T> data, long total) {
        return success("success", data, total);
    }

    public ResultPageVO<T> success(List<T> data) {
        return success(data, data != null ? data.size() : 0L);
    }

    public ResultPageVO<T> fail(String code, String msg) {
        return init(code, "0", msg, null, 0L);
    }

}
