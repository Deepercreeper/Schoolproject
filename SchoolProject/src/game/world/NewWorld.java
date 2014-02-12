package game.world;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import data.DataManager;

public class NewWorld
{
	private final byte		mId;
	
	private final int		mWidth, mHeight;
	
	private final Screen	mScreen;
	
	private final byte[][]	mBlocks;
	
	public NewWorld(int aId, GameContainer gc)
	{
		mId = (byte) aId;
		mBlocks = loadBlocks();
		mWidth = mBlocks.length;
		if (mWidth > 0) mHeight = mBlocks[0].length;
		else mHeight = 0;
		mScreen = new Screen(gc.getWidth(), gc.getHeight());
	}
	
	private byte[][] loadBlocks()
	{
		Image image = DataManager.get("worldData" + mId);
		final int width = image.getWidth(), height = image.getHeight();
		final int redInt = (int) Math.pow(2, 16), greenInt = (int) Math.pow(2, 8);
		byte[][] blocks = new byte[width][height];
		Color color;
		int rgb;
		for (int x = 0; x < width; x++ )
			for (int y = 0; y < height; y++ )
			{
				color = image.getColor(x, y);
				rgb = color.getRed() * redInt + color.getGreen() * greenInt + color.getBlue();
				System.out.println(color.getRed() + " " + color.getGreen() + " " + color.getBlue() + " => " + Integer.toHexString(rgb));
				blocks[x][y] = Block.get(rgb);
			}
		return blocks;
	}
	
	public int getScreenX()
	{
		return mScreen.getX();
	}
	
	public int getScreenY()
	{
		return mScreen.getY();
	}
	
	public void update(Input aInput)
	{	
		
	}
	
	public void render(Graphics g)
	{	
		
	}
}
