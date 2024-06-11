package com.arthur.boot.aware;

import org.springframework.beans.factory.Aware;

/**
 * 概要描述
 * <p>
 * 详细描述通用的
 *
 * @author DearYang
 * @date 2022-08-29
 * @since 1.0
 */
public interface GenericAware<T> extends Aware {

	void set(T bean);

}
