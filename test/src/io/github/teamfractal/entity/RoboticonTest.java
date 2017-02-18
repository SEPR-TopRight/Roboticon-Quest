package io.github.teamfractal.entity;

import static org.junit.Assert.*;

import org.junit.*;

import io.github.teamfractal.entity.enums.ResourceType;
// All JavaDocs added by Josh

/**
 * Integration tests for {@link Roboticon}
 * @author jcn509
 */
public class RoboticonTest {
	private Roboticon roboticon;
	
	/**
	 * Runs before every test and creates the required roboticon object
	 */
	@Before
	public void setup() {
		roboticon = new Roboticon(1);
	}
	
	/**
	 * Tests {@link LandPlot#installRoboticon(Roboticon)} and ensures that after a roboticon is
	 * installed on a given plot the roboticon in question is marked as having been installed
	 */
	@Test
	public void installationTest(){
		LandPlot plot = new LandPlot(0, 0, 0);
		plot.installRoboticon(roboticon);
		assertTrue(roboticon.isInstalled());
	}
	
	// Tests below this comment added by Josh
	
	/**
	 * Tests {@link Roboticon#setInstalledLandplot(LandPlot)} and ensures that it returns true
	 * when the roboticon has not yet been installed on any land plot
	 */
	@Test
	public void testSetInstalledLandPlotReturnTrue(){
		LandPlot plot = new LandPlot(0, 0, 0);
		assertTrue(roboticon.setInstalledLandplot(plot));
	}
	
	/**
	 * Tests {@link Roboticon#setInstalledLandplot(LandPlot)} and ensures that it returns false
	 * when the roboticon has been installed on the land plot already
	 */
	@Test
	public void testSetInstalledLandPlotReturnFalseAlreadyInstalledOnSameLandPlot(){
		LandPlot plot = new LandPlot(0, 0, 0);
		roboticon.setInstalledLandplot(plot); // Already installed on plot
		
		assertFalse(roboticon.setInstalledLandplot(plot));
	}
	
	/**
	 * Tests {@link Roboticon#setInstalledLandplot(LandPlot)} and ensures that it returns false
	 * when the roboticon has been installed on another land plot already
	 */
	@Test
	public void testSetInstalledLandPlotReturnFalseAlreadyInstalledDifferentLandPlot(){
		LandPlot plot = new LandPlot(0, 0, 0);
		roboticon.setInstalledLandplot(new LandPlot(0,0,0)); // Already installed on a plot
		
		assertFalse(roboticon.setInstalledLandplot(plot));
	}
	
	/**
	 * Tests {@link Roboticon#setInstalledLandplot(LandPlot)} and ensures that afterwards the roboticon registers
	 * as having been installed on a land plot when the roboticon has not yet been installed on any land plot
	 */
	@Test
	public void testSetInstalledLandPlotInstalled(){
		LandPlot plot = new LandPlot(0, 0, 0);
		roboticon.setInstalledLandplot(plot);
		assertTrue(roboticon.isInstalled());
	}
}
