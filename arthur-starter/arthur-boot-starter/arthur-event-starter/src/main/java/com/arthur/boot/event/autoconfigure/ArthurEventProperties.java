package com.arthur.boot.event.autoconfigure;

import com.arthur.common.notify.publisher.EventPublisher;
import com.arthur.common.constant.BaseConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 创建{@link EventPublisher}的配置
 *
 * @author DearYang
 * @date 2022-07-28
 * @since 1.0
 */
@ConfigurationProperties(prefix = "arthur.event")
public class ArthurEventProperties {

    private int ringBufferSize = BaseConstants.RING_BUFFER_SIZE;
    private int threadSize = BaseConstants.DISRUPTOR_CONSUMER_SIZE;

    public int getRingBufferSize() {
        return ringBufferSize;
    }

    public void setRingBufferSize(int ringBufferSize) {
        this.ringBufferSize = ringBufferSize;
    }

    public int getThreadSize() {
        return threadSize;
    }

    public void setThreadSize(int threadSize) {
        this.threadSize = threadSize;
    }

}
