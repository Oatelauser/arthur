package com.broadtech.arthur.admin.common.respones;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Machenike
 * @Date 2022/7/20
 * @Version 1.0.0
 * @Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseVo<T> {


    T data;
    ReturnCode code;
    String msg;

   public static <T> ResponseVo<T>
   SUCCESS(T data){
        return new ResponseVo<>(
                data,
                ReturnCode.RC100,
                "SUCCESS"
        );
    }

    public static <T> ResponseVo<T> FAIL(T data){
        return new ResponseVo<>(
                data,
                ReturnCode.RC500,
                "FAIL"
        );
    }

    public static <T> ResponseVo<T> NO_DATA(T data){
        return new ResponseVo<>(
                data,
                ReturnCode.RC101,
                "NO DATA"
        );
    }

}
