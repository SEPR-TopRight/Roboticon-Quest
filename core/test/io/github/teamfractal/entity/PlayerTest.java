package io.github.teamfractal.entity;

import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.entity.enums.PurchaseStatus;
import io.github.teamfractal.entity.enums.ResourceType;
import io.github.teamfractal.exception.NotEnoughResourceException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Enclosed.class)
public class PlayerTest {
	
	/** 
	 * Josh Neil moved any tests that are non-parametrised and should only be ran once into this class
	 */
	public static class PlayerSingleTests{
		@Rule
		public final ExpectedException exception = ExpectedException.none();
	
		Player player;
	
		/**
		 * Runs before every test and creates the Player object that is udner test as well as the RoboticonQuest object
		 * that is requried by some tests
		 */
		@Before
		public void setUp() {
			RoboticonQuest game = new RoboticonQuest();
			player = new Player(game);
		}
	
		/**
		 * Ensures that the quantity of money initially in the player's inventory is correct
		 */
		@Test
		public void testPlayerInitialMoney() {
			assertEquals(100, player.getMoney());
		}
	
		/**
		 * Tests {@link Player#purchaseResourceFromMarket(int, Market, ResourceType)} and ensures that
		 * the correct amount of money is removed from the players inventory, the correct amount of resources are
		 * added to the player's inventory and the correct amount of resources are removed from the market's inventory
		 */
		@Test
		public void testPlayerBuyResource() {
			Market market = new Market();
			market.setOre(16);
			player.setMoney(1000);
	
	
			int playerMoney = player.getMoney();
			int orePrice = market.getSellPrice(ResourceType.ORE);
			//Purchase 5 ore
			player.purchaseResourceFromMarket(5, market, ResourceType.ORE);
			// Player should now have 5 more ores, and the market have 5 less ores.
			assertEquals(playerMoney - 5 * orePrice, player.getMoney());
			assertEquals(5, player.getOre());
			assertEquals(11, market.getOre());
	
	
			playerMoney = player.getMoney();
			int energyPrice = market.getSellPrice(ResourceType.ENERGY);
			//purchase 10 energy
			player.purchaseResourceFromMarket(10, market, ResourceType.ENERGY);
			assertEquals(playerMoney - 10 * energyPrice, player.getMoney());
			assertEquals(10, player.getEnergy());
			assertEquals(6, market.getEnergy());
			
			// Added by Josh Neil
			playerMoney = player.getMoney();
			int foodPrice = market.getSellPrice(ResourceType.FOOD);
			//purchase 11 food
			player.purchaseResourceFromMarket(11, market, ResourceType.FOOD);
			assertEquals(playerMoney - 11 * foodPrice, player.getMoney());
			assertEquals(11, player.getFood());
			assertEquals(5, market.getFood());
		}
	
		/**
		 * Tests {@link Player#sellResourceToMarket(int, Market, ResourceType)} and ensures that
		 * the correct amount of money is added to the players inventory, the correct amount of resources are
		 * removed from the player's inventory and the correct amount of resources are added to the market's inventory
		 */
		@Test
		public void testPlayerSellResource() throws Exception {
			Market market = new Market();
	
			player.setMoney(1000);
			player.setResource(ResourceType.ORE, 15);
			player.setResource(ResourceType.ENERGY, 15);
			player.setResource(ResourceType.FOOD, 15); // Added by Josh Neil
	
	
			int orePrice = market.getBuyPrice(ResourceType.ORE);
			//sell 5 ore
			player.sellResourceToMarket(5, market, ResourceType.ORE);
			assertEquals(1000 + 5 * orePrice, player.getMoney());
			assertEquals(10, player.getOre());
			assertEquals(5, market.getOre());
	
			int energyPrice = market.getBuyPrice(ResourceType.ENERGY);
			player.setMoney(1000);
			//sell 5 energy
			player.sellResourceToMarket(5, market, ResourceType.ENERGY);
			assertEquals(1000 + 5 * energyPrice, player.getMoney());
			assertEquals(10, player.getEnergy());
			assertEquals(21, market.getEnergy());
			
			// Added by Josh Neil
			int foodPrice = market.getBuyPrice(ResourceType.FOOD);
			player.setMoney(1000);
			//sell 5 food
			player.sellResourceToMarket(5, market, ResourceType.FOOD);
			assertEquals(1000 + 5 * foodPrice, player.getMoney());
			assertEquals(10, player.getFood());
			assertEquals(21, market.getFood());
		}
	
		// This test was modified by Josh Neil
		// it use to test that each call threw an exception which was incorrect
		// as the method does not throw an exception but instead returns PurchaseStatus.FailMarketNotEnoughResource
		/**
		 * Tests {@link Player#purchaseResourceFromMarket(int, Market, ResourceType)} ensures
		 * that players cannot buy more of a given resource than the market possesses 
		 * <p>
		 * Also ensures that the players inventory does not change when they try to do this
		 * </p>
		 * @throws Exception An exception should be thrown when the player tries to buy more
		 * of a given resource than is allowed
		 */
		@Test
		public void testPlayerCannotBuyMoreThanAllowed() throws Exception {
			Market market = new Market();
			// Attempt to purchase more ore than allowed
			// Attempt to purchase more ore than allowed
			
			assertEquals(PurchaseStatus.FailMarketNotEnoughResource,player.purchaseResourceFromMarket(100, market, ResourceType.ORE));
				
			assertEquals(100, player.getMoney());
			assertEquals(0, player.getOre());
			// Attempt to purchase more energy than allowed
					
			assertEquals(PurchaseStatus.FailMarketNotEnoughResource,player.purchaseResourceFromMarket(100, market, ResourceType.ENERGY));
							
			assertEquals(100, player.getMoney());
			assertEquals(0, player.getEnergy());
			// Added by Josh Neil
			assertEquals(PurchaseStatus.FailMarketNotEnoughResource,player.purchaseResourceFromMarket(100, market, ResourceType.FOOD));
						
			assertEquals(100, player.getMoney());
			assertEquals(0, player.getFood());
		}
	
