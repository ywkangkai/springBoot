package com.example.travel_project.common;

import com.example.travel_project.entity.TaskEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 响应包装对象
 */
@Data
public class ResponseData<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;
    private Long total;
    private Integer pageSize;
    private Integer pageNum;

    public static final int SUCCESS_CODE=0;     //成功编码
    public static final int FAILURE_CODE=999;   //失败编码

    public static ResponseData success(){
        ResponseData responseData = new ResponseData();
        responseData.setCode(0);
        responseData.setMessage("操作成功。");

        return responseData;
    }

    public static ResponseData success(String message){
        ResponseData responseData = new ResponseData();
        responseData.setCode(0);
        responseData.setMessage(message);

        return responseData;
    }

    public static<T> ResponseData success(T data){
        ResponseData<T> responseData = new ResponseData<>();
        responseData.setCode(0);
        responseData.setMessage("操作成功。");
        responseData.setData(data);

        return responseData;
    }

    public static<T> ResponseData success(String message, T data){
        ResponseData<T> responseData = new ResponseData<>();
        responseData.setCode(0);
        responseData.setMessage(message);
        responseData.setData(data);

        return responseData;
    }

    public static<T> ResponseData success(T data, Long total, Integer pageSize, Integer pageNum){
        ResponseData<T> responseData = new ResponseData<>();
        responseData.setCode(0);
        responseData.setData(data);
        responseData.setTotal(total);
        responseData.setPageSize(pageSize);
        responseData.setPageNum(pageNum);

        return responseData;
    }

    public static<T> ResponseData success(T data, Long total){
        ResponseData<T> responseData = new ResponseData<>();
        responseData.setCode(0);
        responseData.setMessage("操作成功。");
        responseData.setData(data);
        responseData.setTotal(total);

        return responseData;
    }

    public static<T> ResponseData success(String message, T data, Long total){
        ResponseData<T> responseData = new ResponseData<T>();
        responseData.setCode(0);
        responseData.setMessage(message);
        responseData.setData(data);
        responseData.setTotal(total);

        return responseData;
    }


    public static ResponseData failure(){
        ResponseData responseData = new ResponseData();
        responseData.setCode(999);
        responseData.setMessage("操作失败。");

        return responseData;
    }

    public static ResponseData failure(String errorMessage){
        ResponseData responseData = new ResponseData();
        responseData.setCode(200);
        responseData.setMessage(errorMessage);
        return responseData;
    }

    public static ResponseData failure(Integer code, String errorMessage){
        ResponseData responseData = new ResponseData();
        responseData.setCode(code);
        responseData.setMessage(errorMessage);
        return responseData;
    }

}
