package com.arthur.common.utils;

/**
 * 系统环境相关的工具类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2022-11-12
 * @since 1.0
 */
public class SystemUtils {

	/**
	 * 判断运行环境是否在容器中
	 *
	 * @param pid 进程PID
	 * @return yes or no
	 */
	public static boolean isRuntimeContainer(long pid) {
		return pid == 1;
	}

}