		/**
		 * Tests {@link Player#sellResourceToMarket(int, Market, ResourceType)} ensures
		 * that the player cannot sell more energy than they have in their possession
		 * @throws Exception An exception should be thrown when the player tries to do this
		 */
		@Test
		public void testPlayerCannotSellMoreEnergyThanAllowed() throws Exception {
			Market market = new Market();
	
			player.setEnergy(15);
	
			exception.expect(NotEnoughResourceException.class);
			player.sellResourceToMarket(20, market, ResourceType.ENERGY);
		}
	
		/**
		 * Tests {@link Player#sellResourceToMarket(int, Market, ResourceType)} ensures
		 * that the player cannot sell more ore than they have in their possession
		 * @throws Exception An exception should be thrown when the player tries to do this
		 */
		@Test
		public void testPlayerCannotSellMoreOreThanAllowed() throws Exception {
			Market market = new Market();
	
			player.setOre(15);
	
			exception.expect(NotEnoughResourceException.class);
			player.sellResourceToMarket(20, market, ResourceType.ORE);
		}
		
		// Added by Josh Neil
		/**
		 * Tests {@link Player#sellResourceToMarket(int, Market, ResourceType)} ensures
		 * that the player cannot sell more food than they have in their possession
		 * @throws Exception An exception should be thrown when the player tries to do this
		 */
		@Test
		public void testPlayerCannotSellMoreFoodThanAllowed() throws Exception {
			Market market = new Market();
	
			player.setFood(15);
	
			exception.expect(NotEnoughResourceException.class);
			player.sellResourceToMarket(20, market, ResourceType.FOOD);
		}
	
		/**
		 * Tests {@link Player#customiseRoboticon(Roboticon, ResourceType)} and ensures that players
		 * can customise roboticons
		 */
		@Test
		public void testPlayerCanCustomiseRoboticon() {
			// Setup
			Roboticon roboticon = new Roboticon(1);
			player.customiseRoboticon(roboticon, ResourceType.ORE);
			assertEquals(ResourceType.ORE, roboticon.getCustomisation());
	
			Roboticon roboticon2 = new Roboticon(2);
			player.customiseRoboticon(roboticon2, ResourceType.ENERGY);
			assertEquals(ResourceType.ENERGY, roboticon2.getCustomisation());
		}
		
		/**
		 * Tests {@link Player#customiseRoboticon(Roboticon, ResourceType)} and ensures that players
		 * can customise roboticons that they own
		 */
		@Test
		public void testPlayerCanCustomiseOwnedRoboticons() {
			Roboticon roboticon3 = new Roboticon(3); 
			Roboticon roboticon4 = new Roboticon(4);
			player.roboticonList = new Array<Roboticon>();
			player.roboticonList.add(roboticon3);
			player.roboticonList.add(roboticon4);
			player.customiseRoboticon(player.roboticonList.get(0), ResourceType.ORE);
			player.customiseRoboticon(player.roboticonList.get(1), ResourceType.ENERGY);
			assertEquals(ResourceType.ORE, player.roboticonList.get(0).getCustomisation());
			assertEquals(ResourceType.ENERGY, player.roboticonList.get(1).getCustomisation());
		}
		
		// Tests below this comment were added by Josh Neil
		
		/**
		 * Tests {@link Player#addLandPlot(LandPlot)} ensures that the LandPlot is added to the player's
		 * land list when the given plot is owned by the player and not already in their land list
		 */
		@Test
		public void testPlayerAddLandPlot(){
			LandPlot plot = new LandPlot(0,1,1);
			plot.setOwner(player);
			player.addLandPlot(plot);
			assertTrue(player.landList.contains(plot));
		}
		
		/**
		 * Tests {@link Player#addLandPlot(LandPlot)} ensures that the LandPlot is not added to the player's
		 * land list when the given plot is not owned by any player
		 */
		@Test
		public void testPlayerAddLandPlotUnowned(){
			LandPlot plot = new LandPlot(0,1,1);
			player.addLandPlot(plot);
			assertFalse(player.landList.contains(plot));
		}
		
		/**
		 * Tests {@link Player#addLandPlot(LandPlot)} ensures that the LandPlot is not added to the player's
		 * land list when the given plot is owned by another player
		 */
		@Test
		public void testPlayerAddLandPlotOwnedBySomeoneElse(){
			LandPlot plot = new LandPlot(0,1,1);
			plot.setOwner(new Player(null));
			player.addLandPlot(plot);
			assertFalse(player.landList.contains(plot));
		}
		
		/**
		 * Tests {@link Player#addLandPlot(LandPlot)} ensures that the LandPlot is not added to the player's
		 * land list when the given plot is owned by the player and already in their land list
		 */
		@Test
		public void testPlayerAddLandPlotAlreadyInLandList(){
			LandPlot plot = new LandPlot(0,1,1);
			plot.setOwner(player);
			player.addLandPlot(plot); // LandPlot already in player's land list
			int listSizeBefore = player.landList.size();
			player.addLandPlot(plot);
			
			assertEquals(listSizeBefore,player.landList.size());
		}
		
		/**
		 * Tests {@link Player#removeLandPlot(LandPlot)} ensures that when the given plot is removed when it is 
		 * owned by the player and in their land list
		 */
		@Test
		public void testPlayerRemoveLandPlot(){
			LandPlot plot = new LandPlot(0,1,1);
			plot.setOwner(player);
			player.addLandPlot(plot); // LandPlot in player's land list
			player.removeLandPlot(plot);
			assertFalse(player.landList.contains(plot));
		}
		
		/**
		 * Tests {@link Player#removeLandPlot(LandPlot)} ensures that when the no plots are removed when the given LandPlot is 
		 * not owned by anyone
		 */
		@Test
		public void testPlayerRemoveLandPlotNotOwned(){
			LandPlot plot = new LandPlot(0,1,1);
			plot.setOwner(player);
			player.addLandPlot(plot); // Already have some plot in their list
			int landPlotListSizeBefore = player.landList.size();
			
			LandPlot plot2 = new LandPlot(0,0,0);
			player.removeLandPlot(plot2);
			assertEquals(landPlotListSizeBefore,player.landList.size());
		}
		
