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
	
	public Menu(Game aGame)
	{
		mGame = aGame;
	}
	
	public void update(Input aInput)
	{
		if (aInput.isKeyPressed(Input.KEY_SPACE)) mGame.hideMenu();
		if (aInput.isKeyPressed(Input.KEY_ESCAPE)) mGame.stop();
		if (aInput.isKeyPressed(Input.KEY_RIGHT)) DataManager.setNextTexturePack();
		if (aInput.isKeyPressed(Input.KEY_LEFT)) DataManager.setPreviousTexturePack();
	}
	
	public void render(GameContainer gc, Graphics g)
	{
		final int xPos = gc.getWidth() / 2 - WIDTH / 2, yPos = gc.getHeight() / 2 - HEIGHT / 2;
		g.setColor(Color.black);
		g.fillRect(xPos, yPos, WIDTH, HEIGHT);
		g.setColor(Color.white);
		g.drawString("< > - TexturePack: " + DataManager.getTexturePack(), xPos + 10, yPos + 10);
		g.drawString("Esc - Beenden", xPos + 10, yPos + 40);
		g.drawString("Space - Weiter", xPos + 10, yPos + 70);
	}
}
