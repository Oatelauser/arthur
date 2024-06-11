package com.arthur.lang.invoke;

import com.arthur.lang.utils.ClassUtils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

/**
 * 方法句柄工具类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-22
 * @since 1.0
 */
public abstract class MethodHandlers {

	/**
	 * 构造方法的方法句柄
	 *
	 * @param type 方法所在class
	 * @param methodType 方法签名
	 * @return 方法句柄
	 */
	public static MethodHandle findConstructor(Class<?> type, MethodType methodType)
			throws NoSuchMethodException, IllegalAccessException {
		MethodHandles.Lookup lookup = Lookups.trustedLookup(type);
		return lookup.findConstructor(type, methodType);
	}

	/**
	 * 基于{@link Method} 构造方法句柄
	 *
	 * @param method {@link Method}
	 * @return 方法句柄
	 */
	public static MethodHandle findMethod(Method method) throws IllegalAccessException {
		MethodHandles.Lookup lookup = Lookups.trustedLookup(method.getDeclaringClass());
		return lookup.unreflect(method);
	}

	/**
	 * 构造方法句柄
	 *
	 * @param type 方法所在class
	 * @param method 方法名
	 * @param methodType 方法签名
	 * @return 方法句柄
	 */
	public static MethodHandle findMethod(Class<?> type, String method, MethodType methodType)
			throws NoSuchMethodException, IllegalAccessException {
		MethodHandles.Lookup lookup = Lookups.trustedLookup(type);
		return lookup.findVirtual(type, method, methodType);
	}

	/**
	 * 构造父类方法句柄
	 *
	 * @param type       方法所在class
	 * @param superType  父类class
	 * @param method     方法名
	 * @param methodType 方法签名
	 * @return 方法句柄
	 */
	public static MethodHandle findSuperMethod(Class<?> type, Class<?> superType, String method, MethodType methodType)
			throws NoSuchMethodException, IllegalAccessException {
		MethodHandles.Lookup lookup = Lookups.trustedLookup(superType);
		return lookup.findSpecial(superType, method, methodType, type);
	}

	/**
	 * 构造静态方法句柄
	 *
	 * @param type 方法所在class
	 * @param method 方法名
	 * @param methodType 方法签名
	 * @return 方法句柄
	 */
	public static MethodHandle findStaticMethod(Class<?> type, String method, MethodType methodType)
			throws NoSuchMethodException, IllegalAccessException {
		MethodHandles.Lookup lookup = Lookups.trustedLookup(type);
		return lookup.findStatic(type, method, methodType);
	}

	/**
	 * 构造Getter方法句柄
	 *
	 * @param type 方法所在class
	 * @param field 成员变量名
	 * @param fieldType 成员变量类型
	 * @return 方法句柄
	 */
	public static MethodHandle findGetterMethod(Class<?> type, String field, Class<?> fieldType)
			throws NoSuchFieldException, IllegalAccessException {
		MethodHandles.Lookup lookup = Lookups.trustedLookup(type);
		return lookup.findGetter(type, field, fieldType);
	}

	/**
	 * 构造Setter方法句柄
	 *
	 * @param type 方法所在class
	 * @param field 成员变量名
	 * @param fieldType 成员变量类型
	 * @return 方法句柄
	 */
	public static MethodHandle findSetterMethod(Class<?> type, String field, Class<?> fieldType)
			throws NoSuchFieldException, IllegalAccessException {
		MethodHandles.Lookup lookup = Lookups.trustedLookup(type);
		return lookup.findSetter(type, field, fieldType);
	}

	/**
	 * 构造静态Getter方法句柄
	 *
	 * @param type 方法所在class
	 * @param field 成员变量名
	 * @param fieldType 成员变量类型
	 * @return 方法句柄
	 */
	public static MethodHandle findStaticGetterMethod(Class<?> type, String field, Class<?> fieldType)
			throws NoSuchFieldException, IllegalAccessException {
		MethodHandles.Lookup lookup = Lookups.trustedLookup(type);
		return lookup.findStaticGetter(type, field, fieldType);
	}

	/**
	 * 构造静态Setter方法句柄
	 *
	 * @param type 方法所在class
	 * @param field 成员变量名
	 * @param fieldType 成员变量类型
	 * @return 方法句柄
	 */
	public static MethodHandle findStaticSetterMethod(Class<?> type, String field, Class<?> fieldType)
			throws NoSuchFieldException, IllegalAccessException {
		MethodHandles.Lookup lookup = Lookups.trustedLookup(type);
		return lookup.findStaticSetter(type, field, fieldType);
	}

	/**
	 * 安全的方法句柄参数绑定，处理基本数据类型
	 *
	 * @param obj 绑定的参数
	 * @param handle {@link MethodHandle}
	 * @return {@link MethodHandle}
	 */
	public static MethodHandle bindTo(Object obj, MethodHandle handle) {
		if (ClassUtils.isPrimitive(obj.getClass())) {
			handle = handle.asType(handle.type().wrap());
		}
		return handle.bindTo(obj);
	}

}