		/**
		 * Tests {@link Player#removeLandPlot(LandPlot)} ensures that when the no plots are removed when the given LandPlot is 
		 * owned by another player
		 */
		@Test
		public void testPlayerRemoveLandPlotOwnedbyAnotherPlayer(){
			LandPlot plot = new LandPlot(0,1,1);
			plot.setOwner(player);
			player.addLandPlot(plot); // Already have some plot in their list
			int landPlotListSizeBefore = player.landList.size();
			
			LandPlot plot2 = new LandPlot(0,0,0);
			plot2.setOwner(new Player(null));
			player.removeLandPlot(plot2);
			assertEquals(landPlotListSizeBefore,player.landList.size());
		}
		
		
		/**
		 * Helper method that causes the player to acquire three roboticons with given customisations
		 * and add them to three plots that the player also acquires
		 */
		private void acquireThreePlotsWithRoboticons(){
			Roboticon r1 = new Roboticon(0);
			Roboticon r2 = new Roboticon(1);
			Roboticon r3 = new Roboticon(2);
			
			r1.setCustomisation(ResourceType.ORE);
			r2.setCustomisation(ResourceType.ENERGY);
			r3.setCustomisation(ResourceType.FOOD);
			
			LandPlot l1 = new LandPlot(1, 100, 100);
			LandPlot l2 = new LandPlot(2, 200, 200);
			LandPlot l3 = new LandPlot(3, 300, 5);
			
			l1.installRoboticon(r1);
			l1.setHasRoboticon(true);
			l1.setOwner(player);
			
			l2.installRoboticon(r2);
			l2.setHasRoboticon(true);
			l2.setOwner(player);
			
			l3.installRoboticon(r3);
			l3.setHasRoboticon(true);
			l3.setOwner(player);
			
			player.addLandPlot(l1);
			player.addLandPlot(l2);
			player.addLandPlot(l3);		
		}
		
		
		/**
		 * Tests {@link Player#removeRoboticon(Roboticon)} ensures that the given roboticon
		 * is actually removed from the player's inventory (as this is a simple method only a single test is needed)
		 */
		@Test
		public void testRemoveRoboticonRoboticonRemoved(){
			Roboticon r1 = new Roboticon(0);
			Roboticon r2 = new Roboticon(1);
			Roboticon r3 = new Roboticon(2);
			
			player.roboticonList.add(r1);
			player.roboticonList.add(r2);
			player.roboticonList.add(r3);
			
			player.removeRoboticon(r1);
			
			assertFalse(player.roboticonList.contains(r1, true));
		}
		
		/**
		 * Tests {@link Player#removeRoboticon(Roboticon)} ensures that the size of the roboticon list that stores the players
		 * roboticons is reduced by 1 (as this is a simple method only a single test is needed)
		 */
		@Test
		public void testRemoveRoboticonListSizeReduced(){
			Roboticon r1 = new Roboticon(0);
			Roboticon r2 = new Roboticon(1);
			Roboticon r3 = new Roboticon(2);
			
			player.roboticonList.add(r1);
			player.roboticonList.add(r2);
			player.roboticonList.add(r3);
			
			player.removeRoboticon(r1);
			
			assertEquals(2,player.roboticonList.size);
		}
		
		
		// Tests below were removed by Josh Neil as they are unit tests and unit tests
		// are in the PlayerUnitTest class
		/**
		 * Tests {@link Player#setEnergy(int)} ensures that an exception is thrown if a negative value is used
		 *
		@Test(expected=IllegalArgumentException.class)
		public void testNegativeSetEnergy(){
			player.setEnergy(-1);
		}
		
		/**
		 * Tests {@link Player#setOre(int)} ensures that an exception is thrown if a negative value is used
		 *
		@Test(expected=IllegalArgumentException.class)
		public void testNegativeSetOre(){
			player.setOre(-1);
		}
		
		/**
		 * Tests {@link Player#setFood(int)} ensures that an exception is thrown if a negative value is used
		 *
		@Test(expected=IllegalArgumentException.class)
		public void testNegativeSetFood(){
			player.setFood(-1);
		}
		
		/**
		 * Tests {@link Player#setMoney(int)} ensures that an exception is thrown if a negative value is used
		 *
		@Test(expected=IllegalArgumentException.class)
		public void testNegativeSetMoney(){
			player.setMoney(-1);
		}*/
		
		
	}
	
	
	/// Tests added by Josh Neil
	/**
	 * Tests {@link Player#purchaseRoboticonsFromMarket(int, Market)} using various input values and ensures
	 * that the correct results are returned and the attributes of the various objects involved are altered correctly
	 * @author jcn509
	 *
	 */
	@RunWith(Parameterized.class)
	public static class MarketPurchaseRoboticonParamaterisedTests{
		 Player player;
		 Market market;
		 PurchaseStatus purchaseStatus;
		 int initialMoney;
		 int numberOfRoboticonsToPurchase;
		 int moneyRemoved;
		 int playerRoboticonChange;
		 int marketRoboticonChange;
		 int marketInitialRoboticons;
		
		/**
		 * Runs before each test and sets up the values of the variables needed during that test
		 * @param marketInitialRoboticons The number of roboticons in the market before the test runs
		 * @param purchaseStatus The result that should be returned
		 * @param initialMoney The amount of money that the player has before the test
		 * @param moneyRemoved The amount of money that should be removed from the player's inventory
		 * @param playerRoboticonChange The number of roboticons that should be added to the player's inventory
		 * @param marketRoboticonChange The number of roboticons that should be removed from the market's inventory
		 * @param numberOfRoboticonsToPurchase The number of roboticons that the player should attempt to purchase
		 */
		public MarketPurchaseRoboticonParamaterisedTests(int marketInitialRoboticons,PurchaseStatus purchaseStatus,int initialMoney, int moneyRemoved, int playerRoboticonChange, int marketRoboticonChange, int numberOfRoboticonsToPurchase){
			this.marketInitialRoboticons = marketInitialRoboticons;
			this.purchaseStatus = purchaseStatus;
			this.initialMoney = initialMoney;
			this.moneyRemoved = moneyRemoved;
			this.playerRoboticonChange = playerRoboticonChange;
			this.marketRoboticonChange = marketRoboticonChange;
			this.numberOfRoboticonsToPurchase = numberOfRoboticonsToPurchase;
		}
		
