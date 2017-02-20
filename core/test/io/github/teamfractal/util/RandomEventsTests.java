package io.github.teamfractal.util;

import org.junit.*;

import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.entity.Player;
import io.github.teamfractal.entity.enums.ResourceType;

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
	 * Tests {@link RandomEvents#roboticonIsFaulty()} ensures that when it is run Integer.MAX_VALUE number of times
	 * it returns true at least once and false at least once
	 */
	@Test
	public void testRoboticonIsFaulty(){
		boolean atLeastOneTrue=false;
		boolean atLeastOneFalse=false;
		for(int i=0; i<Integer.MAX_VALUE; i++){
			boolean result = RandomEvents.roboticonIsFaulty();
			if(result){
				atLeastOneTrue = true;
			}
			else{
				atLeastOneFalse = true;
			}
			if(atLeastOneTrue && atLeastOneFalse){
				return;
			}
		}
		fail();
	}
	
	/**
	 * Tests {@link RandomEvents#tileHasChest()} ensures that when it is run Integer.MAX_VALUE number of times
	 * it returns true at least once and false at least once
	 */
	@Test
	public void testTileHasChest(){
		boolean atLeastOneTrue=false;
		boolean atLeastOneFalse=false;
		for(int i=0; i<Integer.MAX_VALUE; i++){
			boolean result = RandomEvents.tileHasChest();
			if(result){
				atLeastOneTrue = true;
			}
			else{
				atLeastOneFalse = true;
			}
			if(atLeastOneTrue && atLeastOneFalse){
				return;
			}
		}
		fail();
	}
	
	/**
	 * Tests {@link RandomEvents#geeseAttack()} ensures that when it is run Integer.MAX_VALUE number of times
	 * it returns true at least once and false at least once
	 */
	@Test
	public void testGuestAttack(){
		boolean atLeastOneTrue=false;
		boolean atLeastOneFalse=false;
		for(int i=0; i<Integer.MAX_VALUE; i++){
			boolean result = RandomEvents.geeseAttack();
			if(result){
				atLeastOneTrue = true;
			}
			else{
				atLeastOneFalse = true;
			}
			if(atLeastOneTrue && atLeastOneFalse){
				return;
			}
		}
		fail();
	}
	
	/**
	 * Tests {@link RandomEvents#geeseStealResources(Player, ResourceType)} with food and ensures that it returns zero when the current
	 * player has no food in their inventory
	 */
	@Test
	public void testGeeseReturnHalfFoodZero(){
		RoboticonQuest game = new RoboticonQuest();
		Player player = new Player(game); // Ensures at least one player in the game
		player = game.getPlayer();
		player.setFood(0);
		assertEquals(0,RandomEvents.geeseStealResources(player, ResourceType.FOOD));
	}
	
	/**
	 * Tests {@link RandomEvents#geeseStealResources(Player, ResourceType)} with ore and ensures that no ore is taken out of the players inventory
	 * when the player has no ore in their inventory
	 */
	@Test
	public void testGeeseNoOreTakenZero(){
		RoboticonQuest game = new RoboticonQuest();
		Player player = new Player(game); // Ensures at least one player in the game
		player = game.getPlayer();
		player.setOre(0);
		RandomEvents.geeseStealResources(player,ResourceType.ORE);
		assertEquals(0,player.getOre());
	}
	
	/**
	 * Tests {@link RandomEvents#geeseStealResources(Player, ResourceType)} with energy and ensures that it returns 0 when the current
	 * player has 1 energy in their inventory
	 */
	@Test
	public void testGeeseReturnHalfFoodOne(){
		RoboticonQuest game = new RoboticonQuest();
		Player player = new Player(game); // Ensures at least one player in the game
		player = game.getPlayer();
		player.setEnergy(1);
		assertEquals(0,RandomEvents.geeseStealResources(player, ResourceType.ENERGY));
	}
	
	/**
	 * Tests {@link RandomEvents#geeseStealResources(Player, ResourceType)} with food and ensures that no food is taken out of the players inventory
	 * when the player has 1 food in their inventory
	 */
	@Test
	public void testGeeseNoFoodTakenOne(){
		RoboticonQuest game = new RoboticonQuest();
		Player player = new Player(game); // Ensures at least one player in the game
		player = game.getPlayer();
		player.setFood(1);
		RandomEvents.geeseStealResources(player, ResourceType.FOOD);
		assertEquals(1,player.getFood());
	}
	
	/**
	 * Tests {@link RandomEvents#geeseStealResources(Player, ResourceType)} with food and ensures that it returns 2 when the current
	 * player has 4 food in their inventory
	 */
	@Test
	public void testGeeseReturnHalfFoodFour(){
		RoboticonQuest game = new RoboticonQuest();
		Player player = new Player(game); // Ensures at least one player in the game
		player = game.getPlayer();
		player.setFood(4);
		assertEquals(2,RandomEvents.geeseStealResources(player,ResourceType.FOOD));
	}
	
	/**
	 * Tests {@link RandomEvents#geeseStealResources(Player, ResourceType)} with food and ensures that 2 food are taken out of the players inventory
	 * when the player has 4 food in their inventory
	 */
	@Test
	public void testGeeseTwoFoodTakenFour(){
		RoboticonQuest game = new RoboticonQuest();
		Player player = new Player(game); // Ensures at least one player in the game
		player = game.getPlayer();
		player.setFood(4);
		RandomEvents.geeseStealResources(player,ResourceType.FOOD);
		assertEquals(2,player.getFood());
	}
	
	/**
	 * Tests {@link RandomEvents#geeseStealResources(Player, ResourceType)} with food and ensures that it returns 2 when the current
	 * player has 5 food in their inventory
	 */
	@Test
	public void testGeeseReturnHalfFoodFive(){
		RoboticonQuest game = new RoboticonQuest();
		Player player = new Player(game); // Ensures at least one player in the game
		player = game.getPlayer();
		player.setFood(4);
		assertEquals(2,RandomEvents.geeseStealResources(player,ResourceType.FOOD));
	}
	
	/**
	 * Tests {@link RandomEvents#geeseStealResources(Player, ResourceType)} with ore and ensures that 2 food are taken out of the players inventory
	 * when the player has 5 food in their inventory
	 */
	@Test
	public void testGeeseTwoFoodTakenFive(){
		RoboticonQuest game = new RoboticonQuest();
		Player player = new Player(game); // Ensures at least one player in the game
		player = game.getPlayer();
		player.setOre(5);
		RandomEvents.geeseStealResources(player,ResourceType.ORE);
		assertEquals(3,player.getOre());
	}
	
	/**
	 * Tests {@link RandomEvents#amountOfMoneyInTreasureChest(Player)} 
	 * and ensures that when it is run Integer.MAX_VALUE times it returns at least 2 different values
	 */
	@Test
	public void testAmountOfMoneyInTreasureChestReturnDifferentValues(){
		RoboticonQuest game = new RoboticonQuest();
		Player player = new Player(game); // Ensures at least one player in the game

		int previousValueReturned = RandomEvents.amountOfMoneyInTreasureChest(player);
		for(int i=0; i<Integer.MAX_VALUE; i++){
			if(! (previousValueReturned == RandomEvents.amountOfMoneyInTreasureChest(player))){
				return; // If at least two different values have been returned
			}
		}
		fail();
	}
	
	/**
	 * Tests {@link RandomEvents#amountOfMoneyInTreasureChest(Player)} 
	 * and ensures that the returned value is added to the players inventory
	 */
	@Test
	public void testAmountOfMoneyInTreasureChestAddedtoPlayer(){
		RoboticonQuest game = new RoboticonQuest();
		Player player = new Player(game); // Ensures at least one player in the game
		player = game.getPlayer();
		int moneyBefore = player.getMoney();
		
		int amountThatShouldBeAdded = RandomEvents.amountOfMoneyInTreasureChest(player);
		
		assertEquals(moneyBefore+amountThatShouldBeAdded,player.getMoney());
	}
}
