package com.arthur.web.antisamy.context;

import com.arthur.web.antisamy.util.AntiSamyUtils;
import org.owasp.validator.html.Policy;
import org.springframework.lang.NonNull;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

import static com.arthur.web.antisamy.constant.AntiSamyConstants.WILD_CARD;

/**
 * AntiSamy防御服务
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-06
 * @since 1.0
 */
public interface AntiSamyService {

	/**
	 * 获取AntiSamy策略
	 */
	@NonNull
	Policy policy();

	/**
	 * 获取AntiSamy配置
	 */
	@NonNull
	AntiSamyConfig config();

	/**
	 * AntiSamy XSS防御
	 *
	 * @param taintedHtml 待清理的字符串
	 */
	default String cleaning(String taintedHtml) {
		return AntiSamyUtils.cleaning(taintedHtml, policy());
	}

	/**
	 * 判断是否有XSS防御的请求头
	 *
	 * @param header 请求头
	 * @return true-yes，false-no
	 */
	default boolean hasXssHeader(String header) {
		return contains(header, config().getDefenseHeaders());
	}

	/**
	 * 判断是否有XSS防御的cookie
	 *
	 * @param cookieName cookie
	 * @return true-yes，false-no
	 */
	default boolean hasXssCookie(String cookieName) {
		return contains(cookieName, config().getDefenseCookies());
	}

	private static boolean contains(String value, String[] values) {
		if (!ObjectUtils.isEmpty(values)) {
			return false;
		}

		for (String policyHeader : values) {
			if (WILD_CARD.equals(policyHeader) || value.equals(policyHeader)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 创建默认的AntiSamy服务
	 *
	 * @param policy AntiSamy策略
	 * @param config AntiSamy配置
	 */
	static AntiSamyService create(Policy policy, AntiSamyConfig config) {
		return new Default(Objects.requireNonNull(policy), Objects.requireNonNull(config));
	}

	record Default(Policy policy, AntiSamyConfig config) implements AntiSamyService {
	}

}