		// Added by Josh Neil
		/**
		 * Defines the values to be used in each test
		 */
		@Parameterized.Parameters
		public static Collection roboticonPurchaseValues(){
			 int roboticonPrice = (new Market()).getSellPrice(ResourceType.ROBOTICON);
			 return Arrays.asList(new Object[][] {
		         {1,PurchaseStatus.Success,roboticonPrice,roboticonPrice,1,1,1},
		         {1,PurchaseStatus.Success,roboticonPrice+1,roboticonPrice,1,1,1},
		         {1,PurchaseStatus.Success,roboticonPrice+1,0,0,0,0},
		         {1,PurchaseStatus.FailPlayerNotEnoughMoney,0,0,0,0,1},
		         {1,PurchaseStatus.FailPlayerNotEnoughMoney,roboticonPrice-1,0,0,0,1},
		         {5,PurchaseStatus.FailPlayerNotEnoughMoney,(2*roboticonPrice)-1,0,0,0,2},
		         {0,PurchaseStatus.FailMarketNotEnoughResource,roboticonPrice,0,0,0,1},
		         {1,PurchaseStatus.FailMarketNotEnoughResource,roboticonPrice,0,0,0,2},
		      });
		}
		
		/**
		 * Runs before every test and creates the necessary objects
		 */
		@Before
		public void setup(){
			player = new Player(null);
			market = new Market();
			market.setRoboticon(marketInitialRoboticons);
			player.setMoney(initialMoney);
		}
		
		/**
		 * Tests {@link Player#purchaseRoboticonsFromMarket(int, Market)} ensures that the correct result is
		 * returned
		 */
		@Test
		public void testPurchaseRoboticon(){
			assertEquals(purchaseStatus,player.purchaseRoboticonsFromMarket(numberOfRoboticonsToPurchase, market));
		}
		
		/**
		 * Tests {@link Player#purchaseRoboticonsFromMarket(int, Market)} ensures that when the player 
		 * purchases roboticons the correct number of roboticons are added to their inventory
		 */
		@Test
		public void testPurchaseRoboticonRoboticonAdded(){
			int roboticonsBefore = player.getRoboticons().size;
			player.purchaseRoboticonsFromMarket(numberOfRoboticonsToPurchase, market);
			assertEquals(roboticonsBefore+playerRoboticonChange,player.getRoboticons().size);
		}
		
		/**
		 * Tests {@link Player#purchaseRoboticonsFromMarket(int, Market)} ensures that when the player 
		 * purchases roboticons the correct amount of money is removed from their inventory
		 */
		@Test
		public void testPurchaseRoboticonMoneyRemoved(){
			int moneyBefore = player.getMoney();
			player.purchaseRoboticonsFromMarket(numberOfRoboticonsToPurchase, market);
			assertEquals(moneyBefore-moneyRemoved,player.getMoney());
		}
		
		/**
		 * Tests {@link Player#purchaseRoboticonsFromMarket(int, Market)} ensures that when the player 
		 * purchases roboticons the correct number of roboticons are removed from the markets inventory
		 */
		@Test
		public void testPurchaseRoboticonRoboticonRemoved(){
			int roboticonsBefore = market.getResource(ResourceType.ROBOTICON);
			player.purchaseRoboticonsFromMarket(numberOfRoboticonsToPurchase, market);
			assertEquals(roboticonsBefore-marketRoboticonChange,market.getResource(ResourceType.ROBOTICON));
		}
	}
	
	/// Tests added by Josh Neil
		/**
		 * Runs parameterised tests on {@link Player#purchaseCustomisationFromMarket(ResourceType, Roboticon, Market)} using
		 * many different values and ensures that the correct changes are made to various attributes of the player
		 * and roboticon and the correct value is returned by the method.
		 * @author jcn509
		 *
		 */
		@RunWith(Parameterized.class)
		public static class MarketPurchaseCustomisationParamaterisedTests{
			 Player player;
			 Market market;
			 PurchaseStatus purchaseStatus;
			 int initialMoney;
			 ResourceType customisation;
			 int expectedMoneyRemoved;
			 private Roboticon roboticon;
			 private ResourceType expectedRoboticonCustomisation;
			
			 /**
			  * Runs before each test and sets up the values of the variables needed by the test
			  * @param purchaseStatus The result returned by the method call
			  * @param initialMoney The amount of money that the player has in their inventory before the test is run
			  * @param customisation The customisation that the player should purchase
			  * @param expectedMoneyRemoved The amount of money that should be removed from the player's inventory
			  * @param expectedRoboticonCustomisation The customisation type that the roboticon should have after the test
			  */
			public MarketPurchaseCustomisationParamaterisedTests(PurchaseStatus purchaseStatus, int initialMoney, ResourceType customisation,  int expectedMoneyRemoved, ResourceType expectedRoboticonCustomisation){
				this.purchaseStatus = purchaseStatus;
				this.initialMoney = initialMoney;
				this.customisation = customisation;
				this.expectedMoneyRemoved = expectedMoneyRemoved;
				this.expectedRoboticonCustomisation = expectedRoboticonCustomisation;
			}
			
