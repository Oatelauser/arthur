package com.arthur.oauth2.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.oauth2.core.AuthorizationGrantType.CLIENT_CREDENTIALS;

/**
 * 资源服务器令牌自省处理器
 *
 * @author DearYang
 * @date 2022-08-31
 * @see OpaqueTokenIntrospector
 * @since 1.0
 */
public class OAuth2OpaqueTokenIntrospector implements OpaqueTokenIntrospector {

	private static final Logger LOG = LoggerFactory.getLogger(OAuth2OpaqueTokenIntrospector.class);

	private final OAuth2AuthorizationService authorizationService;
	private final List<OAuth2UserDetailsService> userDetailsServices;

	public OAuth2OpaqueTokenIntrospector(OAuth2AuthorizationService authorizationService,
			List<OAuth2UserDetailsService> userDetailsServices) {
		this.authorizationService = authorizationService;
		this.userDetailsServices = userDetailsServices;
	}

	@Override
	public OAuth2AuthenticatedPrincipal introspect(String token) {
		OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
		if (authorization == null) {
			throw new InvalidBearerTokenException(token);
		}

		if (CLIENT_CREDENTIALS.equals(authorization.getAuthorizationGrantType())) {
			return new DefaultOAuth2AuthenticatedPrincipal(authorization.getPrincipalName(),
				authorization.getAttributes(), AuthorityUtils.NO_AUTHORITIES);
		}

		return (OAuth2User) this.loadUserDetails(authorization);
	}

	@SuppressWarnings("all")
	private UserDetails loadUserDetails(OAuth2Authorization authorization) {
		Optional<OAuth2UserDetailsService> optional = userDetailsServices.stream()
			.filter(service -> service.supports(authorization.getRegisteredClientId(), authorization.getAuthorizationGrantType().getValue()))
			.min(Comparator.comparingInt(Ordered::getOrder));

		UserDetails userDetails = null;
		Object principal = authorization.getAttributes().get(Principal.class.getName());
		try {
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
			Object tokenPrincipal = usernamePasswordAuthenticationToken.getPrincipal();
			// 可能存在空指针异常，这里不用判断
			userDetails = optional.get().loadUserByClientUser((OAuth2User) tokenPrincipal);
		} catch (UsernameNotFoundException ex) {
			if (LOG.isWarnEnabled()) {
				LOG.warn("user name not found: {}", ex.getLocalizedMessage());
			}
			throw ex;
		} catch (Exception ex) {
			LOG.error("OAuth2 resource service introspector token Error: {}", ex.getLocalizedMessage());
		}

		return userDetails;
	}

}
