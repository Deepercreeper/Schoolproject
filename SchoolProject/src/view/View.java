package view;

import game.Game;
import java.awt.Toolkit;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

public class View extends BasicGame
{
	private final AppGameContainer	mGameContainer;
	
	private final Game				mGame;
	
	private final Adapter			mAdapter;
	
	public View() throws SlickException
	{
		super("Black");
		mGame = new Game();
		mAdapter = new Adapter(mGame);
		mGameContainer = new AppGameContainer(this);
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
		Log.info("Initialization");
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
		mAdapter.key(key, true);
	}
	
	@Override
	public void keyReleased(int key, char c)
	{
		mAdapter.key(key, false);
	}
}
