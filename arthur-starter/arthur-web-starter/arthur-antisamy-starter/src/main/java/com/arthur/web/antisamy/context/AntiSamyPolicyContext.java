package com.arthur.web.antisamy.context;

import com.arthur.web.antisamy.constant.AntiSamyPolicyType;
import org.owasp.validator.html.model.Property;
import org.owasp.validator.html.model.Tag;

import java.util.Map;

/**
 * 策略上下文
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-07
 * @since 1.0
 */
public class AntiSamyPolicyContext {

	private String policyId;
	private AntiSamyPolicyType policyType;
	private Map<String, String> directives;
	private Map<String, Tag> tagRules;
	private Map<String, Property> cssRules;

	public String getPolicyId() {
		return policyId;
	}

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}

	public AntiSamyPolicyType getPolicyType() {
		return policyType;
	}

	public void setPolicyType(AntiSamyPolicyType policyType) {
		this.policyType = policyType;
	}

	public Map<String, String> getDirectives() {
		return directives;
	}

	public void setDirectives(Map<String, String> directives) {
		this.directives = directives;
	}

	public Map<String, Tag> getTagRules() {
		return tagRules;
	}

	public void setTagRules(Map<String, Tag> tagRules) {
		this.tagRules = tagRules;
	}

	public Map<String, Property> getCssRules() {
		return cssRules;
	}

	public void setCssRules(Map<String, Property> cssRules) {
		this.cssRules = cssRules;
	}

}
