package com.arthur.plugin.antisamy;

import com.arthur.cloud.gateway.AbstractSmartGatewayFilterFactory;
import com.arthur.web.antisamy.context.AntiSamyConfig;
import com.arthur.web.antisamy.context.AntiSamyPolicyService;
import com.arthur.web.antisamy.context.AntiSamyService;
import org.owasp.validator.html.Policy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.arthur.web.antisamy.constant.AntiSamyConstants.ORDER;
import static org.springframework.cloud.gateway.support.GatewayToStringStyler.filterToStringCreator;

/**
 * AntiSamy GatewayFilterFactory
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-06
 * @since 1.0
 */
public class AntiSamyGatewayFilterFactory extends AbstractSmartGatewayFilterFactory<AntiSamyGatewayFilterFactory.Config> {

	private static final Logger LOG = LoggerFactory.getLogger(AntiSamyGatewayFilterFactory.class);

	private final AntiSamyPolicyService antiSamyService;

	public AntiSamyGatewayFilterFactory(AntiSamyPolicyService antiSamyService) {
		this.antiSamyService = antiSamyService;
	}

	@Override
	public GatewayFilter apply(Config config) {
		String policyId = config.policyId;
		Policy policy = antiSamyService.lookup(policyId);
		if (policy == null && LOG.isDebugEnabled()) {
			LOG.debug("Cannot load antisamy policy by policyId [{}]", policyId);
		}

		AntiSamyService samyService = policy == null ? null : AntiSamyService.create(policy, config);
		return new AntiSamyGatewayFilter(samyService);
	}

	static class AntiSamyGatewayFilter implements GatewayFilter, Ordered {

		private final Mono<AntiSamyService> antiSamyService;

		public AntiSamyGatewayFilter(AntiSamyService antiSamyService) {
			this.antiSamyService = Mono.justOrEmpty(antiSamyService);
		}

		@Override
		public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
			return antiSamyService.switchIfEmpty(Mono.defer(() -> chain.filter(exchange).then(Mono.empty())))
				.flatMap(service -> this.doFilter(service, exchange, chain));
		}

		private Mono<Void> doFilter(AntiSamyService antiSamyService, ServerWebExchange exchange, GatewayFilterChain chain) {
			//return Mono.just(new AntiSamyServerHttpRequestDecorator(exchange.getRequest(), antiSamyService))
			//	.map(request -> exchange.mutate().request(request).build())
			//	.flatMap(chain::filter);
			return Mono.empty();
		}

		@Override
		public int getOrder() {
			return ORDER;
		}

		@Override
		public String toString() {
			return filterToStringCreator(AntiSamyGatewayFilter.this).toString();
		}
	}

	@SuppressWarnings("unused")
	static class Config extends AntiSamyConfig {

		private String policyId;

		public String getPolicyId() {
			return policyId;
		}

		public void setPolicyId(String policyId) {
			this.policyId = policyId;
		}

	}

}
