package com.arthur.plugin.loadbalance.support;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * Spel负载均衡动态表达式评估器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-15
 * @since 1.0
 */
@SuppressWarnings("SpellCheckingInspection")
public class LoadBalancerSpelEvaluator implements LoadBalancerEvaluator, BeanClassLoaderAware, InitializingBean {

	private static final EvaluationContext EVALUATION_CONTEXT =
		SimpleEvaluationContext.forReadOnlyDataBinding().build();

	private SpelExpressionParser spelParser;

	@Override
	public Object eval(String expression, Object rootObject) {
		return spelParser.parseExpression(expression).getValue(EVALUATION_CONTEXT, rootObject);
	}

	@Override
	public <T> T eval(String expression, Object rootObject, Class<T> type) {
		return spelParser.parseExpression(expression)
			.getValue(EVALUATION_CONTEXT, rootObject, type);
	}

	@Override
	public void setBeanClassLoader(@NonNull ClassLoader classLoader) {
		this.spelParser = new SpelExpressionParser(
			new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, classLoader));
	}

	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.spelParser, "spelParser");
	}

}
