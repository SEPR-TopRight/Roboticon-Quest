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
import mockit.*;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Enclosed.class)
public class PlayerUnitTest {
	
	/** 
	 * Josh Neil moved any tests that are non-parametrised and should only be ran once into this class
	 */
	public static class PlayerSingleTests{
		@Rule
		public final ExpectedException exception = ExpectedException.none();
	
		private Player player;
		@Mocked private RoboticonQuest game;
		@Mocked private Market market;
		@Mocked private Roboticon roboticon;
		@Mocked private Roboticon roboticon2;
		@Mocked private Roboticon roboticon3;
		@Mocked private Roboticon roboticon4;
		@Mocked private LandPlot plot;
		@Mocked private LandPlot plot2;
		@Mocked private LandPlot plot3;
	
		/**
		 * Runs before every test and creates the Player object that is udner test as well as the RoboticonQuest object
		 * that is requried by some tests
		 */
		@Before
		public void setUp() {
			RoboticonQuest game = new RoboticonQuest();
			player = new Player(game);
			Market market = new Market();
			roboticon = new Roboticon(1);
			roboticon2 = new Roboticon(2);
			roboticon3 = new Roboticon(3);
			roboticon4 = new Roboticon(4);
			plot = new LandPlot(0,1,1);
			plot2 = new LandPlot(0,1,1);
			plot3 = new LandPlot(0,1,1);
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
			
			new Expectations(){{
				market.hasEnoughResources(ResourceType.ORE, 5); result=true;
				market.getSellPrice(ResourceType.ORE); result=10;
				market.sellResource(ResourceType.ORE, 5);
				
				market.hasEnoughResources(ResourceType.ENERGY, 10);result=true;
				market.getSellPrice(ResourceType.ENERGY); result=11;
				market.sellResource(ResourceType.ENERGY, 10);
				
				market.hasEnoughResources(ResourceType.FOOD, 11);result=true;
				market.getSellPrice(ResourceType.FOOD); result=7;
				market.sellResource(ResourceType.FOOD, 11); 
			}};
			player.setMoney(1000);
	
			int playerMoney = player.getMoney();
			int orePrice = 10;
			//Purchase 5 ore
			player.purchaseResourceFromMarket(5, market, ResourceType.ORE);
			// Player should now have 5 more ores, and the market have 5 less ores.
			assertEquals(playerMoney - 5 * orePrice, player.getMoney());
			assertEquals(5, player.getOre());
	
			playerMoney = player.getMoney();
			int energyPrice = 11;
			//purchase 10 energy
			player.purchaseResourceFromMarket(10, market, ResourceType.ENERGY);
			assertEquals(playerMoney - 10 * energyPrice, player.getMoney());
			assertEquals(10, player.getEnergy());
			
			// Added by Josh Neil
			playerMoney = player.getMoney();
			int foodPrice = 7;
			//purchase 11 food
			player.purchaseResourceFromMarket(11, market, ResourceType.FOOD);
			assertEquals(playerMoney - 11 * foodPrice, player.getMoney());
			assertEquals(11, player.getFood());
		}
	
		/**
		 * Tests {@link Player#sellResourceToMarket(int, Market, ResourceType)} and ensures that
		 * the correct amount of money is added to the players inventory, the correct amount of resources are
		 * removed from the player's inventory and the correct amount of resources are added to the market's inventory
		 */
		@Test
		public void testPlayerSellResource() throws Exception {
			new Expectations(){{
				market.getBuyPrice(ResourceType.ORE); result=5;
				market.buyResource(ResourceType.ORE, 5);
				
				market.getBuyPrice(ResourceType.ENERGY); result=6;
				market.buyResource(ResourceType.ENERGY, 5);
				
				market.getBuyPrice(ResourceType.FOOD); result=7;
				market.buyResource(ResourceType.FOOD, 5); 			
				
			}};
			
			player.setMoney(1000);
			player.setResource(ResourceType.ORE, 15);
			player.setResource(ResourceType.ENERGY, 15);
			player.setResource(ResourceType.FOOD, 15); // Added by Josh Neil
	
			int orePrice = 5;
			//sell 5 ore
			player.sellResourceToMarket(5, market, ResourceType.ORE);
			assertEquals(1000 + 5 * orePrice, player.getMoney());
			assertEquals(10, player.getOre());
	
			int energyPrice = 6;
			player.setMoney(1000);
			//sell 5 energy
			player.sellResourceToMarket(5, market, ResourceType.ENERGY);
			assertEquals(1000 + 5 * energyPrice, player.getMoney());
			assertEquals(10, player.getEnergy());
			
			// Added by Josh Neil
			int foodPrice = 7;
			player.setMoney(1000);
			//sell 5 food
			player.sellResourceToMarket(5, market, ResourceType.FOOD);
			assertEquals(1000 + 5 * foodPrice, player.getMoney());
			assertEquals(10, player.getFood());
		}
	
