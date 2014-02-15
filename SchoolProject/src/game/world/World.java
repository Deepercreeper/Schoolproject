package game.world;

import game.entity.Entity;
import game.entity.Player;
import game.world.block.Block;
import java.util.HashMap;
import java.util.HashSet;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import util.Util;
import data.DataManager;

public class World
{
	public static final float				FRICTION	= 0.99f, GRAVITY = 0.3f;
	
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
		mScreen = new Screen(this, gc.getWidth(), gc.getHeight());
		mPlayer = new Player();
		addEntity(mPlayer);
		DataManager.playMusic("world" + mId);
	}
	
	private byte[][] loadBlocks()
	{
		Image image = DataManager.getImage("worldData" + mId);
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
				byte id = Block.get(rgb);
				if (id == -1) blocks[x][y] = Block.AIR.getId();
				else blocks[x][y] = id;
			}
		return blocks;
	}
	
	public double isFree(float aXV, float aYV, Entity aEntity)
	{
		double result = Float.NaN;
		Rectangle entity = new Rectangle(aEntity.getX() + aXV, aEntity.getY() + aYV, aEntity.getWidth(), aEntity.getHeight());
		for (int x = (int) (entity.getX() / Block.SIZE); x <= (int) (entity.getX() + entity.getWidth() - 0.1) / Block.SIZE && x < mWidth; x++ )
			for (int y = (int) (entity.getY() / Block.SIZE); y <= (int) (entity.getY() + entity.getHeight() - 0.1) / Block.SIZE && y < mHeight; y++ )
			{
				if (Block.get(mBlocks[x][y]).isSolid() && new Rectangle(x * Block.SIZE, y * Block.SIZE, Block.SIZE, Block.SIZE).intersects(entity))
				{
					if (aXV != 0)
					{
						if (aXV > 0) result = Util.minAbs(result, aXV - (entity.getMaxX() % Block.SIZE));
						else result = Util.minAbs(result, -(aEntity.getX() % Block.SIZE));
					}
					else
					{
						if (aYV > 0) result = Util.minAbs(result, aYV - (entity.getMaxY() % Block.SIZE));
						else result = Util.minAbs(result, -(aEntity.getY() % Block.SIZE));
					}
					Block.get(mBlocks[x][y]).hit(x, y, aEntity.getXV(), aEntity.getYV(), this, aEntity);
				}
			}
		for (Entity other : mEntities.values())
			if (other != aEntity && mScreen.contains(other) && other.isSolid() && other.getRect().intersects(entity))
			{
				if (aXV != 0)
				{
					if (aXV > 0) result = Util.minAbs(result, aXV - (entity.getX() + entity.getWidth() - (other.getX())));
					else result = Util.minAbs(result, aXV + (other.getX() + other.getWidth() - (entity.getX())));
				}
				else
				{
					if (aYV > 0) result = Util.minAbs(result, aYV - (entity.getY() + entity.getHeight() - (other.getY())));
					else result = Util.minAbs(result, aYV + (other.getY() + other.getHeight() - (entity.getY())));
				}
			}
		return result;
	}
	
	public void setBlock(int aX, int aY, byte aId)
	{
		mBlocks[aX][aY] = aId;
	}
	
	public int getScreenX()
	{
		return mScreen.getX();
	}
	
	public int getScreenY()
	{
		return mScreen.getY();
	}
	
	public int getScreenWidth()
	{
		return mScreen.getWidth();
	}
	
	public int getScreenHeight()
	{
		return mScreen.getHeight();
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
		// If loading music
		if (DataManager.isLoading()) return;
		
		final HashSet<Integer> remove = new HashSet<>();
		for (Entity entity : mEntities.values())
			if (entity.isRemoved()) remove.add(entity.getId());
		for (int id : remove)
			mEntities.remove(id);
		for (Entity entity : mAddEntities)
			if ( !entity.isRemoved()) mEntities.put(entity.getId(), entity);
		mAddEntities.clear();
		
		// Update entities
		for (Entity entity : mEntities.values())
			entity.update(aInput);
		mScreen.update(mPlayer);
	}
	
	public void render(Graphics g)
	{
		// If loading music
		if (DataManager.isLoading())
		{
			g.drawImage(DataManager.getImage("splash"), 0, 0);
			return;
		}
		
		// Render Blocks
		for (int x = Math.max(mScreen.getX() / Block.SIZE, 0); x <= (mScreen.getX() + mScreen.getWidth()) / Block.SIZE && x < mWidth; x++ )
			for (int y = Math.max(mScreen.getY() / Block.SIZE, 0); y <= (mScreen.getY() + mScreen.getHeight()) / Block.SIZE && y < mHeight; y++ )
				renderBlock(x, y, g);
		// Render entities
		for (Entity entity : mEntities.values())
			entity.render(g);
	}
	
	private void renderBlock(int aX, int aY, Graphics g)
	{
		final Block block = Block.get(mBlocks[aX][aY]);
		if ( !block.isVisible()) return;
		block.render(aX, aY, g, this);
	}
}
