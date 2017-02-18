package io.github.teamfractal.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import io.github.teamfractal.RoboticonQuest;
import io.github.teamfractal.entity.Market;
import io.github.teamfractal.entity.Player;
import io.github.teamfractal.entity.enums.PurchaseStatus;
import io.github.teamfractal.entity.enums.ResourceType;
import io.github.teamfractal.screens.ResourceMarketScreen;
import io.github.teamfractal.util.SoundEffects;
import io.github.teamfractal.util.MessagePopUp;
import io.github.teamfractal.util.StringUtil;


// Heavily modified by Josh Neil as before it only allowed the current player to buy and sell resources
/**
 * Creates all of the widgets that the players can use to buy and sell resources to/from each other and the market.
 * A highly modified version of the original class provided by Team Fractal.
 * @author jcn509
 */
public class ResourceMarketActors extends Table {

	private RoboticonQuest game;
	private Label phaseInfo;
	private ResourceMarketScreen screen;
	private TextButton nextButton;
	private Label marketStats;
	private SelectBox<String> playerToPlayerResourceDropDown;
	private SelectBox<String> playerToPlayerSellerDropDown;
	private SelectBox<String> playerToPlayerBuyerDropDown;
	private AdjustableActor playerToPlayerPriceAdjustableActor;
	private AdjustableActor playerToPlayerQuantityAdjustableActor;
	
	private SelectBox<String> marketResourceDropDown;
	private SelectBox<String> marketPlayerDropDown;
	private AdjustableActor marketQuantityAdjustableActor;
	private SelectBox<String> marketBuyOrSellDropDown;
	private Label[] playerStatsLabels;
	private final Stage stage;
	
	private Table marketTransactionWidget, playerToPlayerTransactionWidget;

<<<<<<< HEAD
	private TextButton playerToPlayerTransactionButton, marketTransactionButton;

	private Image backgroundImage;
	private SpriteBatch batch;
	private float scaleFactorX;
	private float scaleFactorY;
=======
	private Texture backgroundImage;
	private SpriteBatch batch;
>>>>>>> josh
	
	/**
	 * Initialise market actors.
	 * @param game       The game object.
	 * @param screen     The screen object.
	 */
	public ResourceMarketActors(final RoboticonQuest game, ResourceMarketScreen screen) {
		center();
		Skin skin = game.skin;
		this.game = game;
		this.screen = screen;
		this.stage = screen.getStage();
    
    	//Added by Christian Beddows
		batch = (SpriteBatch) game.getBatch();
		backgroundImage = new Image(new Texture(Gdx.files.internal("background/nightfacility.jpg")));
		
		// Modified by Josh Neil
		createPlayerSelectDropDowns();
		createPlayerToPlayerPriceAdjustableActor();
		createResourceSelectBoxes();
		createQuantityAdjustableActors();
		createPlayerStatsLabels();
		createBuyOrSellDropDown();
		
		// Not modified
		// Create UI Components
		phaseInfo = new Label("", game.skin);
		nextButton = new TextButton("Next ->", game.skin);
		marketStats = new Label("", game.skin);
		// Adjust properties.
		phaseInfo.setAlignment(Align.right);

		// Add UI components to screen.
		stage.addActor(phaseInfo);
		stage.addActor(nextButton);
		
		// Added by Josh
		marketTransactionWidget = createMarketTransactionWidget();
		playerToPlayerTransactionWidget = createPlayerToPlayerTransactionWidget();
		
		addAllWidgetsToScreen();
		
		bindEvents();
		widgetUpdate();
	}
	
	/**
	 * Creates the drop down menus that are used by players to select the resource that they want to buy/sell
	 */
	private void createResourceSelectBoxes(){
		playerToPlayerResourceDropDown = new SelectBox<String>(game.skin);
		marketResourceDropDown = new SelectBox<String>(game.skin);
		String[] resources = {"Food","Energy","Ore"};
		playerToPlayerResourceDropDown.setItems(resources);
		marketResourceDropDown.setItems(resources);
	}
	