		/**
		 * Tests {@link Player#purchaseResourceFromMarket(int, Market, ResourceType)} ensures
		 * that players cannot buy more of a given resource than the market possesses and
		 * that PurchaseStatus.FailMarketNotEnoughResource is returned from the call
		 * <p>
		 * Also ensures that the players inventory does not change when they try to do this
		 * </p>
		 */
		@Test
		public void testPlayerCannotBuyMoreThanAllowed() throws Exception {
			new Expectations(){{
				market.hasEnoughResources(ResourceType.ORE, 100);result=false;
				market.hasEnoughResources(ResourceType.ENERGY, 100);result=false;
				market.hasEnoughResources(ResourceType.FOOD, 100);result=false;
			}};
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
			new Expectations(){{
				roboticon.setCustomisation(ResourceType.ORE);
				roboticon2.setCustomisation(ResourceType.ENERGY);
			}};
			player.customiseRoboticon(roboticon, ResourceType.ORE);
	
			player.customiseRoboticon(roboticon2, ResourceType.ENERGY);
		}
		
		/**
		 * Tests {@link Player#customiseRoboticon(Roboticon, ResourceType)} and ensures that players
		 * can customise roboticons that they own
		 */
		@Test
		public void testPlayerCanCustomiseOwnedRoboticons() {
			new Expectations(){{
				roboticon3.setCustomisation(ResourceType.ORE);
				roboticon4.setCustomisation(ResourceType.ENERGY);
			}};
			player.roboticonList = new Array<Roboticon>();
			player.roboticonList.add(roboticon3);
			player.roboticonList.add(roboticon4);
			player.customiseRoboticon(player.roboticonList.get(0), ResourceType.ORE);
			player.customiseRoboticon(player.roboticonList.get(1), ResourceType.ENERGY);
		}
		
		// Tests below this comment were added by Josh Neil
		
		/**
		 * Tests {@link Player#addLandPlot(LandPlot)} ensures that the LandPlot is added to the player's
		 * land list when the given plot is owned by the player and not already in their land list
		 */
		@Test
		public void testPlayerAddLandPlot(){
			new Expectations(){{
				plot.getOwner();result=player;				
			}};
			player.addLandPlot(plot);
			assertTrue(player.landList.contains(plot));
		}
		
		/**
		 * Tests {@link Player#addLandPlot(LandPlot)} ensures that the LandPlot is not added to the player's
		 * land list when the given plot is not owned by any player
		 */
		@Test
		public void testPlayerAddLandPlotUnowned(){
			new Expectations(){{
				plot.getOwner();result=null;				
			}};
			player.addLandPlot(plot);
			assertFalse(player.landList.contains(plot));
		}
		
		/**
		 * Tests {@link Player#addLandPlot(LandPlot)} ensures that the LandPlot is not added to the player's
		 * land list when the given plot is owned by another player
		 */
		@Test
		public void testPlayerAddLandPlotOwnedBySomeoneElse(){
			new Expectations(){{
				plot.getOwner();result=new Player(game);				
			}};
			player.addLandPlot(plot);
			assertFalse(player.landList.contains(plot));
		}
		
