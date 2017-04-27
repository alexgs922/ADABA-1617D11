
package utilities.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import form.RegistrationFormManager;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatchesManager, RegistrationFormManager> {

	@Override
	public void initialize(final PasswordMatchesManager constraintAnnotation) {
	}
	@Override
	public boolean isValid(final RegistrationFormManager form, final ConstraintValidatorContext context) {
		return form.getPassword().equals(form.getPasswordCheck());
	}
}
