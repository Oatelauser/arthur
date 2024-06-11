package com.arthur.boot.context.format;

import org.springframework.beans.factory.Aware;
import org.springframework.format.support.FormattingConversionService;

/**
 * 自动设置{@link FormattingConversionService}对象
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-28
 * @since 1.0
 */
public interface ConversionServiceAware extends Aware {

	void setFormatterRegistry(FormattingConversionService conversionService);

}
