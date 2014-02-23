package view;

import game.Game;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import data.DataManager;

public class PauseMenu extends Menu
{
	/**
	 * Creates a new menu that controls settings.
	 * 
	 * @param aGame
	 *            The parent game.
	 */
	public PauseMenu(GameContainer gc, Game aGame)
	{
		super(aGame, gc.getWidth() / 2 - 300 / 2, gc.getHeight() / 2 - 100 / 2, 300, 100);
	}
	
	/**
	 * Updates the menu.
	 * 
	 * @param aInput
	 *            The input information.
	 */
	@Override
	public void update(Input aInput)
	{
		if (aInput.isKeyPressed(Input.KEY_SPACE)) mGame.mainMenu();
		if (aInput.isKeyPressed(Input.KEY_ESCAPE)) mGame.resume();
		if (aInput.isKeyPressed(Input.KEY_RIGHT)) DataManager.nextTexturePack();
		if (aInput.isKeyPressed(Input.KEY_LEFT)) DataManager.previousTexturePack();
		if (aInput.isKeyPressed(Input.KEY_D)) DataManager.nextTitle();
		if (aInput.isKeyPressed(Input.KEY_A)) DataManager.previousTitle();
	}
	
	/**
	 * Renders the menu into the middle of the screen.
	 * 
	 * @param gc
	 *            The containing game container.
	 * @param g
	 *            The graphics to draw into.
	 */
	@Override
	public void render(Graphics g)
	{
		g.setColor(Color.black);
		g.fillRect(mX, mY, mWidth, mHeight);
		g.setColor(Color.white);
		g.drawString("< > - TexturePack: " + DataManager.getTexturePack(), mX + 10, mY + 5);
		g.drawString("A D - Music title: " + DataManager.getTitle(), mX + 10, mY + 30);
		g.drawString("Space - Main menu", mX + 10, mY + 55);
		g.drawString("Esc - Weiter", mX + 10, mY + 80);
	}
}
