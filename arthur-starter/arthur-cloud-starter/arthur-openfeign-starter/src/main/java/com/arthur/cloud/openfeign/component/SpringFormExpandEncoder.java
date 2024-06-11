package com.arthur.cloud.openfeign.component;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.form.ContentType;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static feign.form.util.CharsetUtil.UTF_8;

/**
 * 拓展{@link SpringFormEncoder}功能
 * <p>
 * 1. <a href="https://springboot.io/t/topic/2499">解决OpenFeign文件上传问题</a><br/>
 * 2. 解决文件上传，没有设置{@code Content-Type=multipart/form-data}，或者文件上传带参数
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2022-11-10
 * @see SpringFormEncoder
 * @since 1.0
 */
@SuppressWarnings({"unchecked", "unused"})
public class SpringFormExpandEncoder extends SpringFormEncoder {

	private static final String DEFAULT_CHARSET_NAME = "UTF-8";
	private static final Pattern CHARSET_PATTERN = Pattern.compile("(?<=charset=)([\\w\\-]+)");

	public SpringFormExpandEncoder(Encoder delegate) {
		super(delegate);
	}

	public SpringFormExpandEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
		this(new SpringEncoder(messageConverters));
	}

	@Override
	public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
		if (isMultipartFileMap(object)) {
			this.encode((Map<String, Object>) object, template);
			return;
		}
		super.encode(object, bodyType, template);
	}

	/**
	 * 解决文件上传，没有设置{@code Content-Type=multipart/form-data}，或者文件上传带参数
	 * <p>
	 * 典型的代码如下：
	 * <pre class="code">
	 * {@code
	 *    @PostMapping(value = "/upload")
	 *  String search(@RequestPart("file") MultipartFile file, @RequestPart User user);
	 * }
	 * </pre>
	 *
	 * @param object   请求参数
	 * @param template {@link RequestTemplate}
	 */
	public void encode(Map<String, Object> object, RequestTemplate template) {
		String contentTypeValue = this.getContentTypeValue(template.headers());
		Charset charset = this.getCharset(contentTypeValue);

		this.getContentProcessor(ContentType.of(MediaType.MULTIPART_FORM_DATA_VALUE))
			.process(template, charset, object);
	}

	/**
	 * 获取Content-Type的字符类型
	 *
	 * @param contentTypeValue Content-Type的值
	 * @return 字符类型
	 */
	private Charset getCharset(String contentTypeValue) {
		Charset charset = UTF_8;
		if (StringUtils.hasText(contentTypeValue)) {
			Matcher matcher = CHARSET_PATTERN.matcher(contentTypeValue);
			String charsetName;
			if (matcher.find() && !DEFAULT_CHARSET_NAME.equalsIgnoreCase((charsetName = matcher.group(1)))) {
				charset = Charset.forName(charsetName);
			}
		}
		return charset;
	}

	/**
	 * 获取{@link RequestTemplate#headers()}中Content-Type的值
	 *
	 * @param headers {@link RequestTemplate#headers()}
	 * @return Content-Type的值
	 */
	private String getContentTypeValue(Map<String, Collection<String>> headers) {
		for (Map.Entry<String, Collection<String>> entry : headers.entrySet()) {
			if (HttpHeaders.CONTENT_TYPE.equalsIgnoreCase(entry.getKey())) {
				for (String contentTypeValue : entry.getValue()) {
					if (contentTypeValue != null) {
						return contentTypeValue;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 判断Map请求参数中是否有文件上传
	 *
	 * @param object 请求参数
	 * @return yes or no
	 */
	private boolean isMultipartFileMap(Object object) {
		if (!(object instanceof Map)) {
			return false;
		}
		for (Map.Entry<String, ?> entry : ((Map<String, ?>) object).entrySet()) {
			Object value = entry.getValue();
			if (this.isMultipartFile(value.getClass(), value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否文件上传
	 *
	 * @param bodyType 请求参数类型
	 * @param body     对应参数类型的请求参数值
	 * @return yes or no
	 */
	private boolean isMultipartFile(Class<?> bodyType, Object body) {
		if (bodyType.equals(MultipartFile[].class)) {
			return true;
		} else if (bodyType.equals(MultipartFile.class)) {
			return true;
		} else {
			return isMultipartFileCollection(body);
		}
	}

	/**
	 * 判断是否文件集合上传
	 *
	 * @param object 请求参数
	 * @return yes or no
	 */
	private boolean isMultipartFileCollection(Object object) {
		if (!(object instanceof Iterable<?> iterable)) {
			return false;
		}
		Iterator<?> iterator = iterable.iterator();
		return iterator.hasNext() && iterator.next() instanceof MultipartFile;
	}

}
