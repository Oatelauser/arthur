package com.arthur.sentinel.scg;

import com.alibaba.csp.sentinel.config.SentinelConfig;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.init.InitOrder;
import com.arthur.common.utils.ClassUtils;

import static com.alibaba.csp.sentinel.init.InitOrder.HIGHEST_PRECEDENCE;

/**
 * 设置网关标识，避免在命令行添加参数
 *
 * @author DearYang
 * @date 2022-09-15
 * @since 1.0
 */
@InitOrder(HIGHEST_PRECEDENCE)
public class GatewayInitFunc implements InitFunc {

	@Override
	public void init() {
		try {
			Class.forName("com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager",
				false, ClassUtils.getDefaultClassLoader());
			System.setProperty(SentinelConfig.APP_TYPE_PROP_KEY, "11");
		} catch (Exception ignore) {
		}
	}

}
