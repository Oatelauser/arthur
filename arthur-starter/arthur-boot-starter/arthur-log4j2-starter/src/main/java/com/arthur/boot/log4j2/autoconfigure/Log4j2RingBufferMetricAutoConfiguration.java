package com.arthur.boot.log4j2.autoconfigure;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.jmx.RingBufferAdminMBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.actuate.autoconfigure.metrics.export.ConditionalOnEnabledMetricsExport;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * monitor log4j2 async log for each logger {@code RingBuffer}
 *
 * @author DearYang
 * @date 2022-07-14
 * @since 1.0
 */
@AutoConfiguration
@ConditionalOnEnabledMetricsExport("prometheus")
@ConditionalOnClass(PrometheusMeterRegistry.class)
public class Log4j2RingBufferMetricAutoConfiguration implements ApplicationRunner {

    /**
     * 只初始化一次
     */
    private volatile boolean initialized = false;
    private final ObjectProvider<PrometheusMeterRegistry> provider;

    public Log4j2RingBufferMetricAutoConfiguration(ObjectProvider<PrometheusMeterRegistry> provider) {
        this.provider = provider;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (initialized) {
            return;
        }

        PrometheusMeterRegistry registry = provider.getIfAvailable();
        if (registry == null) {
            return;
        }

        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        org.apache.logging.log4j.core.config.Configuration configuration = context.getConfiguration();
        String ctxName = context.getName();
        configuration.getLoggers().keySet().forEach(k -> {
            String cfName = hasText(k) ? k : "";
            String gaugeName = hasText(k) ? k : "root";
            Gauge.builder(gaugeName + "_logger_ring_buffer_remaining_capacity", () -> {
                try {
                    ObjectName objectName = new ObjectName(String.format(RingBufferAdminMBean.PATTERN_ASYNC_LOGGER_CONFIG, ctxName, cfName));
                    return (Number) ManagementFactory.getPlatformMBeanServer().getAttribute(objectName, "RemainingCapacity");
                } catch (Exception ignored) {
                }

                return -1;
            }).register(registry);
        });
        initialized = true;
    }

    public boolean hasText(String str) {
        return str != null && !str.isEmpty() && containsText(str);
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();

        for (int i = 0; i < strLen; ++i) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }

        return false;
    }

}
