package com.arthur.lang.invoke;

import com.arthur.lang.utils.JdkUtils;
import com.arthur.lang.utils.UnsafeUtils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

import static java.lang.invoke.MethodType.methodType;

/**
 * {@link MethodHandles.Lookup}工具类
 * <p>
 * 提供安全获取{@link MethodHandles.Lookup}的方法
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-22
 * @since 1.0
 */
public abstract class Lookups {

	protected static final MethodHandles.Lookup IMPL_LOOKUP;

	static volatile MethodHandle CONSTRUCTOR_LOOKUP;
	static volatile boolean CONSTRUCTOR_LOOKUP_ERROR;

	static {
		MethodHandles.Lookup trustedLookup = null;
		try {
			Class<MethodHandles.Lookup> lookupClass = MethodHandles.Lookup.class;
			Field implLookup = lookupClass.getDeclaredField("IMPL_LOOKUP");
			long fieldOffset = UnsafeUtils.UNSAFE.staticFieldOffset(implLookup);
			trustedLookup = (MethodHandles.Lookup) UnsafeUtils.UNSAFE.getObject(lookupClass, fieldOffset);
		} catch (Throwable ignored) {
			// ignored
		}
		if (trustedLookup == null) {
			trustedLookup = MethodHandles.lookup();
		}
		IMPL_LOOKUP = trustedLookup;
	}

	/**
	 * 获取受信任的{@link MethodHandles.Lookup}
	 */
	@SuppressWarnings("all")
	public static MethodHandles.Lookup trustedLookup(Class objectClass) {
		if (!CONSTRUCTOR_LOOKUP_ERROR) {
			try {
				int TRUSTED = -1;

				MethodHandle constructor = CONSTRUCTOR_LOOKUP;
				if (JdkUtils.JVM_VERSION < 15) {
					if (constructor == null) {
						constructor = IMPL_LOOKUP.findConstructor(
							MethodHandles.Lookup.class,
							methodType(void.class, Class.class, int.class)
						);
						CONSTRUCTOR_LOOKUP = constructor;
					}
					int FULL_ACCESS_MASK = 31; // for IBM Open J9 JDK
					return (MethodHandles.Lookup) constructor.invoke(
						objectClass,
						JdkUtils.OPEN_J9 ? FULL_ACCESS_MASK : TRUSTED
					);
				} else {
					if (constructor == null) {
						constructor = IMPL_LOOKUP.findConstructor(
							MethodHandles.Lookup.class,
							methodType(void.class, Class.class, Class.class, int.class)
						);
						CONSTRUCTOR_LOOKUP = constructor;
					}
					return (MethodHandles.Lookup) constructor.invoke(objectClass, null, TRUSTED);
				}
			} catch (Throwable ignored) {
				CONSTRUCTOR_LOOKUP_ERROR = true;
			}
		}

		return IMPL_LOOKUP.in(objectClass);
	}

}
