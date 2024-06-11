package com.arthur.boot.file.core;


import jakarta.servlet.http.HttpServletResponse;

/**
 * 文件下载异常处理器
 *
 * @author DearYang
 * @date 2022-11-06
 * @since 1.0
 */
public interface ExceptionHandler {

	/**
	 * 处理文件下载异常
	 *
	 * @param response {@link HttpServletResponse}
	 * @param t        异常
	 */
	void handleException(HttpServletResponse response, Throwable t);

	/**
	 * 处理无效的下载
	 *
	 * @param type     异常枚举类
	 * @param response {@link HttpServletResponse}
	 */
	void handleInvalidDownload(ErrorEnum type, HttpServletResponse response);

	enum ErrorEnum {

		/**
		 * 文件不存在
		 */
		FILE_NOT_FOUND,


	}

}
