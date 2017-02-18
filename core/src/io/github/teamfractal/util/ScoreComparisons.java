package io.github.teamfractal.util;

import java.util.ArrayList;

import io.github.teamfractal.entity.Player;

// Created by Josh Neil for use in the game over screen
/**
 * Compares players scores and returns appropriate results
 * These methods are only used in the GameOverScreenActors class
 * but this class was created in order to separate these "back-end elements"
 * from the front-end/GUI code
 */
public class ScoreComparisons {
	/**
	 * Returns the text that states which player has won
	 * @param playerList The players that just finished playing the game
	 * @return The text that states which player has won
	 */
	public static String getWinnerText(ArrayList<Player> playerList){
		// Note if some (but not all) players have the same score the first one in the array wins
		// this is unfair but at present that game only has 2 players so this is not an issue
		if(allPlayersSameScore(playerList)){
			return "Its a draw!";
		}
		int highestScoreingPlayer = 0;
		int highestScore = 0;
		for(int playerNumber =0; playerNumber<playerList.size();playerNumber++){
			int score = playerList.get(playerNumber).getScore();
			if(score > highestScore){
				highestScore = score;
				highestScoreingPlayer = playerNumber;
			}
		}
		return "Player "+ Integer.toString(highestScoreingPlayer+1)+" wins!";
	}
	
	/**
	 * Returns true if all players have the same score (and false otherwise)
	 * @param playerList The player's who have finished playing the game
	 * @return true if all players have the same score (and false otherwise)
	 */
	private static boolean allPlayersSameScore(ArrayList<Player> playerList){
		int previousScore = playerList.get(0).getScore(); // Initialised to first player's score
		for(int playerNumber =1; playerNumber<playerList.size();playerNumber++){
			if(playerList.get(playerNumber).getScore() != previousScore){
				return false; // At least one score differs
			}
		}
		return true; // If we've reached this point then the players must have drawn!
	}
}
