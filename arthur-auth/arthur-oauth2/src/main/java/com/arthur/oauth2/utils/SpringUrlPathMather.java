package com.arthur.oauth2.utils;

import com.arthur.boot.core.url.UrlPathMather.AbstractUrlPathMather;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.time.Duration;

/**
 * Spring Web新版本URL匹配器
 *
 * @author DearYang
 * @date 2022-08-16
 * @since 1.0
 */
public class SpringUrlPathMather extends AbstractUrlPathMather {

    private final PathPatternParser pathParser;
	private final Cache<String, PathPattern> pathPatternCache = Caffeine.newBuilder()
		.softValues()
		.maximumSize(1000)
		.expireAfterAccess(Duration.ofMinutes(5))
		.build();

    public SpringUrlPathMather(PathPatternParser pathParser) {
        this.pathParser = pathParser;
    }

    @Override
    public boolean test(String pattern, String path) {
		return pathPatternCache.get(pattern, pathParser::parse)
			.matches(PathContainer.parsePath(path));
    }

}
