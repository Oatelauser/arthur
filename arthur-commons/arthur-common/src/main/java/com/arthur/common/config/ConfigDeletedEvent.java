package com.arthur.common.config;

import com.arthur.common.notify.event.Event;

import java.io.Serial;

/**
 * 配置中心文件删除事件
 *
 * @author DearYang
 * @date 2022-07-26
 * @since 1.0
 */
public class ConfigDeletedEvent extends Event {

    @Serial
	private static final long serialVersionUID = 9143909140696158162L;

    public ConfigDeletedEvent(ConfigMetaData meta) {
        super(meta);
    }

}
