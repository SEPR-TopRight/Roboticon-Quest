package io.github.teamfractal.entity;

import io.github.teamfractal.entity.enums.ResourceType;
import io.github.teamfractal.exception.InvalidResourceTypeException;
import io.github.teamfractal.exception.NotCommonResourceException;

/**
 * Players can use the market to buy and sell resources, roboticons and roboticon customisation
 *
 */
public class Market {
	/**
	 * Initialise the market
	 */
	public Market() {
		setFood(16);
		setEnergy(16);
		setOre(0);
		setRoboticon(12);
		
		// Added by Josh Neil so that we can implement the feature that 
		// allows players to make the market convert some of its ore to roboticons
		roboticonOreConversionRate = 4;
	}

	//<editor-fold desc="Resource getters and setters">
	private int food;
	private int energy;
	private int ore;
	private int roboticon;
	
	// Added by Josh Neil so that we can implement the feature that 
	// allows players to make the market convert some of its ore to roboticons
	private int roboticonOreConversionRate; 

	/**
	 * Get the amount of food in the market
	 * @return The amount of food in the market.
	 */
	int getFood() {
		return food;
	}

	/**
	 * Get the amount of energy in the market
	 * @return The amount of energy in the market.
	 */
	int getEnergy() {
		return energy;
	}

	/**
	 * Get the amount of ore in the market
	 * @return The amount of ore in the market.
	 */
	int getOre() {
		return ore;
	}

	/**
	 * Get the amount of roboticon in the market
	 * @return The amount of roboticon in the market.
	 */
	int getRoboticon() {
		return roboticon;
	}

	/**
	 * Get the total amount of all available resources added together.
	 * @return   The total amount.
	 */
	private synchronized int getTotalResourceCount() {
		return food + energy + ore + roboticon;
	}

	/**
	 * Set the amount of ore in the market
	 * @param amount                     The amount of new ore count.
	 * @throws IllegalArgumentException  If the new amount if negative, this exception will be thrown.
	 */
	synchronized void setOre(int amount) throws IllegalArgumentException {
		if (amount < 0) {
			throw new IllegalArgumentException("Error: Ore can't be negative.");
		}

		this.ore = amount;
	}

	/**
	 * Set the amount of energy in the market
	 * @param amount                     The amount of new energy count.
	 * @throws IllegalArgumentException  If the new amount if negative, this exception will be thrown.
	 */
	synchronized void setEnergy(int amount) throws IllegalArgumentException {
		if (amount < 0) {
			throw new IllegalArgumentException("Error: Energy can't be negative.");
		}

		this.energy = amount;
	}

	/**
	 * Set the amount of food in the market
	 * @param amount                     The amount of new food amount.
	 * @throws IllegalArgumentException  If the new amount if negative, this exception will be thrown.
	 */
	synchronized void setFood(int amount) throws IllegalArgumentException {
		if (amount < 0) {
			throw new IllegalArgumentException("Error: Food can't be negative.");
		}

		this.food = amount;
	}

	/**
	 * Set the amount of roboticon in the market
	 * @param amount                     The amount of new roboticon count.
	 * @throws IllegalArgumentException  If the new amount if negative, this exception will be thrown.
	 */
	void setRoboticon(int amount) throws IllegalArgumentException {
		if (amount < 0) {
			throw new IllegalArgumentException("Error: Roboticon can't be negative.");
		}

		roboticon = amount;
	}
	//</editor-fold>

	/**
	 * Get the amount of specific resource.
	 * @param type   The {@link ResourceType}.
	 * @return       The amount.
	 */
	public int getResource(ResourceType type) {
		switch (type) {
			case ORE:
				return getOre();

			case ENERGY:
				return getEnergy();

			case ROBOTICON:
				return getRoboticon();

			case FOOD:
				return getFood();
				
			case CUSTOMISATION:
				return 1000;

			default:
				throw new NotCommonResourceException(type);
		}
	}



