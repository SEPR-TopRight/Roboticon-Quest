package io.github.teamfractal.entity;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import io.github.teamfractal.entity.enums.ResourceType;
import io.github.teamfractal.exception.InvalidResourceTypeException;
import io.github.teamfractal.exception.NotCommonResourceException;
import io.github.teamfractal.util.PlotManager;

/**
 * Holds all data relating to a single plot including: who owns it, what kind
 * of roboticon it has on it, how much of each resource it produces
 */
public class LandPlot {
	private TiledMapTileLayer.Cell mapTile;
	private TiledMapTileLayer.Cell playerTile;
	private TiledMapTileLayer.Cell roboticonTile;
	private Player owner;
	int x, y;


	//<editor-fold desc="Class getters">
	/**
	 * @return returns the map tile that is displayed on screen for this plot
	 */
	public TiledMapTileLayer.Cell getMapTile() {
		return mapTile;
	}

	/**
	 * @return The tile that is displayed in the player overlay of the map for this plot
	 */
	public TiledMapTileLayer.Cell getPlayerTile() {
		return playerTile;
	}

	/**
	 * @return The tile that is displayed in the roboticon overlapt of the map for this plot
	 */
	public TiledMapTileLayer.Cell getRoboticonTile() {
		return roboticonTile;
	}

	/**
	 * 
	 * @return The player that owns this plot (if any)
	 */
	public Player getOwner() {
		return owner;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	/**
	 * Used to set the owner of this plot and return true if the specified player now owns the 
	 * plot and false if the plot already had an owner
	 * @param player The player who is looking to acquire the plot
	 * @return true if the specified player now owns the plot and false if the plot already had an owner
	 */
	public boolean setOwner(Player player) {
		if (hasOwner()) {
			return false;
		}

		owner = player;
		player.addLandPlot(this);
		return true;
	}

	/**
	 * @return True if the plot is owned by some player and false otherwise
	 */
	public boolean hasOwner() {
		return getOwner() != null;
	}

	/**
	 * Results in this plot no longer being owned by any player
	 */
	public void removeOwner() {
		if (!hasOwner())
			return ;

		owner.removeLandPlot(this);
		owner = null;
	}
	
	//</editor-fold>

	private final int IndexOre = 0;
	private final int IndexEnergy = 1;
	private final int IndexFood = 2;

	/**
	 * Saved modifiers for LandPlot.
	 * [ Ore, Energy, Food ]
	 */
	int[] productionModifiers = {0, 0, 0};

	/**
	 * The base production amounts.
	 * [ Ore, Energy, Food ]
	 */
	private int[] productionAmounts;
	private boolean owned;
	private Roboticon installedRoboticon;
	private boolean hasRoboticon;

	/**
	 * Initialise LandPlot with specific base amount of resources that it could produce.
	 * <p>
	 * Note: the plot will not produce this amount of each resource each turn
	 * it will only produce the type of resource that matches the customisation
	 * of the roboticon that is placed on it
	 * </p>
	 * @param ore     Amount of ore that this plot can produce per turn
	 * @param energy  Amount of energy that this plot can produce per turn
	 * @param food    Amount of food that this plot can produce per turn
	 */
	public LandPlot(int ore, int energy, int food) {
		this.productionAmounts = new int[]{ore, energy, food};
		this.owned = false;
	}

	/**
	 * Sets up the map tile and player and roboticon overlays that are displayed on screen for this tile
	 * @param plotManager
	 * @param x
	 * @param y
	 */
	public void setupTile (PlotManager plotManager, int x, int y) {
		this.x = x;
		this.y = y;
		this.mapTile = plotManager.getMapLayer().getCell(x, y);
		this.playerTile = plotManager.getPlayerOverlay().getCell(x, y);
		this.roboticonTile = plotManager.getRoboticonOverlay().getCell(x, y);
	}

	/**
	 * Get the type index from the {@link ResourceType}
	 * @param resource   The {@link ResourceType}
	 * @return           The index.
	 * @throws InvalidResourceTypeException Exception is thrown if the resource index is invalid.
	 */
	private int resourceTypeToIndex(ResourceType resource) {
		switch (resource) {
			case ORE:    return IndexOre;
			case FOOD:   return IndexFood;
			case ENERGY: return IndexEnergy;
		}

		throw new NotCommonResourceException(resource);
	}

	/**
	 * Install a roboticon to this LandPlot.
	 *
	 * @param roboticon    The roboticon to be installed.
	 */
	public synchronized boolean installRoboticon(Roboticon roboticon) {
		
		// Check if supplied roboticon is already installed.
		if (roboticon.isInstalled()) {
			return false;
		}
		
		if (roboticon.getCustomisation() != ResourceType.Unknown){
			
			int index = resourceTypeToIndex(roboticon.getCustomisation());
			if (roboticon.setInstalledLandplot(this)) {
				productionModifiers[index] += 1;
				
				this.installedRoboticon = roboticon;
				return true;
			}
		}
		else{
			if (roboticon.setInstalledLandplot(this)) {
				this.installedRoboticon = roboticon;
				return true;
			}
		}

		return false;
	}

	// Method removed by Josh Neil as it is not used and does the same thing as the method below
	/*
	/**
	 * Calculate the amount of resources to be produced.
	 *
	 * @return The amount of resources to be produced in an 2D array.
	 *
	public int[] produceResources() {
		int[] produced = new int[3];
		for (int i = 0; i < 2; i++) {
			produced[i] = productionAmounts[i] * productionModifiers[i];
		}
		return produced;
	}*/

	/**
	 * Calculate the amount of resources to be produced for specific resource.
	 * @param resource  The resource type to be calculated.
	 * @return          Calculated amount of resource to be generated.
	 */
	public int produceResource(ResourceType resource) {
		if (this.hasRoboticon){
			int resIndex = resourceTypeToIndex(resource);
			return productionAmounts[resIndex] * productionModifiers[resIndex];
		}
		else return 0;
		
	}

	/**
	 * Returns the amount of the given resource that this plot is capable of producing
	 * @param resource
	 * @return The amount of the given resource that this plot is capable of producing
	 */
	public int getResource(ResourceType resource) {
		int resIndex = resourceTypeToIndex(resource);
		return productionAmounts[resIndex];
	}
	
	public boolean hasRoboticon(){
		return this.hasRoboticon;
	}
	
	/**
	 * Used to indicate whether or not this plot has a roboticon installed on it
	 * @param roboticonInstalled
	 */
	public void setHasRoboticon(boolean roboticonInstalled){
		this.hasRoboticon = roboticonInstalled;
	}


}