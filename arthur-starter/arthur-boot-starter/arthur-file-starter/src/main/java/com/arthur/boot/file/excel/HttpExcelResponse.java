package com.arthur.boot.file.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.arthur.boot.file.constants.FileConstants;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.arthur.boot.file.constants.FileConstants.DEFAULT_SHEET_ROWNUM;
import static com.arthur.boot.file.excel.ExcelStyleUtils.CELL_HEIGHT_STRATEGY;
import static com.arthur.boot.file.excel.ExcelStyleUtils.CELL_STYLE;

/**
 * 提供自定义的Excel文件下载的响应对象
 * <p>
 * 一般用于数据量比较大的情况下，使用该对象分批写入
 *
 * @author DearYang
 * @date 2022-10-01
 * @since 1.0
 */
@SuppressWarnings("unused")
public class HttpExcelResponse implements Closeable {

	private static final Logger LOG = LoggerFactory.getLogger(HttpExcelResponse.class);

	private ExcelWriter writer;
	private boolean hasError = false;
	private final HttpServletResponse response;
	private String fileName = FileConstants.DEFAULT_EXCEL_FILE_NAME;
	private final AtomicBoolean flushed = new AtomicBoolean(false);
	private final AtomicBoolean initialized = new AtomicBoolean(false);

	public HttpExcelResponse(HttpServletResponse response) {
		this.response = response;
	}

	/**
	 * @see HttpExcelResponse#write(String, Class, Collection)
	 */
	public <T> void write(Class<?> head, Collection<?> data) throws IOException {
		this.write(FileConstants.DEFAULT_SHEET_NAME, head, data);
	}

	public <T> void write(Collection<T> data) throws IOException {
		this.write(FileConstants.DEFAULT_SHEET_NAME, data);
	}

	/**
	 * 基于数据泛型的方式，获取excel的头信息，写入excel
	 *
	 * @param sheetName sheet名称
	 * @param data      写入的数据
	 * @param <T>       数据的泛型类型
	 * @throws IOException 写入异常，或者数据类型不对
	 * @see HttpExcelResponse#write(String, Class, Collection)
	 */
	public <T> void write(String sheetName, Collection<T> data) throws IOException {
		Class<?> head = CollectionUtils.findCommonElementType(data);
		if (head != null && !Object.class.equals(head)) {
			this.write(sheetName, head, data);
		}
	}

	/**
	 * Excel写入数据
	 *
	 * @param sheetName sheet名称
	 * @param data      写入的数据
	 * @throws IOException 写入异常，或者数据类型不对
	 */
	public void write(String sheetName, Class<?> head, Collection<?> data) throws IOException {
		if (!initialized.get()) {
			this.configureExcelWriter(head);
		}

		// 解决Excel写入数据太多，导致sheet打不开
		int size = data.size();
		int step = DEFAULT_SHEET_ROWNUM;
		if (size < step) {
			this.doWrite(0, sheetName, data);
			return;
		}

		int i = 0;
		int start = 0;
		List<?> rows = data instanceof List<?> ? (List<?>) data : new ArrayList<>(data);
		for (int end = step; end < size; i++, start += step, end += step) {
			this.doWrite(i, sheetName + i, rows.subList(start, end));
		}
		if (size - start > 0) {
			this.doWrite(i, sheetName + i, rows.subList(start, size));
		}
	}

	private void doWrite(int sheetNo, String sheetName, Collection<?> data) {
		try {
			writer.write(data, EasyExcel.writerSheet(sheetNo, sheetName).build());
		} catch (Exception e) {
			this.hasError = true;
			throw e;
		}
	}

	/**
	 * 配置Excel的参数
	 *
	 * @param head 行
	 */
	private void configureExcelWriter(Class<?> head) throws IOException {
		if (initialized.get()) {
			return;
		}
		if (initialized.compareAndSet(false, true)) {
			this.writer = EasyExcel.write(this.response.getOutputStream(), head)
				.registerWriteHandler(CELL_STYLE)
				.registerWriteHandler(new ExcelCellWidthStyleStrategy())
				.registerWriteHandler(CELL_HEIGHT_STRATEGY)
				.autoCloseStream(false)
				.build();
		}
	}

	/**
	 * 刷新写入数据
	 */
	public void flush() throws IOException {
		if (flushed.get() || this.writer == null) {
			return;
		}
		if (!flushed.compareAndSet(false, true)) {
			return;
		}

		if (hasError) {
			this.response.resetBuffer();
			return;
		}

		this.buildExcelResponse(this.fileName);
		this.writer.close();
		this.response.flushBuffer();
	}

	/**
	 * 构建Excel文件下载的请求参数
	 *
	 * @param fileName 文件名
	 */
	private void buildExcelResponse(String fileName) {
		response.setContentType(FileConstants.DOWNLOAD_EXCEL_CONTENT_TYPE);
		ContentDisposition disposition = ContentDisposition.attachment()
			.filename(fileName + ExcelTypeEnum.XLSX.getValue(), StandardCharsets.UTF_8).build();
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, disposition.toString());
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
	}

	/**
	 * @see HttpExcelResponse#flush()
	 */
	@Override
	public void close() throws IOException {
		this.flush();
	}

	/**
	 * 设置文件文件名
	 *
	 * @param fileName 文件名
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
