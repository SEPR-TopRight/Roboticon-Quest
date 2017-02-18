package io.github.teamfractal.util;

import org.junit.*;

import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.entity.Player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

// Created by Josh Neil
// Please not that there is a very, very small chance that some of these tests could fail even if the code
// is correct as the random events methods are meant to return true or false with a certain probability.
// These tests just check that when these methods are run Integer.MAX_VALUE number of times they return true at least
// once and false at least once.
/**
 * Integration tests involving {@link RandomEvents}
 */
public class RandomEventsTests {
	
	/**
	 * Tests {@link RandomEvents#geese(RoboticonQuest)} and ensures that it returns zero when the current
	 * player has no food in their inventory
	 */
	@Test
	public void testGeeseReturnHalfFoodZero(){
		RoboticonQuest game = new RoboticonQuest();
		Player player = new Player(game); // Ensures at least one player in the game
		player = game.getPlayer();
		player.setFood(0);
		assertEquals(0,RandomEvents.geese(game));
	}
	
	/**
	 * Tests {@link RandomEvents#geese(RoboticonQuest)} and ensures that no food is taken out of the players inventory
	 * when the player has no food in their inventory
	 */
	@Test
	public void testGeeseNoFoodTakenZero(){
		RoboticonQuest game = new RoboticonQuest();
		Player player = new Player(game); // Ensures at least one player in the game
		player = game.getPlayer();
		player.setFood(0);
		RandomEvents.geese(game);
		assertEquals(0,player.getFood());
	}
	
	/**
	 * Tests {@link RandomEvents#geese(RoboticonQuest)} and ensures that it returns 0 when the current
	 * player has 1 in their inventory
	 */
	@Test
	public void testGeeseReturnHalfFoodOne(){
		RoboticonQuest game = new RoboticonQuest();
		Player player = new Player(game); // Ensures at least one player in the game
		player = game.getPlayer();
		player.setFood(1);
		assertEquals(0,RandomEvents.geese(game));
	}
	
	/**
	 * Tests {@link RandomEvents#geese(RoboticonQuest)} and ensures that no food is taken out of the players inventory
	 * when the player has 1 food in their inventory
	 */
	@Test
	public void testGeeseNoFoodTakenOne(){
		RoboticonQuest game = new RoboticonQuest();
		Player player = new Player(game); // Ensures at least one player in the game
		player = game.getPlayer();
		player.setFood(1);
		RandomEvents.geese(game);
		assertEquals(1,player.getFood());
	}
	
	/**
	 * Tests {@link RandomEvents#geese(RoboticonQuest)} and ensures that it returns 2 when the current
	 * player has 4 in their inventory
	 */
	@Test
	public void testGeeseReturnHalfFoodFour(){
		RoboticonQuest game = new RoboticonQuest();
		Player player = new Player(game); // Ensures at least one player in the game
		player = game.getPlayer();
		player.setFood(4);
		assertEquals(2,RandomEvents.geese(game));
	}
	
	/**
	 * Tests {@link RandomEvents#geese(RoboticonQuest)} and ensures that 2 food are taken out of the players inventory
	 * when the player has 4 food in their inventory
	 */
	@Test
	public void testGeeseTwoFoodTakenFour(){
		RoboticonQuest game = new RoboticonQuest();
		Player player = new Player(game); // Ensures at least one player in the game
		player = game.getPlayer();
		player.setFood(4);
		RandomEvents.geese(game);
		assertEquals(2,player.getFood());
	}
	
	/**
	 * Tests {@link RandomEvents#geese(RoboticonQuest)} and ensures that it returns 2 when the current
	 * player has 5 food in their inventory
	 */
	@Test
	public void testGeeseReturnHalfFoodFive(){
		RoboticonQuest game = new RoboticonQuest();
		Player player = new Player(game); // Ensures at least one player in the game
		player = game.getPlayer();
		player.setFood(4);
		assertEquals(2,RandomEvents.geese(game));
	}
	
	/**
	 * Tests {@link RandomEvents#geese(RoboticonQuest)} and ensures that 2 food are taken out of the players inventory
	 * when the player has 5 food in their inventory
	 */
	@Test
	public void testGeeseTwoFoodTakenFive(){
		RoboticonQuest game = new RoboticonQuest();
		Player player = new Player(game); // Ensures at least one player in the game
		player = game.getPlayer();
		player.setFood(5);
		RandomEvents.geese(game);
		assertEquals(3,player.getFood());
	}
	
	/**
	 * Tests {@link RandomEvents#amountOfMoneyInTreasureChest(RoboticonQuest)} 
	 * and ensures that when it is run Integer.MAX_VALUE times it returns at least 2 different values
	 */
	@Test
	public void testAmountOfMoneyInTreasureChestReturnDifferentValues(){
		RoboticonQuest game = new RoboticonQuest();
		Player player = new Player(game); // Ensures at least one player in the game

		int previousValueReturned = RandomEvents.amountOfMoneyInTreasureChest(game);
		for(int i=0; i<Integer.MAX_VALUE; i++){
			if(! (previousValueReturned == RandomEvents.amountOfMoneyInTreasureChest(game))){
				return; // If at least two different values have been returned
			}
		}
		fail();
	}
	
	/**
	 * Tests {@link RandomEvents#amountOfMoneyInTreasureChest(RoboticonQuest)} 
	 * and ensures that the returned value is added to the players inventory
	 */
	@Test
	public void testAmountOfMoneyInTreasureChestAddedtoPlayer(){
		RoboticonQuest game = new RoboticonQuest();
		Player player = new Player(game); // Ensures at least one player in the game
		player = game.getPlayer();
		int moneyBefore = player.getMoney();
		
		int amountThatShouldBeAdded = RandomEvents.amountOfMoneyInTreasureChest(game);
		
		assertEquals(moneyBefore+amountThatShouldBeAdded,player.getMoney());
	}
}
