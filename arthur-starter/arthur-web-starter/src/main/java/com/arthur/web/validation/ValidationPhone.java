package com.arthur.web.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * 校验手机号
 */
public class ValidationPhone implements ConstraintValidator<Phone, String> {

	public static final String PHONE_REGEXP = "^((13[0-9])|(14[5-9])|(15([0-3]|[5-9]))|(16([5,6])|(17[0-8])|(18[0-9]))|(19[1,8,9]))\\d{8}$";

	private final Pattern pattern = Pattern.compile(PHONE_REGEXP);

	/**
	 * 校验手机号
	 *
	 * @param phone                      {@link  String}
	 * @param constraintValidatorContext {@link  ConstraintValidatorContext}
	 * @return {@link  Boolean}
	 */
	@Override
	public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
		if (StringUtils.hasText(phone)) {
			return pattern.matcher(phone).matches();
		}
		return false;
	}

}
