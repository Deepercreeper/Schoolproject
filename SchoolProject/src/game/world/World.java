package game.world;

import game.entity.Entity;
import game.entity.Player;
import java.util.HashMap;
import java.util.HashSet;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import data.DataManager;

public class World
{
	private final byte						mId;
	
	private final int						mWidth, mHeight;
	
	private final Screen					mScreen;
	
	private final byte[][]					mBlocks;
	
	private final HashMap<Integer, Entity>	mEntities;
	
	private final HashSet<Entity>			mAddEntities;
	
	private final Player					mPlayer;
	
	public World(int aId, GameContainer gc)
	{
		mId = (byte) aId;
		mEntities = new HashMap<>();
		mAddEntities = new HashSet<>();
		mBlocks = loadBlocks();
		mWidth = mBlocks.length;
		if (mWidth > 0) mHeight = mBlocks[0].length;
		else mHeight = 0;
		mScreen = new Screen(gc.getWidth(), gc.getHeight());
		mPlayer = new Player();
		addEntity(mPlayer);
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
	
	public float isFree(float aXV, float aYV, Entity aEntity)
	{
		Rectangle entity = new Rectangle(aEntity.getX() + aXV, aEntity.getY() + aYV, aEntity.getWidth(), aEntity.getHeight());
		for (int x = (int) (entity.getX() / Block.SIZE); x <= (int) (entity.getX() + entity.getWidth()) / Block.SIZE; x++ )
			for (int y = (int) (entity.getY() / Block.SIZE); y <= (int) (entity.getY() + entity.getHeight() - 1) / Block.SIZE; y++ )
			{
				if (Block.get(mBlocks[x][y]).isSolid() && new Rectangle(x * Block.SIZE, y * Block.SIZE, Block.SIZE, Block.SIZE).intersects(entity))
				{
					if (aXV != 0)
					{
						if (aXV > 0) return aXV - (entity.getMaxX() % Block.SIZE);
						else return -(aEntity.getX() % Block.SIZE);
					}
					else
					{
						if (aYV > 0) return aYV - (entity.getMaxY() % Block.SIZE);
						else return -(aEntity.getY() % Block.SIZE);
					}
				}
			}
		for (Entity other : mEntities.values())
			if (other != aEntity && mScreen.contains(other) && other.isSolid() && other.getRect().intersects(entity))
			{
				if (aXV != 0)
				{
					if (aXV > 0) return aXV - (entity.getX() + entity.getWidth() - (other.getX()));
					else return aXV + (other.getX() + other.getWidth() - (entity.getX()));
				}
				else
				{
					if (aYV > 0) return aYV - (entity.getY() + entity.getHeight() - (other.getY()));
					else return aYV + (other.getY() + other.getHeight() - (entity.getY()));
				}
			}
		return Float.NaN;
	}
	
	public int getScreenX()
	{
		return mScreen.getX();
	}
	
	public int getScreenY()
	{
		return mScreen.getY();
	}
	
	public int getWidth()
	{
		return mWidth;
	}
	
	public int getHeight()
	{
		return mHeight;
	}
	
	private void addEntity(Entity aEntity)
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
		for (Entity entity : mEntities.values())
			if (entity.isRemoved()) remove.add(entity.getId());
		for (int id : remove)
			mEntities.remove(id);
		for (Entity entity : mAddEntities)
			if ( !entity.isRemoved()) mEntities.put(entity.getId(), entity);
		mAddEntities.clear();
		
		// Update player input
		mPlayer.updateInput(aInput);
		// Update entities
		for (Entity entity : mEntities.values())
			entity.update();
	}
	
	public void render(Graphics g)
	{
		// Render Blocks
		for (int x = mScreen.getX() / Block.SIZE; x <= (mScreen.getX() + mScreen.getWidth()) / Block.SIZE && x < mWidth; x++ )
			for (int y = mScreen.getY() / Block.SIZE; y <= (mScreen.getY() + mScreen.getHeight()) / Block.SIZE && y < mHeight; y++ )
				renderBlock(x, y, g);
		// Render entities
		for (Entity entity : mEntities.values())
			entity.render(g);
	}
	
	private void renderBlock(int aX, int aY, Graphics g)
	{
		final Block block = Block.get(mBlocks[aX][aY]);
		if ( !block.isVisible()) return;
		g.drawImage(block.getImage(), aX * Block.SIZE - mScreen.getX(), aY * Block.SIZE - mScreen.getY());
	}
}
