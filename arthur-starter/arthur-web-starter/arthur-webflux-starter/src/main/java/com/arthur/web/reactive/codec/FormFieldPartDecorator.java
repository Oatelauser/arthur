package com.arthur.web.reactive.codec;

import com.arthur.web.utils.WebUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 适配{@link FormFieldPart}
 *
 * 参考源码{@code org.springframework.http.codec.multipart.DefaultParts.DefaultFormFieldPart}
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-13
 * @since 1.0
 */
public class FormFieldPartDecorator implements FormFieldPart {

	private final String fieldValue;
	private final FormFieldPart delegate;

	public FormFieldPartDecorator(String fieldValue, FormFieldPart fieldPart) {
		this.delegate = fieldPart;
		this.fieldValue = fieldValue;
	}

	@NonNull
	@Override
	public String value() {
		return fieldValue;
	}

	@NonNull
	@Override
	public String name() {
		return delegate.name();
	}

	@NonNull
	@Override
	public HttpHeaders headers() {
		return delegate.headers();
	}

	@NonNull
	@Override
	public Flux<DataBuffer> content() {
		return Flux.defer(() -> {
			byte[] bytes = this.fieldValue.getBytes(WebUtils.resolveHeaderCharacter(headers()));
			return Flux.just(DefaultDataBufferFactory.sharedInstance.wrap(bytes));
		});
	}

	@NonNull
	@Override
	public Mono<Void> delete() {
		return delegate.delete();
	}

}
