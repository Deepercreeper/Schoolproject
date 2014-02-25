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
	
	/**
	 * Creates a new Minion bros. game.
	 * 
	 * @throws SlickException
	 */
	public View() throws SlickException
	{
		super("Super Minion Bros");
		final Toolkit tk = Toolkit.getDefaultToolkit();
		mGameContainer = new AppGameContainer(this);
		mGameContainer.setDisplayMode(tk.getScreenSize().width, tk.getScreenSize().height, true);
		mGameContainer.setVSync(true);
		mGameContainer.setTargetFrameRate(60);
		mGameContainer.setShowFPS(false);
		
		mGame = new Game();
		mGameContainer.start();
	}
	
	@Override
	public void render(GameContainer aGC, Graphics aG)
	{
		mGame.render(aG);
	}
	
	@Override
	public void update(GameContainer aGC, int aDelta)
	{
		if (mGame.isRunning()) mGame.update(aDelta);
		else mGameContainer.exit();
	}
	
	@Override
	public void init(GameContainer aGC) throws SlickException
	{
		mGame.init(aGC);
	}
}