	/**
	 * Called whenever the sell button is clicked to carry out a sale from one player to another
	 * <p>
	 * If the sale is successful then both of the player's inventory data will be updated on screen.
	 * If it is not successful then then a MessagePopUp is deployed that lets the players know why it failed.
	 * </p>
	 */
	private void completePlayerToPlayerTransaction(){
		int buyingPlayerIndex = playerToPlayerBuyerDropDown.getSelectedIndex();
		int sellingPlayerIndex = playerToPlayerSellerDropDown.getSelectedIndex();
		
		
		
		Player buyingPlayer = game.playerList.get(buyingPlayerIndex);
		Player sellingPlayer = game.playerList.get(sellingPlayerIndex);
		
		int quantity = playerToPlayerQuantityAdjustableActor.getValue();
		
		if(buyingPlayerIndex == sellingPlayerIndex || quantity == 0){
			return;  // Player selling to itself or selling 0 of anything is pointless
		}
		
		String resourceString = playerToPlayerResourceDropDown.getSelected().toLowerCase();
		ResourceType resource = StringUtil.stringToResource(resourceString);
		
		int pricePerUnit = playerToPlayerPriceAdjustableActor.getValue();
		
		PurchaseStatus purchaseStatus = sellingPlayer.sellResourceToPlayer(buyingPlayer, quantity, resource, pricePerUnit);
		
		switch(purchaseStatus){
			case Success:
				widgetUpdate();
				break;
			case FailPlayerNotEnoughMoney:
				stage.addActor(new MessagePopUp("Not enough money!",
						"Player "+Integer.toString(buyingPlayerIndex+1)+" does not have enough money to buy "
						+Integer.toString(quantity)+" "+resourceString+
						" for "+Integer.toString(quantity*pricePerUnit)+" credits!"));
				break;
			case FailPlayerNotEnoughResource:
				stage.addActor(new MessagePopUp("Not enough "+resourceString+"!",
						"Player "+Integer.toString(sellingPlayerIndex+1)+" has less than "
						+Integer.toString(quantity)+" "+resourceString));
				break;
			default:
				break;
		}
		
		
	}
	
	/**
	 * Called whenever the complete transaction button is clicked to carry out a transaction between the specified player and the market
	 * <p>
	 * If the sale is successful then the player's and market's inventory data will be updated on screen.
	 * If it is not successful then then a MessagePopUp is deployed that lets the player know why it failed.
	 * </p>
	 */
	private void completeMarketTransaction(){
		int playerIndex = marketPlayerDropDown.getSelectedIndex();
		int quantity = marketQuantityAdjustableActor.getValue();
		Player player = game.playerList.get(playerIndex);
		
		// Converted to lower case as we don't want it to be upper case when its used in the pop up messages
		String resourceString = marketResourceDropDown.getSelected().toLowerCase();
		
		ResourceType resource = StringUtil.stringToResource(resourceString);
		if(playerIndex == -1 || resource == null){
			return; // Not enough information has been supplied, cannot complete transaction
		}
		
		if(marketBuyOrSellDropDown.getSelectedIndex() == 0){ // Buying is the first option
			PurchaseStatus purchaseStatus = player.purchaseResourceFromMarket(quantity, game.market, resource);
			
			// Not possible, with the current implementation for the player to attempt to buy more of a given resource than
			// is available, left this code here in case that changes!
			if(purchaseStatus== PurchaseStatus.FailMarketNotEnoughResource){
				stage.addActor(new MessagePopUp("Not enough " + resourceString+"!",
						"The market does not have enough " + resourceString+"!"));
			}
			else if(purchaseStatus== PurchaseStatus.FailPlayerNotEnoughMoney){
				stage.addActor(new MessagePopUp("Not enough money!",
						"Player "+Integer.toString(playerIndex+1)+" does not have enough money to buy "+
						Integer.toString(quantity)+" "+resourceString+"!"));
			}
		}
		else{ // Selling
			
			// Players can only sell as much as they have so there is no need to do anything else
			player.sellResourceToMarket(quantity, game.market, resource);
		}
		widgetUpdate();
	}
	
	/**
	 * Creates the AdjustableActor that is used to specify the price players want to buy for each unit
	 * of a given resource.
	 */
	private void createPlayerToPlayerPriceAdjustableActor(){
		playerToPlayerPriceAdjustableActor = new AdjustableActor(game.skin , 1,1,50);		
	}
	
	/**
	 * Creates the drop down menus that can be used to select the player(s) involved in a transaction with another player
	 * or with the market
	 */
	private void createPlayerSelectDropDowns(){
		playerToPlayerSellerDropDown = new SelectBox<String>(game.skin);
		playerToPlayerBuyerDropDown = new SelectBox<String>(game.skin);
		marketPlayerDropDown = new SelectBox<String>(game.skin);
		String[] players = new String[game.playerList.size()];
		for(int player=0;player<game.playerList.size();player++){
			players[player] = "Player "+Integer.toString(player+1);
		}
		playerToPlayerSellerDropDown.setItems(players);
		playerToPlayerBuyerDropDown.setItems(players);
		marketPlayerDropDown.setItems(players);
	}
	
	/**
	 * Creates the drop down menu that is used to indicate whether a player wants to buy from the market or sell to it
	 */
	private void createBuyOrSellDropDown(){
		marketBuyOrSellDropDown = new SelectBox<String>(game.skin);
		String[] options = {"buy","sell"};
		marketBuyOrSellDropDown.setItems(options);
	}

