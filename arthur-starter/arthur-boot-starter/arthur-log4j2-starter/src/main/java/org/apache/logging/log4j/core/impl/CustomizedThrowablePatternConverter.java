package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.PatternConverter;
import org.apache.logging.log4j.core.pattern.ThrowablePatternConverter;

/**
 * this class is a throwable filter tools.
 * <p>
 * log4j2 plugin load by log4j-core annotation processor.
 *
 * @author Dear Yang
 * @see CustomizedThrowableProxyRenderer
 */
@Plugin(name = "CustomizedThrowablePatternConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({"cusEx", "cusThrowable", "cusException"})
public class CustomizedThrowablePatternConverter extends ThrowablePatternConverter {

    public CustomizedThrowablePatternConverter(Configuration config, String[] options) {
        super("CustomizedThrowable", "throwable", options, config);
    }

    @Override
    public void format(LogEvent event, StringBuilder buffer) {
        ThrowableProxy proxy = event.getThrownProxy();
        Throwable throwable = event.getThrown();
        if ((throwable == null && proxy == null) || !options.allLines()) {
            return;
        }

        if (proxy == null) {
            super.format(event, buffer);
            return;
        }

        int len = buffer.length();
        if (len > 0 && !Character.isWhitespace(buffer.charAt(len - 1))) {
            buffer.append(" ");
        }
        CustomizedThrowableProxyRenderer.formatExtendedStackTraceTo(proxy, buffer, options.getIgnorePackages(), options.getTextRenderer(), getSuffix(event), options.getSeparator());
    }

    public static CustomizedThrowablePatternConverter newInstance(final Configuration config, final String[] options) {
        return new CustomizedThrowablePatternConverter(config, options);
    }

}