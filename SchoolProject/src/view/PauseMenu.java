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
	 * @param aGC
	 *            The containing game container.
	 * @param aGame
	 *            The parent game.
	 */
	public PauseMenu(GameContainer aGC, Game aGame)
	{
		super(aGame, aGC.getWidth() / 2 - 300 / 2, aGC.getHeight() / 2 - 100 / 2, 300, 100);
	}
	
	@Override
	public void update(Input aInput)
	{
		if (aInput.isKeyPressed(Input.KEY_SPACE)) mGame.mainMenu();
		if (aInput.isKeyPressed(Input.KEY_ESCAPE)) mGame.resume();
		if (aInput.isKeyPressed(Input.KEY_RIGHT)) DataManager.nextTexturePack();
		if (aInput.isKeyPressed(Input.KEY_LEFT)) DataManager.previousTexturePack();
		// if (aInput.isKeyPressed(Input.KEY_D)) DataManager.nextTitle();
		// if (aInput.isKeyPressed(Input.KEY_A)) DataManager.previousTitle();
	}
	
	@Override
	public void render(Graphics aG)
	{
		aG.setColor(Color.black);
		aG.fillRect(mX, mY, mWidth, mHeight);
		aG.setColor(Color.white);
		aG.drawString("< > - TexturePack: " + DataManager.getTexturePack(), mX + 10, mY + 5);
		aG.drawString("A D - Music title: " + DataManager.getTitle(), mX + 10, mY + 30);
		aG.drawString("Space - Main menu", mX + 10, mY + 55);
		aG.drawString("Esc - Weiter", mX + 10, mY + 80);
	}
}