	/**
	 * Creates the adjustable actors that are used to select the amount of a given resource that is to be traded
	 * either between players or between a player and the market
	 */
	private void createQuantityAdjustableActors(){
		playerToPlayerQuantityAdjustableActor = new AdjustableActor(game.skin,0,0,100);
		marketQuantityAdjustableActor = new AdjustableActor(game.skin,0,0,100);
	}
	
	/**
	 * Updates the player inventory data (form all players) that is displayed on screen
	 */
	private void updatePlayerStatsLabels(){
		for(int playerIndex =0;playerIndex<game.playerList.size();playerIndex++){
			Player player = game.playerList.get(playerIndex);
			String labelText = "Player "+Integer.toString(playerIndex+1)+
					"    Ore: "+player.getResource(ResourceType.ORE)+
					"   Energy: "+player.getResource(ResourceType.ENERGY)+
					"   Food: "+player.getResource(ResourceType.FOOD)+
					"   Money: "+player.getMoney();
			playerStatsLabels[playerIndex].setText(labelText);
		}
	}
	
	/**
	 * Creates the the labels that are used to display the players inventory data on screen
	 * (creates one for each player that is currently playing the game)
	 */
	private void createPlayerStatsLabels(){
		int numberOfPlayers = game.playerList.size();
		playerStatsLabels = new Label[numberOfPlayers];
		for(int player =0;player<numberOfPlayers;player++){
			playerStatsLabels[player] = new Label("",game.skin);
		}
	}
	
	/**
	 * Adds all of the player stats labels to the screen separated by a new row
	 * (note must be called after {@link ResourceMarketActors#createPlayerStatsLabels()} 
	 * and it will add as many labels as that method has created to the screen)
	 */
	private void addPlayerStatsLabels(){
		for(int player=0; player<playerStatsLabels.length;player++){
			add(playerStatsLabels[player]).left();
			row();
		}
	}
	
	/**
	 * Creates a table that contains all of the widgets that the player uses to 
	 * set up and carry out a transaction between themselves and another player.
	 * <p>
	 * All placed in a table so treated like 1 single widget by LibGDX
	 * </p>
	 * @return A table that contains all of the widgets that the player uses to set up and carry out a transaction between themselves and another player.
	 */
	private Table createPlayerToPlayerTransactionWidget(){
		Table container = new Table();
		container.add(playerToPlayerSellerDropDown).padRight(10);
		container.add(new Label("sell",game.skin)).padRight(10);
		container.add(playerToPlayerQuantityAdjustableActor).padRight(10);
		container.add(playerToPlayerResourceDropDown).padRight(10);
		container.add(new Label("to",game.skin)).padRight(10);
		container.add(playerToPlayerBuyerDropDown).padRight(10);
		container.add(new Label("for",game.skin)).padRight(10);
		container.add(playerToPlayerPriceAdjustableActor).padRight(10);
		container.add(new Label("money per unit",game.skin)).padRight(10);
		playerToPlayerTransactionButton = new TextButton(" Sell ", game.skin);
		container.add(playerToPlayerTransactionButton);
		return container;
	}
	
	/**
	 * Creates a table that contains all of the widgets that the player uses to 
	 * set up and carry out a transaction between themselves and the market.
	 * <p>
	 * All placed in a table so treated like 1 single widget by LibGDX
	 * </p>
	 * @return A table that contains all of the widgets that the player uses to set up and carry out a transaction between themselves and the market.
	 */
	private Table createMarketTransactionWidget(){
		// All placed in a table so treated like 1 single widget for a clean layout
		Table container = new Table();
		container.add(marketPlayerDropDown).padRight(10);
		container.add(marketBuyOrSellDropDown).padRight(10);
		container.add(marketQuantityAdjustableActor).padRight(10);
		container.add(marketResourceDropDown).padRight(10);
		container.add(new Label("to / from the market",game.skin)).padRight(10);
		marketTransactionButton = new TextButton(" Complete transaction ", game.skin);
		container.add(marketTransactionButton);
		return container;
	}
	
	/**
	 * Creates a table that is used to display the buying and selling prices of all the resources bought and sold by the market.
	 * @param market The market in question
	 * @return A table that is used to display the buying and selling prices of all the resources bought and sold by the market.
	 */
	private Table createMarketCostDisplayWidget(Market market){
		Table marketCostsTable = new Table();
		
		marketCostsTable.add(new Label("Market prices",game.skin)).colspan(3).left();
		marketCostsTable.row();
		
		marketCostsTable.add(new Label("Resource  ",game.skin)).left();
		marketCostsTable.add(new Label("We sell for  ",game.skin)).left();
		marketCostsTable.add(new Label("We buy for  ",game.skin)).left();
		marketCostsTable.row();
		
		String resourceStrings[] = {"Ore","Energy","Food"};
		ResourceType resourceTypes[] = {ResourceType.ORE,ResourceType.ENERGY,ResourceType.FOOD};
		for (int resource =0;resource<3;resource++) {
			Label buyCostLabel = new Label(Integer.toString(market.getSellPrice(resourceTypes[resource])),game.skin);
			Label sellCostLabel = new Label(Integer.toString(market.getBuyPrice(resourceTypes[resource])),game.skin);
			
			marketCostsTable.add(new Label(resourceStrings[resource],game.skin)).left();
			marketCostsTable.add(buyCostLabel).left();
			marketCostsTable.add(sellCostLabel).left();
			marketCostsTable.row();
		}	
		
		return marketCostsTable;
	}
	
