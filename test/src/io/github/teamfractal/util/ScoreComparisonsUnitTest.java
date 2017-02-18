package io.github.teamfractal.util;

import org.junit.*;

import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.entity.Market;
import io.github.teamfractal.entity.Player;
import mockit.Expectations;
import mockit.Mocked;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

// Created by Josh Neil
// Note the score comparison does one linear scan of all the scores
// if the scores are not all equal it does another scan to determine which is the largest
// that is why the expectations are set up in the manner that they are
/**
 * Test case for {@link ScoreComparisons}
 */
public class ScoreComparisonsUnitTest {
	@Mocked private Player player1;
	@Mocked private Player player2;
	ArrayList<Player> playerList;
	@Mocked private RoboticonQuest game;
	
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
		new Expectations(){{
			player1.getScore();result=2;
			player2.getScore();result=1;
			player1.getScore();result=2;
			player2.getScore();result=1;
		}};
		String winnerText = ScoreComparisons.getWinnerText(playerList).toLowerCase();
		assertTrue(!winnerText.contains("draw") && winnerText.contains("player 1") && !winnerText.contains("player 2"));
	}
	
	/**
	 * Tests {@link ScoreComparisons#getWinnerText(ArrayList)} ensures that a string that states that 
	 * player 1 has won is returned when player 1's score is far larger than player 2's
	 */
	@Test
	public void testPlayer1ScoreMuchGreater(){
		new Expectations(){{
			player1.getScore();result=2090;
			player2.getScore();result=132;
			player1.getScore();result=2090;
			player2.getScore();result=132;
		}};
		String winnerText = ScoreComparisons.getWinnerText(playerList).toLowerCase();
		assertTrue(!winnerText.contains("draw") && winnerText.contains("player 1") && !winnerText.contains("player 2"));
	}
	
	/**
	 * Tests {@link ScoreComparisons#getWinnerText(ArrayList)} ensures that a string that states that 
	 * player 2 has won is returned when player 2's score is 1 larger than player 1's
	 */
	@Test
	public void testPlayer2ScoreGreater(){
		new Expectations(){{
			player1.getScore();result=9;
			player2.getScore();result=10;
			player1.getScore();result=9;
			player2.getScore();result=10;
		}};
		String winnerText = ScoreComparisons.getWinnerText(playerList).toLowerCase();
		assertTrue(!winnerText.contains("draw") && winnerText.contains("player 2") && !winnerText.contains("player 1"));
	}
	
	/**
	 * Tests {@link ScoreComparisons#getWinnerText(ArrayList)} ensures that a string that states that 
	 * player 2 has won is returned when player 2's score is far larger than player 1's
	 */
	@Test
	public void testPlayer2ScoreMuchGreater(){
		new Expectations(){{
			player1.getScore();result=3123;
			player2.getScore();result=1232131;
			player1.getScore();result=3123;
			player2.getScore();result=1232131;
		}};
		String winnerText = ScoreComparisons.getWinnerText(playerList).toLowerCase();
		assertTrue(!winnerText.contains("draw") && winnerText.contains("player 2") && !winnerText.contains("player 1"));
	}
	
	/**
	 * Tests {@link ScoreComparisons#getWinnerText(ArrayList)} ensures that a string that states that 
	 * it was a draw is returned when both player's score is 0
	 */
	@Test
	public void testBothScoresZero(){
		new Expectations(){{
			player1.getScore();result=0;
			player2.getScore();result=0;
		}};
		String winnerText = ScoreComparisons.getWinnerText(playerList).toLowerCase();
		assertTrue(winnerText.contains("draw") && !winnerText.contains("player 2") && !winnerText.contains("player 1"));
	}
	
	/**
	 * Tests {@link ScoreComparisons#getWinnerText(ArrayList)} ensures that a string that states that 
	 * it was a draw is returned when both player's score is 1
	 */
	@Test
	public void testBothScoresOne(){
		new Expectations(){{
			player1.getScore();result=1;
			player2.getScore();result=1;
		}};
		String winnerText = ScoreComparisons.getWinnerText(playerList).toLowerCase();
		assertTrue(winnerText.contains("draw") && !winnerText.contains("player 2") && !winnerText.contains("player 1"));
	}
	
	/**
	 * Tests {@link ScoreComparisons#getWinnerText(ArrayList)} ensures that a string that states that 
	 * it was a draw is returned when both player's score is 52
	 */
	@Test
	public void testBothScoresFiftyTwo(){
		new Expectations(){{
			player1.getScore();result=52;
			player2.getScore();result=52;
		}};;
		String winnerText = ScoreComparisons.getWinnerText(playerList).toLowerCase();
		assertTrue(winnerText.contains("draw") && !winnerText.contains("player 2") && !winnerText.contains("player 1"));
	}
}
