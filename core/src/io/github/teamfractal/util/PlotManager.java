package io.github.teamfractal.util;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.entity.LandPlot;
import io.github.teamfractal.entity.Player;

/**
 * Manages LandPlot objects and map tiles that are displayed on screen
 *
 */
public class PlotManager {
	private LandPlot[][] plots;
	private TiledMapTileSets tiles;
	private TiledMapTileLayer mapLayer;
	private TiledMapTileLayer playerOverlay;
	private TiledMapTileLayer roboticonOverlay;
	private int width;
	private int height;
	private TiledMapTile cityTile;
	private TiledMapTile waterTile;
	private TiledMapTile forestTile;
	private TiledMapTile hillTile1;
	private TiledMapTile hillTile2;
	private TiledMapTile hillTile3;
	private TiledMapTile hillTile4;

	public PlotManager() {

	}

	/**
	 * Set up the plot manager.
	 * @param tiles    Tiles.
	 * @param layers   Layers.
	 */
	public void setup(TiledMapTileSets tiles, MapLayers layers) {
		this.tiles = tiles;
		this.mapLayer = (TiledMapTileLayer)layers.get("MapData");
		this.playerOverlay = (TiledMapTileLayer)layers.get("PlayerOverlay");
		this.roboticonOverlay = (TiledMapTileLayer)layers.get("RoboticonOverlay");

		this.cityTile = tiles.getTile(60);
		this.waterTile = tiles.getTile(9);
		this.forestTile = tiles.getTile(61);
		this.hillTile1 = tiles.getTile(4);
		this.hillTile2 = tiles.getTile(5);
		this.hillTile3 = tiles.getTile(6);
		this.hillTile4 = tiles.getTile(7);

		width = mapLayer.getWidth();
		height = mapLayer.getHeight();
		plots = new LandPlot[width][height];
	}

	/// Added by Josh Neil so that we can check whether all plots have been acquired
	/// (and can end the game if they have been)
	/**
	 * Returns true if all plots are owned by a player, otherwise returns false
	 * @return True if all plots are owned by a player, otherwise returns false
	 */ 
	public boolean allOwned(){
		for(int row=0;row<plots.length;row++){
			for(int column=0;column<plots[0].length;column++){
				
				// If the plot has not been created or not been acquired
				if(plots[row][column]== null || !plots[row][column].hasOwner()){
					return false; // At least one plot is unowned
				}
			}
		}
		return true; // It must be the case that all plots are owned
	}
	
	/**
	 * Get {@link LandPlot} at specific position.
	 * @param x   The x index.
	 * @param y   The y index.
	 * @return    The corresponding {@link LandPlot} object.
	 */
	public LandPlot getPlot(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return null;

		// Lazy load
		LandPlot p = plots[x][y];
		if (p == null) {
			p = createLandPlot(x, y);
		}

		return p;
	}
	/**
	 * Creates a landplot from a tile tile on the tiled map
	 * @param x - x coordinate on tiled map
	 * @param y - y coordinate on tiled map
	 * @return new Landplot with statistics determined by tile on tiled map
	 */
	private LandPlot createLandPlot(int x, int y) {
		int ore, energy, food;
		TiledMapTile tile = mapLayer.getCell(x, y).getTile();

		if (tile == cityTile){
			ore = 1;
			energy = 2;
			food = 3;
		}
		else if (tile == forestTile){
			ore = 2;
			energy = 3;
			food = 1;
		}
		else if (tile == waterTile){
			ore = 3;
			energy = 1;
			food = 2;
		}
		else if (tile == hillTile1 || tile == hillTile2 ||tile == hillTile3 || tile == hillTile4 ){
			ore = 3;
			energy = 2;
			food = 1;
		}
		else{
			ore = 2;
			energy = 2;
			food = 2;
		}


		LandPlot p = new LandPlot(ore, energy, food);
		p.setupTile(this, x, y);
		plots[x][y] = p;
		return p;
	}

	public TiledMapTileLayer getMapLayer() {
		return mapLayer;
	}

	public TiledMapTileLayer getPlayerOverlay() {
		return playerOverlay;
	}

	public TiledMapTileLayer getRoboticonOverlay() {
		return roboticonOverlay;
	}
}