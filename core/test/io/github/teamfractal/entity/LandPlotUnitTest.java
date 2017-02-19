package io.github.teamfractal.entity;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import io.github.teamfractal.entity.enums.ResourceType;
import mockit.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.experimental.runners.Enclosed;

// Created by Josh Neil
/**
 * Test case for {@link LandPlot}
 * @author jcn509
 *
 */
@RunWith(Enclosed.class)
public class LandPlotUnitTest {
	
	@RunWith(Parameterized.class)
	public static class LandPlotParamTests{
		
		private int plotOreProductionQuantity;
		private int plotEnergyProductionQuantity;
		private int plotFoodProductionQuantity;
		private int expectedAmountOfOre;
		private int expectedAmountOfEnergy;
		private int expectedAmountOfFood;
		private ResourceType roboticonCustomisation;
		private LandPlot plot;
		@Mocked private Roboticon roboticon;
		
		/**
		 * Parameterised test for {@link LandPlot#produceResource(ResourceType)}
		 * <p>
		 * Tests it using many different combinations of values and ensures that the correct result is produced.
		 * </p>
		 * @param oreProductionQuantity The amount of ore the plot should produce if it has an ore roboticon on it
		 * @param energyProductionQuantity The amount of energy the plot should produce if it has an energy roboticon on it
		 * @param foodProductionQuantity The amount of food the plot should produce if it has a food roboticon on it
		 * @param roboticonCustomisation The type of resource that the roboticon placed on the plot should produce
		 * @param expectedAmountOfOre The amount of ore that the plot is expected to produce (depends on the above parameters)
		 * @param expectedAmountOfEnergy The amount of energy that the plot is expected to produce (depends on the above parameters)
		 * @param expectedAmountOfFood The amount of food that the plot is expected to produce (depends on the above parameters)
		 */
		public LandPlotParamTests(int oreProductionQuantity,int energyProductionQuantity,int foodProductionQuantity,ResourceType roboticonCustomisation, int expectedAmountOfOre, int expectedAmountOfEnergy, int expectedAmountOfFood){
			this.plotOreProductionQuantity = oreProductionQuantity;
			this.plotEnergyProductionQuantity = energyProductionQuantity;
			this.plotFoodProductionQuantity = foodProductionQuantity;
			this.expectedAmountOfOre = expectedAmountOfOre;
			this.expectedAmountOfEnergy = expectedAmountOfEnergy;
			this.expectedAmountOfFood = expectedAmountOfFood;
			this.roboticonCustomisation = roboticonCustomisation;
		}
		
		@Parameterized.Parameters
		public static Collection resourceProductionValues(){
			 return Arrays.asList(new Object[][] {
		         {1,1,1,ResourceType.ORE,1,0,0},
		         {1,2,3,ResourceType.ORE,1,0,0},
		         {0,2,3,ResourceType.ORE,0,0,0},
		         {0,0,0,ResourceType.ORE,0,0,0},
		         {5,3,1,ResourceType.ORE,5,0,0},
		         {6,3,4,ResourceType.ORE,6,0,0},
		         
		         {1,1,1,ResourceType.ENERGY,0,1,0},
		         {1,2,3,ResourceType.ENERGY,0,2,0},
		         {0,2,3,ResourceType.ENERGY,0,2,0},
		         {0,0,0,ResourceType.ENERGY,0,0,0},
		         {5,3,1,ResourceType.ENERGY,0,3,0},
		         {6,3,4,ResourceType.ENERGY,0,3,0},
		         
		         {1,1,1,ResourceType.FOOD,0,0,1},
		         {1,2,3,ResourceType.FOOD,0,0,3},
		         {0,2,3,ResourceType.FOOD,0,0,3},
		         {0,0,0,ResourceType.FOOD,0,0,0},
		         {5,3,1,ResourceType.FOOD,0,0,1},
		         {6,3,4,ResourceType.FOOD,0,0,4}
		      });
		}
		/**
		 * Runs before every test. Sets the plot up with the correct parameters that describe how much of each resource
		 * it should produce and installs a roboticon of the given customisation type on it.
		 */
		@Before
		public void setup(){
			plot = new LandPlot(plotOreProductionQuantity,plotEnergyProductionQuantity,plotFoodProductionQuantity);
			roboticon = new Roboticon(5);
			
			new Expectations(){{
				roboticon.isInstalled();result=false;
				roboticon.getCustomisation();result = roboticonCustomisation;
				roboticon.getCustomisation();result = roboticonCustomisation; // called twice
				roboticon.setInstalledLandplot(plot);result=true;
			}};
			plot.installRoboticon(roboticon);
			
			
			plot.setHasRoboticon(true); // Design decision made by team fractal means that we have to set this expliclity...
		}
	
