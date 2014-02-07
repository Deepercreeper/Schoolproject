package view;

import game.Game;
import java.awt.Toolkit;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class View extends BasicGame
{
	private final AppGameContainer	mGameContainer;
	
	private final Game				mGame;
	
	public View() throws SlickException
	{
		super("Black");
		mGameContainer = new AppGameContainer(this);
		mGame = new Game();
		Toolkit tk = Toolkit.getDefaultToolkit();
		mGameContainer.setDisplayMode(tk.getScreenSize().width, tk.getScreenSize().height, true);
		mGameContainer.start();
	}
	
	@Override
	public void render(GameContainer gc, Graphics g)
	{
		mGame.render(gc, g);
	}
	
	@Override
	public void init(GameContainer gc)
	{
		mGame.init(gc);
	}
	
	@Override
	public void update(GameContainer gc, int aDelta)
	{
		if (mGame.isRunning()) mGame.update(gc, aDelta);
		else mGameContainer.exit();
	}
	
	@Override
	public void keyPressed(int key, char c)
	{
		mGame.key(key, true);
	}
	
	@Override
	public void keyReleased(int key, char c)
	{
		mGame.key(key, false);
	}
	
	@Override
	public void mouseClicked(int aButton, int aX, int aY, int aClickCount)
	{
		mGame.mouseClick(aButton, aX, aY);
	}
	
	@Override
	public void mousePressed(int aButton, int aX, int aY)
	{
		mGame.mouseClick(aButton, true);
	}
	
	@Override
	public void mouseReleased(int aButton, int aX, int aY)
	{
		mGame.mouseClick(aButton, false);
	}
	
	@Override
	public void mouseMoved(int aOldX, int aOldY, int aNewX, int aNewY)
	{
		mGame.mouseMove(aNewX - aOldX, aNewY - aOldY);
	}
}
