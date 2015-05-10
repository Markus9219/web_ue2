package controllers;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import at.ac.tuwien.big.we15.lab2.api.Avatar;
import controllers.AuthenticationController.LoginForm;
import models.UserModel;
import play.data.Form;
import play.data.validation.ValidationError;
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
		
		for(String key:registerData.data().keySet()) {
			Logger.debug(key + " -> " + registerData.data().get(key));
		}
		
		Map<String, List<ValidationError>> errors = registerData.errors();
		for(String key:errors.keySet()) {
			Logger.debug(key + " -> " + errors.get(key).get(0).toString());
		}
		
		
		if(registerData.hasErrors()) {
			Logger.info("register Data has errors");
			return badRequest(views.html.registration.render(registerData));
		}

		String username = registerData.get().getName();
		EntityManager em = play.db.jpa.JPA.em();
		
		// check if username exists
		UserModel existing = em.find(UserModel.class, username);
		
//		UserModel um = new UserModel();
//		um.setAvatar(Avatar.ALDRICH_KILLIAN);
//		um.setDateOfBirth(new Date());
//		um.
//		
		if(existing != null) {
			// Validation exception
			registerData.reject("This username is already in use!");
			Logger.info("Username bereits vorhanden");
			return badRequest(views.html.registration.render(registerData));
		} else {
			em.persist(registerData.get());
			Logger.info("Neuen User angelegt");
			return ok(views.html.authentication.render(Form.form(LoginForm.class)));
		}
	}
	
	public String validate() {
		Logger.debug("validate Register");
		
		return null;
	}
}
