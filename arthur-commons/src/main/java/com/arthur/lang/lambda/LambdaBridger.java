package com.arthur.lang.lambda;

import com.arthur.lang.invoke.Lookups;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaConversionException;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;

import static java.lang.invoke.MethodType.methodType;

/**
 * {@link LambdaMetafactory} 工具类
 * <p>
 * 用于动态创建lambda表达式，性能比反射更高，等价于编码中的lambda表达式性能
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-22
 * @see Lookups
 * @see LambdaRegistry
 * @since 1.0
 */
public abstract class LambdaBridger extends Lookups {

	/**
	 * 创建lambda表达式
	 *
	 * @param functionType 函数接口的类型
	 * @param lookup       {@link Lookup}
	 * @param handle       {@link MethodHandle}
	 * @return 返回生成的lambda表达式的方法句柄 {@link MethodHandle}
	 * @throws LambdaConversionException 参数异常
	 */
	public static CallSite createLambda(Class<?> functionType, Lookup lookup,
			MethodHandle handle) throws Throwable {
		LambdaRegistry.LambdaInfo lambdaInfo = LambdaRegistry.getOrCreate(functionType);
		return LambdaMetafactory.metafactory(lookup, lambdaInfo.methodName(),
			methodType(functionType), lambdaInfo.type(), handle, handle.type().wrap());
	}

	/**
	 * @see #createLambda(Class, Lookup, MethodHandle)
	 */
	public static CallSite createLambda(Class<?> functionType, MethodHandle handle) throws Throwable {
		return createLambda(functionType, IMPL_LOOKUP, handle);
	}

}
