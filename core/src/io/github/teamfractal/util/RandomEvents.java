package io.github.teamfractal.util;

import java.util.Random;

import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.entity.Player;
import io.github.teamfractal.entity.enums.ResourceType;

// Class created by Josh Neil - contains code needed to implement random events
/**
 * Contains most of the logic needed to implement the random events used 
 * throughout the game
 * @author jcn509, cb1423, bp713
 */
public class RandomEvents {
	
	// Methods added to implement the random event where a roboticon is faulty and explodes upon being placed
	/**
	 * Returns a boolean value that states whether or not a given roboticon is
	 * faulty
	 * @return A boolean value that states whether or not a given roboticon is
	 * faulty
	 */
	public static boolean roboticonIsFaulty(){
		return Math.random() >= 0.95; // Five percent chance of a roboticon being faulty
	}
	
	
	// 2 methods bellow added to implement the random event where a player finds a treasure chest with money in it
	/**
	 * Returns a boolean value that states whether or not an acquired tile contains a treasure chest.
	 * @return A boolean value that states whether or not an acquired tile contains a treasure chest.
	 */
	public static boolean tileHasChest(){
		return Math.random() >= 0.8;
	}
	
	
	/**
	 * Returns an integer representing the amount of treasure found in a chest and 
	 * increase the player's balance by this amount.
	 * @return An integer representing the amount of treasure found in a chest.
	 */
	public static int amountOfMoneyInTreasureChest(Player player){
		int treasure = (int)(Math.random()*40)+10;
		int balance = player.getMoney();
		player.setMoney(balance+treasure);
		return treasure;
	}
	
	
	// Below methods added by Ben to implement the random events where geese steal some of a players reources
	/**
	 * Returns a boolean value that states whether or not Geese have attacked a players food.
	 * @return A boolean value that states whether or not Geese have attacked a players food.
	 */
	public static boolean geeseAttack(){
		return Math.random() >= 0.9;
	}
	
	/**
	 * Returns the resource that geese will steal from a given player
	 * @param player 
	 * @return the resource that geese will steal from a given player
	 */
	public static ResourceType getResourceStolenByGeese(Player player){
		// Note this method takes player as an argument in case we decide to do something different
		// like base the likelihood of taking a given resource on the quantity of that resource that
		// a player possesses
		ResourceType resources[] = {ResourceType.ORE,ResourceType.ENERGY,ResourceType.FOOD};
		int randomSelection = new Random().nextInt(resources.length);
	    return resources[randomSelection];
	}
	
	/**
	 * Returns an integer representing the amount of resource lost
	 * also removes this much of the resource from the players inventory
	 * @param player
	 * @param resource The resource that the geese shall steel
	 * @return an integer representing the amount of resource lost
	 */
	public static int geeseStealResources(Player player, ResourceType resource){
		int quantity = player.getResource(resource)/2;
		if(resource == ResourceType.FOOD){
			player.setFood(player.getFood() - quantity);
		}
		else if(resource == ResourceType.ORE){
			player.setOre(player.getOre() - quantity);
		}
		else if(resource == ResourceType.ENERGY){
			player.setEnergy(player.getEnergy() - quantity);
		}
				
		return quantity;
	}
	
	
}
