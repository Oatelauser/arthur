package com.arthur.plugin.loadbalance.support;

/**
 * 负载均衡动态表达式评估器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-15
 * @since 1.0
 */
public interface LoadBalancerEvaluator {

	/**
	 * 执行负载均衡动态表达式
	 *
	 * @param expression 表达式
	 * @param rootObject 表达式中的参数
	 * @return 表达式执行结果
	 */
	Object eval(String expression, Object rootObject);

	/**
	 * 执行负载均衡动态表达式
	 *
	 * @param expression 表达式
	 * @param rootObject 表达式中的参数
	 * @param type       执行结果类型
	 * @param <T>        执行结果泛型
	 * @return 表达式执行结果
	 */
	<T> T eval(String expression, Object rootObject, Class<T> type);

}
