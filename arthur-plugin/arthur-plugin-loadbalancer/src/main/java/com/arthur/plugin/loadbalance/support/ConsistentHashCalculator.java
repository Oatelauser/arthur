package com.arthur.plugin.loadbalance.support;

import com.arthur.plugin.loadbalance.core.ConsistentHashLoadBalancer;
import org.springframework.cloud.client.loadbalancer.RequestData;

/**
 * 一致性哈希值动态计算器标识接口
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-16
 * @see ConsistentHashLoadBalancer
 * @since 1.0
 */
public interface ConsistentHashCalculator {

    /**
     * 构建{@link #getHashValue(RequestData)}方法字符串
     */
    String HASH_METHOD_PREFIX = "public String getHashValue(RequestData requestData) {return ";
    String HASH_METHOD_SUFFIX = ";}";

    /**
     * 计算哈希表达式
     *
     * @param requestData 请求数据
     * @return 待哈希的字符串
     */
    String getHashValue(RequestData requestData);

    static String buildHashMethod(String methodBody) {
        return HASH_METHOD_PREFIX + methodBody + HASH_METHOD_SUFFIX;
    }

}
