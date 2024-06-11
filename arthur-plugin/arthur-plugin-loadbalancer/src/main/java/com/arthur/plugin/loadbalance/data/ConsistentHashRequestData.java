package com.arthur.plugin.loadbalance.data;

import com.arthur.plugin.loadbalance.support.ConsistentHashCalculator;
import org.springframework.cloud.client.loadbalancer.RequestData;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientRequest;

import java.net.URI;
import java.util.Map;

/**
 * 一致性哈希负载均衡请求数据
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-15
 * @since 1.0
 */
public class ConsistentHashRequestData extends RequestData {

    /**
     * 一致性哈希计算器
     */
    private ConsistentHashCalculator consistentHashCalculator;

    public ConsistentHashRequestData(HttpMethod httpMethod, URI url, HttpHeaders headers, MultiValueMap<String, String> cookies, Map<String, Object> attributes) {
        super(httpMethod, url, headers, cookies, attributes);
    }

    public ConsistentHashRequestData(ClientRequest request) {
        super(request);
    }

    public ConsistentHashRequestData(HttpRequest request) {
        super(request);
    }

    public ConsistentHashRequestData(ServerHttpRequest request) {
        this(request, Map.of());
    }

    public ConsistentHashRequestData(ServerHttpRequest request, Map<String, Object> attributes) {
        super(request, attributes);
    }

    public ConsistentHashCalculator getConsistentHashCalculator() {
        return consistentHashCalculator;
    }

    public void setConsistentHashCalculator(ConsistentHashCalculator consistentHashCalculator) {
        this.consistentHashCalculator = consistentHashCalculator;
    }

}
