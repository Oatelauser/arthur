package com.arthur.web.model;

import com.arthur.web.server.ServerStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

import java.io.Serial;
import java.io.Serializable;
import java.util.function.Supplier;

/**
 * 泛型响应对象
 *
 * @author DearYang
 * @date 2022-08-17
 * @since 1.0
 */
@SuppressWarnings({"unused"})
@Schema(name = "通用服务器响应")
public class GenericServerResponse<T> implements Serializable {

    @Serial
	private static final long serialVersionUID = -6475306864977053088L;

    /**
     * response code
     */
	@Schema(description = "响应代码")
    private String code;

    /**
     * response message
     */
	@Schema(description = "响应信息")
    private String msg;

    /**
     * response detail data
     */
	@Valid
	@Schema(description = "响应消息")
    private T data;

    public GenericServerResponse() {
    }

    public GenericServerResponse(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 判断请求是否成功
     *
     * @return yes or no
     */
    public boolean isSuccess() {
        return ArthurStatusEnum.SUCCESS.code.equals(code);
    }

    /**
     * 获取data
     *
     * @param supplier data为null，自定义抛异常
     * @param <E>      异常类
     * @return {@link Throwable}
     * @throws E 异常类
     */
    public <E extends Throwable> T orElseThrow(Supplier<E> supplier) throws E {
        if (data == null) {
            throw supplier.get();
        }

        return data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> GenericServerResponse<T> ofSuccess() {
        return GenericServerResponse.ofSuccess((String) null, null);
    }

    public static <T> GenericServerResponse<T> ofSuccessMessage(String msg) {
        return GenericServerResponse.ofSuccess(msg, null);
    }

    public static <T> GenericServerResponse<T> ofSuccess(T data) {
        return GenericServerResponse.ofSuccess((String) null, data);
    }

    public static <T> GenericServerResponse<T> ofSuccess(String msg, T data) {
        return GenericServerResponse.ofSuccess(ArthurStatusEnum.SUCCESS.code, msg, data);
    }

    public static <T> GenericServerResponse<T> ofSuccess(String code, String msg, T data) {
        return GenericServerResponse.of(code, msg, data);
    }

    public static <T> GenericServerResponse<T> ofSuccess(ServerStatus status, T data) {
        return GenericServerResponse.of(status.getCode(), status.getMsg(), data);
    }

    public static <T> GenericServerResponse<T> ofSuccess(ServerStatus status) {
        return ofSuccess(status, null);
    }

    public static <T> GenericServerResponse<T> ofError() {
        return GenericServerResponse.of(ArthurStatusEnum.FAIL.code, null, null);
    }

    public static <T> GenericServerResponse<T> ofError(String errorCode, String errorMessage) {
        return GenericServerResponse.ofError(errorCode, errorMessage, null);
    }

    public static <T> GenericServerResponse<T> ofError(String errorCode, String errorMessage, T data) {
        return GenericServerResponse.of(errorCode, errorMessage, data);
    }

    public static <T> GenericServerResponse<T> ofError(ServerStatus status, T data) {
        return GenericServerResponse.of(status.getCode(), status.getMsg(), data);
    }

    public static <T> GenericServerResponse<T> ofError(ServerStatus status) {
        return ofError(status, null);
    }

    public static <T> GenericServerResponse<T> of(String code, String msg, T data) {
        return new GenericServerResponse<>(code, msg, data);
    }

}
