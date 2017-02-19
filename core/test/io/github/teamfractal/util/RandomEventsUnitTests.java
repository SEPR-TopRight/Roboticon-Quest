package io.github.teamfractal.util;

import org.junit.*;

import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.entity.Player;
import io.github.teamfractal.entity.enums.ResourceType;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;

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
 * Test case for {@link RandomEvents}
 */
public class RandomEventsUnitTests {
	@Mocked private Player player;
	@Mocked private RoboticonQuest game;
	
	/**
	 * Runs before every test and sets up the required mocked objects
	 */
	@Before
	public void setup(){
		game = new RoboticonQuest();
		player = new Player(game);
	}
	
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
	public void testGeeseAttack(){
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
	 * Tests {@link RandomEvents#geeseStealResources(Player,ResourceType)} with food and ensures that it returns zero when the current
	 * player has no food in their inventory
	 */
	@Test
	public void testGeeseReturnHalfFoodZero(){
		new Expectations(){{
			player.getResource(ResourceType.FOOD);result=0;
			player.getFood();result=0;
			player.setFood(0);
		}};
		assertEquals(0,RandomEvents.geeseStealResources(player,ResourceType.FOOD));
	}
	
	
	/**
	 * Tests {@link RandomEvents#geeseStealResources(Player,ResourceType)} with energy and ensures that it returns 0 when the current
	 * player has 1 energy in their inventory
	 */
	@Test
	public void testGeeseReturnHalfEnergyOne(){
		new Expectations(){{
			player.getResource(ResourceType.ENERGY);result=1;
			player.getEnergy();result=1;
			player.setEnergy(1);
		}};
		assertEquals(0,RandomEvents.geeseStealResources(player,ResourceType.ENERGY));
	}

	
	/**
	 * Tests {@link RandomEvents#geeseStealResources(Player,ResourceType)} with ore and ensures that it returns 2 when the current
	 * player has 4 ore in their inventory
	 */
	@Test
	public void testGeeseReturnHalfOreFour(){
		new Expectations(){{
			player.getResource(ResourceType.ORE);result=4;
			player.getOre();result=4;
			player.setOre(2);
		}};
	
		assertEquals(2,RandomEvents.geeseStealResources(player, ResourceType.ORE));
	}
	
	/**
	 * Tests {@link RandomEvents#geeseStealResources(Player,ResourceType)} with food and ensures that it returns 2 when the current
	 * player has 5 food in their inventory
	 */
	@Test
	public void testGeeseReturnHalfFoodFive(){
		new Expectations(){{
			player.getResource(ResourceType.FOOD);result=5;
			player.getFood();result=5;
			player.setFood(3);
		}};
	
		assertEquals(2,RandomEvents.geeseStealResources(player,ResourceType.FOOD));
	}
	
	
	/**
	 * Tests {@link RandomEvents#amountOfMoneyInTreasureChest(Player)} 
	 * and ensures that when it is run Integer.MAX_VALUE times it returns at least 2 different values
	 */
	@Test
	public void testAmountOfMoneyInTreasureChestReturnDifferentValues(){
		int previousValueReturned = RandomEvents.amountOfMoneyInTreasureChest(player);
		for(int i=0; i<Integer.MAX_VALUE; i++){
			if(! (previousValueReturned == RandomEvents.amountOfMoneyInTreasureChest(player))){
				return; // If at least two different values have been returned
			}
		}
		fail();
	}
	
	/**
	 * Tests {@link RandomEvents#getResourceStolenByGeese(Player)} 
	 * and ensures that when it is run Integer.MAX_VALUE times it returns food, energy and ore at least once
	 */
	@Test
	public void testGetResourceStolenByGeese(){
		boolean returnedOre = false;
		boolean returnedFood = false;
		boolean returnedEnergy = false;
		for(int i=0; i<Integer.MAX_VALUE; i++){
			ResourceType result = RandomEvents.getResourceStolenByGeese(player);
			if(result == ResourceType.ENERGY){
				returnedEnergy = true;
			}
			else if(result == ResourceType.ORE){
				returnedOre = true;
			}
			else if(result == ResourceType.FOOD){
				returnedFood = true;
			}
			if(returnedOre && returnedFood && returnedEnergy){
				return; // If at least two different values have been returned
			}
		}
		fail();
	}
}
