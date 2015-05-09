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

public class Application extends Controller {
	
    @Security.Authenticated(Authenticator.class)
    public static Result jeopardy(){
    	String id = session().get("game");
    	JeopardyGame game;
    	if(id == null){
    		game = newGame();
    	}else{
    		game = (JeopardyGame) Cache.get(id);
    	}
    	id = session().get("username");
    	session("game", id);
    	
    	Cache.set(id, game);
    	return ok(jeopardy.render(game, Form.form(Question.class)));    	
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
    	int questionID = form.get().questionSelected;
    	String gId = session().get("game");
    	JeopardyGame game = null;
    	if(gId != null){
    		game = (JeopardyGame) Cache.get(gId);
    	}
    	game.chooseHumanQuestion(questionID);
    	return ok(question.render());
    }
    
    
    public static class Question{
    	public int questionSelected;
    }

}