		/**
		 * Tests {@link Player#addLandPlot(LandPlot)} ensures that the LandPlot is not added to the player's
		 * land list when the given plot is owned by the player and already in their land list
		 */
		@Test
		public void testPlayerAddLandPlotAlreadyInLandList(){
			new Expectations(){{
				plot.getOwner();result=player;			
			}};
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
			new Expectations(){{
				plot.getOwner();result=player;			
				plot.getOwner();result=player;		// Called in removeLandPlot as well as in addLandPlot
			}};
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
			new Expectations(){{
				plot.getOwner();result=player;
			}};
			player.addLandPlot(plot); // Already have some plot in their list
			int landPlotListSizeBefore = player.landList.size();
			
			player.removeLandPlot(plot2);
			assertEquals(landPlotListSizeBefore,player.landList.size());
		}
		
		/**
		 * Tests {@link Player#removeLandPlot(LandPlot)} ensures that when the no plots are removed when the given LandPlot is 
		 * owned by another player
		 */
		@Test
		public void testPlayerRemoveLandPlotOwnedbyAnotherPlayer(){
			new Expectations(){{
				plot.getOwner();result=player;
			}};
			player.addLandPlot(plot); // Already have some plot in their list
			int landPlotListSizeBefore = player.landList.size();
			
			player.removeLandPlot(plot2);
			assertEquals(landPlotListSizeBefore,player.landList.size());
		}
		
		
		/**
		 * Tests {@link Player#removeRoboticon(Roboticon)} ensures that the given roboticon
		 * is actually removed from the player's inventory (as this is a simple method only a single test is needed)
		 */
		@Test
		public void testRemoveRoboticonRoboticonRemoved(){
		
			player.roboticonList.add(roboticon);
			player.roboticonList.add(roboticon2);
			player.roboticonList.add(roboticon3);
			
			player.removeRoboticon(roboticon);
			
			assertFalse(player.roboticonList.contains(roboticon, true));
		}
		
		/**
		 * Tests {@link Player#removeRoboticon(Roboticon)} ensures that the size of the roboticon list that stores the players
		 * roboticons is reduced by 1 (as this is a simple method only a single test is needed)
		 */
		@Test
		public void testRemoveRoboticonListSizeReduced(){
			
			player.roboticonList.add(roboticon);
			player.roboticonList.add(roboticon2);
			player.roboticonList.add(roboticon3);
			
			player.removeRoboticon(roboticon);
			
			assertEquals(2,player.roboticonList.size);
		}
		
		
		
		/**
		 * Tests {@link Player#setEnergy(int)} ensures that an exception is thrown if a negative value is used
		 */
		@Test(expected=IllegalArgumentException.class)
		public void testNegativeSetEnergy(){
			player.setEnergy(-1);
		}
		
		/**
		 * Tests {@link Player#setOre(int)} ensures that an exception is thrown if a negative value is used
		 */
		@Test(expected=IllegalArgumentException.class)
		public void testNegativeSetOre(){
			player.setOre(-1);
		}
		
		/**
		 * Tests {@link Player#setFood(int)} ensures that an exception is thrown if a negative value is used
		 */
		@Test(expected=IllegalArgumentException.class)
		public void testNegativeSetFood(){
			player.setFood(-1);
		}
		
		/**
		 * Tests {@link Player#setMoney(int)} ensures that an exception is thrown if a negative value is used
		 */
		@Test(expected=IllegalArgumentException.class)
		public void testNegativeSetMoney(){
			player.setMoney(-1);
		}
		
		
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
		 @Mocked private Market market;
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
			 int roboticonPrice = 10;
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
			if(marketInitialRoboticons<numberOfRoboticonsToPurchase){
				new Expectations(){{
					market.hasEnoughResources(ResourceType.ROBOTICON,numberOfRoboticonsToPurchase); result=false;			
				}};
			}
			else{
				new Expectations(){{
					market.hasEnoughResources(ResourceType.ROBOTICON,numberOfRoboticonsToPurchase); result=true;			
					market.getSellPrice(ResourceType.ROBOTICON);result=10;
				}};
				if(numberOfRoboticonsToPurchase *10 <= initialMoney){
					new Expectations(){{
						market.sellResource(ResourceType.ROBOTICON, numberOfRoboticonsToPurchase);
					}};
				}
			}
			player = new Player(null);
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
			 @Mocked Market market;
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
				 int orePrice = 10;
				 int energyPrice = 10;
				 int foodPrice = 10;
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
				new Expectations(){{
					market.hasEnoughResources(ResourceType.CUSTOMISATION, 1);result=true;
					market.getSellPrice(ResourceType.CUSTOMISATION);result=10;
				}};
				if(initialMoney>=10){ // Player can afford the customisation (10 is the arbitrary cost used in this test)
					new Expectations(){{
						market.sellResource(ResourceType.CUSTOMISATION, 1);
					}};
				}
				player = new Player(null);
				
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
			 @Mocked private LandPlot plot;
			 private boolean landPlotAlreadyOwned;
			 private boolean landPlotShouldBeOwnedByPlayer;
			 private int initialMoney;
			 private int moneyRemoved;
			 @Mocked private RoboticonQuest game;
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
				new Expectations(){{
					plot.hasOwner();result=landPlotAlreadyOwned;
				}};
				if(initialMoney>=10 && !landPlotAlreadyOwned){
					new Expectations(){{
						plot.setOwner(player);
					}};
				}
				player.setMoney(initialMoney);
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
		
