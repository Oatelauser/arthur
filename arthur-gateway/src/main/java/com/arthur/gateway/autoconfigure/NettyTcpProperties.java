/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.arthur.gateway.autoconfigure;

import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * The netty tcp properties.
 */
@ConfigurationProperties(prefix = "netty.tcp")
public class NettyTcpProperties {

    private Boolean webServerFactoryEnabled = true;

    private Integer selectCount = 1;

    private Integer workerCount = Runtime.getRuntime().availableProcessors() << 1;

    @NestedConfigurationProperty
    private ServerSocketChannelProperties serverSocketChannel = new ServerSocketChannelProperties();

    @NestedConfigurationProperty
    private SocketChannelProperties socketChannel = new SocketChannelProperties();

    /**
     * gets webServerFactoryEnabled.
     *
     * @return webServerFactoryEnabled
     */
    public Boolean isWebServerFactoryEnabled() {
        return webServerFactoryEnabled;
    }

    /**
     * set webServerFactoryEnabled.
     * set to false, user can custom the netty tcp server config.
     *
     * @param webServerFactoryEnabled web server factory enabled
     */
    public void setWebServerFactoryEnabled(final Boolean webServerFactoryEnabled) {
        this.webServerFactoryEnabled = webServerFactoryEnabled;
    }

    /**
     * get select count.
     *
     * @return selectCount
     */
    public Integer getSelectCount() {
        return selectCount;
    }

    /**
     * set selectCount.
     *
     * @param selectCount select count
     */
    public void setSelectCount(final Integer selectCount) {
        this.selectCount = selectCount;
    }

    /**
     * get workerCount.
     *
     * @return workerCount
     */
    public Integer getWorkerCount() {
        return workerCount;
    }

    /**
     * set workerCount.
     *
     * @param workerCount worker count
     */
    public void setWorkerCount(final Integer workerCount) {
        this.workerCount = workerCount;
    }

    /**
     * get serverSocketChannel.
     *
     * @return serverSocketChannel
     */
    public ServerSocketChannelProperties getServerSocketChannel() {
        return serverSocketChannel;
    }

    /**
     * set serverSocketChannel.
     *
     * @param serverSocketChannel server socket channel config
     */
    public void setServerSocketChannel(final ServerSocketChannelProperties serverSocketChannel) {
        this.serverSocketChannel = serverSocketChannel;
    }

    /**
     * set socketChannel.
     *
     * @param socketChannel socket channel config
     */
    public void setSocketChannel(final SocketChannelProperties socketChannel) {
        this.socketChannel = socketChannel;
    }

    /**
     * get socketChannel.
     *
     * @return socketChannel
     */
    public SocketChannelProperties getSocketChannel() {
        return socketChannel;
    }

    /**
     * Netty channel properties.
     */
    public static class NettyChannelProperties {

        private static final String UN_POOLED = "unpooled";

        private static final String POOLED = "pooled";

        private Integer connectTimeoutMillis = 10000;

        private Integer writeBufferHighWaterMark = 65536;

        private Integer writeBufferLowWaterMark = 32768;

        private Integer writeSpinCount = 16;

        private Boolean autoRead = true;

        /**
         * options: unpooled or pooled.
         */
        private String allocType = POOLED;

        private Boolean soReuseAddr = false;

        private Integer soRcvBuf = 87380;

        /**
         * get connectTimeoutMillis.
         *
         * @return connectTimeoutMillis
         */
        public Integer getConnectTimeoutMillis() {
            return connectTimeoutMillis;
        }

        /**
         * set connectTimeoutMillis.
         *
         * @param connectTimeoutMillis the connection timeout millis
         */
        public void setConnectTimeoutMillis(final Integer connectTimeoutMillis) {
            this.connectTimeoutMillis = connectTimeoutMillis;
        }

        /**
         * get writeBufferHighWaterMark.
         *
         * @return writeBufferHighWaterMark
         */
        public Integer getWriteBufferHighWaterMark() {
            return writeBufferHighWaterMark;
        }

        /**
         * set writeBufferHighWaterMark.
         *
         * @param writeBufferHighWaterMark write buffer high water mark
         */
        public void setWriteBufferHighWaterMark(final Integer writeBufferHighWaterMark) {
            this.writeBufferHighWaterMark = writeBufferHighWaterMark;
        }

        /**
         * get writeBufferLowWaterMark.
         *
         * @return writeBufferLowWaterMark
         */
        public Integer getWriteBufferLowWaterMark() {
            return writeBufferLowWaterMark;
        }

        /**
         * set writeBufferLowWaterMark.
         *
         * @param writeBufferLowWaterMark write buffer low water mark
         */
        public void setWriteBufferLowWaterMark(final Integer writeBufferLowWaterMark) {
            this.writeBufferLowWaterMark = writeBufferLowWaterMark;
        }

        /**
         * get writeSpinCount.
         *
         * @return writeSpinCount
         */
        public Integer getWriteSpinCount() {
            return writeSpinCount;
        }

