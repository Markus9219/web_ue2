package controllers;

import java.util.Date;

import javax.persistence.EntityManager;

import controllers.AuthenticationController.LoginForm;
import models.UserModel;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.Logger;

public class Register extends Controller {
	
	public static Result registration(){
		Logger.info("Open Registration blabla");
		return ok(views.html.registration.render(Form.form(UserModel.class)));
	}
	
	@Transactional
	public static Result register() {
		Logger.info("entered Register method");
		Form<UserModel> registerData = Form.form(UserModel.class).bindFromRequest();
//		UserModel registerData = registerForm.bindFromRequest().get();
		
		if(registerData.hasErrors()) {
			Logger.info("register Data has errors");
			return badRequest(views.html.registration.render(registerData));
		}

		String username = registerData.get().getName();
		EntityManager em = play.db.jpa.JPA.em();
		
		// check if username exists
		UserModel existing = em.find(UserModel.class, username);
		
		if(existing != null) {
			// Validation exception
			registerData.reject("This username is already in use!");
			Logger.info("Username bereits vorhanden");
			return badRequest(views.html.registration.render(registerData));
		} else {
			em.persist(registerData);
			Logger.info("Neuen User angelegt");
			return ok(views.html.authentication.render(Form.form(LoginForm.class)));
		}
	}
}
