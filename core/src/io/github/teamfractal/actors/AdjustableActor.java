package io.github.teamfractal.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

/**
 * A UI widget that works similarly to spinbox that allows users to enter
 * a numerical value using their mouse to click buttons to increase and decrease said value
 *
 */
public class AdjustableActor extends Table {
	//<editor-fold desc="UI Components">
	private final TextButton subButton;
	private final TextButton addButton;
	private final TextButton actButton;
	private final Label valueLabel;
	private final Label titleLabel;
	//</editor-fold>

	//<editor-fold desc="Getter / Setter">
	private int value = 0;
	private int min   = 0;
	private int max   = 9;
	private ChangeListener actionEvent;

	/**
	 * Get current title string.
	 * @return     The title text.
	 */
	public String getTitle() {
		return titleLabel.getText().toString();
	}

	/**
	 * Set a new title string.
	 * @param title  The new Title.
	 */
	public void setTitle(String title) {
		titleLabel.setText(title);
	}

	/**
	 * Get current adjusted value
	 * @return     Current value.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Set a new adjusted value.
	 * @param value       The new value.
	 */
	public void setValue(int value) {
		this.value = value;
		ensureValueInRange();
	}

	/**
	 * Get current minimum value.
	 * @return     Currently set minimum value.
	 */
	public int getMin() {
		return min;
	}

	/**
	 * Set a new minimum value.
	 * @param min       The new minimum value.
	 */
	public void setMin(int min) {
		this.min = min;
		ensureValueInRange();
	}

	/**
	 * Get current maximum value.
	 * @return     Currently set maximum value.
	 */
	public int getMax() {
		return max;
	}

	/**
	 * Set a new maximum value.
	 * @param max       The new maximum value.
	 */
	public void setMax(int max) {
		this.max = max;
		ensureValueInRange();
	}

	/**
	 * Set the new action button event.
	 * @param actionEvent        The new action button handle event.
	 */
	public void setActionEvent(ChangeListener actionEvent) {
		this.actionEvent = actionEvent;
	}
	//</editor-fold>

	/**
	 * Ensure that the value is in the range of min and max.
	 */
	private void ensureValueInRange() {
		if (min > max) {
			// Swap min and max range.
			int temp_min = min;
			min = max;
			max = temp_min;
		}

		if (value < min) {
			value = min;
		} else if (value > max) {
			value = max;
		}

		updateValueDisplay();
	}

	
	// Added by Josh Neil so that we can not display the label and button
	/**
	 * The adjustable actor
	 * For an easy way of adjust values in a step of 1 / -1.
	 *
	 * @param skin    The skin file for the UI.
	 * @param title   The adjustable title.
	 * @param action  The action button text.
	 * @param labelAndButton specifies whether or not the user wishes to have the title label and action button displayed on screen
	 */
	public AdjustableActor(Skin skin, String title, String action, boolean labelAndButton) {
		subButton = new TextButton("<", skin);
		addButton = new TextButton(">", skin);
		actButton = new TextButton(action, skin);
		valueLabel = new Label("0", skin);
		titleLabel = new Label(title, skin);
		bindButtonEvents();

		/*
		 *   +---------------+
		 *   |   [ title ]   |
		 *   +---------------+
		 *   | < | value | > |
		 *   +---------------+
		 *   |   [ button ]  |
		 *   +---------------+
		 */

		titleLabel.setAlignment(Align.center);
		valueLabel.setWidth(80);
		valueLabel.setAlignment(Align.center);
		subButton.padLeft(5).padRight(5);
		addButton.padLeft(5).padRight(5);
		if(labelAndButton){ // If added by Josh
			add(titleLabel).colspan(3).fillX().spaceBottom(10);
			row();
		}

		add(subButton).align(Align.left);
		
		add(valueLabel).fillX();
		add(addButton).align(Align.right);
		row();
		if(labelAndButton){ // If added by Josh
			add(actButton).colspan(3).fillX().spaceTop(10);
			row();
		}
	}
	
	/**
	 * The adjustable actor
	 * For an easy way of adjust values in a step of 1 / -1.
	 *
	 * @param skin    The skin file for the UI.
	 * @param title   The adjustable title.
	 * @param action  The action button text.
	 */
	public AdjustableActor(Skin skin, String title, String action) {
		this(skin,title,action,true); // Modified by Josh Neil
	}

	/**
	 * The adjustable actor
	 * For an easy way of adjust values in a step of 1 / -1.
	 *
	 * @param skin    The skin file for the UI.
	 * @param value   The default value.
	 * @param min     The minimum value.
	 * @param max     The maximum value.
	 * @param title   The adjustable title.
	 * @param action  The action button text.
	 */
	public AdjustableActor(Skin skin, int value, int min, int max, String title, String action) {
		this(skin, title, action,true);

		setMax(max);
		setMin(min);
		setValue(value);
	}
	
	// Added by Josh Neil so that we can create AdjustableActors that do not have labels
	// or action buttons for use in the ResourceMarket screen (ResourceMarketActors class)
	/**
	 * Create an adjustable actor that does not have an action button or a label attached to it
	 * @param skin    The skin file for the UI.
	 * @param value   The default value.
	 * @param min     The minimum value.
	 * @param max     The maximum value.
	 */
	public AdjustableActor(Skin skin, int value, int min, int max) {
		this(skin, "", "",false);

		setMax(max);
		setMin(min);
		setValue(value);
	}

	/**
	 * Binds events to buttons.
	 */
	private void bindButtonEvents() {
		actButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (actionEvent != null)
					actionEvent.changed(event, actor);
			}
		});

		subButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				value --;
				ensureValueInRange();
			}
		});

		addButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				value ++;
				ensureValueInRange();
			}
		});
	}

	/**
	 * Update label text to current value.
	 */
	private void updateValueDisplay() {
		valueLabel.setText(String.valueOf(value));
	}

	@Override
	protected void sizeChanged() {
		super.sizeChanged();

		// TODO: manipulate actor size?
	}
}
