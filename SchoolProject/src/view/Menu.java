package view;

import game.Game;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import data.DataManager;

public class Menu
{
	private static final int	WIDTH	= 300, HEIGHT = 100;
	
	private final Game			mGame;
	
	/**
	 * Creates a new menu that controls settings.
	 * 
	 * @param aGame
	 *            The parent game.
	 */
	public Menu(Game aGame)
	{
		mGame = aGame;
	}
	
	/**
	 * Updates the menu.
	 * 
	 * @param aInput
	 *            The input information.
	 */
	public void update(Input aInput)
	{
		if (aInput.isKeyPressed(Input.KEY_SPACE)) mGame.stop();
		if (aInput.isKeyPressed(Input.KEY_ESCAPE)) mGame.hideMenu();
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
	public void render(GameContainer gc, Graphics g)
	{
		final int xPos = gc.getWidth() / 2 - WIDTH / 2, yPos = gc.getHeight() / 2 - HEIGHT / 2;
		g.setColor(Color.black);
		g.fillRect(xPos, yPos, WIDTH, HEIGHT);
		g.setColor(Color.white);
		g.drawString("< > - TexturePack: " + DataManager.getTexturePack(), xPos + 10, yPos + 5);
		g.drawString("A D - Music title: " + DataManager.getTitle(), xPos + 10, yPos + 30);
		g.drawString("Space - Beenden", xPos + 10, yPos + 55);
		g.drawString("Esc - Weiter", xPos + 10, yPos + 80);
	}
	
	public void initKeys(Input aInput)
	{
		// Prevents switching title when no key was pressed.
		aInput.isKeyPressed(Input.KEY_RIGHT);
		aInput.isKeyPressed(Input.KEY_LEFT);
		aInput.isKeyPressed(Input.KEY_D);
		aInput.isKeyPressed(Input.KEY_A);
	}
}