		/**
		 * Tests {@link Player#sellResourceToPlayer(Player, int, ResourceType, int)} using various input values and ensures that the correct result
		 * is returned, the correct amount of money, ore, energy and food is removed/added from/to each player's inventory 
		 * @author jcn509
		 *
		 */
		@RunWith(Parameterized.class)
		public static class PlayerToPlayerSaleParamaterisedTests{
			 private Player buyingPlayer,sellingPlayer;
			 private int buyingPlayerOreBefore;
			 private int buyingPlayerEnergyBefore;
			 private int buyingPlayerFoodBefore;
			 private int buyingPlayerExpectedOreChange;
			 private int buyingPlayerExpectedEnergyChange;
			 private int buyingPlayerExpectedFoodChange;
			 private int sellingPlayerOreBefore;
			 private int sellingPlayerEnergyBefore;
			 private int sellingPlayerFoodBefore;
			 private int sellingPlayerExpectedOreChange;
			 private int sellingPlayerExpectedEnergyChange;
			 private int sellingPlayerExpectedFoodChange;
			 private int buyingPlayerMoneyBefore;
			 private int sellingPlayerMoneyBefore;
			 private int buyingPlayerExpectedMoneyChange;
			 private int sellingPlayerExpectedMoneyChange;
			 private ResourceType resource;
			 private PurchaseStatus expectedReturnValue;
			 private int pricePerUnit;
			 private int quantity;
			
