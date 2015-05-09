package controllers;

import at.ac.tuwien.big.we15.lab2.api.JeopardyFactory;
import at.ac.tuwien.big.we15.lab2.api.JeopardyGame;
import at.ac.tuwien.big.we15.lab2.api.Question;
import at.ac.tuwien.big.we15.lab2.api.impl.PlayJeopardyFactory;
import play.*;
import play.cache.Cache;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;
import views.html.*;
import views.html.helper.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Security.Authenticated(Authenticator.class)
public class Application extends Controller {
	
//	public static Result authentication(){
//		return AuthenticationController.authentication();
//	}
	
    public static Result jeopardy(){
    	String username = session().get("username");
    	JeopardyGame game;
    	if(username == null){
    		game = newGame();	
    	}else{
    		game = (JeopardyGame) Cache.get(username);
    	}
    	username = request().username();
    	session("username", username);
    	
    	Cache.set(username, game);
    	return ok(views.html.jeopardy.render(game, Form.form(Question.class)));    	
    }
    
    private static JeopardyGame newGame(){
    	String lang = Controller.lang().code();
    	JeopardyFactory factory = (JeopardyFactory) Cache.get(lang);
    	if(factory == null){
    		factory = new PlayJeopardyFactory("data."+lang+".json");
    		Cache.set(lang, factory);
    	}
    	return factory.createGame(session("username"));
    }
    
    public static Result question(){
    	Form<Question> form = Form.form(Question.class).bindFromRequest();
    	int questionID = form.get().getId();
    	String gId = session().get("game");
    	JeopardyGame game = null;
    	if(gId != null){
    		game = (JeopardyGame) Cache.get(gId);
    	}
    	game.chooseHumanQuestion(questionID);
    	return ok(views.html.question.render());
    }
    
    
//    public static class Question{
//    	public int questionSelected;
//    }

}
