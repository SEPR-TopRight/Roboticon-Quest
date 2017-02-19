package io.github.teamfractal.entity;

import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.entity.enums.GamblingResult;
import io.github.teamfractal.entity.enums.ResourceType;
import mockit.*;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.Random;

import static org.junit.Assert.*;

// Josh Neil created this
/**
 * Integration tests for {@link Market} and {@link Player}
 */
public class MarketTest {
	@Rule
	public final ExpectedException exception = ExpectedException.none();

	private Market market;
	private Player player;
	private RoboticonQuest game;

	/**
	 * Reset market to its default status.
	 */
	@Before
	public void Contractor() {
		market = new Market();
		game = new RoboticonQuest();
		player = new Player(game);
	}
	
	/**
	 * Tests {@link Market#playerGamble(Player, int)} ensures that it returns GamblingResult.NOTENOUGHMONEY when
	 * a player has no money (and the cost of the bet is at least 1)
	 */
	@Test
	public void testPlayerGambleNoMoney(){
		player.setMoney(0);
		assertEquals(GamblingResult.NOTENOUGHMONEY,market.playerGamble(player, 10));
	}
	
	/**
	 * Tests {@link Market#playerGamble(Player, int)} ensures that it returns GamblingResult.NOTENOUGHMONEY when
	 * a player has 1 less money than the bet
	 */
	@Test
	public void testPlayerGambleOneLessMoneyThanBet(){
		player.setMoney(5);
		assertEquals(GamblingResult.NOTENOUGHMONEY,market.playerGamble(player, 6));
	}
	
	/**
	 * Tests {@link Market#playerGamble(Player, int)} ensures that it does not return GamblingResult.NOTENOUGHMONEY when
	 * a player has exactly the same amount of money as the bet
	 */
	@Test
	public void testPlayerGambleMoneySameAsBet(){
		player.setMoney(5);
		assertNotEquals(GamblingResult.NOTENOUGHMONEY,market.playerGamble(player, 5));
	}
	
	/**
	 * Tests {@link Market#playerGamble(Player, int)} ensures that an IllegalArgumentException is thrown when a 
	 * negative bet is passed to it
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testPlayerGambleNegativeBet(){
		market.playerGamble(player,-1);
	}
	
	// This test could potentially loop forever if the result Gambling.LOST is never returned...
	// Note: there is not a unit test version of this test as it was too complex to do with mocked objects
	/**
	 * Tests {@link Market#playerGamble(Player, int)} ensures that when it returns GamblingResult.LOST
	 * the bet is removed from the players money (when the bet is 5)
	 */
	public void testPlayerGambleLostBetRemovedFive(){
		while(true){
			player.setMoney(16);
			GamblingResult result = market.playerGamble(player, 5);
			if(result == GamblingResult.LOST){
				break;
			}
		}
		assertEquals(9,player.getMoney());
		
	}

	// This test could potentially loop forever if the result Gambling.LOST is never returned...
	// Note: there is not a unit test version of this test as it was too complex to do with mocked objects
	/**
	 * Tests {@link Market#playerGamble(Player, int)} ensures that when it returns GamblingResult.LOST
	 * the bet is removed from the players money (when the bet is 12)
	 */
	public void testPlayerGambleLostBetRemovedTwelve(){
		while(true){
			player.setMoney(16);
			GamblingResult result = market.playerGamble(player, 12);
			if(result == GamblingResult.LOST){
				break;
			}
		}
		assertEquals(4,player.getMoney());
		
	}
	
	// This test could potentially loop forever if the result Gambling.WON is never returned...
	// Note: there is not a unit test version of this test as it was too complex to do with mocked objects
	/**
	 * Tests {@link Market#playerGamble(Player, int)} ensures that when it returns GamblingResult.WON
	 * the bet is added to the players money (when the bet is 10)
	 */
	public void testPlayerGambleWonBetAddedTen(){
		while(true){
			player.setMoney(16);
			GamblingResult result = market.playerGamble(player, 10);
			if(result == GamblingResult.WON){
				break;
			}
		}
		assertEquals(26,player.getMoney());
		
	}
	// This test could potentially loop forever if the result Gambling.WON is never returned...
	// Note: there is not a unit test version of this test as it was too complex to do with mocked objects
	/**
	 * Tests {@link Market#playerGamble(Player, int)} ensures that when it returns GamblingResult.WON
	 * the bet is added to the players money (when the bet is 1)
	 */
	public void testPlayerGambleWonBetAddedOne(){
		while(true){
			player.setMoney(300);
			GamblingResult result = market.playerGamble(player, 1);
			if(result == GamblingResult.WON){
				break;
			}
		}
		assertEquals(301,player.getMoney());
		
	}
	
	

}