        /**
         * set writeSpinCount.
         *
         * @param writeSpinCount WRITE_SPIN_COUNT
         */
        public void setWriteSpinCount(final Integer writeSpinCount) {
            this.writeSpinCount = writeSpinCount;
        }

        /**
         * get autoRead.
         *
         * @return autoRead
         */
        public Boolean isAutoRead() {
            return autoRead;
        }

        /**
         * set autoRead.
         *
         * @param autoRead AUTO_READ
         */
        public void setAutoRead(final Boolean autoRead) {
            this.autoRead = autoRead;
        }

        /**
         * get allocator.
         *
         * @return ByteBufAllocator
         */
        public ByteBufAllocator getAllocator() {
            return UN_POOLED.equals(getAllocType()) ? UnpooledByteBufAllocator.DEFAULT : PooledByteBufAllocator.DEFAULT;
        }

        /**
         * get allocator type.
         *
         * @return allocator type
         */
        public String getAllocType() {
            return allocType;
        }

        /**
         * set allocator type.
         *
         * @param allocType allocator type
         */
        public void setAllocType(final String allocType) {
            this.allocType = allocType;
        }

        /**
         * get SoReuseaddr.
         *
         * @return soReuseAddr
         */
        public Boolean isSoReuseAddr() {
            return soReuseAddr;
        }

        /**
         * ser setSoReuseAddr.
         *
         * @param soReuseAddr SO_REUSEADDR
         */
        public void setSoReuseAddr(final Boolean soReuseAddr) {
            this.soReuseAddr = soReuseAddr;
        }

        /**
         * get soRcvBuf.
         *
         * @return soRcvBuf
         */
        public Integer getSoRcvBuf() {
            return soRcvBuf;
        }

        /**
         * set soRcvBuf.
         *
         * @param soRcvBuf SO_RCVBUF
         */
        public void setSoRcvBuf(final Integer soRcvBuf) {
            this.soRcvBuf = soRcvBuf;
        }
    }


    public static class ServerSocketChannelProperties extends NettyChannelProperties {

        private Integer soBacklog = 128;

        /**
         * get soBacklog.
         *
         * @return soBacklog
         */
        public Integer getSoBacklog() {
            return soBacklog;
        }

        /**
         * set soBacklog.
         *
         * @param soBacklog SO_BACKLOG
         */
        public void setSoBacklog(final Integer soBacklog) {
            this.soBacklog = soBacklog;
        }
    }

    public static class SocketChannelProperties extends NettyChannelProperties {

        private Boolean soKeepAlive = false;

        private Integer soLinger = -1;

        private Boolean tcpNoDelay = true;

        private Integer soSndBuf = 16384;

        private Integer ipTos = 0;

        private Boolean allowHalfClosure = false;

        /**
         * get soKeepAlive.
         *
         * @return soKeepAlive
         */
        public Boolean isSoKeepAlive() {
            return soKeepAlive;
        }

        /**
         * set soKeepAlive.
         *
         * @param soKeepAlive SO_KEEPALIVE
         */
        public void setSoKeepAlive(final Boolean soKeepAlive) {
            this.soKeepAlive = soKeepAlive;
        }

        /**
         * get soLinger.
         *
         * @return soLinger
         */
        public Integer getSoLinger() {
            return soLinger;
        }

        /**
         * set soLinger.
         *
         * @param soLinger SO_LINGER
         */
        public void setSoLinger(final Integer soLinger) {
            this.soLinger = soLinger;
        }

        /**
         * get tcpNoDelay.
         *
         * @return tcpNoDelay
         */
        public Boolean isTcpNoDelay() {
            return tcpNoDelay;
        }

        /**
         * set tcpNoDelay.
         *
         * @param tcpNoDelay TCP_NODELAY
         */
        public void setTcpNoDelay(final Boolean tcpNoDelay) {
            this.tcpNoDelay = tcpNoDelay;
        }

        /**
         * get soSndBuf.
         *
         * @return soSndBuf
         */
        public Integer getSoSndBuf() {
            return soSndBuf;
        }

        /**
         * set soSndBuf.
         *
         * @param soSndBuf SO_SNDBUF
         */
        public void setSoSndBuf(final Integer soSndBuf) {
            this.soSndBuf = soSndBuf;
        }

        /**
         * get ipTos.
         * @return ipTos
         */
        public Integer getIpTos() {
            return ipTos;
        }

        /**
         * set ipTos.
         *
         * @param ipTos IP_TOS
         */
        public void setIpTos(final Integer ipTos) {
            this.ipTos = ipTos;
        }

        /**
         * get isAllowHalfClosure.
         *
         * @return isAllowHalfClosure
         */
        public Boolean isAllowHalfClosure() {
            return allowHalfClosure;
        }

        /**
         * set allowHalfClosure.
         *
         * @param allowHalfClosure ALLOW_HALF_CLOSURE
         */
        public void setAllowHalfClosure(final Boolean allowHalfClosure) {
            this.allowHalfClosure = allowHalfClosure;
        }
    }
}
