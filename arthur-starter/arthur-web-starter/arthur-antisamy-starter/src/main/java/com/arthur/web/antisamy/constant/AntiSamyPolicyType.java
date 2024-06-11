package com.arthur.web.antisamy.constant;

import org.owasp.validator.html.Policy;

import java.net.URL;

import static com.arthur.web.antisamy.context.AntiSamyPolicy.DEFAULT_POLICY_RESOURCE;

/**
 * AntiSamy策略类型
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-07
 * @since 1.0
 */
public enum AntiSamyPolicyType {

	/**
	 * 允许大部分HTML通过
	 */
	DEFAULT {
		@Override
		public URL getResource() {
			return Policy.class.getClassLoader().getResource(DEFAULT_POLICY_RESOURCE);
		}
	},

	/**
	 * 非常危险，允许HTML、CSS、JS通过
	 */
	ANYTHING {
		@Override
		public URL getResource() {
			return Policy.class.getClassLoader().getResource("antisamy-anythinggoes.xml");
		}
	},

	/**
	 * 相对安全，对内容过滤，适用于电子商务网站，允许用户输入HTML脚本作为页面的一部分
	 */
	EBAY {
		@Override
		public URL getResource() {
			return Policy.class.getClassLoader().getResource("antisamy-ebay.xml");
		}
	},

	/**
	 * 相对安全，只允许文本格式通过
	 */
	TINYMCE {
		@Override
		public URL getResource() {
			return Policy.class.getClassLoader().getResource("antisamy-tinymce.xml");
		}
	},

	/**
	 * 相对危险，适用于社交网站，允许用户输入作为整个页面
	 */
	MYSPACE {
		@Override
		public URL getResource() {
			return Policy.class.getClassLoader().getResource("antisamy-myspace.xml");
		}
	},

	/**
	 * 适用于新闻网站的评论过滤
	 */
	SLASHDOT {
		@Override
		public URL getResource() {
			return Policy.class.getClassLoader().getResource("antisamy-slashdot.xml");
		}
	},
	;

	/**
	 * 获取策略资源
	 */
	public abstract URL getResource();

}
