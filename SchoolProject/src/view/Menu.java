package view;

import game.Game;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public abstract class Menu
{
	protected final Game	mGame;
	
	protected final int		mX, mY, mWidth, mHeight;
	
	public Menu(Game aGame, int aX, int aY, int aWidth, int aHeight)
	{
		mGame = aGame;
		mX = aX;
		mY = aY;
		mWidth = aWidth;
		mHeight = aHeight;
	}
	
	public abstract void render(Graphics g);
	
	public abstract void update(Input aInput);
	
}
