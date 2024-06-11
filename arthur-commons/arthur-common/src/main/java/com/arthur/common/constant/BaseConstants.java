package com.arthur.common.constant;

import com.arthur.common.notify.publisher.DisruptorEventPublisher;

/**
 * 常量
 *
 * @author DearYang
 * @date 2022-07-27
 * @since 1.0
 */
@SuppressWarnings("unused")
public interface BaseConstants {

	/**
	 * {@link com.lmax.disruptor.RingBuffer} size
	 *
	 * @see DisruptorEventPublisher
	 */
	int RING_BUFFER_SIZE = 4096 << 1 << 1;

	/**
	 * disruptor consumer thread size
	 *
	 * @see DisruptorEventPublisher
	 */
	int DISRUPTOR_CONSUMER_SIZE = Runtime.getRuntime().availableProcessors() << 1;

	/**
	 * 最大查询深度
	 */
	int MAX_SEARCH_DEEP = 10;

	/**
	 * 横线
	 */
	String LINE_DELIMITER = "-";

	/**
	 * 点号
	 */
	String DOT = ".";

	/**
	 * 冒号
	 */
	String COLON = ":";

	/**
	 * 单斜杠
	 */
	String SLASH = "/";

	/**
	 * 双斜杠
	 */
	String DOUBLE_SLASH = "//";

	/**
	 * 逗号
	 */
	String COMMA = ",";

	/**
	 * 字符串false
	 */
	String STRING_FALSE = "false";

	/**
	 * 字符串true
	 */
	String STRING_TRUE = "true";

}
