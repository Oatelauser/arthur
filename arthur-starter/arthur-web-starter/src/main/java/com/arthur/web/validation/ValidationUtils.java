package com.arthur.web.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.executable.ExecutableValidator;
import jakarta.validation.groups.Default;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Bean Validation工具类封装
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2022-12-12
 * @since 1.0
 */
@SuppressWarnings("unused")
public class ValidationUtils {

	private static final Validator VALIDATOR;

	static {
		try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
			VALIDATOR = validatorFactory.getValidator();
		}
	}

	public static Validator obtainValidator() {
		return VALIDATOR;
	}

	public static ExecutableValidator obtainExecutableValidator() {
		return VALIDATOR.forExecutables();
	}

	public static BindingResult validate(Object obj) {
		return validate(obj, null, Default.class);
	}

	public static BindingResult validate(Object obj, String propertyName) {
		return validate(obj, propertyName, Default.class);
	}

	public static BindingResult validate(Object obj, String propertyName, Class<?>... groups) {
		Assert.notNull(obj, "validation object cannot be empty");
		Set<ConstraintViolation<Object>> violations = StringUtils.hasText(propertyName)
			? VALIDATOR.validateProperty(obj, propertyName, groups) : VALIDATOR.validate(obj, groups);
		return wrap(violations);
	}

	public static BindingResult wrap(Set<ConstraintViolation<Object>> violations) {
		BindingResult bindingResult = new BindingResult();
		if (CollectionUtils.isEmpty(violations)) {
			return bindingResult;
		}

		bindingResult.setHasError(true);
		Map<String, String> errorMsg = violations.stream()
			.collect(Collectors.toMap(violation -> violation.getPropertyPath().toString(),
				ConstraintViolation::getMessage));
		bindingResult.setErrorMsg(errorMsg);
		return bindingResult;
	}

	public static class BindingResult {

		private boolean hasError;
		private Map<String, String> errorMsg;

		public boolean isHasError() {
			return hasError;
		}

		public void setHasError(boolean hasError) {
			this.hasError = hasError;
		}

		public Map<String, String> getErrorMsg() {
			return errorMsg;
		}

		public void setErrorMsg(Map<String, String> errorMsg) {
			this.errorMsg = errorMsg;
		}

	}

}
