package com.arthur.nacos.client.utils;

import com.alibaba.nacos.common.utils.StringUtils;

import static com.arthur.nacos.client.constant.NacosClientConstants.DEFAULT_NAMESPACE_ID;

/**
 * 命名空间工具类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-03-03
 * @since 1.0
 */
public abstract class NamespaceUtils {

	private static final String NAMESPACE_NULL_KEY = "null";

	/**
	 * 解析命名空间ID
	 *
	 * @param namespaceId 命名空间ID
	 */
	public static String resolveNamespace(String namespaceId) {
		if (StringUtils.isBlank(namespaceId) || DEFAULT_NAMESPACE_ID.equalsIgnoreCase(namespaceId)
			|| NAMESPACE_NULL_KEY.equalsIgnoreCase(namespaceId)) {
			return DEFAULT_NAMESPACE_ID;
		}
		return namespaceId.trim();
	}

}
