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

// Josh Neil changed the name of this class from MarketTest to MarketUnitTest as it contains only unit tests
/**
 * Test case for {@link Market}
 */
public class MarketUnitTest {
	@Rule
	public final ExpectedException exception = ExpectedException.none();

	private Market market;
	@Mocked private Player player;
	@Mocked private RoboticonQuest game;

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
	 * The market should start with correct amount of resources.
	 * 16 Food and Energy, 0 Ore, 12 Robotics
	 */
	@Test
	public void marketShouldInitWithCorrectValues() {
		assertEquals(16, market.getFood());
		assertEquals(16, market.getEnergy());
		assertEquals(0, market.getOre());
		assertEquals(12, market.getRoboticon());
	}

	/**
	 * test setEnergy(), setOre(), setFood(), setRoboticon()
	 * The market should be able to set and get resources.
	 */
	@Test
	public void marketShouldAbleToGetAndSetResources() {
		Random rnd = new Random();
		int valueToTest = rnd.nextInt(100);
		market.setEnergy(valueToTest);
		market.setOre(valueToTest);
		market.setFood(valueToTest);
		market.setRoboticon(valueToTest);


		assertEquals(valueToTest, market.getEnergy());
		assertEquals(valueToTest, market.getOre());
		assertEquals(valueToTest, market.getFood());
		assertEquals(valueToTest, market.getRoboticon());
	}

	/**
	 * test: getBuyPrice()
	 * The market should start with correct price for player to buy.
	 * The price is 90% of the sell price.
	 * This could change in later development.
	 */
	@Test
	public void marketShouldHaveCorrectPricesForResources() throws Exception {
		assertEquals(9, market.getBuyPrice(ResourceType.ORE));
		assertEquals(18, market.getBuyPrice(ResourceType.ENERGY));
		assertEquals(27, market.getBuyPrice(ResourceType.FOOD));
		assertEquals(36, market.getBuyPrice(ResourceType.ROBOTICON));
	}


	/**
	 * test: hasEnoughResources
	 * player class can use this method to find out that the amount of resource
	 * player want to buy is available in the market, if the amount of resource
	 * in the market is less than the amount of resources player want to buy then
	 * throw exception
	 */

	@Test
	public void marketCanCheckResourceMoreThanAmountYouWantToBuy() {
		assertFalse(market.hasEnoughResources(ResourceType.ORE, 1000000));
		assertFalse(market.hasEnoughResources(ResourceType.ENERGY, 1000000));
		assertFalse(market.hasEnoughResources(ResourceType.ROBOTICON, 1000000));
		assertFalse(market.hasEnoughResources(ResourceType.FOOD, 1000000));
	}


	/**
	 * test: getSellPrice()
	 */

	@Test
	public void marketShouldReturnCorrectSellPrice(){
		int valueToTest1 = 20;
		market.setEnergy(valueToTest1);
		market.setOre(valueToTest1);
		market.setFood(valueToTest1);
		market.setRoboticon(valueToTest1);

		assertEquals(30,market.getSellPrice(ResourceType.FOOD));
		assertEquals(10,market.getSellPrice(ResourceType.ORE));
		assertEquals(40,market.getSellPrice(ResourceType.ROBOTICON));
		assertEquals(20,market.getSellPrice(ResourceType.ENERGY));
	}

	/**
	 * Tests {@link Market#sellResource(ResourceType, int)} ensures that resources are actually
	 * removed from the market
	 */
	@Test
	public void marketShouldReduceResourcesWhenSells(){
		market.setEnergy(10);
		market.setOre(10);
		market.setFood(10);
		market.setRoboticon(10);

		market.sellResource(ResourceType.FOOD, 5);
		market.sellResource(ResourceType.ORE, 5);
		market.sellResource(ResourceType.ENERGY, 5);
		market.sellResource(ResourceType.ROBOTICON, 5);

		assertEquals(5, market.getFood() );
		assertEquals(5, market.getOre() );
		assertEquals(5, market.getEnergy() );
		assertEquals(5, market.getRoboticon() );

	}
	
	// Tests below this comment added by Josh Neil
	
	/**
	 * Tests {@link Market#playerGamble(Player, int)} ensures that it returns GamblingResult.NOTENOUGHMONEY when
	 * a player has no money (and the cost of the bet is at least 1)
	 */
	@Test
	public void testPlayerGambleNoMoney(){
		new Expectations(){{
			player.getMoney();result=0;
		}};
		assertEquals(GamblingResult.NOTENOUGHMONEY,market.playerGamble(player, 10));
	}
	
	/**
	 * Tests {@link Market#playerGamble(Player, int)} ensures that it returns GamblingResult.NOTENOUGHMONEY when
	 * a player has 1 less money than the bet
	 */
	@Test
	public void testPlayerGambleOneLessMoneyThanBet(){
		new Expectations(){{
			player.getMoney();result=5;
		}};
		assertEquals(GamblingResult.NOTENOUGHMONEY,market.playerGamble(player, 6));
	}
	
