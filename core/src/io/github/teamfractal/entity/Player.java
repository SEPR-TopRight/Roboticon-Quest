package io.github.teamfractal.entity;

import com.badlogic.gdx.utils.Array;
import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.animation.AnimationAddResources;
import io.github.teamfractal.animation.IAnimation;
import io.github.teamfractal.animation.IAnimationFinish;
import io.github.teamfractal.entity.enums.PurchaseStatus;
import io.github.teamfractal.entity.enums.ResourceType;
import io.github.teamfractal.exception.NotCommonResourceException;
import io.github.teamfractal.exception.NotEnoughResourceException;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Random;

import static io.github.teamfractal.entity.enums.ResourceType.ENERGY;
import static io.github.teamfractal.entity.enums.ResourceType.FOOD;
import static io.github.teamfractal.entity.enums.ResourceType.ORE;

/**
 * Holds all data relating to a single player and implements the logic required
 * for players to buy and sell to/from the market and other players, aquire land and 
 * make that land produce resources
 */
public class Player {
	//<editor-fold desc="Resource getter and setter">
	private int money = 100;
	private int ore = 0;
	private int energy = 0;
	private int food = 0;
	ArrayList<LandPlot> landList = new ArrayList<LandPlot>();
	Array<Roboticon> roboticonList;
	private RoboticonQuest game;

	public int getMoney() { return money; }
	public int getOre() { return ore; }
	public int getEnergy() { return energy; }
	public int getFood() { return food; }
	
	public Player(RoboticonQuest game){
		this.game = game;
		this.roboticonList = new Array<Roboticon>();

		
	}
	//Made mutator (money, food, ore, energy) methods public so we can use it in RandomEvents - Christian Beddows
	/**
	 * Set the amount of money player has
	 * @param money                      The amount of new money.
	 * @throws IllegalArgumentException  If the new money if negative, this exception will be thrown.
	 */
	public synchronized void setMoney(int money) throws IllegalArgumentException {
		if (money < 0) {
			throw new IllegalArgumentException("Error: Money can't be negative."); //My bank account would disagree
		}

		this.money = money;
	}

	/**
	 * Set the amount of ore player has
	 * @param amount                     The new amount for ore.
	 * @throws IllegalArgumentException  If the new ore amount if negative, this exception will be thrown.
	 */
	public synchronized void setOre(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("Error: Ore can't be negative.");
		}

