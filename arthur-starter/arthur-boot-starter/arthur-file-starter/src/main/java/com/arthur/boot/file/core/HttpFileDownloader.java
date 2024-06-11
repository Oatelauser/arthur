package com.arthur.boot.file.core;

import com.arthur.boot.file.utils.FileChannelUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.arthur.boot.file.core.ExceptionHandler.ErrorEnum.FILE_NOT_FOUND;

/**
 * Servlet文件下载器
 *
 * @author DearYang
 * @date 2022-10-10
 * @see ExceptionHandler
 * @see FileChannelUtils
 * @since 1.0
 */
@SuppressWarnings("unused")
public class HttpFileDownloader implements ResourceLoaderAware {

	private final ExceptionHandler exceptionHandler;
	private PathMatchingResourcePatternResolver resolver;

	public HttpFileDownloader(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	/**
	 * 基于文件表达式解析器{@link PathMatchingResourcePatternResolver}下载文件
	 *
	 * @param location 文件路径
	 * @param response {@link HttpServletResponse}
	 * @throws IOException IO异常
	 */
	public void attach(String location, HttpServletResponse response) throws IOException {
		Resource resource = resolver.getResource(location);
		Path path = resource.getFile().toPath();
		//Path path = Paths.get(location);
		// 文件不存在，或者目标文件是目录
		if (Files.notExists(path) || Files.isDirectory(path)) {
			exceptionHandler.handleInvalidDownload(FILE_NOT_FOUND, response);
			return;
		}

		// 尝试获取文件的ContentType
		String mediaType = Files.probeContentType(path);
		if (mediaType == null) {
			// 获取失败，使用通用的二进制文件类型
			mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
		}

		response.setHeader(HttpHeaders.CONTENT_TYPE, mediaType);
		// Content-Disposition
		ContentDisposition disposition = ContentDisposition.attachment()
			.filename(path.getFileName().toString(), StandardCharsets.UTF_8).build();
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, disposition.toString());
		try (FileChannel fileChannel = FileChannel.open(path)) {
			// Content-Length
			response.setContentLengthLong(fileChannel.size());
			FileChannelUtils.safeTransferTo(fileChannel, Channels.newChannel(response.getOutputStream()));
			response.flushBuffer();
		} catch (Exception ex) {
			response.resetBuffer();
			exceptionHandler.handleException(response, ex);
		}
	}

	/**
	 * @see #attach(String, InputStream, HttpServletResponse)
	 */
	public void attach(InputStream is, HttpServletResponse response) {
		this.attach("Unknown", is, response);
	}

	/**
	 * 基于输入流下载文件
	 *
	 * @param fileName 文件名
	 * @param is       输入流
	 * @param response {@link HttpServletResponse}
	 */
	public void attach(String fileName, InputStream is, HttpServletResponse response) {
		response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
		// Content-Disposition
		ContentDisposition disposition = ContentDisposition.attachment()
			.filename(fileName, StandardCharsets.UTF_8).build();
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, disposition.toString());
		try {
			// Content-Length
			long contentLength = StreamUtils.copy(is, response.getOutputStream());
			response.setContentLengthLong(contentLength);
			response.flushBuffer();
		} catch (Exception ex) {
			response.resetBuffer();
			exceptionHandler.handleException(response, ex);
		}
	}

	@Override
	public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
		resolver = new PathMatchingResourcePatternResolver(resourceLoader);
	}

}
