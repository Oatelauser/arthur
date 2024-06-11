package com.arthur.gateway.filter;

import com.arthur.gateway.autoconfigure.NettyTcpProperties;
import io.netty.channel.ChannelOption;
import io.netty.channel.WriteBufferWaterMark;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.logging.AccessLogFactory;
import reactor.netty.resources.LoopResources;

/**
 * netty configuration & access log
 *
 * @author DearYang
 * @date 2022-07-17
 * @since 1.0
 */
public class ConfigurationNettyCustomizer implements NettyServerCustomizer {

    private final NettyTcpProperties nettyTcpProperties;
    private final ObjectProvider<AccessLogFactory> accessLog;

    /**
     * Instantiates a new Event loop netty customizer.
     *
     * @param nettyTcpProperties the netty tcp config
     */
    public ConfigurationNettyCustomizer(NettyTcpProperties nettyTcpProperties, ObjectProvider<AccessLogFactory> accessLog) {
        this.accessLog = accessLog;
        this.nettyTcpProperties = nettyTcpProperties;
    }

    @Override
    public HttpServer apply(final HttpServer httpServer) {
        HttpServer customizeHttpServer = httpServer.runOn(LoopResources.create("arthur-netty", nettyTcpProperties.getSelectCount(), nettyTcpProperties.getWorkerCount(), true), true)
                .option(ChannelOption.SO_BACKLOG, nettyTcpProperties.getServerSocketChannel().getSoBacklog())
                .option(ChannelOption.SO_REUSEADDR, nettyTcpProperties.getServerSocketChannel().isSoReuseAddr())
                .option(ChannelOption.SO_RCVBUF, nettyTcpProperties.getServerSocketChannel().getSoRcvBuf())
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, nettyTcpProperties.getServerSocketChannel().getConnectTimeoutMillis())
                .option(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(nettyTcpProperties.getServerSocketChannel().getWriteBufferLowWaterMark(),
                        nettyTcpProperties.getServerSocketChannel().getWriteBufferHighWaterMark()))
                .option(ChannelOption.WRITE_SPIN_COUNT, nettyTcpProperties.getServerSocketChannel().getWriteSpinCount())
                //.option(ChannelOption.AUTO_READ, nettyTcpProperties.getServerSocketChannel().isAutoRead())
                .option(ChannelOption.ALLOCATOR, nettyTcpProperties.getServerSocketChannel().getAllocator())

                .childOption(ChannelOption.SO_KEEPALIVE, nettyTcpProperties.getSocketChannel().isSoKeepAlive())
                .childOption(ChannelOption.SO_REUSEADDR, nettyTcpProperties.getSocketChannel().isSoReuseAddr())
                .childOption(ChannelOption.SO_LINGER, nettyTcpProperties.getSocketChannel().getSoLinger())
                .childOption(ChannelOption.TCP_NODELAY, nettyTcpProperties.getSocketChannel().isTcpNoDelay())
                .childOption(ChannelOption.SO_RCVBUF, nettyTcpProperties.getSocketChannel().getSoRcvBuf())
                .childOption(ChannelOption.SO_SNDBUF, nettyTcpProperties.getSocketChannel().getSoSndBuf())
                .childOption(ChannelOption.IP_TOS, nettyTcpProperties.getSocketChannel().getIpTos())
                .childOption(ChannelOption.ALLOW_HALF_CLOSURE, nettyTcpProperties.getSocketChannel().isAllowHalfClosure())
                .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, nettyTcpProperties.getSocketChannel().getConnectTimeoutMillis())
                .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(nettyTcpProperties.getSocketChannel().getWriteBufferLowWaterMark(),
                        nettyTcpProperties.getSocketChannel().getWriteBufferHighWaterMark()))
                .childOption(ChannelOption.WRITE_SPIN_COUNT, nettyTcpProperties.getSocketChannel().getWriteSpinCount())
                //.childOption(ChannelOption.AUTO_READ, nettyTcpProperties.getSocketChannel().isAutoRead())
                .childOption(ChannelOption.ALLOCATOR, nettyTcpProperties.getSocketChannel().getAllocator());

        AccessLogFactory accessLogFactory = accessLog.getIfAvailable();
        if (accessLogFactory != null) {
            customizeHttpServer.accessLog(true, accessLogFactory);
        }

        return customizeHttpServer;
    }

}