		/**
		 * Tests that the correct amount of ore is produced by a given plot with a given roboticon on it
		 */
		@Test
		public void testProduceResourcesOre(){
			
			assertEquals(expectedAmountOfOre,plot.produceResource(ResourceType.ORE));
		}
		
		/**
		 * Tests that the correct amount of energy is produced by a given plot with a given roboticon on it
		 */
		@Test
		public void testProduceResourcesEnergy(){
			
			assertEquals(expectedAmountOfEnergy,plot.produceResource(ResourceType.ENERGY));
		}
		
		/**
		 * Tests that the correct amount of food is produced by a given plot with a given roboticon on it
		 */
		@Test
		public void testProduceResourcesFood(){
			
			assertEquals(expectedAmountOfFood,plot.produceResource(ResourceType.FOOD));
		}
	
	}
	
	// Josh Neil - moved all of the old tests and any of our new non-parameterised tests into this class
	/**
	 * Non-parameterised tests for {@link LandPlot}
	 * @author jcn509
	 *
	 */
	public static class LandPlotSingleTests{
		private LandPlot plot;
		@Mocked private Roboticon roboticon;
		@Mocked private Roboticon roboticon2;
		@Mocked private Roboticon roboticon3;
		@Mocked private Roboticon roboticon4;
		@Mocked private Player player;
		/**
		 * Runs before every test. Creates the Land plot object that is under test
		 */
		@Before
		public void setup() {
			plot = new LandPlot(3, 0, 0);
			roboticon = new Roboticon(0);
			roboticon2 = new Roboticon(0);
			roboticon3 = new Roboticon(0);
			roboticon4 = new Roboticon(0);
		}
		
		/**
		 * Tests {@link LandPlot#installRoboticon(Roboticon)} ensures that the plot is set up to produce
		 * the correct resources (and that the roboticon is actually installed)
		 * @throws Exception
		 */
		@Test
		public void testInstallRobiticon() throws Exception {
			
	
			new Expectations(){{
				roboticon.isInstalled();result=false;
				roboticon.getCustomisation();result=ResourceType.ORE;
				// Called twice
				roboticon.getCustomisation();result=ResourceType.ORE;
				roboticon.setInstalledLandplot(plot);result=true;
				
				roboticon2.isInstalled();result=false;
				roboticon2.getCustomisation();result=ResourceType.ENERGY;
				// Called twice
				roboticon2.getCustomisation();result=ResourceType.ENERGY;
				roboticon2.setInstalledLandplot(plot);result=true;
				
				roboticon3.isInstalled();result=false;
				roboticon3.getCustomisation();result=ResourceType.ORE;
				// Called twice
				roboticon3.getCustomisation();result=ResourceType.ORE;
				roboticon3.setInstalledLandplot(plot);result=true;
				
				roboticon4.isInstalled();result=false;
				roboticon4.getCustomisation();result=ResourceType.FOOD;
				// Called twice
				roboticon4.getCustomisation();result=ResourceType.FOOD;
				roboticon4.setInstalledLandplot(plot);result=true;
					
			}};
			assertTrue(plot.installRoboticon(roboticon));
			assertArrayEquals(new int[] {1, 0, 0}, plot.productionModifiers);
	
			
			assertTrue(plot.installRoboticon(roboticon2));
			assertArrayEquals(new int[] {1, 1, 0}, plot.productionModifiers);
	
		
			assertTrue(plot.installRoboticon(roboticon3));
			assertArrayEquals(new int[] {2, 1, 0}, plot.productionModifiers);
	
			
			assertTrue(plot.installRoboticon(roboticon4));
			assertArrayEquals(new int[] {2, 1, 1}, plot.productionModifiers);
		}
		
		/**
		 * Tests {@link LandPlot#installRoboticon} ensures that a roboticon cannot be
		 * installed on a plot that already has one (i.e. that the installed roboticon
		 * is not replaced)
		 */
		@Test
		public void landPlotShouldNotReinstallRoboticon () {
	
			new Expectations(){{
				roboticon.isInstalled();result=false;
				
				roboticon.getCustomisation();result=ResourceType.ORE;
				
				// Called twice
				roboticon.getCustomisation();result=ResourceType.ORE;
				
				roboticon.setInstalledLandplot(plot);result=true;
			}};
			assertTrue(plot.installRoboticon(roboticon));
			assertArrayEquals(new int[] {1, 0, 0}, plot.productionModifiers);
			
			new Expectations(){{
				roboticon.isInstalled();result=true;
			}};
			assertFalse(plot.installRoboticon(roboticon));
			assertArrayEquals(new int[] {1, 0, 0}, plot.productionModifiers);
		}
		
