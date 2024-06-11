package com.arthur.web.antisamy.util;

import org.apache.commons.text.StringEscapeUtils;
import org.owasp.validator.html.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Pattern;

/**
 * AntiSamy XSS 工具类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-05
 * @since 1.0
 */
public abstract class AntiSamyUtils {

	private static final Logger LOG = LoggerFactory.getLogger(AntiSamyUtils.class);

	private static volatile boolean nonInit = true;
	private static final String nbsp = "&nbsp;";
	private static volatile String COMPLIED_NBSP;
	private static final String ensp = "&ensp;";
	private static volatile String COMPLIED_ENSP;
	//private static final String quot;
	public static final AntiSamy DEFAULT_ANTISAMY = new AntiSamy();
	private static final Pattern HTML_PATTERN = Pattern.compile("<[^>]+>");

	public static String cleaning(String taintedHtml, Policy policy) {
		if (policy == null || !StringUtils.hasText(taintedHtml)) {
			return taintedHtml;
		}

		if (nonInit) {
			initializeCompliedWord(policy);
		}
		return doCleaning(taintedHtml, policy);
	}

	private static String doCleaning(String taintedHtml, Policy policy) {
		String cleanedHtml = taintedHtml;
		try {
			CleanResults cleanResults = DEFAULT_ANTISAMY.scan(taintedHtml, policy);
			List<String> errorMessages = cleanResults.getErrorMessages();
			if (!CollectionUtils.isEmpty(errorMessages)) {
				recordErrorMessages(errorMessages);
			}

			cleanedHtml = cleanResults.getCleanHTML();
			if (HTML_PATTERN.matcher(cleanedHtml).matches()) {
				cleanedHtml = doCleaning(cleanedHtml);
			}
		} catch (ScanException | PolicyException e) {
			LOG.warn("AntiSamy scan tainted html has error: {}", e.getLocalizedMessage());
		}
		return cleanedHtml;
	}

	private static String doCleaning(String cleanedHtml) {
		cleanedHtml = StringEscapeUtils.unescapeHtml4(cleanedHtml);
		return cleanedHtml.replace(COMPLIED_NBSP, nbsp)
			.replace(COMPLIED_ENSP, ensp);
	}

	private static void recordErrorMessages(List<String> errorMessages) {
		if (!LOG.isDebugEnabled()) {
			return;
		}

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < errorMessages.size(); i++) {
			builder.append(i).append(".").append(errorMessages.get(i)).append(".\n");
		}
		builder.delete(builder.length() - "\n".length(), builder.length());
		LOG.debug(builder.toString());
	}

	private static void initializeCompliedWord(Policy policy) {
		synchronized (AntiSamyUtils.class) {
			if (nonInit) {
				COMPLIED_NBSP = doCleaning(nbsp, policy);
				COMPLIED_ENSP = doCleaning(ensp, policy);
				//quot = doCleaning("\"", policy);
				nonInit = false;
			}
		}
	}

}
