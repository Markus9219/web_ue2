package at.ac.tuwien.big.we15.lab2.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.ac.tuwien.big.we15.lab2.api.Answer;
import at.ac.tuwien.big.we15.lab2.api.Avatar;
import at.ac.tuwien.big.we15.lab2.api.Category;
import at.ac.tuwien.big.we15.lab2.api.Question;

public class GameBeanImpl {
	private Game game;
	private List<Category> categories;
	private Question currentQuestion;
	private int currentCategory;
	private List<Message> messageLog = new ArrayList<Message>();
	
	public GameBeanImpl() {
		Avatar player =  Avatar.BLACK_WIDOW;
		Avatar ki = Avatar.DEADPOOL;
		game = new Game(player, ki);
	}
	
	public void setCategories(List<Category> categories) {
		System.out.println("setCategories wird aufgerufen aber zu spaet wahrscheinlich.");
		this.categories = categories;
	}
	
//	public GameBeanImpl(List<Category> categories) {
//		this.categories = categories;
//		Avatar player =  Avatar.BLACK_WIDOW;
//		Avatar ki = Avatar.DEADPOOL;
//		game = new Game(player, ki);
//	}


	// select question
	public Question selectQuestion(int id){
		for(Category category:categories){
			for(Question question:category.getQuestions()){
				if(question.getId() == id) {
					return question;
				}
			}
		}
		return null;
	}
	
	// answer question
	public boolean answerQuestion(List<Integer> answerIds) {
		boolean correct = false;
		if(whoStartsRound() == true){
			correct = playerTurn(answerIds);
			startKITurn();
		}else{
			startKITurn();
			correct = playerTurn(answerIds);
		}
		game.increaseRound();
		return correct;
	}
	
	public boolean playerTurn(List<Integer> answerIds){
		List<Answer> answers = currentQuestion.getAllAnswers();
		boolean correct = false;
		if(answers.size() != answerIds.size()){
			correct = false;
		}else{
			for(int i = 0; i < answerIds.size(); i++){
				for(int j = 0; j < answers.size(); j++){
					if(answers.get(j).getId() == answerIds.get(i)){
						correct = true;
					}
				}
			}
		}
		if(correct == true){
			game.increaseScorePlayer(currentQuestion.getValue());
			messageLog.add(new Message("Du hast richtig geantwortet: +" + currentQuestion.getValue() + "€", MessageType.POSITIVE));
		}else{
			game.increaseScorePlayer(-currentQuestion.getValue());
			messageLog.add(new Message("Du hast falsch geantwortet: -" + currentQuestion.getValue() + "€", MessageType.NEGATIVE));
		}
		categories.get(currentCategory).removeQuestion(currentQuestion);
		return correct;
	}
	
	public void startKITurn(){
		List<Question> questionsKI = null;
		for(int i = 0; i < categories.size(); i++){
			for(int j = 0; j < categories.get(i).getQuestions().size(); j++){
				questionsKI.add(categories.get(i).getQuestions().get(j));
			}
		}
		int r = (int) (Math.random() * questionsKI.size());
		// Deadpool hat TUWIEN für € 1000 gewählt.
		String nachricht = game.getKI().getName() + " hat " + questionsKI.get(r).getCategory().getName() + " für € " + questionsKI.get(r).getValue() + " gewählt.";
		messageLog.add(new Message(nachricht, MessageType.NEUTRAL));
		
		double c = Math.random();
		if(c > 0.5){
			game.increaseScoreKI(questionsKI.get(r).getValue());
			messageLog.add(new Message("Deadpool hat richtig geantwortet: +" + questionsKI.get(r).getValue() + "€", MessageType.POSITIVE));
		}else{
			game.increaseScoreKI(-questionsKI.get(r).getValue());
			messageLog.add(new Message("Deadpool hat falsch geantwortet: -" + questionsKI.get(r).getValue() + "€", MessageType.NEGATIVE));
		}
		for(int i = 0; i < categories.size(); i++){
			if(categories.get(i).getQuestions().contains(questionsKI.get(r))){
				categories.get(i).getQuestions().remove(questionsKI.get(r));
			}
		}
	}
	
	public boolean whoStartsRound(){
		if(getScorePlayer() >= getScoreNpc()){
			return true;
		}else{
			return false;
		}
	}
	
	// get score player
	public int getScorePlayer() {
		return game.getScorePlayer();
	}
	
	// get score npc
	public int getScoreNpc(){
		return game.getScoreKI();
	}
	
	public Avatar getPlayerAvatar() {
		return game.getPlayer();
	}
	
	public Avatar getNpcAvatar() {
		return game.getKI();
	}
	
	public Date lastGameDate() {
		return null;
	}
	
	/**
	 * Gibt die Statusmeldungen zurueck. (z.B. Deadpool hat falsch geantwortet)
	 * @return
	 */
	public List<Message> getMessageLog() {
		return messageLog;
	}
	
	public int getCurrentRound() {
		return game.getCurrentRound();
	}
	
	public Question getActiveQuestion() {
		return currentQuestion;
	}
	
	public Avatar getWinner() {
		if(game.getCurrentRound() == 10){
			if(game.getScorePlayer() >= game.getScoreKI()){
				return game.getPlayer();
			}else{
				return game.getKI();
			}
		}else{
			return null;
		}
	}
	
	public Avatar getLoser() {
		if(game.getCurrentRound() == 10){
			if(game.getScorePlayer() < game.getScoreKI()){
				return game.getPlayer();
			}else{
				return game.getKI();
			}
		}else{
			return null;
		}
	}
	
	public int getScoreWinner(){
		if(game.getScorePlayer() > game.getScoreKI()){
			return game.getScorePlayer();
		}else{
			return game.getScoreKI();
		}
	}
	
	public int getScoreLoser(){
		if(game.getScorePlayer() < game.getScoreKI()){
			return game.getScorePlayer();
		}else{
			return game.getScoreKI();
		}
	}
}
