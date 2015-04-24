package at.ac.tuwien.big.we15.lab2.api.impl;

import at.ac.tuwien.big.we15.lab2.api.Avatar;

public class Game {
	
	private int currentRound = 1;
	private Avatar player = null;
	private Avatar ki = null;
	private int scorePlayer = 0;
	private int scoreKI = 0;
	
	public Game(Avatar player, Avatar ki){
		this.player = player;
		this.ki = ki;
	}
	
	public Avatar getPlayer(){
		return this.player;
	}
	
	public Avatar getKI(){
		return this.ki;
	}
	
	public int getCurrentRound(){
		return currentRound;
	}
	
	public int getScorePlayer(){
		return scorePlayer;
	}
	
	public int getScoreKI(){
		return scoreKI;
	}
	
	public void setScorePlayer(int scorePlayer){
		this.scorePlayer = scorePlayer;
	}
	
	public void setScoreKI(int scoreKI){
		this.scoreKI = scoreKI;
	}
	

}
