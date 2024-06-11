package com.arthur.nacos.client.security;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.client.security.SecurityProxy;
import com.alibaba.nacos.common.utils.ThreadUtils;

import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.alibaba.nacos.client.constant.Constants.Security.SECURITY_INFO_REFRESH_INTERVAL_MILLS;

/**
 * 拓展{@link SecurityProxy}，新增定时更新nacos的令牌
 * <p>
 * 该类需要使用者自己调用{@link #shutdown()}释放资源
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-02
 * @see SecurityProxy
 * @since 1.0
 */
@SuppressWarnings({ "unused" ,"SpellCheckingInspection" })
public class ScheduledSecurityProxy extends DelegatingSecurityProxy {

	private ScheduledExecutorService executorService;
	private long delayInMills = SECURITY_INFO_REFRESH_INTERVAL_MILLS;

	public ScheduledSecurityProxy(SecurityProxy delegate) {
		super(delegate);
	}

	/**
	 * 参考 {@code com.alibaba.nacos.client.naming.NacosNamingMaintainService#initSecurityProxy(java.util.Properties)}
	 */
	public void initSecurityProxy(Properties properties) {
		this.executorService = new ScheduledThreadPoolExecutor(1, r -> {
			Thread t = new Thread(r);
			t.setName("com.arthur.nacos.client.scheduled.security");
			t.setDaemon(true);
			return t;
		});

		this.executorService.scheduleWithFixedDelay(() -> this.login(properties),
				this.delayInMills, this.delayInMills, TimeUnit.MILLISECONDS);
	}

	@Override
	public void shutdown() throws NacosException {
		super.shutdown();
		ThreadUtils.shutdownThreadPool(executorService);
	}

	public long getDelayInMills() {
		return delayInMills;
	}

	public void setDelayInMills(long delayInMills) {
		this.delayInMills = delayInMills;
	}

}
