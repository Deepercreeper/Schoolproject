package view;

import game.Game;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public abstract class Menu
{
	protected final Game	mGame;
	
	protected final int		mX, mY, mWidth, mHeight;
	
	/**
	 * Creates a new menu for the parent game.
	 * 
	 * @param aGame
	 *            The parent game.
	 * @param aX
	 *            The x position.
	 * @param aY
	 *            The y position.
	 * @param aWidth
	 *            The menu width.
	 * @param aHeight
	 *            The menu height.
	 */
	public Menu(Game aGame, int aX, int aY, int aWidth, int aHeight)
	{
		mGame = aGame;
		mX = aX;
		mY = aY;
		mWidth = aWidth;
		mHeight = aHeight;
	}
	
	/**
	 * Renders the menu into the given graphics.
	 * 
	 * @param aG
	 *            The graphics to draw into.
	 */
	public abstract void render(Graphics aG);
	
	/**
	 * Updates the menu.
	 * 
	 * @param aInput
	 *            The input done at the last tick.
	 */
	public abstract void update(Input aInput);
}
