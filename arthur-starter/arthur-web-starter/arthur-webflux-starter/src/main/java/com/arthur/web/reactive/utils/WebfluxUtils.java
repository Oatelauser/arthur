package com.arthur.web.reactive.utils;

import org.springframework.http.codec.multipart.Part;

/**
 * Webflux工具类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-25
 * @since 1.0
 */
public abstract class WebfluxUtils {

	/**
	 * 判断是否有文件上传
	 *
	 * @param part {@link Part}
	 * @return true-文件上传；false-单纯的{@code multipart/form-data}请求
	 */
	public static boolean judgeMultipartFile(Part part) {
		if (part == null) {
			return false;
		}
		return part.headers().getContentDisposition().getFilename() != null;
	}

}
