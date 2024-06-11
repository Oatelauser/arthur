package com.arthur.nacos.client.event;

import com.alibaba.nacos.common.notify.Event;
import com.arthur.nacos.client.model.Namespace;

import java.io.Serial;

/**
 * 命名空间变动通知时间
 * <p>
 * 变动类型{@link Namespace#getChangeType()}
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-03
 * @since 1.0
 */
public class NamespacesChangeEvent extends Event {

	@Serial
	private static final long serialVersionUID = 2565901336555131189L;

	private final Namespace[] namespaces;

	public NamespacesChangeEvent(Namespace[] namespaces) {
		this.namespaces = namespaces;
	}

	public Namespace[] getNamespaces() {
		return namespaces;
	}

}
