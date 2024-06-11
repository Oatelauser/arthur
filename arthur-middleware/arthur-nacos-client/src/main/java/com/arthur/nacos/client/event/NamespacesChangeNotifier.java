package com.arthur.nacos.client.event;

import com.alibaba.nacos.common.notify.Event;
import com.alibaba.nacos.common.notify.NotifyCenter;
import com.alibaba.nacos.common.notify.listener.Subscriber;

/**
 * 命名空间变化事件通知器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-03
 * @since 1.0
 */
public class NamespacesChangeNotifier extends Subscriber<NamespacesChangeEvent> {

	public NamespacesChangeNotifier() {
		NotifyCenter.registerSubscriber(this);
	}

	@Override
	public void onEvent(NamespacesChangeEvent event) {
	}

	@Override
	public Class<? extends Event> subscribeType() {
		return NamespacesChangeEvent.class;
	}

}
