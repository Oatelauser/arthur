package com.arthur.security.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.util.Assert;

import java.util.function.Supplier;

/**
 * 基于{@link TransmittableThreadLocal}实现的{@link SecurityContextHolderStrategy}，防止异步线程无法获取{@link SecurityContext}
 *
 * 参考实现{@code org.springframework.security.core.context.ThreadLocalSecurityContextHolderStrategy}
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-31
 * @since 1.0
 */
public final class TransmittableThreadLocalSecurityContextHolderStrategy implements SecurityContextHolderStrategy, ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final TransmittableThreadLocal<Supplier<SecurityContext>> contextHolder = new TransmittableThreadLocal<>();

    @Override
    public void clearContext() {
        contextHolder.remove();
    }

    @Override
    public SecurityContext getContext() {
        return getDeferredContext().get();
    }

    @Override
    public Supplier<SecurityContext> getDeferredContext() {
        Supplier<SecurityContext> result = contextHolder.get();
        if (result == null) {
            SecurityContext context = createEmptyContext();
            result = () -> context;
            contextHolder.set(result);
        }
        return result;
    }

    @Override
    public void setContext(SecurityContext context) {
        Assert.notNull(context, "Only non-null SecurityContext instances are permitted");
        contextHolder.set(() -> context);
    }

    @Override
    public void setDeferredContext(Supplier<SecurityContext> deferredContext) {
        Assert.notNull(deferredContext, "Only non-null Supplier instances are permitted");
        Supplier<SecurityContext> notNullDeferredContext = () -> {
            SecurityContext result = deferredContext.get();
            Assert.notNull(result, "A Supplier<SecurityContext> returned null and is not allowed.");
            return result;
        };
        contextHolder.set(notNullDeferredContext);
    }

    @Override
    public SecurityContext createEmptyContext() {
        return new SecurityContextImpl();
    }

    @Override
    public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {
        SecurityContextHolder.setContextHolderStrategy(this);
    }

}