	/**
	 * Tests {@link Market#playerGamble(Player, int)} ensures that it does not return GamblingResult.NOTENOUGHMONEY when
	 * a player has exactly the same amount of money as the bet
	 */
	@Test
	public void testPlayerGambleMoneySameAsBet(){
		new Expectations(){{
			player.getMoney();result=5;
			player.getMoney();result=5;
			player.setMoney(anyInt);
		}};
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

	/**
	 * Tests {@link Market#setOre(int)} ensures that an exception is thrown when a negative value is passed to it
	 */
	@Test(expected = IllegalArgumentException.class)
	public void setOreNegativeValue(){
		market.setOre(-1);
	}
	
	/**
	 * Tests {@link Market#setEnergy(int)} ensures that an exception is thrown when a negative value is passed to it
	 */
	@Test(expected = IllegalArgumentException.class)
	public void setEnergyNegativeValue(){
		market.setEnergy(-1);
	}
	
	/**
	 * Tests {@link Market#setFood(int)} ensures that an exception is thrown when a negative value is passed to it
	 */
	@Test(expected = IllegalArgumentException.class)
	public void setFoodNegativeValue(){
		market.setFood(-1);
	}
	
	/**
	 * Tests {@link Market#setRoboticon(int)} ensures that an exception is thrown when a negative value is passed to it
	 */
	@Test(expected = IllegalArgumentException.class)
	public void setRoboticonNegativeValue(){
		market.setRoboticon(-1);
	}
	
	/**
	 * Tests {@link Market#attemptToProduceRoboticon()} ensures that it returns true when the market has enough ore
	 */
	@Test
	public void testAttemptToProduceRoboticonReturnTrue(){
		market.setOre(4); // The exact correct amount of ore
		assertTrue(market.attemptToProduceRoboticon());
	}
	
	/**
	 * Tests {@link Market#attemptToProduceRoboticon()} ensures that one roboticon is added to the market when the market has enough ore
	 */
	@Test
	public void testAttemptToProduceRoboticonRoboticonAdded(){
		market.setOre(4); // The exact correct amount of ore
		int roboticonsBefore = market.getRoboticon();
		market.attemptToProduceRoboticon();
		assertEquals(roboticonsBefore+1,market.getRoboticon());
	}
	
	/**
	 * Tests {@link Market#attemptToProduceRoboticon()} ensures that the correct amount of ore is removed from the market
	 */
	@Test
	public void testAttemptToProduceRoboticonOreRemoved(){
		market.setOre(6); // Enough ore
		int oreBefore = market.getOre();
		market.attemptToProduceRoboticon();
		assertEquals(oreBefore-4,market.getOre());
	}
	
	/**
	 * Tests {@link Market#attemptToProduceRoboticon()} ensures that it returns false when the market does have enough ore
	 */
	@Test
	public void testAttemptToProduceRoboticonReturnFalse(){
		market.setOre(3); // Not enough ore
		assertFalse(market.attemptToProduceRoboticon());
	}
	
	/**
	 * Tests {@link Market#attemptToProduceRoboticon()} ensures that no roboticons are added to the market when the market does have enough ore
	 */
	@Test
	public void testAttemptToProduceRoboticonNoRoboticonAdded(){
		market.setOre(3); // Not enough ore
		int roboticonsBefore = market.getRoboticon();
		market.attemptToProduceRoboticon();
		assertEquals(roboticonsBefore,market.getRoboticon());
	}
	
	/**
	 * Tests {@link Market#attemptToProduceRoboticon()} ensures that no ore is removed from the market
	 * (when the market does not have enough ore to produce a roboticon)
	 */
	@Test
	public void testAttemptToProduceRoboticonNoOreRemoved(){
		market.setOre(3); // Not enough ore
		int oreBefore = market.getOre();
		market.attemptToProduceRoboticon();
		assertEquals(oreBefore,market.getOre());
	}
	
	/**
	 * Tests {@link Market#buyResource(ResourceType, int)} 
	 * ensures that three ore are added to the market when called with ResourceType.ORE and 3
	 * as it is simply a wrapper for setResource only a few simple tests are needed
	 */
	@Test
	public void testBuyThreeOre(){
		int oreBefore = market.getResource(ResourceType.ORE);
		
		market.buyResource(ResourceType.ORE, 3);
		
		assertEquals(oreBefore+3,market.getResource(ResourceType.ORE));
	}
	
	/**
	 * Tests {@link Market#buyResource(ResourceType, int)} 
	 * ensures that one ore is added to the market when called with ResourceType.ORE and 1
	 * as it is simply a wrapper for setResource only a few simple tests are needed
	 */
	@Test
	public void testBuyOneOre(){
		int oreBefore = market.getResource(ResourceType.ORE);
		
		market.buyResource(ResourceType.ORE, 1);
		
		assertEquals(oreBefore+1,market.getResource(ResourceType.ORE));
	}
	
	/**
	 * Tests {@link Market#buyResource(ResourceType, int)} 
	 * ensures that five energy are added to the market when called with ResourceType.ENERGY and 5
	 * as it is simply a wrapper for setResource only a few simple tests are needed
	 */
	@Test
	public void testBuyFiveEnergy(){
		int energyBefore = market.getResource(ResourceType.ENERGY);
		
		market.buyResource(ResourceType.ENERGY, 5);
		
		assertEquals(energyBefore+5,market.getResource(ResourceType.ENERGY));
	}
	
	/**
	 * Tests {@link Market#buyResource(ResourceType, int)} 
	 * ensures that one energy is added to the market when called with ResourceType.ENERGY and 1
	 * as it is simply a wrapper for setResource only a few simple tests are needed
	 */
	@Test
	public void testBuyOneEnergy(){
		int energyBefore = market.getResource(ResourceType.ENERGY);
		
		market.buyResource(ResourceType.ENERGY, 1);
		
		assertEquals(energyBefore+1,market.getResource(ResourceType.ENERGY));
	}
	
	/**
	 * Tests {@link Market#buyResource(ResourceType, int)} 
	 * ensures that four food are added to the market when called with ResourceType.FOOD and 4
	 * as it is simply a wrapper for setResource only a few simple tests are needed
	 */
	@Test
	public void testBuyFourFood(){
		int foodBefore = market.getResource(ResourceType.FOOD);
		
		market.buyResource(ResourceType.FOOD, 4);
		
		assertEquals(foodBefore+4,market.getResource(ResourceType.FOOD));
	}
	
	/**
	 * Tests {@link Market#buyResource(ResourceType, int)} 
	 * ensures that one food is added to the market when called with ResourceType.FOOD and 1
	 * as it is simply a wrapper for setResource only a few simple tests are needed
	 */
	@Test
	public void testBuyOneFood(){
		int foodBefore = market.getResource(ResourceType.FOOD);
		
		market.buyResource(ResourceType.FOOD, 1);
		
		assertEquals(foodBefore+1,market.getResource(ResourceType.FOOD));
	}
	
	/**
	 * Tests {@link Market#sellResource(ResourceType, int)} 
	 * ensures that three ore are removed from the market when called with ResourceType.ORE and 3
	 * as it is simply a wrapper for setResource only a few simple tests are needed
	 */
	@Test
	public void testSellThreeOre(){
		
		// Start the market off with enough ore
		market.setResource(ResourceType.ORE, 5);
		int oreBefore = market.getResource(ResourceType.ORE);
		
		market.sellResource(ResourceType.ORE, 3);
		
		assertEquals(oreBefore-3,market.getResource(ResourceType.ORE));
	}
	
	/**
	 * Tests {@link Market#sellResource(ResourceType, int)} 
	 * ensures that one ore is removed from the market when called with ResourceType.ORE and 1
	 * as it is simply a wrapper for setResource only a few simple tests are needed
	 */
	@Test
	public void testSellOneOre(){
		// Start the market off with enough ore
		market.setResource(ResourceType.ORE, 5);
		int oreBefore = market.getResource(ResourceType.ORE);
		
		market.sellResource(ResourceType.ORE, 1);
		
		assertEquals(oreBefore-1,market.getResource(ResourceType.ORE));
	}
	
	/**
	 * Tests {@link Market#sellResource(ResourceType, int)} 
	 * ensures that five energy are removed from the market when called with ResourceType.ENERGY and 5
	 * as it is simply a wrapper for setResource only a few simple tests are needed
	 */
	@Test
	public void testSellFiveEnergy(){
		int energyBefore = market.getResource(ResourceType.ENERGY);
		
		market.sellResource(ResourceType.ENERGY, 5);
		
		assertEquals(energyBefore-5,market.getResource(ResourceType.ENERGY));
	}
	
	/**
	 * Tests {@link Market#sellResource(ResourceType, int)} 
	 * ensures that one energy is removed from the market when called with ResourceType.ENERGY and 1
	 * as it is simply a wrapper for setResource only a few simple tests are needed
	 */
	@Test
	public void testSellOneEnergy(){
		int energyBefore = market.getResource(ResourceType.ENERGY);
		
		market.sellResource(ResourceType.ENERGY, 1);
		
		assertEquals(energyBefore-1,market.getResource(ResourceType.ENERGY));
	}
	
	/**
	 * Tests {@link Market#sellResource(ResourceType, int)} 
	 * ensures that four food are removed from the market when called with ResourceType.FOOD and 4
	 * as it is simply a wrapper for setResource only a few simple tests are needed
	 */
	@Test
	public void testSellFourFood(){
		int foodBefore = market.getResource(ResourceType.FOOD);
		
		market.sellResource(ResourceType.FOOD, 4);
		
		assertEquals(foodBefore-4,market.getResource(ResourceType.FOOD));
	}
	
	/**
	 * Tests {@link Market#sellResource(ResourceType, int)} 
	 * ensures that one food is removed from the market when called with ResourceType.FOOD and 1
	 * as it is simply a wrapper for setResource only a few simple tests are needed
	 */
	@Test
	public void testSellOneFood(){
		int foodBefore = market.getResource(ResourceType.FOOD);
		
		market.sellResource(ResourceType.FOOD, 1);
		
		assertEquals(foodBefore-1,market.getResource(ResourceType.FOOD));
	}
}
