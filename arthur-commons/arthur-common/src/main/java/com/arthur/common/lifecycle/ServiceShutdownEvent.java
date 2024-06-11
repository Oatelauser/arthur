package com.arthur.common.lifecycle;

import com.arthur.common.notify.event.Event;
import edu.umd.cs.findbugs.annotations.Nullable;

import java.io.Serial;

/**
 * 服务关闭事件
 *
 * @author DearYang
 * @date 2022-08-01
 * @since 1.0
 */
public class ServiceShutdownEvent extends Event {

    @Serial
	private static final long serialVersionUID = -8425892736783828721L;

    public ServiceShutdownEvent(@Nullable Object eventObject) {
        super(eventObject);
    }

}
