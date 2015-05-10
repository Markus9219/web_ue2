package controllers;

import at.ac.tuwien.big.we15.lab2.api.JeopardyFactory;
import at.ac.tuwien.big.we15.lab2.api.JeopardyGame;
import at.ac.tuwien.big.we15.lab2.api.impl.PlayJeopardyFactory;
import play.cache.Cache;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;
import play.Logger;



@Security.Authenticated(Authenticator.class)
public class Application extends Controller {
	
//	public static Result authentication(){
//		return AuthenticationController.authentication();
//	}

//    @play.db.jpa.Transactional
//    public static Result login(){
//        Form<Login> form = Form.form(Login.class).bindFromRequest();
//        try{
//            if(form.hasErrors()){
//                return badRequest(authentication.render(form));
//            }
//        }catch(Exception e){
//            return badRequest(authentication.render(form));
//        }
//        session("username", form.get().username);
//        return null;//TODO
//    }

    public static Result jeopardy(){
    	String username = session().get("username");
    	Logger.debug("Application.jeopardy - username='"+request().username()+"'");
    	JeopardyGame game = (JeopardyGame) Cache.get(username);
    	
    	Logger.debug("Das game welches geholt wurde: " + game);
    	
    	Cache.set(username, game);
    	return ok(views.html.jeopardy.render(game, Form.form(QuestionForm.class)));    	
    }
    
    public static void newGame(){
    	String lang = Controller.lang().code();
    	JeopardyFactory factory = (JeopardyFactory) Cache.get(lang);
    	if(factory == null){
    		factory = new PlayJeopardyFactory("data."+lang+".json");
    		Cache.set(lang, factory);
    	}
    	
    	JeopardyGame game = factory.createGame(session("username"));
    	
    	// create new game
		Logger.info("Neues Spiel erzeugt speichere im cache unter: " + session("username"));
		Cache.set(session("username"), game);
    }
    
    public static Result question(){
//    	Form<QuestionForm> form = Form.form(QuestionForm.class).bindFromRequest();
    	
//    	String idString = Form.form().get("question_selection");
    	DynamicForm form = Form.form().bindFromRequest();
    	String idString = form.get("question_selection");
    	Logger.debug("selected question = " + idString);
    	int questionID = Integer.parseInt(idString);
    	String gId = session().get("username");
    	JeopardyGame game = null;
    	if(gId != null){
    		game = (JeopardyGame) Cache.get(gId);
    	}
    	game.chooseHumanQuestion(questionID);
    	return ok(views.html.question.render(game));
    }

//    public static Result languageChanged() {
//
//    }
    
    public static class QuestionForm {
    	public int questionSelected;
    }
    
    public String validate() {
		Logger.debug("validate Application");
		
		return null;
	}

}
