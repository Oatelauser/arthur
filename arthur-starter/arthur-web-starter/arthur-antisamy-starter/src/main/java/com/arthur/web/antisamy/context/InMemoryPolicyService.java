package com.arthur.web.antisamy.context;

import com.arthur.web.antisamy.autoconfigure.AntiSamyProperties;
import com.arthur.web.antisamy.constant.AntiSamyPolicyType;
import org.owasp.validator.html.Policy;
import org.springframework.lang.NonNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存实现的{@link AntiSamyPolicyService}
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-06
 * @since 1.0
 */
@SuppressWarnings("SpellCheckingInspection")
public class InMemoryPolicyService implements AntiSamyPolicyService {

	private volatile Policy defaultPolicy;
	private final AntiSamyProperties properties;
	private final Map<String, Policy> antiSamyPolicys = new ConcurrentHashMap<>(16);

	public InMemoryPolicyService(AntiSamyProperties properties) {
		this.properties = properties;
	}

	@NonNull
	@Override
	public Policy getDefaultPolicy() {
		if (defaultPolicy == null) {
			synchronized (this) {
				if (defaultPolicy == null) {
					AntiSamyPolicyType defaultPolicyType = properties.getDefaultPolicyType();
					defaultPolicy = AntiSamyLoader.getPolicy(defaultPolicyType);
				}
			}
		}
		return defaultPolicy;
	}

	@Override
	public Policy lookup(String policyId) {
		return antiSamyPolicys.get(policyId);
	}

	@Override
	public Policy getPolicy(AntiSamyPolicyContext context) {
		Policy policy = AntiSamyLoader.getPolicy(context.getPolicyType());
		policy = new AntiSamyPolicy(policy, context.getDirectives(),
			context.getTagRules(), context.getCssRules());
		antiSamyPolicys.put(context.getPolicyId(), policy);
		return policy;
	}

}
