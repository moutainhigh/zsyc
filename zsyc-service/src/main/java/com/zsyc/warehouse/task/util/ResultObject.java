package com.zsyc.warehouse.task.util;

/************************************** 返回结果实体类 ********************************************/

/**
 * @author lixk
 * @version [1.0, 2017年8月14日]
 * @ClassName: ResultObject
 * @Description: TODO
 * @date 2017年8月14日 上午10:57:17
 * @since version 1.0
 */

public class ResultObject {

    /**
     * 成功
     */
    public static final int SUCCESS = 200;
    /**
     * 失败
     */
    public static final int FAILED = 300;
    /**
     * 系统异常
     */
    public static final int ERROR = 500;

    private int code;// 返回码 200成功，500系统异常
    private String message;// 消息
    private Object data;// 数据

    public ResultObject() {
    }

    /**
     * @param code 返回码
     */
    public ResultObject(int code) {
        super();
        this.code = code;
    }

    /**
     * @param code    返回码
     * @param message 返回信息
     */
    public ResultObject(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    /**
     * @param code 返回码
     * @param data 返回数据
     */
    public ResultObject(int code, Object data) {
        super();
        this.code = code;
        this.data = data;
    }

    /**
     * @param code    返回码
     * @param message 返回信息
     * @param data    返回数据
     */
    public ResultObject(int code, String message, Object data) {
        super();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}