			/**
			 * Runs before every test and sets up the values of the required parameters
			 * @param buyingPlayerQuantityBefore The amount of the given resource that the buying player had prior to the sale
			 * @param buyingPlayerExpectedQuantityChange The amount of the given resource that should be added to the buying player's inventory
			 * @param buyingPlayerMoneyBefore The amount of money that the buying player had prior to the sale
			 * @param buyingPlayerExpectedMoneyChange The amount of money that should be removed from the buying player's inventory
			 * @param sellingPlayerQuantityBefore The amount of the given resource that the selling player had prior to the sale 
			 * @param sellingPlayerExpectedQuantityChange The amount of the given resource that should be removed from the selling player's inventory
			 * @param sellingPlayerMoneyBefore The amount of money that the selling player had prior to the sale
			 * @param sellingPlayerExpectedMoneyChange The amount of money that should be added to the selling player's inventory
			 * @param resource The resource that the selling player is selling
			 * @param pricePerUnit The amount of money that is to be paid for each unit of the resource that is being sold
			 * @param quantity The amount of the resource that is to be sold
			 * @param expectedReturnValue The result returned by {@link Player#sellResourceToPlayer(Player, int, ResourceType, int)}
			 */
			public PlayerToPlayerSaleParamaterisedTests(int buyingPlayerQuantityBefore, int buyingPlayerExpectedQuantityChange,
														int buyingPlayerMoneyBefore,int buyingPlayerExpectedMoneyChange,
														int sellingPlayerQuantityBefore, 
														int sellingPlayerExpectedQuantityChange, int sellingPlayerMoneyBefore,
														int sellingPlayerExpectedMoneyChange, 
														ResourceType resource, int pricePerUnit,int quantity,PurchaseStatus expectedReturnValue){
				this.buyingPlayerOreBefore = 0;
				this.buyingPlayerEnergyBefore = 0;
				this.buyingPlayerFoodBefore = 0;
				this.buyingPlayerExpectedOreChange = 0;
				this.buyingPlayerExpectedEnergyChange = 0;
				this.buyingPlayerExpectedFoodChange = 0;
				this.sellingPlayerOreBefore = 0;
				this.sellingPlayerEnergyBefore = 0;
				this.sellingPlayerFoodBefore = 0;
				this.sellingPlayerExpectedOreChange = 0;
				this.sellingPlayerExpectedEnergyChange = 0;
				this.sellingPlayerExpectedFoodChange = 0;
				this.buyingPlayerMoneyBefore = buyingPlayerMoneyBefore;
				this.sellingPlayerMoneyBefore = sellingPlayerMoneyBefore;
				this.buyingPlayerExpectedMoneyChange = buyingPlayerExpectedMoneyChange;
				this.sellingPlayerExpectedMoneyChange = sellingPlayerExpectedMoneyChange;
				this.resource = resource;
				this.expectedReturnValue = expectedReturnValue;
				this.pricePerUnit = pricePerUnit;
				this.quantity = quantity;
				
				switch(resource){
					case ORE:
						buyingPlayerOreBefore = buyingPlayerQuantityBefore;
						buyingPlayerExpectedOreChange = buyingPlayerExpectedQuantityChange;
						sellingPlayerOreBefore = sellingPlayerQuantityBefore;
						sellingPlayerExpectedOreChange = sellingPlayerExpectedQuantityChange;
					break;
					
					case ENERGY:
						buyingPlayerEnergyBefore = buyingPlayerQuantityBefore;
						buyingPlayerExpectedEnergyChange = buyingPlayerExpectedQuantityChange;
						sellingPlayerEnergyBefore = sellingPlayerQuantityBefore;
						sellingPlayerExpectedEnergyChange = sellingPlayerExpectedQuantityChange;
					break;
					
					case FOOD:
						buyingPlayerFoodBefore = buyingPlayerQuantityBefore;
						buyingPlayerExpectedFoodChange = buyingPlayerExpectedQuantityChange;
						sellingPlayerFoodBefore = sellingPlayerQuantityBefore;
						sellingPlayerExpectedFoodChange = sellingPlayerExpectedQuantityChange;
					break;
				}
			}
			
