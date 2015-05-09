package controllers;

import javax.persistence.EntityManager;

import models.UserModel;
import play.cache.Cache;
import play.data.Form;
import play.data.validation.ValidationError;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class AuthenticationController extends Controller{
	
	public static Result authentication(){
		return ok(views.html.authentication.render(Form.form(Login.class)));
	}
	
	@Transactional
	public static Result authenticate(){
		Form<Login> login = Form.form(Login.class).bindFromRequest();
		
		if(!login.hasErrors()){
			if(!isCorrect(login.get().username, login.get().password)){
				login.reject(new ValidationError("Username Password Check", "Invalid Username oder password"));
			}
		}
		
		if(login.hasErrors()){
			return badRequest(views.html.authentication.render(login));
		}else{
			session().clear();
			session("username", login.get().username);
			return redirect(routes.Application.jeopardy());
		}
		
	}
	
	public static boolean isCorrect(String username, String password){
		EntityManager em = play.db.jpa.JPA.em();
		UserModel user = em.find(UserModel.class, username);
		if(user == null){
			return false;
		}else{
			return user.getPassword().equals(password);
		}
	}
	
	@Security.Authenticated(Authenticator.class)
	public static Result logout(){
		session().clear();
		Cache.remove(request().username());
		return redirect(routes.AuthenticationController.authentication());
	}
	
	public static class Login{
		public String username;
		public String password;
	}

}