		// Test removed By Josh as method removed
		/*@Test
		public void testProduceResources() throws Exception {
			Roboticon roboticon = new Roboticon(0);
			roboticon.setCustomisation(ResourceType.ORE);
			plot.installRoboticon(roboticon);
			assertArrayEquals(new int[] {3, 0, 0}, plot.produceResources());
			Roboticon roboticon2 = new Roboticon(0);
			roboticon2.setCustomisation(ResourceType.ORE);
			plot.installRoboticon(roboticon2);
			assertArrayEquals(new int[] {6, 0, 0}, plot.produceResources());
		}*/
	
		/// Tests below this comment added by Josh
		
		/**
		 * Ensures that newly created LandPlots don't have an owner
		 */
		@Test
		public void initiallyNoOwner(){
			assertFalse(plot.hasOwner());
		}
		
		/**
		 * Tests {@link LandPlot#setOwner(Player)} ensures that it is possible to 
		 * set the owner of an unowned plot
		 */
		@Test
		public void testSetOwnerNoOwner(){
			player = new Player(null);
			assertTrue(plot.setOwner(player));
		}
		
		/**
		 * Tests {@link LandPlot#setOwner(Player)} ensures that the owner is set correctly
		 */
		@Test
		public void testSetOwnerCorrectOwner(){
			player = new Player(null);
			plot.setOwner(player);
			assertEquals(player,plot.getOwner());
		}
		
		/**
		 * Tests {@link LandPlot#setOwner(Player)} ensures that it is not possible to 
		 * set the owner of a plot that is already owned by some player (ensures that the method returns false)
		 */
		@Test
		public void testSetOwnerAlreadyHasOwner(){
			player = new Player(null);
			plot.setOwner(player);
			assertFalse(plot.setOwner(new Player(null)));
		}
		
		/**
		 * Tests {@link LandPlot#setOwner(Player)} ensures that it is not possible to 
		 * set the owner of a plot that is already owned by some player (checks that the owner is not overwritten)
		 */
		@Test
		public void testSetOwnerAlreadyHasOwnerUnchanged(){
			player = new Player(null);
			plot.setOwner(player);
			assertEquals(player,plot.getOwner()); // Owner not changed to the new one
		}
		
		/**
		 * Tests {@link LandPlot#removeOwner()} ensures that the plot no longer has an owner after it is called
		 */
		@Test
		public void testRemoveOwner(){
			player = new Player(null);
			plot.setOwner(player);
			plot.removeOwner();
			assertFalse(plot.hasOwner()); // No longer owned!
		}
		
		/**
		 * Ensures that LandPlots do not initially have a roboticon on them
		 */
		@Test
		public void testInitiallyNoRoboticon(){
			assertFalse(plot.hasRoboticon());
		}
		
		/**
		 * Tests {@link LandPlot#installRoboticon(Roboticon)} ensures that a roboticon is actually placed on the plot
		 */
		@Test
		public void testInstallRoboticon(){
			new Expectations(){{
				roboticon.isInstalled();result=false;
				
				roboticon.getCustomisation();result=ResourceType.ENERGY;
				
				// Called twice
				roboticon.getCustomisation();result=ResourceType.ENERGY;
				
				roboticon.setInstalledLandplot(plot);result=true;
			}};
			assertTrue(plot.installRoboticon(roboticon));
		}
		
		/**
		 * Tests {@link LandPlot#installRoboticon(Roboticon)} ensures that it is not possible to install a roboticon on a plot that already has one
		 */
		@Test
		public void testInstallRoboticonAlreadyHasOneReturnFalse(){
			new Expectations(){{
				roboticon.isInstalled();result=false;
				roboticon.getCustomisation();result=ResourceType.ENERGY;
				
				// Called twice
				roboticon.getCustomisation();result=ResourceType.ENERGY;
				roboticon.setInstalledLandplot(plot);result=true;
				
				roboticon2.isInstalled();result=true;
			}};
			plot.installRoboticon(roboticon);
			assertFalse(plot.installRoboticon(roboticon2));
		}
		
		
	}	

}