package com.yyt.librarynetwork.base;

import java.util.List;

/**
 * @author kzcai
 * @packageName com.yyt.librarynetwork.base
 * @date 2020/6/9
 */
public class  BaseRes<T> {

    private T data;

    private String jsonStr;

    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
