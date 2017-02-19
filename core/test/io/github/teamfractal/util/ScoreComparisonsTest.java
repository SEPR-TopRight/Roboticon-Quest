package io.github.teamfractal.util;

import org.junit.*;

import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.entity.Player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

// Created by Josh Neil
/**
 * Integration tests for {@link ScoreComparisons}, {@link io.github.teamfractal.entity.Market} and {@link io.github.teamfractal.entity.Player}
 * (note market is stored within game and thus not initialised here)
 */
public class ScoreComparisonsTest {
	private Player player1;
	private Player player2;
	ArrayList<Player> playerList;
	RoboticonQuest game;
	
	/**
	 * Runs before every test and sets up all of the required objects
	 */
	@Before
	public void setup(){
		game = new RoboticonQuest();
		player1 = new Player(game);
		player2 = new Player(game);
		
		// All resources to 0, that way score is determined only by money
		// Which makes the getWinnerText method far easier to test
		player1.setEnergy(0);
		player1.setOre(0);
		player1.setFood(0);
		
		player2.setEnergy(0);
		player2.setOre(0);
		player2.setFood(0);
		
		playerList = new ArrayList<Player>();
		playerList.add(player1);
		playerList.add(player2);
	}
	
	/**
	 * Tests {@link ScoreComparisons#getWinnerText(ArrayList)} ensures that a string that states that 
	 * player 1 has won is returned when player 1's score is 1 larger than player 2's
	 */
	@Test
	public void testPlayer1ScoreGreater(){
		player1.setMoney(2);
		player2.setMoney(1);
		String winnerText = ScoreComparisons.getWinnerText(playerList).toLowerCase();
		assertTrue(!winnerText.contains("draw") && winnerText.contains("player 1") && !winnerText.contains("player 2"));
	}
	
	/**
	 * Tests {@link ScoreComparisons#getWinnerText(ArrayList)} ensures that a string that states that 
	 * player 1 has won is returned when player 1's score is far larger than player 2's
	 */
	@Test
	public void testPlayer1ScoreMuchGreater(){
		player1.setMoney(2090);
		player2.setMoney(132);
		String winnerText = ScoreComparisons.getWinnerText(playerList).toLowerCase();
		assertTrue(!winnerText.contains("draw") && winnerText.contains("player 1") && !winnerText.contains("player 2"));
	}
	
	/**
	 * Tests {@link ScoreComparisons#getWinnerText(ArrayList)} ensures that a string that states that 
	 * player 2 has won is returned when player 2's score is 1 larger than player 1's
	 */
	@Test
	public void testPlayer2ScoreGreater(){
		player1.setMoney(9);
		player2.setMoney(10);
		String winnerText = ScoreComparisons.getWinnerText(playerList).toLowerCase();
		assertTrue(!winnerText.contains("draw") && winnerText.contains("player 2") && !winnerText.contains("player 1"));
	}
	
	/**
	 * Tests {@link ScoreComparisons#getWinnerText(ArrayList)} ensures that a string that states that 
	 * player 2 has won is returned when player 2's score is far larger than player 1's
	 */
	@Test
	public void testPlayer2ScoreMuchGreater(){
		player1.setMoney(3123);
		player2.setMoney(342324);
		String winnerText = ScoreComparisons.getWinnerText(playerList).toLowerCase();
		assertTrue(!winnerText.contains("draw") && winnerText.contains("player 2") && !winnerText.contains("player 1"));
	}
	
	/**
	 * Tests {@link ScoreComparisons#getWinnerText(ArrayList)} ensures that a string that states that 
	 * it was a draw is returned when both player's score is 0
	 */
	@Test
	public void testBothScoresZero(){
		player1.setMoney(0);
		player2.setMoney(0);
		String winnerText = ScoreComparisons.getWinnerText(playerList).toLowerCase();
		assertTrue(winnerText.contains("draw") && !winnerText.contains("player 2") && !winnerText.contains("player 1"));
	}
	
	/**
	 * Tests {@link ScoreComparisons#getWinnerText(ArrayList)} ensures that a string that states that 
	 * it was a draw is returned when both player's score is 1
	 */
	@Test
	public void testBothScoresOne(){
		player1.setMoney(1);
		player2.setMoney(1);
		String winnerText = ScoreComparisons.getWinnerText(playerList).toLowerCase();
		assertTrue(winnerText.contains("draw") && !winnerText.contains("player 2") && !winnerText.contains("player 1"));
	}
	
	/**
	 * Tests {@link ScoreComparisons#getWinnerText(ArrayList)} ensures that a string that states that 
	 * it was a draw is returned when both player's score is 52
	 */
	@Test
	public void testBothScoresFiftyTwo(){
		player1.setMoney(52);
		player2.setMoney(52);
		String winnerText = ScoreComparisons.getWinnerText(playerList).toLowerCase();
		assertTrue(winnerText.contains("draw") && !winnerText.contains("player 2") && !winnerText.contains("player 1"));
	}
}
