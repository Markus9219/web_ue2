package controllers;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import at.ac.tuwien.big.we15.lab2.api.JeopardyGame;
import models.UserModel;
import play.cache.Cache;
import play.data.Form;
import play.data.validation.ValidationError;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.Logger;

public class AuthenticationController extends Controller{
	
	public static Result authentication(){
		return ok(views.html.authentication.render(Form.form(LoginForm.class)));
	}
	
	@Transactional
	public static Result authenticate(){
		Logger.debug("authenticate beginning");
		Form<LoginForm> login = Form.form(LoginForm.class).bindFromRequest();
		
		for(String key:login.data().keySet()) {
			Logger.debug(key + " -> " + login.data().get(key));
		}
		
		Map<String, List<ValidationError>> errors = login.errors();
		for(String key:errors.keySet()) {
			Logger.debug(key + " --> " + errors.get(key).get(0).toString());
		}
		
		if(!login.hasErrors()){
			if(!isCorrect(login.get().username, login.get().password)){
				Logger.debug("Invalid Username oder password");
				login.reject("Invalid Username oder password");
			}
		}
		
		if(login.hasErrors()){
			Logger.debug("authenticate hasErrors");
			return badRequest(views.html.authentication.render(login));
		}else{
			session("username", login.get().username);
			Application.newGame();
    		
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
		Cache.remove(session("username"));
		return redirect(routes.AuthenticationController.authentication());
	}
	
	public static class LoginForm {
		public String username;
		public String password;
	}

	public String validate() {
		Logger.debug("validate AuthenticationController");
		
		return null;
	}

}