			/**
			 * Defines the values to be used in each test
			 */
			@Parameterized.Parameters
			public static Collection playerToPlayerSaleValues(){
				 return Arrays.asList(new Object[][] {
			         {0, 10,100,100,10, 10, 0,100, ResourceType.ORE, 10,10,PurchaseStatus.Success},
			         {0, 9,90,90,9, 9, 0,90, ResourceType.ORE, 10,9,PurchaseStatus.Success},
			         {1, 1,90,1,1, 1, 0,1, ResourceType.ORE, 1,1,PurchaseStatus.Success},
			         {0, 10,100,100,10, 10, 0,100, ResourceType.ORE, 10,10,PurchaseStatus.Success},
			         {100, 10,100,100,10, 10, 0,100, ResourceType.ORE, 10,10,PurchaseStatus.Success},
			         {100, 10,100,100,100, 10, 0,100, ResourceType.ORE, 10,10,PurchaseStatus.Success},
			         {0, 0,99,0,10, 0, 0,0, ResourceType.ORE, 10,10,PurchaseStatus.FailPlayerNotEnoughMoney},
			         {0, 0,990,0,100, 0, 0,0, ResourceType.ORE, 100,10,PurchaseStatus.FailPlayerNotEnoughMoney},
			         {0, 0,990,0,1000, 0, 0,0, ResourceType.ORE, 1,1000,PurchaseStatus.FailPlayerNotEnoughMoney},
			         {0, 0,99,0,10, 0, 0,0, ResourceType.ORE, 1,100,PurchaseStatus.FailPlayerNotEnoughResource},
			         {0, 0,99,0,10, 0, 0,0, ResourceType.ORE, 100,100,PurchaseStatus.FailPlayerNotEnoughResource},
			         {0,0,0,0,0,0,0,0,ResourceType.ORE,10,10,PurchaseStatus.FailPlayerNotEnoughResource},
			         {0, 0,100,0,9, 0, 0,0, ResourceType.ORE, 10,10,PurchaseStatus.FailPlayerNotEnoughResource},
			         
			         {0, 10,100,100,10, 10, 0,100, ResourceType.ENERGY, 10,10,PurchaseStatus.Success},
			         {0, 9,90,90,9, 9, 0,90, ResourceType.ENERGY, 10,9,PurchaseStatus.Success},
			         {1, 1,90,1,1, 1, 0,1, ResourceType.ENERGY, 1,1,PurchaseStatus.Success},
			         {0, 10,100,100,10, 10, 0,100, ResourceType.ENERGY, 10,10,PurchaseStatus.Success},
			         {100, 10,100,100,10, 10, 0,100, ResourceType.ENERGY, 10,10,PurchaseStatus.Success},
			         {100, 10,100,100,100, 10, 0,100, ResourceType.ENERGY, 10,10,PurchaseStatus.Success},
			         {0, 0,99,0,10, 0, 0,0, ResourceType.ENERGY, 10,10,PurchaseStatus.FailPlayerNotEnoughMoney},
			         {0, 0,990,0,100, 0, 0,0, ResourceType.ENERGY, 100,10,PurchaseStatus.FailPlayerNotEnoughMoney},
			         {0, 0,990,0,1000, 0, 0,0, ResourceType.ENERGY, 1,1000,PurchaseStatus.FailPlayerNotEnoughMoney},
			         {0, 0,99,0,10, 0, 0,0, ResourceType.ENERGY, 1,100,PurchaseStatus.FailPlayerNotEnoughResource},
			         {0, 0,99,0,10, 0, 0,0, ResourceType.ENERGY, 100,100,PurchaseStatus.FailPlayerNotEnoughResource},
			         {0,0,0,0,0,0,0,0,ResourceType.ENERGY,10,10,PurchaseStatus.FailPlayerNotEnoughResource},
			         {0, 0,100,0,9, 0, 0,0, ResourceType.ENERGY, 10,10,PurchaseStatus.FailPlayerNotEnoughResource},
			         
			         {0, 10,100,100,10, 10, 0,100, ResourceType.FOOD, 10,10,PurchaseStatus.Success},
			         {0, 9,90,90,9, 9, 0,90, ResourceType.FOOD, 10,9,PurchaseStatus.Success},
			         {1, 1,90,1,1, 1, 0,1, ResourceType.FOOD, 1,1,PurchaseStatus.Success},
			         {0, 10,100,100,10, 10, 0,100, ResourceType.FOOD, 10,10,PurchaseStatus.Success},
			         {100, 10,100,100,10, 10, 0,100, ResourceType.FOOD, 10,10,PurchaseStatus.Success},
			         {100, 10,100,100,100, 10, 0,100, ResourceType.FOOD, 10,10,PurchaseStatus.Success},
			         {0, 0,99,0,10, 0, 0,0, ResourceType.FOOD, 10,10,PurchaseStatus.FailPlayerNotEnoughMoney},
			         {0, 0,990,0,100, 0, 0,0, ResourceType.FOOD, 100,10,PurchaseStatus.FailPlayerNotEnoughMoney},
			         {0, 0,990,0,1000, 0, 0,0, ResourceType.FOOD, 1,1000,PurchaseStatus.FailPlayerNotEnoughMoney},
			         {0, 0,99,0,10, 0, 0,0, ResourceType.FOOD, 1,100,PurchaseStatus.FailPlayerNotEnoughResource},
			         {0, 0,99,0,10, 0, 0,0, ResourceType.FOOD, 100,100,PurchaseStatus.FailPlayerNotEnoughResource},
			         {0,0,0,0,0,0,0,0,ResourceType.FOOD,10,10,PurchaseStatus.FailPlayerNotEnoughResource},
			         {0, 0,100,0,9, 0, 0,0, ResourceType.FOOD, 10,10,PurchaseStatus.FailPlayerNotEnoughResource},
			      });
			}
			/**
			 * Runs before every test and creates the necessary objects
			 */
			@Before
			public void setup(){
				buyingPlayer = new Player(null);
				buyingPlayer.setOre(buyingPlayerOreBefore);
				buyingPlayer.setEnergy(buyingPlayerEnergyBefore);
				buyingPlayer.setFood(buyingPlayerFoodBefore);
				buyingPlayer.setMoney(buyingPlayerMoneyBefore);
				sellingPlayer = new Player(null);
				sellingPlayer.setOre(sellingPlayerOreBefore);
				sellingPlayer.setEnergy(sellingPlayerEnergyBefore);
				sellingPlayer.setFood(sellingPlayerFoodBefore);
				sellingPlayer.setMoney(sellingPlayerMoneyBefore);
				
				
			}
			
