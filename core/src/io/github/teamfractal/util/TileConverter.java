package io.github.teamfractal.util;


import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.entity.Player;
import io.github.teamfractal.entity.enums.ResourceType;
import io.github.teamfractal.entity.enums.RoboticonType;
import io.github.teamfractal.exception.NotCommonResourceException;
/**
 * Perfroms various operations relating to the map tiles (and player and roboticon overlays) that are
 * displayed on screen
 *
 */
public class TileConverter {
	private static TiledMapTileSets tiles;
	private static RoboticonQuest game;

	/**
	 * Initialisation
	 * @param tiles the set of tiles displayed on screen
	 * @param game The RoboticonQuest object bein used to play the game
	 */
	public static void setup(TiledMapTileSets tiles, RoboticonQuest game) {
		TileConverter.tiles = tiles;
		TileConverter.game = game;
	}

	/**
	 * Gets the tile that is displayed to show that a given plot belongs to the
	 * specified player
	 * @param player The player in question
	 * @return The tile that is displayed to show that a given plot belongs to the specified player
	 */
	public static TiledMapTile getPlayerTile(Player player) {
		int playerIndex = game.getPlayerIndex(player);
		return tiles.getTile(68 + playerIndex);
	}

	/**
	 * Returns the tile that is used to display a roboticon of a given customisation on the map
	 * @param resource
	 * @return the tile that is used to display a roboticon of a given customisation on the map
	 */
	public static TiledMapTile getRoboticonTile(RoboticonType resource) {
		int resourceIndex = 0;

		switch(resource) {
			case ENERGY:
				resourceIndex = 2;
				break;

			case ORE:
				resourceIndex = 3;
				break;
			
			case FOOD:
				resourceIndex = 4;
				break;

			case NO_CUST:
				resourceIndex = 1;
				break;

			case NONE:
				resourceIndex = 0;
				break;
		}

		return tiles.getTile(100 + resourceIndex);
	}

	/**
	 * Returns the tile that is used to display a roboticon of a given customisation on the map
	 * @param resource
	 * @return the tile that is used to display a roboticon of a given customisation on the map
	 */
	public static TiledMapTile getRoboticonTile(ResourceType resource) {
		System.out.println("WARN: getRoboticonTile(ResourceType) is old! Use getRoboticonTile(RoboticonType) instead.");

		RoboticonType rt = RoboticonType.NONE;

		switch(resource) {
			case ENERGY:
				rt = RoboticonType.ENERGY;
				break;

			case ORE:
				rt = RoboticonType.ORE;
				break;

			case FOOD:
				//added by ab
				rt = RoboticonType.FOOD;
				break;

			case ROBOTICON:
				rt = RoboticonType.NO_CUST;
				break;

			case CUSTOMISATION:
				break;

			case Unknown:
				rt = RoboticonType.NO_CUST;
				break;
		}

		return getRoboticonTile(rt);
	}
}
