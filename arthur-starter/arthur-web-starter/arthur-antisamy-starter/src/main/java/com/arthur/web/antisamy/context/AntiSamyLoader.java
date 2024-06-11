package com.arthur.web.antisamy.context;

import com.arthur.web.antisamy.constant.AntiSamyPolicyType;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AntiSamy策略加载器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-07
 * @see AntiSamyPolicyService
 * @since 1.0
 */
public class AntiSamyLoader {

	/**
	 * 缓存最基本的AntiSamy策略
	 */
	private static final Map<AntiSamyPolicyType, Policy> BASE_POLICY = new ConcurrentHashMap<>(8);

	public static Policy getPolicy(AntiSamyPolicyType policyType) {
		return BASE_POLICY.computeIfAbsent(policyType, key -> {
			try {
				return Policy.getInstance(policyType.getResource());
			} catch (PolicyException e) {
				throw new IllegalStateException(e);
			}
		});
	}

}