			/**
			 * Tests {@link Player#sellResourceToPlayer(Player, int, ResourceType, int)} ensures that it
			 * returns the correct value
			 */
			@Test
			public void testCorrectReturnValue(){
				assertEquals(expectedReturnValue,sellingPlayer.sellResourceToPlayer(buyingPlayer, quantity, resource, pricePerUnit));
			}
			
			/**
			 * Tests {@link Player#sellResourceToPlayer(Player, int, ResourceType, int)} ensures the buying
			 * player is left with the correct amount of energy
			 */
			@Test
			public void testCorrectEnergyBuyingPlayer(){
				sellingPlayer.sellResourceToPlayer(buyingPlayer, quantity, resource, pricePerUnit);
				assertEquals(buyingPlayerEnergyBefore+buyingPlayerExpectedEnergyChange,buyingPlayer.getEnergy());
			}
			
			/**
			 * Tests {@link Player#sellResourceToPlayer(Player, int, ResourceType, int)} ensures the buying
			 * player is left with the correct amount of ore
			 */
			@Test
			public void testCorrectOreBuyingPlayer(){
				sellingPlayer.sellResourceToPlayer(buyingPlayer, quantity, resource, pricePerUnit);
				assertEquals(buyingPlayerOreBefore+buyingPlayerExpectedOreChange,buyingPlayer.getOre());
			}
			
			/**
			 * Tests {@link Player#sellResourceToPlayer(Player, int, ResourceType, int)} ensures the buying
			 * player is left with the correct amount of ore
			 */
			@Test
			public void testCorrectFoodBuyingPlayer(){
				sellingPlayer.sellResourceToPlayer(buyingPlayer, quantity, resource, pricePerUnit);
				assertEquals(buyingPlayerFoodBefore+buyingPlayerExpectedFoodChange,buyingPlayer.getFood());
			}
			
			/**
			 * Tests {@link Player#sellResourceToPlayer(Player, int, ResourceType, int)} ensures the buying
			 * player is left with the correct amount of money
			 */
			@Test
			public void testCorrectMoneyBuyingPlayer(){
				sellingPlayer.sellResourceToPlayer(buyingPlayer, quantity, resource, pricePerUnit);
				assertEquals(buyingPlayerMoneyBefore-buyingPlayerExpectedMoneyChange,buyingPlayer.getMoney());
			}
			
			/**
			 * Tests {@link Player#sellResourceToPlayer(Player, int, ResourceType, int)} ensures the selling
			 * player is left with the correct amount of energy
			 */
			@Test
			public void testCorrectEnergySellingPlayer(){
				sellingPlayer.sellResourceToPlayer(buyingPlayer, quantity, resource, pricePerUnit);
				assertEquals(sellingPlayerEnergyBefore-sellingPlayerExpectedEnergyChange,sellingPlayer.getEnergy());
			}
			
			/**
			 * Tests {@link Player#sellResourceToPlayer(Player, int, ResourceType, int)} ensures the selling
			 * player is left with the correct amount of ore
			 */
			@Test
			public void testCorrectOreSellingPlayer(){
				sellingPlayer.sellResourceToPlayer(buyingPlayer, quantity, resource, pricePerUnit);
				assertEquals(sellingPlayerOreBefore-sellingPlayerExpectedOreChange,sellingPlayer.getOre());
			}
			
