package com.broadtech.arthur.admin.rule.entiry.api.Constant;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/15
 */
public class SentinelGatewayConstants {
    public static final int APP_TYPE_GATEWAY = 1;

    public static final int RESOURCE_MODE_ROUTE_ID = 0;
    public static final int RESOURCE_MODE_CUSTOM_API_NAME = 1;

    public static final int PARAM_PARSE_STRATEGY_CLIENT_IP = 0;
    public static final int PARAM_PARSE_STRATEGY_HOST = 1;
    public static final int PARAM_PARSE_STRATEGY_HEADER = 2;
    public static final int PARAM_PARSE_STRATEGY_URL_PARAM = 3;
    public static final int PARAM_PARSE_STRATEGY_COOKIE = 4;

    public static final int URL_MATCH_STRATEGY_EXACT = 0;
    public static final int URL_MATCH_STRATEGY_PREFIX = 1;
    public static final int URL_MATCH_STRATEGY_REGEX = 2;

    public static final int PARAM_MATCH_STRATEGY_EXACT = 0;
    public static final int PARAM_MATCH_STRATEGY_PREFIX = 1;
    public static final int PARAM_MATCH_STRATEGY_REGEX = 2;
    public static final int PARAM_MATCH_STRATEGY_CONTAINS = 3;

    public static final String GATEWAY_CONTEXT_DEFAULT = "sentinel_gateway_context_default";
    public static final String GATEWAY_CONTEXT_PREFIX = "sentinel_gateway_context$$";
    public static final String GATEWAY_CONTEXT_ROUTE_PREFIX = "sentinel_gateway_context$$route$$";

    public static final String GATEWAY_NOT_MATCH_PARAM = "$NM";
    public static final String GATEWAY_DEFAULT_PARAM = "$D";

    private SentinelGatewayConstants() {}
}