			// Added by Josh Neil
			/**
			 * Defines the values to be used in each test
			 */
			@Parameterized.Parameters
			public static Collection customisationPurchaseValues(){
				 int orePrice = (new Market()).getSellPrice(ResourceType.CUSTOMISATION);
				 int energyPrice = (new Market()).getSellPrice(ResourceType.CUSTOMISATION);
				 int foodPrice = (new Market()).getSellPrice(ResourceType.CUSTOMISATION);
				 return Arrays.asList(new Object[][] {
			         {PurchaseStatus.Success,orePrice,ResourceType.ORE,orePrice,ResourceType.ORE},
			         {PurchaseStatus.Success,2*orePrice,ResourceType.ORE,orePrice,ResourceType.ORE},
			         {PurchaseStatus.FailPlayerNotEnoughMoney,orePrice-1,ResourceType.ORE,0,ResourceType.Unknown},
			         {PurchaseStatus.FailPlayerNotEnoughMoney,0,ResourceType.ORE,0,ResourceType.Unknown},
			         
			         {PurchaseStatus.Success,energyPrice,ResourceType.ENERGY,energyPrice,ResourceType.ENERGY},
			         {PurchaseStatus.Success,2*energyPrice,ResourceType.ENERGY,energyPrice,ResourceType.ENERGY},
			         {PurchaseStatus.FailPlayerNotEnoughMoney,energyPrice-1,ResourceType.ENERGY,0,ResourceType.Unknown},
			         {PurchaseStatus.FailPlayerNotEnoughMoney,0,ResourceType.ENERGY,0,ResourceType.Unknown},
			         
			         {PurchaseStatus.Success,foodPrice,ResourceType.FOOD,foodPrice,ResourceType.FOOD},
			         {PurchaseStatus.Success,2*foodPrice,ResourceType.FOOD,foodPrice,ResourceType.FOOD},
			         {PurchaseStatus.FailPlayerNotEnoughMoney,foodPrice-1,ResourceType.FOOD,0,ResourceType.Unknown},
			         {PurchaseStatus.FailPlayerNotEnoughMoney,0,ResourceType.FOOD,0,ResourceType.Unknown},
			         
			      });
			}
			/**
			 * Runs before every test and creates the necessary objects
			 */
			@Before
			public void setup(){
				player = new Player(null);
				market = new Market();
				player.setMoney(initialMoney);
				roboticon = new Roboticon(0);
			}
			
			/**
			 * Tests {@link Player#purchaseCustomisationFromMarket(ResourceType, Roboticon, Market)} ensures 
			 * that the correct value is returned when a player tries to purchase a customisation
			 */
			@Test
			public void testPurchaseCustomisationReturnedValue(){
				assertEquals(player.purchaseCustomisationFromMarket(customisation, roboticon, market),purchaseStatus);
			}
			
			/**
			 * Tests {@link Player#purchaseCustomisationFromMarket(ResourceType, Roboticon, Market)} ensures
			 * that the correct amount of money is removed from a player's inventory when they attempt
			 * to purchase a customisation
			 */
			@Test
			public void testCorrectMoneyRemoved(){
				player.purchaseCustomisationFromMarket(customisation, roboticon, market);
				assertEquals(initialMoney-expectedMoneyRemoved,player.getMoney());
			}			
			
			/**
			 * Tests {@link Player#purchaseCustomisationFromMarket(ResourceType, Roboticon, Market)} ensures 
			 * that when a player attempts to purchase a customisation from the market the roboticon
			 * used ends up with the correct customisation
			 */
			@Test
			public void roboticonCustomisationCorrect(){
				player.purchaseCustomisationFromMarket(customisation, roboticon, market);
				assertEquals(roboticon.getCustomisation(),expectedRoboticonCustomisation);
			}
			
		}
		
		/**
		 * Tests {@link Player#purchaseLandPlot(LandPlot)} using various input values and ensures that the correct result
		 * is returned, the correct amount of money is removed from the player's inventory 
		 * and the plot is set to be owned by the player if appropriate
		 * @author jcn509
		 *
		 */
		@RunWith(Parameterized.class)
		public static class PlayerPurhaseLandPlotParamaterisedTests{
			 private Player player;
			 private LandPlot plot;
			 private boolean landPlotAlreadyOwned;
			 private boolean landPlotShouldBeOwnedByPlayer;
			 private int initialMoney;
			 private int moneyRemoved;
			 private RoboticonQuest game;
			 private boolean expectedReturnValue;
			
			 /**
			  * Runs before every test and sets up the values of variables needed in / used by those tests
			  * @param landPlotAlreadyOwned Is the land plot that the player wants to buy aready owned by a player
			  * @param landPlotShouldBeOwnedByPlayer After the player has attempted to purchase the land plot should they own it
			  * @param initialMoney How much money does the player have before trying to purchase the land plot
			  * @param moneyRemoved How much money should be removed from the players inventory
			  * @param expectedReturnValue What value should {@link Player#purchaseLandPlot(LandPlot)} return
			  */
			public PlayerPurhaseLandPlotParamaterisedTests(boolean landPlotAlreadyOwned, boolean landPlotShouldBeOwnedByPlayer,int initialMoney, int moneyRemoved, boolean expectedReturnValue){
				this.landPlotAlreadyOwned = landPlotAlreadyOwned;
				this.landPlotShouldBeOwnedByPlayer = landPlotShouldBeOwnedByPlayer;
				this.initialMoney = initialMoney;
				this.moneyRemoved = moneyRemoved;
				this.expectedReturnValue = expectedReturnValue;
			}
			
			// Added by Josh Neil
			/**
			 * Defines the values to be used in each test
			 */
			@Parameterized.Parameters
			public static Collection landPurchaseValues(){
				 int landPrice = 10;
				 return Arrays.asList(new Object[][] {
			         {false,true,landPrice,landPrice,true},
			         {false,true,landPrice*20,landPrice,true},
			         {false,false,landPrice-1,0,false},
			         {true,false,landPrice-1,0,false},
			         {true,false,landPrice,0,false},
			         {true,false,landPrice*100,0,false},
			      });
			}
			/**
			 * Runs before every test and creates the necessary objects
			 */
			@Before
			public void setup(){
				game = new RoboticonQuest();
				player = new Player(game);
				plot = new LandPlot(0, 0, 0);
				if(landPlotAlreadyOwned){
					plot.setOwner(new Player(null));
				}
				player.setMoney(initialMoney);
			}
			
			/**
			 * Tests {@link Player#purchaseLandPlot(LandPlot)} ensures that the land is owned after attempting to purchase it if it should be
			 */
			@Test
			public void testLandOwnedIfShouldBe(){
				player.purchaseLandPlot(plot);
				assertEquals(plot.hasOwner(),landPlotShouldBeOwnedByPlayer || landPlotAlreadyOwned);
			}
			
			/**
			 * Tests {@link Player#purchaseLandPlot(LandPlot)} ensures that the land is owned by the player after attempting to purchase it if it should be
			 */
			@Test
			public void testLandOwnedByPlayerIfShouldBe(){
				player.purchaseLandPlot(plot);
				if(landPlotShouldBeOwnedByPlayer){
					assertEquals(player,plot.getOwner());
				}
			}
			