	/**
	 * Once all the UI widgets have been created this method is used
	 * to add them all the the screen in the correct places
	 */
	private void addAllWidgetsToScreen(){
		addPlayerStatsLabels(); // Added by Josh
		row();
		add(marketStats).left().padBottom(20);
		row();
		add(createMarketCostDisplayWidget(game.market)).left().padBottom(45);
		row();
		add(playerToPlayerTransactionWidget).left().padBottom(30);
		row();
		add(marketTransactionWidget).left();
	}

	/**
	 * returns the background image
	 * @return Image
	 */
	public Image getBackgroundImage() {
		return backgroundImage;
	}
	
	/**
	 * Sets the maximum value that can be selected using the market quantity adjustable actor
	 * if the marketBuyOrSellDropDown is set to buy then the value is the number of 
	 * the resource selected using the marketResourceDropDown otherwise it is the number
	 * of the given resource that the selected player is in possession of.
	 */
	private void updateMaxMarketQuantity(){
		ResourceType resource = StringUtil.stringToResource(marketResourceDropDown.getSelected());
		if(marketBuyOrSellDropDown.getSelectedIndex() == 0){ // Buying is the first option
			
			marketQuantityAdjustableActor.setMax(game.market.getResource(resource));
		}
		else{
			int playerIndex = marketPlayerDropDown.getSelectedIndex();
			Player player = game.playerList.get(playerIndex);
			marketQuantityAdjustableActor.setMax(player.getResource(resource));			
		}
	}
	
	/**
	 * Sets the maximum value that can be selected using the playerToPlayerQuantityAdjustableActor
	 * to the number of the chosen resource that the selling player has in their possession
	 */
	private void updateMaxPlayerQuantity(){
		ResourceType resource = StringUtil.stringToResource(playerToPlayerResourceDropDown.getSelected());
		int playerIndex = playerToPlayerSellerDropDown.getSelectedIndex();
		Player player = game.playerList.get(playerIndex);
		playerToPlayerQuantityAdjustableActor.setMax(player.getResource(resource));
	}

	/**
	 * Sets the action that is performed when each of the on screen buttons is clicked
	 */
	private void bindEvents() {
		nextButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SoundEffects.click();
				game.nextPhase();
			}
		});
		
		marketTransactionWidget.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				SoundEffects.click();
				updateMaxMarketQuantity();
			}

		});
		
		playerToPlayerTransactionWidget.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				SoundEffects.click();
				updateMaxPlayerQuantity();
			}
		});
		
		playerToPlayerTransactionButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SoundEffects.click();
				completePlayerToPlayerTransaction();
			}
		});
		
		marketTransactionButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SoundEffects.click();
				completeMarketTransaction();
			}
		});
		
	}

	// Josh changed to private
	/**
	 * Updates all widgets on screen with new data
	 */
	private void widgetUpdate() {
		// update player stats, phase text, and the market stats.
		String phaseText =
				"Phase " + game.getPhase() + " - " + game.getPhaseString();

		String marketStatText = "Market      "+
				"Ore: " +    game.market.getResource(ResourceType.ORE   ) + "   " +
				"Energy: " + game.market.getResource(ResourceType.ENERGY) + "   " +
				"Food: " +   game.market.getResource(ResourceType.FOOD  );

		phaseInfo.setText(phaseText);
		updatePlayerStatsLabels();
		marketStats.setText(marketStatText);
		updateMaxMarketQuantity();
		updateMaxPlayerQuantity();
	}

	/**
	 * Respond to the screen resize event, updates widgets position
	 * accordingly.
	 * @param width    The new width.
	 * @param height   The new Height.
	 */
	public void screenResize(float width, float height) {
		//Added by Christian Beddows
		scaleFactorX = width/backgroundImage.getWidth();
		scaleFactorY = height/backgroundImage.getHeight();
		backgroundImage.setScale(scaleFactorX,scaleFactorY);

		// Bottom Left
		phaseInfo.setPosition(0, height - 20);
		phaseInfo.setWidth(width - 10);

		// Bottom Right
		nextButton.setPosition(width - nextButton.getWidth() - 10, 10);

		setWidth(width);
	}
}
