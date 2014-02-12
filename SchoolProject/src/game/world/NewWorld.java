package game.world;

import game.entity.NewEntity;
import java.util.HashMap;
import java.util.HashSet;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import data.DataManager;

public class NewWorld
{
	private final byte							mId;
	
	private final int							mWidth, mHeight;
	
	private final Screen						mScreen;
	
	private final byte[][]						mBlocks;
	
	private final HashMap<Integer, NewEntity>	mEntities;
	
	private final HashSet<NewEntity>			mAddEntities;
	
	public NewWorld(int aId, GameContainer gc)
	{
		mId = (byte) aId;
		mEntities = new HashMap<>();
		mAddEntities = new HashSet<>();
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
				blocks[x][y] = Block.get(rgb);
			}
		return blocks;
	}
	
	public boolean isFree(float aXV, float aYV, NewEntity aEntity)
	{
		final int x = (int) (aEntity.getX() + aXV), y = (int) (aEntity.getY() + aYV);
		final int width = aEntity.getWidth(), height = aEntity.getHeight();
		final Rectangle entity = new Rectangle(x, y, width, height);
		Rectangle block;
		if (aXV == 0)
		{	
			
		}
		else
		{	
			
		}
		// TODO Implement
		return true;
	}
	
	public int getScreenX()
	{
		return mScreen.getX();
	}
	
	public int getScreenY()
	{
		return mScreen.getY();
	}
	
	private void addEntity(NewEntity aEntity)
	{
		aEntity.init(this, generateId());
		mAddEntities.add(aEntity);
	}
	
	private int generateId()
	{
		for (int i = 0;; i++ )
			if ( !mEntities.containsKey(i)) return i;
	}
	
	public void update(Input aInput)
	{
		final HashSet<Integer> remove = new HashSet<>();
		for (NewEntity entity : mEntities.values())
			if (entity.isRemoved()) remove.add(entity.getId());
		for (int id : remove)
			mEntities.remove(id);
		for (NewEntity entity : mAddEntities)
			if ( !entity.isRemoved()) mEntities.put(entity.getId(), entity);
		
		// Update player input
		if (aInput.isKeyDown(Input.KEY_D)) mScreen.addX(5);
		if (aInput.isKeyDown(Input.KEY_A)) mScreen.addX( -5);
		if (aInput.isKeyDown(Input.KEY_S)) mScreen.addY(5);
		if (aInput.isKeyDown(Input.KEY_W)) mScreen.addY( -5);
		// Update entities
		for (NewEntity entity : mEntities.values())
			entity.update();
	}
	
	public void render(Graphics g)
	{
		// Render Blocks
		for (int x = mScreen.getX() / Block.SIZE; x <= (mScreen.getX() + mScreen.getWidth()) / Block.SIZE && x < mWidth; x++ )
			for (int y = mScreen.getY() / Block.SIZE; y <= (mScreen.getY() + mScreen.getHeight()) / Block.SIZE && y < mHeight; y++ )
				renderBlock(x, y, g);
		// Render entities
		for (NewEntity entity : mEntities.values())
			entity.render(g);
	}
	
	private void renderBlock(int aX, int aY, Graphics g)
	{
		final Block block = Block.get(mBlocks[aX][aY]);
		if ( !block.isVisible()) return;
		g.setColor(block.getColor());
		g.fillRect(aX * Block.SIZE - mScreen.getX(), aY * Block.SIZE - mScreen.getY(), Block.SIZE, Block.SIZE);
	}
}
