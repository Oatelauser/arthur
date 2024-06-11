package com.arthur.lang.date;

import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日期格式化工具类
 *
 * @author DearYang
 * @date 2022-07-13
 * @since 1.0
 */
public abstract class DateFormatterUtils {

	public static final String HH_MM_DD = "HH:mm:ss";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YYYY_MM_DD_HH_MM_DD = "yyyy-MM-dd HH:mm:ss";

	private static final Map<String, DateTimeFormatter> DATETIME_FORMATTER_MAP = new ConcurrentHashMap<>(12);

	public static DateTimeFormatter ofPattern(String pattern) {
		DateTimeFormatter formatter = DATETIME_FORMATTER_MAP.get(pattern);
		if (formatter != null) {
			return formatter;
		}

		synchronized (DateFormatterUtils.class) {
			formatter = DATETIME_FORMATTER_MAP.get(pattern);
			if (formatter == null) {
				DATETIME_FORMATTER_MAP.put(pattern, DateTimeFormatter.ofPattern(pattern));
			}
		}

		return formatter;
	}

}