			/**
			 * Tests {@link Player#purchaseLandPlot(LandPlot)} ensures that the correct amount of money is removed from the players inventory
			 */
			@Test
			public void testCorrectMoneyRemoved(){
				int moneyBefore = player.getMoney();
				player.purchaseLandPlot(plot);
				assertEquals(moneyBefore-moneyRemoved,player.getMoney());
			}
			
			/**
			 * Tests {@link Player#purchaseLandPlot(LandPlot)} ensures that the correct value is returned when it is called
			 */
			@Test
			public void testCorrectMoneyReturnValue(){
				assertEquals(player.purchaseLandPlot(plot),expectedReturnValue);
			}
			
		}
		
		
		//Exists in this class as well as PlayerUnitTest because this version depends on the market
		/**
		 * Tests {@link Player#getScore()} using  player objects with varying quantities of money, ore, energy and food
		 * and ensures that the correct result is returned
		 * @author jcn509
		 *
		 */
		@RunWith(Parameterized.class)
		public static class PlayerGetScoreParamaterisedTests{
			private int expectedScore;
			private int moneyQuantity;
			private int oreQuantity;
			private int foodQuantity;
			private int energyQuantity;
			private Player player;
			
			/**
			 * Sets up the required values for each test
			 * @param expectedScore The score that the player should have
			 * @param moneyQuantity The amount of money that the player has
			 * @param oreQuantity The amount of ore that the player has
			 * @param energyQuantity The amount of energy that the player has
			 * @param foodQuantity The amount of food that the player has
			 */
			public PlayerGetScoreParamaterisedTests(int expectedScore, int moneyQuantity, int oreQuantity,int energyQuantity,int foodQuantity){
				this.expectedScore = expectedScore;
				this.moneyQuantity = moneyQuantity;
				this.oreQuantity = oreQuantity;
				this.foodQuantity = foodQuantity;
				this.energyQuantity = energyQuantity;
			}
			
			/**
			 * Defines the values to be used in each test
			 */
			@Parameterized.Parameters
			public static Collection playerScoreTestValues(){
				 int oreValue = (new RoboticonQuest()).market.getSellPrice(ResourceType.ORE);
				 int energyValue = (new RoboticonQuest()).market.getSellPrice(ResourceType.ENERGY);
				 int foodValue = (new RoboticonQuest()).market.getSellPrice(ResourceType.FOOD);
				 return Arrays.asList(new Object[][] {
			         {10 +oreValue+energyValue+foodValue,10,1,1,1},
			         {0,0,0,0,0},
			         {1,1,0,0,0},
			         {2*oreValue,0,2,0,0},
			         {3*energyValue,0,0,3,0},
			         {5*foodValue,0,0,0,5},
			         {(5*foodValue)+oreValue,0,1,0,5},
			         {(5*foodValue)+oreValue +(2*energyValue),0,1,2,5},
			         {(15*foodValue)+(4*oreValue) +(97*energyValue),0,4,97,15},
			         {101+(15*foodValue)+(4*oreValue) +(97*energyValue),101,4,97,15},
			      });
			}
			
			/**
			 * Runs before every test and creates the player object required with the defined amount of
			 * money, ore, energy and food
			 */
			@Before
			public void setup(){
				RoboticonQuest game = new RoboticonQuest();
				player = new Player(game);
				player.setMoney(moneyQuantity);
				player.setEnergy(energyQuantity);
				player.setOre(oreQuantity);
				player.setFood(foodQuantity);	
			}
			
			/**
			 * Tests {@link Player#getScore()} ensures that the correct score is returned
			 * when the player has the defined quantity of money, ore, energy and food
			 */
			@Test
			public void testScore(){
				assertEquals(expectedScore,player.getScore());
			}
			
			
		}
		
		/**
		 * Tests {@link Player#generateResources()} using  player objects with various different plots with different attributes
		 * and ensures that the correct resources are added to the players inventory
		 * @author jcn509
		 *
		 */
		@RunWith(Parameterized.class)
		public static class PlayerGenerateResourcesParamaterisedTests{
			private int plot1OreValue;
			private int plot1FoodValue;
			private int plot1EnergyValue;
			private ResourceType plot1RoboticonCustomisation;
			private int plot2OreValue;
			private int plot2FoodValue;
			private int plot2EnergyValue;
			private ResourceType plot2RoboticonCustomisation;
			private int plot3OreValue;
			private int plot3FoodValue;
			private int plot3EnergyValue;
			private ResourceType plot3RoboticonCustomisation;
			private int expectedAmountOfOre;
			private int expectedAmountOfEnergy;
			private int expectedAmountOfFood;
			
			private Player player;
			private LandPlot plot1,plot2,plot3;
			
			
			public PlayerGenerateResourcesParamaterisedTests(int plot1OreValue, int plot1FoodValue, int plot1EnergyValue, 
					ResourceType plot1RoboticonCustomisation, int plot2OreValue, int plot2FoodValue, 
					int plot2EnergyValue, ResourceType plot2RoboticonCustomisation, int plot3OreValue, 
					int plot3FoodValue, int plot3EnergyValue, ResourceType plot3RoboticonCustomisation, 
					int expectedAmountOfOre, int expectedAmountOfEnergy, int expectedAmountOfFood){
				this.plot1OreValue = plot1OreValue;
				this.plot1FoodValue = plot1FoodValue;
				this.plot1EnergyValue = plot1EnergyValue;
				this.plot1RoboticonCustomisation = plot1RoboticonCustomisation;
				this.plot2OreValue = plot2OreValue;
				this.plot2FoodValue = plot2FoodValue;
				this.plot2EnergyValue = plot2EnergyValue;
				this.plot2RoboticonCustomisation = plot2RoboticonCustomisation;
				this.plot3OreValue = plot3OreValue;
				this.plot3FoodValue = plot3FoodValue;
				this.plot3EnergyValue = plot3EnergyValue;
				this.plot3RoboticonCustomisation = plot3RoboticonCustomisation;
				this.expectedAmountOfOre = expectedAmountOfOre;
				this.expectedAmountOfEnergy = expectedAmountOfEnergy;
				this.expectedAmountOfFood = expectedAmountOfFood;
			}
			
