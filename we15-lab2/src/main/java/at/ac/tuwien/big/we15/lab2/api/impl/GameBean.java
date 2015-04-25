package at.ac.tuwien.big.we15.lab2.api.impl;

import java.util.Date;
import java.util.List;

import at.ac.tuwien.big.we15.lab2.api.Avatar;
import at.ac.tuwien.big.we15.lab2.api.Category;
import at.ac.tuwien.big.we15.lab2.api.Question;

public class GameBean {
	private Game game;
	
	public GameBean(List<Category> categories) {
		//game = new Game();
	}

	// start new
	public void startNewGame() {
		
	}
	
	// get questions
	public List<Category> getQuestions() {
		return null;
	}
	
	// select question
	public Question selectQuestion(int id){
		return null;
	}
	
	// answer question
	public boolean answerQuestion(List<Integer> answerIds) {
		return false;
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
	public List<String> getMessageLog() {
		return null;
	}
	
	public int getCurrentRound() {
		return 0;
	}
	
	public Question getActiveQuestion() {
		return null;
	}
	
	public Avatar getWinner() {
		return null;
	}
	
	public Avatar getLoser() {
		return null;
	}
	public int getScoreWinner(){
		return 0;
	}
	public int getScoreLoser(){
		return 0;
	}
}
