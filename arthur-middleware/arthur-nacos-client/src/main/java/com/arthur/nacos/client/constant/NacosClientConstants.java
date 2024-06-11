package com.arthur.nacos.client.constant;

import com.alibaba.nacos.api.common.Constants;
import com.alibaba.nacos.client.naming.utils.UtilAndComs;

import static com.alibaba.nacos.api.naming.CommonParams.NAMESPACE_ID;

/**
 * Nacos常量类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-02
 * @since 1.0
 */
@SuppressWarnings("SpellCheckingInspection")
public interface NacosClientConstants {

	String NS_BASE_URL = UtilAndComs.nacosUrlBase.replace("ns", "");
	String NS_URL = NS_BASE_URL + "console/namespaces";

	/**
	 * 避免namespace冲突，新建key
	 *
	 * @see com.arthur.nacos.client.naming.NamespaceNamingHttpClientProxy
	 */
	String NS_NAMESPACE_ID = "__" + NAMESPACE_ID + "__";

	/**
	 * 命名空间API无效参数
	 */
	int NS_INVALID_PARAM = 601;

	/**
	 * 默认命名空间不能修改
	 */
	int NS_INVALID_ID = 602;

	/**
	 * 默认命名空间ID
	 */
	String DEFAULT_NAMESPACE_ID = Constants.DEFAULT_NAMESPACE_ID;

}
