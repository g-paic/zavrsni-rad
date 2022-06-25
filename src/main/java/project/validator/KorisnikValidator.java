package project.validator;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import project.dao.KorisnikDAO;
import project.entities.KorisnikENTITY;
import project.forms.KorisnikForm;

@Component
public class KorisnikValidator implements Validator {

	private EmailValidator emailValidator = EmailValidator.getInstance();

	@Autowired
	private KorisnikDAO korisnikDAO;

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz == KorisnikForm.class;
	}

	@Override
	public void validate(Object target, Errors errors) {
		KorisnikForm korisnikForm = (KorisnikForm) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "NotEmpty.korisnikForm.firstName");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "NotEmpty.korisnikForm.lastName");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.korisnikForm.email");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "company", "NotEmpty.korisnikForm.company");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "NotEmpty.korisnikForm.userName");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.korisnikForm.password");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "NotEmpty.korisnikForm.confirmPassword");
		
		if(errors.hasErrors()) {
			return;
		}

		// check email
		if(!this.emailValidator.isValid(korisnikForm.getEmail())) {
			// Invalid email.
			errors.rejectValue("email", "Pattern.korisnikForm.email");
			return;
		}
		
		if(!korisnikForm.getPassword().equals(korisnikForm.getConfirmPassword())) {
			errors.rejectValue("confirmPassword", "Match.korisnikForm.confirmPassword");
			return;
		}
		
		KorisnikENTITY korisnik = korisnikDAO.findUserByEmail(korisnikForm.getEmail());
		if(korisnik != null) {
			// Email has been used by another account.
			errors.rejectValue("email", "Duplicate.korisnikForm.email");
			return;
		}
		
		korisnik = korisnikDAO.findUserByCompany(korisnikForm.getCompany());
		if(korisnik != null) {
			errors.rejectValue("company", "Duplicate.korisnikForm.company");
			return;
		}
		
		korisnik = korisnikDAO.findUserByName(korisnikForm.getUserName());
		if(korisnik != null) {
			errors.rejectValue("userName", "Duplicate.korisnikForm.userName");
			return;
		}
	}
}