

package utilities.validators;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import form.RegistrationForm;
import form.RegistrationFormManager;
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatchesManager, RegistrationFormManager> {



	@Override
	public void initialize(PasswordMatchesManager constraintAnnotation) {
	}
	@Override
	public boolean isValid(RegistrationFormManager form, ConstraintValidatorContext context) {
		return form.getPassword().equals(form.getPasswordCheck());
	}
}