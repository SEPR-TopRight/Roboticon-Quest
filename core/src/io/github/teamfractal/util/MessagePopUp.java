package io.github.teamfractal.util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;

//// Taken from our previous SEPR project 
//// Removed the getCloseButton() method as we aren't doing automated UI testing
//// So its not needed anymore

/**
 * A pop up window that can be used to display a message to the user
 * @author jcn509
 */
public class MessagePopUp extends Dialog{
	private TextButton closeButton;
	
	/**
	 * Constructor
	 * @param title The title of the window (displayed at the top of the window)
	 * @param message The message that is to be displayed at the top of the window
	 */
	public MessagePopUp(String title, String message){
		super(title,new Skin(Gdx.files.internal("uiskin.json")));
		addCloseButton();
		addMessageLabel(message);
		
		// Make the window exactly as large as it needs to be to hold its message
		setSize(getPrefWidth(),getPrefHeight());
		
		moveToMiddleOfScreen();
	}

	/**
	 * Adds the given message to the window
	 * @param message The message that is to be displayed in the window
	 */
	private void addMessageLabel(String message){
		Label messageLabel = new Label(message, new Skin(Gdx.files.internal("uiskin.json")));
		text(messageLabel); // Add the label to the window
	}
	
	/**
	 * Adds a close button to the window (destroys the window when its clicked)
	 */
	private void addCloseButton(){
		closeButton = new TextButton("Ok", new Skin(Gdx.files.internal("uiskin.json")));
		
		// Defines what happens when the close button is clicked
		closeButton.addListener(new ClickListener(){ 
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				remove(); // Destroy the window
			}
		});
		button(closeButton); // Add the close button to the window
	}

	/**
	 * Moves the window to the middle of the screen
	 * <p>
	 * Afterwards the centre of the window will be in the centre of the screen
	 * </p>
	 */
	private void moveToMiddleOfScreen(){
		
		// Width and height of the pop up window
		float windowWidth = getWidth();
		float windowHeight = getHeight();
		
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		
		// Move the window
		setX((screenWidth/2)-(windowWidth/2));
		setY((screenHeight/2)-(windowHeight/2));
	}

}