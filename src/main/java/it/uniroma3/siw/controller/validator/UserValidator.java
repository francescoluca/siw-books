package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.UserService;

@Component
public class UserValidator implements Validator {

	@Autowired
	private UserService userService;

	@Override
	public void validate(Object o, Errors errors) {
		User user = (User) o;
		if (user.getEmail() != null && userService.existsByEmail(user.getEmail())) {
			errors.rejectValue("email", "user.email.duplicate", "Email già in uso");
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return User.class.equals(aClass);
	}
}