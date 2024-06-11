package com.arthur.gateway.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * gateway global config
 *
 * @author DearYang
 * @date 2022-07-17
 * @since 1.0
 */
@ConfigurationProperties(prefix = "arthur")
public class ArthurProperties {

    @NestedConfigurationProperty
    private CrossFilterConfig cross;

    @NestedConfigurationProperty
    private ExcludePathConfig exclude;

    @SuppressWarnings("all")
    @NestedConfigurationProperty
    private AccessLogConfig accesslog;

    @NestedConfigurationProperty
    private FileConfig file;

    @NestedConfigurationProperty
    private FallbackPath fallback;

    @NestedConfigurationProperty
    private Local local;

    public static class CrossFilterConfig {

        private static final Set<String> DEFAULT_ALLOWED_HEADERS;

        static {
            DEFAULT_ALLOWED_HEADERS = new HashSet<>(8);
            DEFAULT_ALLOWED_HEADERS.add("x-requested-with");
            DEFAULT_ALLOWED_HEADERS.add("authorization");
            DEFAULT_ALLOWED_HEADERS.add("Content-Type");
            DEFAULT_ALLOWED_HEADERS.add("Authorization");
            DEFAULT_ALLOWED_HEADERS.add("credential");
            DEFAULT_ALLOWED_HEADERS.add("X-XSRF-TOKEN");
            DEFAULT_ALLOWED_HEADERS.add("token");
            DEFAULT_ALLOWED_HEADERS.add("username");
            DEFAULT_ALLOWED_HEADERS.add("client");
        }

        private boolean enabled = true;

        /**
         * Comma-separated of “header”.
         */
        private String allowedHeaders = "";

        /**
         * Comma-separated of “method”.
         */
        private String allowedMethods = "*";

        private String allowedOrigin = "*";

        private String allowedExpose = "*";

        private String maxAge = "18000";

        private boolean allowCredentials = true;

        /**
         * wrapper the headers.
         *
         * @param headers headers
         * @return wrapped headers
         */
        private String wrapperHeaders(final String headers) {
            final Set<String> headerSet = DEFAULT_ALLOWED_HEADERS;
            if (StringUtils.hasText(headers)) {
                headerSet.addAll(Stream.of(headers.split(",")).collect(Collectors.toSet()));
            }
            return String.join(",", headerSet);
        }


        /**
         * Gets the value of allowedHeaders.
         *
         * @return the value of allowedHeaders
         */
        public String getAllowedHeaders() {
            return allowedHeaders = wrapperHeaders(allowedHeaders);
        }

        /**
         * Sets the allowedHeaders.
         *
         * @param allowedHeaders allowedHeaders
         */
        public void setAllowedHeaders(final String allowedHeaders) {
            this.allowedHeaders = wrapperHeaders(allowedHeaders);
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getAllowedMethods() {
            return allowedMethods;
        }

        public void setAllowedMethods(String allowedMethods) {
            this.allowedMethods = allowedMethods;
        }

        public String getAllowedOrigin() {
            return allowedOrigin;
        }

        public void setAllowedOrigin(String allowedOrigin) {
            this.allowedOrigin = allowedOrigin;
        }

        public String getAllowedExpose() {
            return allowedExpose;
        }

        public void setAllowedExpose(String allowedExpose) {
            this.allowedExpose = allowedExpose;
        }

        public String getMaxAge() {
            return maxAge;
        }

        public void setMaxAge(String maxAge) {
            this.maxAge = maxAge;
        }

        public boolean isAllowCredentials() {
            return allowCredentials;
        }

        public void setAllowCredentials(boolean allowCredentials) {
            this.allowCredentials = allowCredentials;
        }
    }

    public static class ExcludePathConfig {

        private boolean enabled;

        private Set<String> paths = new HashSet<>();

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public Set<String> getPaths() {
            return paths;
        }

        public void setPaths(Set<String> paths) {
            this.paths = paths;
        }
    }

    public static class AccessLogConfig {

        private boolean enabled;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    /**
     * The type File config.
     */
    public static class FileConfig {

        private boolean enabled = true;

        private Integer maxSize = 10;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public Integer getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(Integer maxSize) {
            this.maxSize = maxSize;
        }
    }

    /**
     * The type fallback path.
     */
    public static class FallbackPath {

        private boolean enabled = false;

        private String path;

        /**
         * Gets enabled.
         *
         * @return the enabled
         */
        public boolean getEnabled() {
            return enabled;
        }

        /**
         * Sets enabled.
         *
         * @param enabled the enabled
         */
        public void setEnabled(final boolean enabled) {
            this.enabled = enabled;
        }

        /**
         * Sets path.
         *
         * @param path the path
         */
        public void setPath(final String path) {
            this.path = path;
        }

        /**
         * get path.
         *
         * @return paths path
         */
        public String getPath() {
            return path;
        }
    }

    public CrossFilterConfig getCross() {
        return cross;
    }

    /**
     * The local config.
     */
    public static class Local {

        private boolean enabled = true;

        /**
         * 加密字符串
         */
        private String localSecretKey;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getLocalSecretKey() {
            return localSecretKey;
        }

        public void setLocalSecretKey(String localSecretKey) {
            this.localSecretKey = localSecretKey;
        }
    }

    public void setCross(CrossFilterConfig cross) {
        this.cross = cross;
    }

    public ExcludePathConfig getExclude() {
        return exclude;
    }

    public void setExclude(ExcludePathConfig exclude) {
        this.exclude = exclude;
    }

    public AccessLogConfig getAccesslog() {
        return accesslog;
    }

    public void setAccesslog(AccessLogConfig accesslog) {
        this.accesslog = accesslog;
    }

    public FileConfig getFile() {
        return file;
    }

    public void setFile(FileConfig file) {
        this.file = file;
    }

    public FallbackPath getFallback() {
        return fallback;
    }

    public void setFallback(FallbackPath fallback) {
        this.fallback = fallback;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }
}
