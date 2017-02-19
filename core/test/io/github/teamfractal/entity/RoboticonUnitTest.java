package io.github.teamfractal.entity;

import static org.junit.Assert.*;

import org.junit.*;

import io.github.teamfractal.entity.enums.ResourceType;
import mockit.Mocked;
// All JavaDocs added by Josh

/**
 * Test case for {@link Roboticon}
 * @author jcn509
 */
public class RoboticonUnitTest {
	private Roboticon roboticon;
	@Mocked private LandPlot plot;
	
	/**
	 * Runs before every test and creates the required roboticon object
	 */
	@Before
	public void setup() {
		roboticon = new Roboticon(1);
		plot = new LandPlot(0, 0, 0);
	}
	
	/**
	 * Ensures that when a new roboticon object is created it is not installed on any land plot and
	 * it is not customised
	 */
	@Test
	public void initialisationTest(){
		assertEquals(roboticon.getCustomisation(), ResourceType.Unknown);
		assertFalse(roboticon.isInstalled());
	}
	
	/**
	 * Tests {@link Roboticon#setCustomisation(ResourceType)} and ensures that the customisation is set correctly
	 * (this is a very simple setter method and therefore only needs one test)
	 */
	@Test
	public void customisationTest(){
		roboticon.setCustomisation(ResourceType.ORE);
		assertEquals(roboticon.getCustomisation(), ResourceType.ORE);
	}
	
	
	// Tests below this comment added by Josh
	
	/**
	 * Tests {@link Roboticon#setInstalledLandplot(LandPlot)} and ensures that it returns true
	 * when the roboticon has not yet been installed on any land plot
	 */
	@Test
	public void testSetInstalledLandPlotReturnTrue(){
		assertTrue(roboticon.setInstalledLandplot(plot));
	}
	
	/**
	 * Tests {@link Roboticon#setInstalledLandplot(LandPlot)} and ensures that it returns false
	 * when the roboticon has been installed on the land plot already
	 */
	@Test
	public void testSetInstalledLandPlotReturnFalseAlreadyInstalledOnSameLandPlot(){
		roboticon.setInstalledLandplot(plot); // Already installed on plot
		
		assertFalse(roboticon.setInstalledLandplot(plot));
	}
	
	/**
	 * Tests {@link Roboticon#setInstalledLandplot(LandPlot)} and ensures that it returns false
	 * when the roboticon has been installed on another land plot already
	 */
	@Test
	public void testSetInstalledLandPlotReturnFalseAlreadyInstalledDifferentLandPlot(){
		roboticon.setInstalledLandplot(new LandPlot(0,0,0)); // Already installed on a plot
		
		assertFalse(roboticon.setInstalledLandplot(plot));
	}
	
	/**
	 * Tests {@link Roboticon#setInstalledLandplot(LandPlot)} and ensures that afterwards the roboticon registers
	 * as having been installed on a land plot when the roboticon has not yet been installed on any land plot
	 */
	@Test
	public void testSetInstalledLandPlotInstalled(){
		roboticon.setInstalledLandplot(plot);
		assertTrue(roboticon.isInstalled());
	}

	

}
