package com.arthur.gateway.filter;

import reactor.netty.http.server.logging.AccessLog;
import reactor.netty.http.server.logging.AccessLogArgProvider;
import reactor.netty.http.server.logging.AccessLogFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Objects;

import static com.arthur.plugin.constant.Constant.*;


/**
 * Spring Cloud Gateway AccessLog
 *
 * @author DearYang
 * @date 2022-07-21
 * @since 1.0
 */
public class NettyAccessLogFactory implements AccessLogFactory {

    private static final String MISSING = "-";
    private static final String DEFAULT_LOG_FORMAT = "[{},{},{}] -> {} - {} [{}] \"{} {} {}\" {} {} {} ms";

    static String applyAddress(SocketAddress socketAddress) {
        if (socketAddress instanceof InetSocketAddress inetSocketAddress) {
			return inetSocketAddress.getHostString() + ":" + inetSocketAddress.getPort();
        }

        return MISSING;
    }

    @Override
    public AccessLog apply(AccessLogArgProvider provider) {
        return AccessLog.create(DEFAULT_LOG_FORMAT,
                provider.responseHeader(SLEUTH_TRACE_ID) == null ? MISSING : provider.responseHeader(SLEUTH_SPAN_ID),
                provider.responseHeader(SLEUTH_SPAN_ID) == null ? MISSING : provider.responseHeader(SLEUTH_SPAN_ID),
                provider.responseHeader(SLEUTH_PARENT_SPAN_ID) == null ? MISSING : provider.responseHeader(SLEUTH_PARENT_SPAN_ID),
                applyAddress(Objects.requireNonNull(provider.connectionInformation()).remoteAddress()),
                provider.user(),
                provider.accessDateTime(),
                provider.method(),
                provider.uri(),
                provider.protocol(),
                provider.status(),
                provider.contentLength() > -1 ? provider.contentLength() : MISSING,
                provider.duration()
        );
    }

}
