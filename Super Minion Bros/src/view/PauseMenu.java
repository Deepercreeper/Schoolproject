package view;

import game.Game;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

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
	public PauseMenu(final GameContainer aGC, final Game aGame)
	{
		super(aGame, aGC.getWidth() / 2 - 300 / 2, aGC.getHeight() / 2 - 100 / 2, 300, 60);
	}
	
	@Override
	public void update(final Input aInput)
	{
		if (aInput.isKeyPressed(Input.KEY_SPACE)) mGame.mainMenu();
		if (aInput.isKeyPressed(Input.KEY_ESCAPE)) mGame.resume();
	}
	
	@Override
	public void render(final Graphics aG)
	{
		aG.setColor(Color.black);
		aG.fillRect(mX, mY, mWidth, mHeight);
		aG.setColor(Color.white);
		aG.drawString("Space - Main menu", mX + 10, mY + 10);
		aG.drawString("Esc - Weiter", mX + 10, mY + 35);
	}
}
