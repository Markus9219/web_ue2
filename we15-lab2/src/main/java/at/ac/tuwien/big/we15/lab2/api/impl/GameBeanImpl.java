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
	private List<Message> messageLog = new ArrayList<Message>();
	private List<Question> answered = new ArrayList<Question>();
	private List<Question> remainingQuestions = new ArrayList<Question>();
	private Avatar activePlayer;
	
	public GameBeanImpl() {
		Avatar player =  Avatar.BLACK_WIDOW;
		Avatar ki = Avatar.DEADPOOL;
		game = new Game(player, ki);
		activePlayer = player;
	}
	
	public void setCategories(List<Category> categories) {
		System.out.println("setCategories wird aufgerufen aber zu spaet wahrscheinlich.");
		this.categories = categories;
		
		for(int i = 0; i < categories.size(); i++){
			for(int j = 0; j < categories.get(i).getQuestions().size(); j++){
				remainingQuestions.add(categories.get(i).getQuestions().get(j));
			}
		}
	}

	public boolean wasAnswered(int id) {
		for(Question q:answered){
			if(q.getId() == id)
				return true;
		}
		
		return false;
	}
	
	// select question
	public Question selectQuestion(int id){
		for(Category category:categories){
			for(Question question:category.getQuestions()){
				if(question.getId() == id) {
					currentQuestion = question;
					
					if(activePlayer.isVillain()) {
						String nachricht = activePlayer.getName() + " hat " + currentQuestion.getCategory().getName() + " für € " + currentQuestion.getValue() + " gewählt.";
						messageLog.add(new Message(nachricht, MessageType.NEUTRAL));
					}
					
					return question;
				}
			}
		}
		return null;
	}
	
	public boolean answerQuestion(List<Integer> answerIds){
		List<Answer> correctAnswers = currentQuestion.getCorrectAnswers();
		
		boolean correct = true;
		if(correctAnswers.size() != answerIds.size()){
			correct = false;
		}else{
			for(Answer correctAnswer:correctAnswers) {
				if(!answerIds.contains(correctAnswer.getId()))
					correct = false;
			}
		}
		
		if(!activePlayer.isVillain()) {
			if(correct == true){
				game.increaseScorePlayer(currentQuestion.getValue());
				messageLog.add(new Message("Du hast richtig geantwortet: +" + currentQuestion.getValue() + "€", MessageType.POSITIVE));
			}else {
				game.increaseScorePlayer(-currentQuestion.getValue());
				messageLog.add(new Message("Du hast falsch geantwortet: -" + currentQuestion.getValue() + "€", MessageType.NEGATIVE));
			}
		}else{
			if(correct == true){
				game.increaseScoreKI(currentQuestion.getValue());
				messageLog.add(new Message("Deadpool hat richtig geantwortet: +" + currentQuestion.getValue() + "€", MessageType.POSITIVE));
			}else {
				game.increaseScoreKI(-currentQuestion.getValue());
				messageLog.add(new Message("Deadpool hat falsch geantwortet: -" + currentQuestion.getValue() + "€", MessageType.NEGATIVE));
			}
		}
		
		
		answered.add(currentQuestion);
		remainingQuestions.remove(currentQuestion);
		
		if(answered.size() % 2 == 0) {
			game.increaseRound();
		}
		
		return correct;
	}
	
	public void changeActivePlayer() {
		if(activePlayer.isVillain()) {
			activePlayer = game.getPlayer();
		}else{
			activePlayer = game.getKI();
		}
	}
	
	public void kiTurn() {
		// select question
		int r = (int) (Math.random() * remainingQuestions.size());
		
		Question kiSelectedQuestion = remainingQuestions.get(r);
		
		selectQuestion(kiSelectedQuestion.getId());
		
		// bestimme zufaellig ob richtig oder falsch
		double c = Math.random();
		List<Integer> answerIds = new ArrayList<Integer>();
		if(c > 0.5){
			for(Answer correctAnswer : kiSelectedQuestion.getCorrectAnswers()) {
				answerIds.add(correctAnswer.getId());
			}
			
			answerQuestion(answerIds);
		}else{
			answerQuestion(answerIds);
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
		if(game.getCurrentRound() >= 10){
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
		if(game.getCurrentRound() >= 10){
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

	public void setActivePlayer(Avatar activePlayer) {
		this.activePlayer = activePlayer;
		
	}
}
