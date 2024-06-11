package com.arthur.lang.utils;

/**
 * JDK工具类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-22
 * @since 1.0
 */
public abstract class JdkUtils {

	public static final int JVM_VERSION;
	public static final boolean ANDROID;
	@SuppressWarnings("SpellCheckingInspection")
	public static final boolean GRAAL;
	public static final boolean OPEN_J9;

	static {
		int jvmVersion = -1;
		String javaSpecVer = System.getProperty("java.specification.version");
		// android is 0.9
		if (javaSpecVer.startsWith("1.")) {
			javaSpecVer = javaSpecVer.substring(2);
		}
		if (javaSpecVer.indexOf('.') == -1) {
			jvmVersion = Integer.parseInt(javaSpecVer);
		}
		JVM_VERSION = jvmVersion;

		String jmvName = System.getProperty("java.vm.name");
		ANDROID = jmvName.equals("Dalvik");
		GRAAL = jmvName.equals("Substrate VM");
		OPEN_J9 = jmvName.contains("OpenJ9");
	}

}
