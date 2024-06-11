package com.arthur.auth.uaa.handler;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * 登出事件监听器
 *
 * @author DearYang
 * @date 2022-08-26
 * @since 1.0
 */
public class LogoutSuccessEventHandler implements ApplicationListener<LogoutSuccessEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(LogoutSuccessEventHandler.class);

    @Override
    public void onApplicationEvent(@NonNull LogoutSuccessEvent event) {
        Authentication authentication = (Authentication) event.getSource();
        if (authentication instanceof PreAuthenticatedAuthenticationToken authenticationToken) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("user [{}] logout success", authenticationToken.getPrincipal());
			}
        }
    }

}