		this.ore = amount;
	}

	/**
	 * Set the amount of energy player has
	 * @param amount                     The new amount for energy.
	 * @throws IllegalArgumentException  If the new energy amount if negative, this exception will be thrown.
	 */

	public synchronized void setEnergy(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("Error: Energy can't be negative.");
		}

		this.energy = amount;
	}

	/**
	 * Set the amount of food player has
	 * @param amount                     The new amount for food.
	 * @throws IllegalArgumentException  If the new food amount if negative, this exception will be thrown.
	 */

	public synchronized void setFood(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("Error: Food can't be negative.");
		}

		this.food = amount;
	}

	/**
	 * Set the resource amount current player have.
	 * @param resource  The {@link ResourceType}
	 * @param amount    The new amount.
	 */
	void setResource(ResourceType resource, int amount) {
		switch (resource) {
			case ENERGY:
				setEnergy(amount);
				break;

			case ORE:
				setOre(amount);
				break;

			case FOOD:
				setFood(amount);
				break;

			default:
				throw new NotCommonResourceException(resource);
		}
	}

	/**
	 * Get the resource amount current player have.
	 * @param type   The {@link ResourceType}
	 * @return       The amount of specified resource.
	 */
	public int getResource(ResourceType type) {
		switch (type) {
			case ENERGY:
				return getEnergy();

			case ORE:
				return getOre();

			case FOOD:
				return getFood();


			default:
				throw new NotCommonResourceException(type);
		}
	}

	/**
	 * Purchase roboticon from the market.
	 * @param amount
	 * @param market
	 * @return
	 */
	public PurchaseStatus purchaseRoboticonsFromMarket(int amount, Market market) {
		Random random = new Random();
		
		
		if (!market.hasEnoughResources(ResourceType.ROBOTICON, amount)) {
			return PurchaseStatus.FailMarketNotEnoughResource;
		}

		int cost = amount * market.getSellPrice(ResourceType.ROBOTICON);
		int money = getMoney();
		if (cost > money) {
			return PurchaseStatus.FailPlayerNotEnoughMoney;
		}
		
		market.sellResource(ResourceType.ROBOTICON, amount);
		setMoney(money - cost);
		for (int roboticon = 0; roboticon < amount; roboticon++) {
			roboticonList.add(new Roboticon(random.nextInt(10000)));
		}
		
		return PurchaseStatus.Success;
	}

	/**
	 * Purchase roboticon customisation from the market.
	 * @param resource    The resource type.
	 * @param roboticon   The roboticon to be customised.
	 * @param market      The market.
	 * @return            Purchase status.
	 */
	public PurchaseStatus purchaseCustomisationFromMarket(ResourceType resource,
	                                                      Roboticon roboticon, Market market) {
		
		if (!market.hasEnoughResources(ResourceType.CUSTOMISATION, 1)) {
			return PurchaseStatus.FailMarketNotEnoughResource;
		}

		int cost = 1 * market.getSellPrice(ResourceType.CUSTOMISATION);
		int money = getMoney();
		if (cost > money) {
			return PurchaseStatus.FailPlayerNotEnoughMoney;
		}
		
		market.sellResource(ResourceType.CUSTOMISATION, 1);
		setMoney(money - cost);
		customiseRoboticon(roboticon, resource);
		
		return PurchaseStatus.Success;
	}
	
	//</editor-fold>

	/**
	 * Action for player to purchase resources from the market.
	 *
	 * @param amount     Amount of resources to purchase.
	 * @param market     The market instance.
	 * @param resource   The resource type.
	 * @return           If the purchase was success or not.
	 */
	public PurchaseStatus purchaseResourceFromMarket(int amount, Market market, ResourceType resource) {
		if (!market.hasEnoughResources(resource, amount)) {
			return PurchaseStatus.FailMarketNotEnoughResource;
		}

		int cost = amount * market.getSellPrice(resource);
		int money = getMoney();
		if (cost > money) {
			return PurchaseStatus.FailPlayerNotEnoughMoney;
		}

		market.sellResource(resource, amount);
		setMoney(money - cost);
		setResource(resource, getResource(resource) + amount);
		return PurchaseStatus.Success;
	}
	
	
	// Added by Josh Neil so that players can sell to other players
	/**
	 * Allows players to sell a given quantity of a given resource to another player for a given price
	 * @param buyingPlayer
	 * @param quantity
	 * @param resource
	 * @param pricePerUnit
	 * @return
	 */
	public PurchaseStatus sellResourceToPlayer(Player buyingPlayer,int quantity, ResourceType resource, int pricePerUnit) {
		int totalCost = quantity * pricePerUnit;
		if(getResource(resource) < quantity){
			return PurchaseStatus.FailPlayerNotEnoughResource;
		}
		else if(buyingPlayer.getMoney() < totalCost){
			return PurchaseStatus.FailPlayerNotEnoughMoney;
		}

		setMoney(totalCost + getMoney());
		setResource(resource, getResource(resource) - quantity);
		buyingPlayer.setMoney(buyingPlayer.getMoney() - totalCost);
		buyingPlayer.setResource(resource, buyingPlayer.getResource(resource) + quantity);
		return PurchaseStatus.Success;
	}

	/**
	 * Action for player to sell resources to the market.
	 *
	 * @param amount    Amount of resources to sell.
	 * @param market    The market instance.
	 * @param resource  The resource type.
	 */
	public void sellResourceToMarket(int amount, Market market, ResourceType resource) {
		int resourcePrice = market.getBuyPrice(resource);

		if (getResource(resource) >= amount) {
			market.buyResource(resource, amount);
			setResource(resource, getResource(resource) - amount);
			setMoney(getMoney() + amount * resourcePrice);
		} else {
			throw new NotEnoughResourceException("Player.sellResourceToMarket", resource, amount, getResource(resource));
		}
	}

	/**
	 * Check if the player have enough money for the {@link LandPlot}.
	 * @param plot           The landplot to purchase
	 * @return  true if the player have enough money for that plot.
	 */
	public synchronized boolean haveEnoughMoney(LandPlot plot) {
		return getMoney() >= 10;
	}

	/**
	 * Player add a landplot to their inventory for gold
	 * @param plot           The landplot to purchase
	 */
	public synchronized boolean purchaseLandPlot(LandPlot plot){
		if (plot.hasOwner() || !haveEnoughMoney(plot)) {
			return false;
		}

		landList.add(plot);
		this.setMoney(this.getMoney() - 10);
		plot.setOwner(this);
		game.landPurchasedThisTurn();
		return true;
	}
	
	// Marked as deprecated by Josh Neil as it is no longer in use (generate resources is used instead)
	@Deprecated
	/**
	 * Get a landplot to produce resources
	 */
	public void produceResources(){
		for (LandPlot plot : landList) {
			energy += plot.produceResource(ENERGY);
			ore += plot.produceResource(ResourceType.ORE);
			food += plot.produceResource(ResourceType.FOOD); // Added by Josh Neil - now plots can produce food
		}
	}
	/**
	 * Apply roboticon customisation
	 * @param roboticon  The roboticon to be customised
	 * @param type       The roboticon customisation type.
	 * @return           The roboticon
	 */
	public Roboticon customiseRoboticon(Roboticon roboticon, ResourceType type) {
		roboticon.setCustomisation(type);
		return roboticon;
	}

	/**
	 * Add landplot to current user.
	 *
	 * @param landPlot  LandPlot to be bind to the user.
	 *                  <code>LandPlot.setOwner(this_user)</code> first.
	 */
	public void addLandPlot(LandPlot landPlot) {
		if (landPlot != null && !landList.contains(landPlot) && landPlot.getOwner() == this) {
			landList.add(landPlot);
		}
	}

	/**
	 * Remove the LandPlot from the user.
	 *
	 * @param landPlot  LandPlot to be removed from the user.
	 *                  <code>this_user</code> must be the current owner first.
	 */
	public void removeLandPlot(LandPlot landPlot) {
		if (landPlot != null && landList.contains(landPlot) && landPlot.getOwner() == this) {
			landList.remove(landPlot);
		}
	}

	// No longer used but left here in case it is needed for future modifications/extensions!
	/**
	 * Get a string list of roboticons available for the player.
	 * Mainly for the dropdown selection.
	 *
	 * @return  The string list of roboticons.
	 */
	public Array<String> getRoboticonAmountList() {
		int ore = 0;
		int energy = 0;
		//added by andrew - used to keep track of the number of food roboticons available to the player
		int food = 0;
		int uncustomised = 0;
		Array<String> roboticonAmountList = new Array<String>();

		for (Roboticon r : roboticonList) {
			if (!r.isInstalled()) {
				switch (r.getCustomisation()) {
					case ORE:
						ore += 1;
						break;
					case ENERGY:
						energy += 1;
						break;
					//Added by andrew - added to increment the number of food roboticons
					case FOOD:
						food += 1;
						break;
					default:
						uncustomised += 1;
						break;
				}
			}
		}

		roboticonAmountList.add("Ore Specific x "    + ore);
		roboticonAmountList.add("Energy Specific x " + energy);
		//added by andrew - used to add the number of food roboticons to the drop down roboticon selection menu
		roboticonAmountList.add("Food Specific x " + food);
		roboticonAmountList.add("Uncustomised x "    + uncustomised);
		return roboticonAmountList;
	}
	
	///////// Added method - Josh Neil - don't want to place uncustomised roboticons
	/**
	 * Get a string list of customised roboticons available for the player.
	 * Mainly for the dropdown selection.
	 *
	 * @return  The string list of roboticons.
	 */
	public Array<String> getCustomisedRoboticonAmountList() {
		int ore = 0;
		int energy = 0;
		//added by andrew - used to keep track the number of food roboticons
		int food = 0;
		Array<String> roboticonAmountList = new Array<String>();

		for (Roboticon r : roboticonList) {
			if (!r.isInstalled()) {
				switch (r.getCustomisation()) {
					case ORE:
						ore += 1;
						break;
					case ENERGY:
						energy += 1;
						break;
					//added by andrew - used to increment the number of food roboticons
					case FOOD:
						food += 1;
						break;
					default:
						break;
				}
			}
		}

		roboticonAmountList.add("Ore Specific x "    + ore);
		roboticonAmountList.add("Energy Specific x " + energy);
		//added by andrew - used to display the number of food roboticons in the roboticon drop down menu
		roboticonAmountList.add("Food Specific x " + food);
		return roboticonAmountList;
	}
	public Array<Roboticon> getRoboticons(){
		return this.roboticonList;
	}

	// Modified by Josh Neil to return a HashMap that maps ResourceType onto the number
	// of that type of resource that was produced. This is so that RoboticonQuest can use this
	// information to produce an animation that displays the resources generated. This
	// method use to produce that animation but that caused problems with testing (automated
	// testing of LibGDX dependent classes is tricky).
	/**
	 * Generate resources produced from each LandPlot
	 * @return A HashMap that maps ResourceType onto the quantity of that resource that was generated
	 */
	public HashMap generateResources() {
		int energy = 0;
		int food = 0;
		int ore = 0;

		for (LandPlot land : landList) {
			energy += land.produceResource(ENERGY);
			ore += land.produceResource(ResourceType.ORE);
			food += land.produceResource(ResourceType.FOOD);
		}

		setEnergy(getEnergy() + energy);
		setFood(getFood() + food);
		setOre(getOre() + ore);

		// Added by Josh Neil
		HashMap<ResourceType, Integer> returnValues = new HashMap<ResourceType, Integer>();
		returnValues.put(ResourceType.FOOD, food);
		returnValues.put(ResourceType.ENERGY, energy);
		returnValues.put(ResourceType.ORE, ore);
		return returnValues;
	}
	
	/////// Added by Josh Neil to support the random event where a roboticon is faulty and breaks - Josh Neil
	/**
	 * Removes a given roboticon from the player's inventory
	 * @param roboticon The roboticon to be removed from the players inventory
	 */
	public void removeRoboticon(Roboticon roboticon){
		roboticonList.removeValue(roboticon, true);
	}
	
	
	////// Added by Josh Neil to support the game over screen where each player needs a final score
	////// Modified by Christian Beddows to account for the resources and roboticons that the player owns
	/**
	 * Returns the player's score
	 * @return The player's score
	 */
	public int getScore(){
		int score = getMoney() + (getResource(ENERGY)*game.market.getSellPrice(ENERGY)) + 
				(getResource(ORE)*game.market.getSellPrice(ORE)) + 
				(getResource(FOOD)*game.market.getSellPrice(FOOD));
		return score;
	}
}
