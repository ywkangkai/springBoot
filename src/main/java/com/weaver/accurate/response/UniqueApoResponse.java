package com.weaver.accurate.response;


import com.weaver.accurate.common.BaseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UniqueApoResponse<T> extends ApiResponse<T>{
    private String uniqueData;

    public UniqueApoResponse<T> success(T data,String uniqueData) {
        super.setCode(BaseCode.SUCCESS.getCode());
        super.setMsg(BaseCode.SUCCESS.getInfo());
        super.setData(data);
        this.uniqueData = uniqueData;
        return this;
    }
}
