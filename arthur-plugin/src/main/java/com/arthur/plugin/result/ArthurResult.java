package com.arthur.plugin.result;

import com.arthur.common.response.ServerResponse;
import com.arthur.common.response.ServerStatus;
import org.springframework.web.server.ServerWebExchange;


/**
 * arthur result
 *
 * @author DearYang
 * @date 2022-07-14
 * @since 1.0
 */
public class ArthurResult implements WebFluxResult<ServerResponse> {

    @Override
    public ServerResponse error(String code, String message, Object object) {
        return ServerResponse.ofError(code, message, object);
    }

    /**
     * Success object.
     *
     * @param exchange the exchange
     * @param object   the object
     * @return the success object
     */
    public static Object ofSuccess(final ServerWebExchange exchange, final Object object) {
        return build().result(exchange, object);
    }

    /**
     * Error object.
     *
     * @param exchange     the exchange
     * @param serverStatus the Result
     * @param object       the object
     * @return the object
     */
    public static Object ofError(final ServerWebExchange exchange, final ServerStatus serverStatus, final Object object) {
        return build().error(exchange, serverStatus.getCode(), serverStatus.getMsg(), object);
    }

    /**
     * Error object.
     *
     * @param serverStatus the Result
     * @param object       the object
     * @return the object
     */
    public static Object ofError(final ServerStatus serverStatus, final Object object) {
        return build().error(serverStatus.getCode(), serverStatus.getMsg(), object);
    }

    /**
     * Error object.
     *
     * @param exchange the exchange
     * @param code     the code
     * @param message  the message
     * @param object   the object
     * @return the object
     */
    public static Object ofError(final ServerWebExchange exchange, final String code, final String message, final Object object) {
        return build().error(exchange, code, message, object);
    }

    public static ArthurResult build() {
        return new ArthurResult();
    }

}
