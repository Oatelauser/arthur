package com.arthur.web.model;

import com.arthur.web.server.ServerStatus;

import java.io.Serial;

/**
 * unified response data struct
 *
 * @author DearYang
 */
@SuppressWarnings("unused")
public class ServerResponse extends GenericServerResponse<Object> {

    @Serial
	private static final long serialVersionUID = 8797427940990824066L;

    public ServerResponse() {
    }

    public ServerResponse(String code, String msg, Object data) {
        super(code, msg, data);
    }

    public static ServerResponse ofSuccess() {
        return ServerResponse.ofSuccess((String) null, null);
    }

    public static ServerResponse ofSuccessMessage(String msg) {
        return ServerResponse.ofSuccess(msg, null);
    }

    public static ServerResponse ofSuccess(Object data) {
        return ServerResponse.ofSuccess((String) null, data);
    }

    public static ServerResponse ofSuccess(String msg, Object data) {
        return ServerResponse.ofSuccess(ArthurStatusEnum.SUCCESS.getCode(), msg, data);
    }

    public static ServerResponse ofSuccess(String code, String msg, Object data) {
        return ServerResponse.of(code, msg, data);
    }

    public static ServerResponse ofSuccess(ServerStatus status, Object data) {
        return of(status.getCode(), status.getMsg(), data);
    }

    public static ServerResponse ofSuccess(ServerStatus status) {
        return ofSuccess(status, null);
    }

    public static ServerResponse ofError() {
        return ServerResponse.of(null, null, null);
    }

    public static ServerResponse ofError(String errorCode, String errorMessage) {
        return ServerResponse.ofError(errorCode, errorMessage, null);
    }

    public static ServerResponse ofError(String errorCode, String errorMessage, Object data) {
        return ServerResponse.of(errorCode, errorMessage, data);
    }

    public static ServerResponse ofError(ServerStatus status, Object data) {
        return of(status.getCode(), status.getMsg(), data);
    }

    public static ServerResponse ofError(ServerStatus status) {
        return ofError(status, null);
    }

    public static ServerResponse of(String code, String msg, Object data) {
        return new ServerResponse(code, msg, data);
    }

}
