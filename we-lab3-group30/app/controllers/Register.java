package controllers;

import java.util.Date;

import javax.persistence.EntityManager;

import controllers.AuthenticationController.LoginForm;
import models.UserModel;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class Register extends Controller {
	@Transactional
	public static Result register() {
		Form<UserModel> registerForm = Form.form(UserModel.class);
		UserModel registerData = registerForm.bindFromRequest().get();
		
		if(registerForm.hasErrors()) {
			return badRequest(views.html.registration.render(registerForm));
		}

		String username = registerData.getName();
		EntityManager em = play.db.jpa.JPA.em();
		
		// check if username exists
		UserModel existing = em.find(UserModel.class, username);
		
		if(existing != null) {
			// Validation exception
			registerForm.reject("This username is already in use!");
			return badRequest(views.html.registration.render(registerForm));
		} else {
			em.persist(registerData);
			return ok(views.html.authentication.render(Form.form(LoginForm.class)));
		}
	}
}
