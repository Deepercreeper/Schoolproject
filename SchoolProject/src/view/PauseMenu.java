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
	}
	
	@Override
	public void render(Graphics aG)
	{
		aG.setColor(Color.black);
		aG.fillRect(mX, mY, mWidth, mHeight);
		aG.setColor(Color.white);
		aG.drawString("< > - TexturePack: " + DataManager.getTexturePack(), mX + 10, mY + 10);
		aG.drawString("Space - Main menu", mX + 10, mY + 35);
		aG.drawString("Esc - Weiter", mX + 10, mY + 60);
	}
}
