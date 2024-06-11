package com.arthur.oauth2.annotation;

import com.arthur.oauth2.constant.OAuth2Constants;
import edu.umd.cs.findbugs.annotations.NonNull;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.core.Ordered;

import java.io.Serial;

/**
 * Spring Aop Advisor
 *
 * @author DearYang
 * @date 2022-08-28
 * @see AuthPermission
 * @since 1.0
 */
public class AuthPermissionAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    @Serial
	private static final long serialVersionUID = 1209397538470925154L;

	public AuthPermissionAdvisor(HttpServletRequest request) {
		setAdviceBeanName(OAuth2Constants.DEFAULT_ADVICE_BEAN_NAME);
		this.setAdvice(new AuthPermissionAdvice(request));
		this.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
	}

	@NonNull
    @Override
    public Pointcut getPointcut() {
        return new AnnotationMatchingPointcut(AuthPermission.class, AuthPermission.class);
    }

}