			/**
			 * Defines the values to be used in each test
			 */
			@Parameterized.Parameters
			public static Collection playerGenerateResourcesTestValues(){
				 
				 return Arrays.asList(new Object[][] {
			         {1,1,1,ResourceType.ORE,0,0,0,ResourceType.Unknown,0,0,0,ResourceType.Unknown,1,0,0},
			         {1,1,1,ResourceType.ENERGY,0,0,0,ResourceType.Unknown,0,0,0,ResourceType.Unknown,0,1,0},
			         {1,1,1,ResourceType.FOOD,0,0,0,ResourceType.Unknown,0,0,0,ResourceType.Unknown,0,0,1},
			         {1,1,1,ResourceType.ORE,1,1,1,ResourceType.ORE,1,1,1,ResourceType.ORE,3,0,0},
			         {1,1,1,ResourceType.ENERGY,1,1,1,ResourceType.ENERGY,1,1,1,ResourceType.ENERGY,0,3,0},
			         {1,1,1,ResourceType.FOOD,1,1,1,ResourceType.FOOD,1,1,1,ResourceType.FOOD,0,0,3},
			         {1,15,1,ResourceType.FOOD,1,1,1,ResourceType.ENERGY,1,1,1,ResourceType.ORE,1,1,15},
			   
				      
			      });
			}
			
			/**
			 * Runs before every test and creates the player object and the required plot objects with the required attributes
			 */
			@Before
			public void setup(){
				RoboticonQuest game = new RoboticonQuest();
				Roboticon r1 = new Roboticon(0);
				Roboticon r2 = new Roboticon(1);
				Roboticon r3 = new Roboticon(3);
				
				player = new Player(game);
				plot1 = new LandPlot(plot1OreValue,plot1EnergyValue,plot1FoodValue);
				plot2 = new LandPlot(plot2OreValue,plot2EnergyValue,plot2FoodValue);
				plot3 = new LandPlot(plot3OreValue,plot3EnergyValue,plot3FoodValue);
				
				plot1.setOwner(player);
				plot2.setOwner(player);
				plot3.setOwner(player);
				
				player.addLandPlot(plot1);
				player.addLandPlot(plot2);
				player.addLandPlot(plot3);
				
				if(plot1RoboticonCustomisation == ResourceType.FOOD || plot1RoboticonCustomisation == ResourceType.ORE || plot1RoboticonCustomisation == ResourceType.ENERGY){
					r1.setCustomisation(plot1RoboticonCustomisation);
					plot1.installRoboticon(r1);
					plot1.setHasRoboticon(true);
				}
				
				if(plot2RoboticonCustomisation == ResourceType.FOOD || plot2RoboticonCustomisation == ResourceType.ORE || plot2RoboticonCustomisation == ResourceType.ENERGY){
					r2.setCustomisation(plot2RoboticonCustomisation);
					plot2.installRoboticon(r2);
					plot2.setHasRoboticon(true);
				}
				
				if(plot3RoboticonCustomisation == ResourceType.FOOD || plot3RoboticonCustomisation == ResourceType.ORE || plot3RoboticonCustomisation == ResourceType.ENERGY){
					r3.setCustomisation(plot3RoboticonCustomisation);
					plot3.installRoboticon(r3);
					plot3.setHasRoboticon(true);
				}
				
			}
			
			/**
			 * Tests {@link Player#generateResources()} ensures that the correct amount of energy is generated and added
			 * to the player's inventory
			 */
			@Test
			public void testCorrectEnergyAddedToInventory(){
				int energyBefore = player.getEnergy();
				player.generateResources();
				
				assertEquals(energyBefore+expectedAmountOfEnergy,player.getEnergy());
			}
			
			/**
			 * Tests {@link Player#generateResources()} ensures that the correct amount of ore is generated and added
			 * to the player's inventory
			 */
			@Test
			public void testCorrectOreAddedToInventory(){
				int oreBefore = player.getOre();
				player.generateResources();
				assertEquals(oreBefore+expectedAmountOfOre,player.getOre());
			}
			
			/**
			 * Tests {@link Player#generateResources()} ensures that the correct amount of food is generated and added
			 * to the player's inventory
			 */
			@Test
			public void testCorrectFoodAddedToInventory(){
				int foodBefore = player.getFood();
				player.generateResources();
				
				assertEquals(foodBefore+expectedAmountOfFood,player.getFood());
			}
			
			/**
			 * Tests {@link Player#generateResources()} ensures that the correct amount of energy is returned
			 */
			@Test
			public void testCorrectEnergyReturned(){
				assertEquals(expectedAmountOfEnergy,player.generateResources().get(ResourceType.ENERGY));
			}
			
			/**
			 * Tests {@link Player#generateResources()} ensures that the correct amount of ore is returned
			 */
			@Test
			public void testCorrectOreReturned(){
				assertEquals(expectedAmountOfOre,player.generateResources().get(ResourceType.ORE));
			}
			
			/**
			 * Tests {@link Player#generateResources()} ensures that the correct amount of food is returned
			 */
			@Test
			public void testCorrectFoodReturned(){
				assertEquals(expectedAmountOfFood,player.generateResources().get(ResourceType.FOOD));
			}
		}
		
		// Due to the fact that these tests only rely on 2 getter methods from Roboticon
		// there is not a separate unit test version of them
		/**
		 * Tests {@link Player#getCustomisedRoboticonAmountList()} using player objects with differning numbers of roboticons of various
		 * types and ensures that the correct values are returned
		 * @author jcn509
		 *
		 */
		@RunWith(Parameterized.class)
		public static class PlayerGetCustomisedRoboticonAmountListParamaterisedTests{
			private int numberOfUncustomisedRoboticons;
			private int numberOfEnergyRoboticons;
			private int numberOfOreRoboticons;
			private int numberOfFoodRoboticons;
			private int numberOfOreRoboticonsAlreadyPlaced;
			private int numberOfEnergyRoboticonsAlreadyPlaced;
			private int numberOfFoodRoboticonsAlreadyPlaced;
			
