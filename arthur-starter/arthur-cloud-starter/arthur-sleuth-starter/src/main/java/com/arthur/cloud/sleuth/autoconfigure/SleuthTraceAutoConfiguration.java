package com.arthur.cloud.sleuth.autoconfigure;

import com.arthur.cloud.sleuth.filter.TraceFilter;
import com.arthur.cloud.sleuth.wrapper.TraceablePublisherFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.sleuth.CurrentTraceContext;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.autoconfig.instrument.web.ConditionalOnSleuthWeb;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;

/**
 * sleuth auto config
 *
 * @author DearYang
 * @date 2022-07-21
 * @since 1.0
 */
@AutoConfiguration
@ConditionalOnSleuthWeb
@ConditionalOnClass({Tracer.class, CurrentTraceContext.class})
public class SleuthTraceAutoConfiguration {

    @Bean
	@ConditionalOnBean(Tracer.class)
    public GlobalFilter sleuthTraceFilter(@NonNull Tracer tracer) {
        return new TraceFilter(tracer);
    }

    @Bean
	@ConditionalOnBean({Tracer.class, CurrentTraceContext.class})
    public TraceablePublisherFactory tracedPublisherFactory(Tracer tracer, CurrentTraceContext currentTraceContext) {
        return new TraceablePublisherFactory(tracer, currentTraceContext);
    }

}
