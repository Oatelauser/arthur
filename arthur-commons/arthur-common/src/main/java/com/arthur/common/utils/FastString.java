package com.arthur.common.utils;

import com.arthur.common.invoke.LambdaMetafactorys;
import com.arthur.common.invoke.Lookups;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.nio.charset.Charset;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import static java.lang.invoke.MethodType.methodType;

/**
 * 高性能字符串，只支持JDK11+
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-25
 * @since 1.0
 */
public abstract class FastString {

	public static final ToIntFunction<String> STRING_CODER;
	public static final Function<String, byte[]> STRING_VALUE;
	public static final BiFunction<byte[], Charset, String> STRING_CREATOR;

	static {
		BiFunction<byte[], Charset, String> stringCreator;
		Function<String, byte[]> stringValue;
		ToIntFunction<String> stringCoder;

		Lookup lookup = Lookups.trustedLookup(String.class);
		try {
			stringCreator = getStringCreator(lookup);
			stringValue = getStringValue(lookup);
			stringCoder = getStringCoder(lookup);
		} catch (Throwable t) {
			stringCreator = null;
			stringValue = null;
			stringCoder = null;
		}
		STRING_VALUE = stringValue;
		STRING_CODER = stringCoder;
		STRING_CREATOR = stringCreator;
	}

	@SuppressWarnings("unchecked")
	private static BiFunction<byte[], Charset, String> getStringCreator(Lookup lookup) throws Throwable {
		MethodHandle handle = lookup.findStatic(String.class, "newStringNoRepl",
			methodType(String.class, byte[].class, Charset.class));
		return (BiFunction<byte[], Charset, String>) LambdaMetafactorys.createLambda(BiFunction.class, lookup, handle)
			.getTarget().invokeExact();
	}

	@SuppressWarnings("unchecked")
	private static Function<String, byte[]> getStringValue(Lookup lookup) throws Throwable {
		MethodHandle handle = lookup.findSpecial(String.class, "value",
			methodType(byte[].class), String.class);
		return (Function<String, byte[]>) LambdaMetafactorys.createLambda(Function.class, lookup, handle)
			.getTarget().invokeExact();
	}

	@SuppressWarnings("unchecked")
	private static ToIntFunction<String> getStringCoder(Lookup lookup) throws Throwable {
		MethodHandle handle = lookup.findSpecial(String.class, "coder",
			methodType(byte.class), String.class);
		return (ToIntFunction<String>) LambdaMetafactorys.createLambda(ToIntFunction.class, lookup, handle)
			.getTarget().invokeExact();
	}

}
