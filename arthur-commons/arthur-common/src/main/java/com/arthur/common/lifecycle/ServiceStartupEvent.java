package com.arthur.common.lifecycle;

import com.arthur.common.notify.event.Event;
import edu.umd.cs.findbugs.annotations.Nullable;

import java.io.Serial;

/**
 * 服务启动事件
 *
 * @author DearYang
 * @date 2022-08-02
 * @since 1.0
 */
public class ServiceStartupEvent extends Event {


    @Serial
	private static final long serialVersionUID = 5912927361511540848L;

    public ServiceStartupEvent(@Nullable Object eventObject) {
        super(eventObject);
    }

}
