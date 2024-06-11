package com.arthur.auth.uaa.authentication;

import com.arthur.auth.uaa.authentication.password.OAuth2UsernamePasswordAuthenticationToken;
import com.arthur.auth.uaa.utils.WebUtils;
import com.arthur.boot.utils.BeanUtils;
import com.arthur.oauth2.constant.SecurityConstants;
import com.arthur.oauth2.resource.OAuth2UserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.arthur.oauth2.utils.WebUtils.getRequest;

/**
 * 自定义{@link UsernamePasswordAuthenticationToken}实现多用户体系
 *
 * @author DearYang
 * @date 2022-08-26
 * @see org.springframework.security.authentication.dao.DaoAuthenticationProvider
 * @since 1.0
 */
@SuppressWarnings("unused")
public class CompositeDaoAuthenticationProvider extends AbstractDaoAuthenticationProvider {

	private static final Logger LOG = LoggerFactory.getLogger(CompositeDaoAuthenticationProvider.class);

	private static final AuthenticationConverter CLIENT_CONVERTER = new BasicAuthenticationConverter();

	private final List<OAuth2UserDetailsService> userDetails;

	public CompositeDaoAuthenticationProvider(List<OAuth2UserDetailsService> userDetails) {
		userDetails = BeanUtils.sort(userDetails);
		this.userDetails = userDetails;
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		OAuth2UsernamePasswordAuthenticationToken oAuth2Authentication = null;
		if (authentication instanceof OAuth2UsernamePasswordAuthenticationToken authenticationToken) {
			oAuth2Authentication = authenticationToken;
		}

		String grantType = this.getGrantType(oAuth2Authentication);
		if (SecurityConstants.APP.equals(grantType)) {
			return;
		}

		super.additionalAuthenticationChecks(userDetails, authentication);
	}

	@Override
	protected UserDetailsService getUserDetailsService(String username, UsernamePasswordAuthenticationToken authenticationToken) {
		OAuth2UsernamePasswordAuthenticationToken authentication = null;
		if (authenticationToken instanceof OAuth2UsernamePasswordAuthenticationToken oauth2AuthenticationToken) {
			authentication = oauth2AuthenticationToken;
		}

		String clientId = this.getClientId(authentication);
		String grantType = this.getGrantType(authentication);

		// UserDetailsService
		for (OAuth2UserDetailsService userDetailsService : userDetails) {
			if (userDetailsService.supports(clientId, grantType)) {
				return userDetailsService;
			}
		}

		throw new InternalAuthenticationServiceException("UserDetailsService no match: grantType ["
			+ grantType + "], clientId [" + clientId + "]");
	}

	private String getClientId(OAuth2UsernamePasswordAuthenticationToken authentication) {
		String clientId;
		if (authentication == null || (clientId = authentication.getClientId()) == null) {
			HttpServletRequest request = getRequest().orElseThrow(() -> new InternalAuthenticationServiceException("web request is null"));
			clientId = WebUtils.getParameter(OAuth2ParameterNames.CLIENT_ID, request);
			if (!StringUtils.hasText(clientId)) {
				clientId = CLIENT_CONVERTER.convert(request).getName();
			}
		}
		return clientId;
	}

	private String getGrantType(OAuth2UsernamePasswordAuthenticationToken authentication) {
		String grantType;
		if (authentication == null || (grantType = authentication.getGrantType()) == null) {
			HttpServletRequest request = getRequest().orElseThrow(() -> new InternalAuthenticationServiceException("web request is null"));
			grantType = WebUtils.getParameter(OAuth2ParameterNames.GRANT_TYPE, request);
		}
		return grantType;
	}

	@Override
	protected void doAfterPropertiesSet() {}

}
