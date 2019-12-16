package com.qf.v3.common.pojo;

import java.io.Serializable;

public class ResultBean<T> implements Serializable {

    //正确返回给前端信息      code --->0  取data
    //错误返回给前端错误信息   code --->1  取 message
    private Integer code;
    private T       data;
    private String  message;

    public ResultBean() {
    }

    public ResultBean(Integer code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public ResultBean(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResultBean(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     *  成功返回信息
     * @param data
     * @return
     */
    public static ResultBean successResult(String data){
        ResultBean bean=new ResultBean();
        bean.setCode(0);
        bean.setData(data);
        return bean;
    }


    /**
     * 失败返回信息
     * @param data
     * @return
     */
    public static ResultBean errorResult(String data){
        ResultBean bean=new ResultBean();
        bean.setCode(1);
        bean.setData(data);
        return bean;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
