package com.arthur.web.antisamy.context;

import org.owasp.validator.html.InternalPolicy;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.model.Property;
import org.owasp.validator.html.model.Tag;

import java.util.Map;

/**
 * AntiSamy XSS 防御策略
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-06
 * @since 1.0
 */
@SuppressWarnings("all")
public class AntiSamyPolicy extends InternalPolicy {

	public static final String DEFAULT_POLICY_RESOURCE = DEFAULT_POLICY_URI;

	public AntiSamyPolicy(ParseContext parseContext) {
		super(parseContext);
	}

	public AntiSamyPolicy(Policy old, Map<String, String> directives,
			Map<String, Tag> tagRules, Map<String, Property> cssRules) {
		super(old, directives, tagRules, cssRules);
	}

}
