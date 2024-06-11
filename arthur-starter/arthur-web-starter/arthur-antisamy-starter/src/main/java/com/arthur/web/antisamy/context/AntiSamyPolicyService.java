package com.arthur.web.antisamy.context;

import org.owasp.validator.html.Policy;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * AntiSamy策略服务
 * <p>
 * 缓存基于基本策略修改的AntiSamy策略
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-06
 * @since 1.0
 */
public interface AntiSamyPolicyService {

	/**
	 * 获取默认的策略
	 *
	 * @return 策略对象
	 */
	@NonNull
	Policy getDefaultPolicy();

	/**
	 * 查找策略
	 *
	 * @param policyId 策略ID
	 * @return 策略对象
	 */
	@Nullable
	Policy lookup(String policyId);

	/**
	 * 创建并获取策略
	 *
	 * @param context {@link AntiSamyPolicyContext}
	 * @return 策略对象
	 */
	Policy getPolicy(AntiSamyPolicyContext context);

	/**
	 * 获取策略，或者使用默认策略
	 *
	 * @param policyId 策略ID
	 * @return 策略对象
	 */
	default Policy getPolicyOrDefault(String policyId) {
		Policy policy = lookup(policyId);
		if (policy == null) {
			policy = getDefaultPolicy();
		}
		return policy;
	}

}
