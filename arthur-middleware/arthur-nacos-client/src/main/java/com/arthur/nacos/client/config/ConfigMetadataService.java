package com.arthur.nacos.client.config;

/**
 * 概要描述
 * <p>
 * 详细描述
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-05
 * @since 1.0
 */
public interface ConfigMetadataService {

	String getMetadataConfig(String dataId, String group, long timeoutMs);



}