			private Player player;
			private LandPlot plot1,plot2,plot3;
			
			
			public PlayerGetCustomisedRoboticonAmountListParamaterisedTests(int numberOfUncustomisedRoboticons,
															int numberOfEnergyRoboticons,
															int numberOfOreRoboticons,
															int numberOfFoodRoboticons,
															int numberOfOreRoboticonsAlreadyPlaced,
															int numberOfEnergyRoboticonsAlreadyPlaced,
															int numberOfFoodRoboticonsAlreadyPlaced){
				this.numberOfUncustomisedRoboticons = numberOfUncustomisedRoboticons;
				this.numberOfEnergyRoboticons = numberOfEnergyRoboticons;
				this.numberOfOreRoboticons = numberOfOreRoboticons;
				this.numberOfFoodRoboticons = numberOfFoodRoboticons;
				this.numberOfOreRoboticonsAlreadyPlaced = numberOfOreRoboticonsAlreadyPlaced;
				this.numberOfEnergyRoboticonsAlreadyPlaced = numberOfEnergyRoboticonsAlreadyPlaced;
				this.numberOfFoodRoboticonsAlreadyPlaced = numberOfFoodRoboticonsAlreadyPlaced;
			}
			
			/**
			 * Defines the values to be used in each test
			 */
			@Parameterized.Parameters
			public static Collection playerCustomisedRoboticonAmountListTestValues(){
				 
				 return Arrays.asList(new Object[][] {
			         {15,1,1,1,0,0,0},
			         {0,1,1,1,0,0,0},
			         {16,1,2,3,0,0,0},
			         {16,6,7,2,0,0,0},
			         {15,2,2,2,0,0,0},
			         {1111,67,89,10,0,0,0},
			         {112135,1,0,0,0,0,0},
			         {112315,0,0,1,0,0,0},
			         {1335,0,1,0,0,0,0},
			         {112135,20,0,0,0,0,0},
			         {112315,0,0,34,0,0,0},
			         {1335,0,56,0,0,0,0},
			         {0,20,56,34,0,0,0},
			         {15,1,1,1,0,1,1},
			         {1111,67,89,10,3,0,0},
			         {1111,67,89,10,0,4,0},
			         {1111,67,89,10,0,0,2},
			         {1111,67,89,10,1,3,9},
			      });
			}
			
			/**
			 * Runs before every test and creates the player object and the required roboticon objects
			 */
			@Before
			public void setup(){
				RoboticonQuest game = new RoboticonQuest();
			
				player = new Player(game);
				for(int uncustomised =0; uncustomised<numberOfUncustomisedRoboticons; uncustomised++){
					Roboticon roboticon = new Roboticon(uncustomised);
					player.roboticonList.add(roboticon);
				}
				
				int numberToPlace = numberOfOreRoboticonsAlreadyPlaced;
				for(int ore =0; ore<numberOfOreRoboticons; ore++){
					Roboticon roboticon = new Roboticon(ore);
					roboticon.setCustomisation(ResourceType.ORE);
					player.roboticonList.add(roboticon);
		
					if(numberToPlace>0){
						numberToPlace--;
						LandPlot plot = new LandPlot(0, 0, 0);
						roboticon.setInstalledLandplot(plot);
					}
				}
				
				numberToPlace = numberOfEnergyRoboticonsAlreadyPlaced;
				for(int energy =0; energy<numberOfEnergyRoboticons; energy++){
					Roboticon roboticon = new Roboticon(energy);
					roboticon.setCustomisation(ResourceType.ENERGY);
					player.roboticonList.add(roboticon);
					
					if(numberToPlace>0){
						numberToPlace--;
						LandPlot plot = new LandPlot(0, 0, 0);
						roboticon.setInstalledLandplot(plot);
					}
				}
				
				numberToPlace = numberOfFoodRoboticonsAlreadyPlaced;
				for(int food =0; food<numberOfFoodRoboticons; food++){
					Roboticon roboticon = new Roboticon(food);
					roboticon.setCustomisation(ResourceType.FOOD);
					player.roboticonList.add(roboticon);
					
					if(numberToPlace>0){
						numberToPlace--;
						LandPlot plot = new LandPlot(0, 0, 0);
						roboticon.setInstalledLandplot(plot);
					}
				}
			}
			
			/**
			 * Tests {@link Player#getCustomisedRoboticonAmountList()} ensures that the returned list
			 * contains exactly 3 elements
			 */
			@Test
			public void testReturnedListCorrectLength(){
				Array<String> roboticonList = player.getCustomisedRoboticonAmountList();
				assertEquals(3,roboticonList.size);
			}
			
			/**
			 * Tests {@link Player#getCustomisedRoboticonAmountList()} ensures that the number of ore roboticons listed is correct
			 */
			@Test
			public void testCorrectOre(){
				Array<String> roboticonList = player.getCustomisedRoboticonAmountList();
				assertEquals("ore specific x "+Integer.toString(numberOfOreRoboticons-numberOfOreRoboticonsAlreadyPlaced),roboticonList.get(0).toLowerCase());
			}
			
			/**
			 * Tests {@link Player#getCustomisedRoboticonAmountList()} ensures that the number of energy roboticons listed is correct
			 */
			@Test
			public void testCorrectEnergy(){
				Array<String> roboticonList = player.getCustomisedRoboticonAmountList();
				assertEquals("energy specific x "+Integer.toString(numberOfEnergyRoboticons-numberOfEnergyRoboticonsAlreadyPlaced),roboticonList.get(1).toLowerCase());
			}
			
			/**
			 * Tests {@link Player#getCustomisedRoboticonAmountList()} ensures that the number of food roboticons listed is correct
			 */
			@Test
			public void testCorrectFood(){
				System.out.println(numberOfFoodRoboticons);
				System.out.println(numberOfFoodRoboticonsAlreadyPlaced);
				Array<String> roboticonList = player.getCustomisedRoboticonAmountList();
				assertEquals("food specific x "+Integer.toString(numberOfFoodRoboticons-numberOfFoodRoboticonsAlreadyPlaced),roboticonList.get(2).toLowerCase());
			}
			
		}
		

}