

package utilities.validators;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import form.RegistrationForm;
import form.RegistrationFormManager;
public class PasswordMatchesValidatorManager implements ConstraintValidator<PasswordMatches, RegistrationFormManager> {



	@Override
	public void initialize(PasswordMatches constraintAnnotation) {
	}
	@Override
	public boolean isValid(RegistrationFormManager form, ConstraintValidatorContext context) {
		return form.getPassword().equals(form.getPasswordCheck());
	}
}