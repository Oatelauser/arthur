package com.arthur.boot.aware;

import com.arthur.common.notify.publisher.EventPublisher;

/**
 * 概要描述
 * <p>
 * 详细描述
 *
 * @author DearYang
 * @date 2022-08-29
 * @since 1.0
 */
public class MyBean implements GenericAware<EventPublisher> {

	@Override
	public void set(EventPublisher bean) {
		System.out.println(bean);
	}

	public static class MyBean2 extends MyBean {
	}

}