	/**
	 * Set the amount of specific resource.
	 * @param type   The {@link ResourceType}.
	 * @param amount The new resource amount.
	 * @throws IllegalArgumentException      Will be thrown if the new amount is negative.
	 * @throws InvalidResourceTypeException  Will be thrown if the resource specified is invalid.
	 */
	void setResource(ResourceType type, int amount)
			throws IllegalArgumentException, InvalidResourceTypeException {

		switch (type) {
			case ORE:
				setOre(amount);
				break;

			case ENERGY:
				setEnergy(amount);
				break;

			case ROBOTICON:
				setRoboticon(amount);
				break;

			case FOOD:
				setFood(amount);;
				break;
				
			case CUSTOMISATION:
				break;

			default:
				throw new NotCommonResourceException(type);
		}

	}

	/**
	 * Method to ensure the market have enough resources for user to purchase.
	 * @param type    The {@link ResourceType}.
	 * @param amount  the amount of resource to check.
	 * @return        If there are enough resources.
	 * @throws InvalidResourceTypeException  Will be thrown if the resource specified is invalid.
	 */
	boolean hasEnoughResources(ResourceType type, int amount)
			throws InvalidResourceTypeException {
		int resource = getResource(type);
		return amount <= resource;
	}

	/**
	 * Get the price that the market will pay a player for a single unit of a given resource.
	 * @param resource   The {@link ResourceType}.
	 * @return           The buy in price.
	 */
	public int getBuyPrice(ResourceType resource) {
		return (int)(getSellPrice(resource) * 0.9f);
	}

	/**
	 * Get the price that a player must pay the market for a single unit of a given resource.
	 * @param resource   The {@link ResourceType}.
	 * @return           The sell price.
	 */
	public int getSellPrice(ResourceType resource) {
		int price;
		switch (resource) {
			case ORE:
				price = 10;
				return price;

			case ENERGY:
				price = 20;
				return price;

			case FOOD:
				price = 30;
				return price;

			case ROBOTICON:
				price = 40;
				return price;

			case CUSTOMISATION:
				price = 10;
				return price;

			default:
				throw new IllegalArgumentException("Error: Resource type is incorrect.");
		}
	}

	/**
	 * Buy Resource from the market, caller <i>must</i> be doing all the checks.
	 * For example, take money away from the player.
	 *
	 * This method will only increase the amount of specified resource.
	 *
	 * @param resource    The {@link ResourceType}
	 * @param amount      The amount of resource to buy in.
	 */
	public synchronized void buyResource(ResourceType resource, int amount){
		setResource(resource, getResource(resource) + amount);
	}

	/**
	 * Sell Resource from the market, caller <i>must</i> be doing all the checks.
	 * For example, add money in to the player.
	 *
	 * This method will only decrease the amount of specified resource.
	 *
	 * @param resource    The {@link ResourceType}
	 * @param amount      The amount of resource to sell out.
	 */
	public synchronized void sellResource(ResourceType resource, int amount) {
		setResource(resource, getResource(resource) - amount);
	}
	
	// Added by Josh Neil so that we can implement the feature that 
    // allows players to make the market convert some of its ore to roboticons	
	/**
	 * If the market has enough ore then that ore will be converted
	 * into a roboticon and this method will return true. Otherwise
	 * nothing will happen and it will return false.
	 * @return
	 */
	public boolean attemptToProduceRoboticon(){
		if(hasEnoughResources(ResourceType.ORE, roboticonOreConversionRate)){
			int roboticonsBefore = getResource(ResourceType.ROBOTICON);
			int oreBefore = getResource(ResourceType.ORE);
			
			setResource(ResourceType.ROBOTICON,roboticonsBefore+1);
			setResource(ResourceType.ORE,oreBefore-roboticonOreConversionRate);
			return true;
		}
		return false;
		
	}
}








