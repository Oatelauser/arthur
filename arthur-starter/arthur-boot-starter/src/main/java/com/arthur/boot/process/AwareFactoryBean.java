package com.arthur.boot.process;

import org.springframework.core.ResolvableType;

import java.util.Objects;

/**
 * Aware描述器
 *
 * @author DearYang
 * @date 2022-08-02
 * @see AwareBeanPostProcessor
 * @since 1.0
 */
public class AwareFactoryBean {

    /**
     * Aware接口
     */
    private final ResolvableType declaredType;

	public AwareFactoryBean(Class<?> awareClass) {
		this.declaredType = ResolvableType.forClass(Objects.requireNonNull(awareClass));
	}

    public ResolvableType getDeclaredType() {
        return declaredType;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "declaredType=" + declaredType + '}';
    }

}
