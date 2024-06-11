package com.arthur.common.invoke;

import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.*;

import static java.lang.invoke.MethodType.methodType;

/**
 * lambda表达式信息注册器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-22
 * @since 1.0
 */
public abstract class LambdaRegistry {

	@SuppressWarnings("rawtypes")
	private static final Map<Class, LambdaInfo> LAMBDA_MAPPING = new ConcurrentHashMap<>();

	static {
		LAMBDA_MAPPING.put(Function.class, new LambdaInfo("apply",
			methodType(Object.class, Object.class)));
		LAMBDA_MAPPING.put(BiFunction.class, new LambdaInfo("apply",
			methodType(Object.class, Object.class, Object.class)));

		LAMBDA_MAPPING.put(IntFunction.class, new LambdaInfo("apply",
			methodType(Object.class, int.class)));
		LAMBDA_MAPPING.put(LongFunction.class, new LambdaInfo("apply",
			methodType(Object.class, long.class)));
		LAMBDA_MAPPING.put(DoubleFunction.class, new LambdaInfo("apply",
			methodType(Object.class, double.class)));

		LAMBDA_MAPPING.put(ToIntFunction.class, new LambdaInfo("applyAsInt",
			methodType(int.class, Object.class)));
		LAMBDA_MAPPING.put(ToLongFunction.class, new LambdaInfo("applyAsLong",
			methodType(long.class, Object.class)));
		LAMBDA_MAPPING.put(ToDoubleFunction.class, new LambdaInfo("applyAsDouble",
			methodType(double.class, Object.class)));

		LAMBDA_MAPPING.put(ToIntBiFunction.class, new LambdaInfo("applyAsInt",
			methodType(int.class, Object.class, Object.class)));
		LAMBDA_MAPPING.put(ToLongBiFunction.class, new LambdaInfo("applyAsLong",
			methodType(long.class, Object.class, Object.class)));
		LAMBDA_MAPPING.put(ToDoubleBiFunction.class, new LambdaInfo("applyAsDouble",
			methodType(double.class, Object.class, Object.class)));

		LAMBDA_MAPPING.put(Supplier.class, new LambdaInfo("get",
			methodType(Object.class)));
		LAMBDA_MAPPING.put(BooleanSupplier.class, new LambdaInfo("getAsBoolean",
			methodType(boolean.class)));

		LAMBDA_MAPPING.put(Consumer.class, new LambdaInfo("accept",
			methodType(void.class, Object.class)));
		LAMBDA_MAPPING.put(BiConsumer.class, new LambdaInfo("accept",
			methodType(void.class, Object.class, Object.class)));
	}

	public static void registry(Class<?> type) {
		if (LAMBDA_MAPPING.containsKey(type)) {
			return;
		}
		if (!type.isInterface()) {
			throw new IllegalStateException("class [" + type + "] type must be interface");
		}
		if (type.getMethods().length != 1) {
			throw new IllegalStateException("class [" + type + "] is not functional interface");
		}

		Method method = type.getMethods()[0];
		LAMBDA_MAPPING.put(type, new LambdaInfo(method.getName(),
			methodType(method.getReturnType(), method.getParameterTypes())));
	}

	public static LambdaInfo getOrCreate(Class<?> type) {
		return LAMBDA_MAPPING.computeIfAbsent(type, key -> {
			registry(type);
			return LAMBDA_MAPPING.get(type);
		});
	}

	record LambdaInfo(String methodName, MethodType type) {
	}

}
