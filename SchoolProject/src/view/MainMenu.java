package view;

import game.Game;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import data.DataManager;

public class MainMenu extends Menu
{
	private int	mWorld	= 0;
	
	public MainMenu(GameContainer gc, Game aGame)
	{
		super(aGame, 0, 0, gc.getWidth(), gc.getHeight());
	}
	
	@Override
	public void render(Graphics g)
	{
		g.setColor(Color.black);
		g.fillRect(mX, mY, mWidth, mHeight);
		g.setColor(Color.white);
		g.drawString("< > - World: " + mWorld, mWidth / 2 - 100, mHeight / 2 - 15);
		g.drawString("Space - Start", mWidth / 2 - 100, mHeight / 2);
		g.drawString("Escape - Ende", mWidth / 2 - 100, mHeight / 2 + 15);
	}
	
	@Override
	public void update(Input aInput)
	{
		if (aInput.isKeyPressed(Input.KEY_LEFT)) mWorld = (mWorld - 1 + DataManager.getWorlds().length) % DataManager.getWorlds().length;
		if (aInput.isKeyPressed(Input.KEY_RIGHT)) mWorld = (mWorld + 1) % DataManager.getWorlds().length;
		if (aInput.isKeyPressed(Input.KEY_SPACE)) mGame.start(mWorld);
		if (aInput.isKeyPressed(Input.KEY_ESCAPE)) mGame.stop();
	}
	
	@Override
	public void initKeys(Input aInput)
	{}
}
