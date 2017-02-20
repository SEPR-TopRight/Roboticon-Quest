package io.github.teamfractal.animation;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import io.github.teamfractal.entity.Player;
import io.github.teamfractal.screens.AbstractAnimationScreen;

/**
 * Adds an animation that moves onto the screen then off again and displays 
 * the resources that have been added to or removed from the given players inventory
 */
public class AnimationAddResources implements IAnimation {
	private final Player player;
	private final int energy;
	private final int food;
	private final int ore;
	private float time;
	private static BitmapFont font = new BitmapFont();

	/**
	 * Initialise the animation.
	 * @param player    Current player
	 * @param energy    The amount of energy generated.
	 * @param food      The amount of food generated.
	 * @param ore       The amount of ore generated.
	 */
	public AnimationAddResources(Player player, int energy, int food, int ore) {
		time = 0;
		this.player = player;
		this.energy = energy;
		this.food = food;
		this.ore = ore;
	}

	/**
	 * Generate the animation text.
	 * @param resCount        Resource Count.
	 * @param type            Resource Type.
	 * @return                Generated text.
	 */
	private String resStr(int resCount, String type) {
		// -- Energy
		// +3 Energy
		if (resCount == 0) {
			return "-- " + type + "   ";
		}
		return (resCount > 0 ? "+" : "-") + resCount + " " + type + "   ";
	}

	/**
	 * Generate and concat resource strings.
	 * @return  Generated resource generation message.
	 */
	private String generateResourceString() {
		return (resStr(energy, "Energy")
				+ resStr(food, "Food")
				+ resStr(ore, "Ore")).trim();
	}

	/**
	 * The quadratic function.
	 * @param t      Time, between 0 and 1.
	 * @return       If <code>t</code> is larger than <code>1</code>,
	 *               then it will return <code>1</code>; otherwise
	 *               the square of <code>t</code>.
	 */
	private float fn_quad(float t) {
		if (t > 1f) return 1;
		return t * t;
	}

	/**
	 * Opacity calculation based on current time.
	 * @return Calculated opacity.
	 */
	private float fn_opacity () {
		if (time < 1f) {
			// Fade in
			return fn_quad(time);
		} else if (time >= 1f && time < 2f) {
			// Stay visible.
			return 1;
		} else {
			// 2 ~ 3:
			// Reverse of the function to fade out.
			return 1f - fn_quad(time - 2f);
		}
	}

	// Time length for the animation in second.
	private static final float animationLength = 3;

	/**
	 * Render call.
	 * @param delta     Time change since last call.
	 * @param screen    The screen to draw on.
	 * @param batch     The Batch for drawing stuff.
	 * @return          return <code>true</code> if the animation has completed.
	 */
	@Override
	public boolean tick(float delta, AbstractAnimationScreen screen, Batch batch) {
		time += delta;
		if (time > animationLength) {
			return true;
		}

		batch.begin();
		font.setColor(1,1,1, fn_opacity());
		font.draw(batch, generateResourceString(), 20, fn_quad(time) * 30);
		batch.end();
		return false;
	}

	private IAnimationFinish callback;

	/**
	 * Set animation finish callback.
	 * @param callback
	 */
	@Override
	public void setAnimationFinish(IAnimationFinish callback) {
		this.callback = callback;
	}

	/**
	 * Call animation finish callback.
	 * Should only be called from {@link AbstractAnimationScreen}.
	 */
	public void callAnimationFinish() {
		if (callback != null)
			callback.OnAnimationFinish();
	}

	/**
	 * Cancel the animation.
	 */
	@Override
	public void cancelAnimation() {
		callback = null;
		time += animationLength;
	}
}
