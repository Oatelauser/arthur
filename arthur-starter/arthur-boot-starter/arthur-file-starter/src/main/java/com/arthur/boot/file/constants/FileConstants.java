package com.arthur.boot.file.constants;

/**
 * 文件相关的常量
 *
 * @author DearYang
 * @date 2022-09-28
 * @since 1.0
 */
public interface FileConstants {

	/**
	 * Excel文件下载的Content-Type
	 */
	String DOWNLOAD_EXCEL_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	/**
	 * 默认生成Excel文件的名称
	 */
	String DEFAULT_EXCEL_FILE_NAME = "default";

	/**
	 * 默认的Sheet名称
	 */
	String DEFAULT_SHEET_NAME = "Sheet";

	/**
	 * 默认sheet最大行数
	 */
	int DEFAULT_SHEET_ROWNUM = 40_000;

	/**
	 * 一次读取字节的大小
	 */
	int READ_BYTE_SIZE = 1024 * 4;

}