			/**
			 * Tests {@link Player#sellResourceToPlayer(Player, int, ResourceType, int)} ensures the selling
			 * player is left with the correct amount of ore
			 */
			@Test
			public void testCorrectFoodSellingPlayer(){
				sellingPlayer.sellResourceToPlayer(buyingPlayer, quantity, resource, pricePerUnit);
				assertEquals(sellingPlayerFoodBefore-sellingPlayerExpectedFoodChange,sellingPlayer.getFood());
			}
			
			/**
			 * Tests {@link Player#sellResourceToPlayer(Player, int, ResourceType, int)} ensures the selling
			 * player is left with the correct amount of money
			 */
			@Test
			public void testCorrectMoneySellingPlayer(){
				
				sellingPlayer.sellResourceToPlayer(buyingPlayer, quantity, resource, pricePerUnit);
				assertEquals(sellingPlayerMoneyBefore+sellingPlayerExpectedMoneyChange,sellingPlayer.getMoney());
			}
			
		}
		
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
			@Mocked private Market market;
			@Mocked RoboticonQuest game;
			
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
				 int oreValue = 10;
				 int energyValue = 11;
				 int foodValue = 12;
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
				game = new RoboticonQuest();
				market = new Market();
				game.market=market;
				player = new Player(game);
				player.setMoney(moneyQuantity);
				player.setEnergy(energyQuantity);
				player.setOre(oreQuantity);
				player.setFood(foodQuantity);	
				new Expectations(){{
					market.getSellPrice(ResourceType.ENERGY);result=11;
					market.getSellPrice(ResourceType.ORE);result=10;
					market.getSellPrice(ResourceType.FOOD);result=12;
				}};
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
		
		
		// Note: an integration test version of this test does not exist due to complications with LibGDX and the RoboticonQuest class
		/**
		 * Tests {@link Player#generateResources()} using  player objects with various different plots with different attributes
		 * and ensures that the correct resources are added to the players inventory
		 * @author jcn509
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
			@Mocked private LandPlot plot1,plot2,plot3;
			
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
				
				player = new Player(game);
				plot1 = new LandPlot(plot1OreValue,plot1EnergyValue,plot1FoodValue);
				plot2 = new LandPlot(plot2OreValue,plot2EnergyValue,plot2FoodValue);
				plot3 = new LandPlot(plot3OreValue,plot3EnergyValue,plot3FoodValue);
				
				
				final ArrayList<LandPlot> plots = new ArrayList<LandPlot>();
				plots.add(plot1);
				plots.add(plot2);
				plots.add(plot3);

				player.landList=plots;
				Array<ResourceType> customisations = new Array<ResourceType>();
				customisations.add(plot1RoboticonCustomisation);
				customisations.add(plot2RoboticonCustomisation);
				customisations.add(plot3RoboticonCustomisation);
				
				final int plotProductionValues[][] = {{plot1EnergyValue,plot1OreValue,plot1FoodValue},
												{plot2EnergyValue,plot2OreValue,plot2FoodValue},
												{plot3EnergyValue,plot3OreValue,plot3FoodValue}};
				
				for(int plot=0;plot<plots.size();plot++){
					final int p = plot;
					if(customisations.get(plot) == ResourceType.ENERGY){
						new Expectations(){{
							plots.get(p).produceResource(ResourceType.ENERGY);result=plotProductionValues[p][0];
						}};
					}
					else{
						new Expectations(){{
							plots.get(p).produceResource(ResourceType.ENERGY);result=0;
						}};
					}
					
					if(customisations.get(plot) == ResourceType.ORE){
						new Expectations(){{
							plots.get(p).produceResource(ResourceType.ORE);result=plotProductionValues[p][1];
						}};
					}
					else{
						new Expectations(){{
							plots.get(p).produceResource(ResourceType.ORE);result=0;
						}};
					}
					
					if(customisations.get(plot) == ResourceType.FOOD){
						new Expectations(){{
							plots.get(p).produceResource(ResourceType.FOOD);result=plotProductionValues[p][2];
						}};
					}
					else{
						new Expectations(){{
							plots.get(p).produceResource(ResourceType.FOOD);result=0;
						}};
					}
				}
				
				player.addLandPlot(plot1);
				player.addLandPlot(plot2);
				player.addLandPlot(plot3);
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
}