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
import util.Direction;
import util.Rectangle;
import util.Util;
import data.DataManager;

public class World
{
	/**
	 * Friction and gravity of the world.
	 */
	public static final double				FRICTION	= 0.99, GRAVITY = 0.3;
	
	private final byte						mId;
	
	private final int						mWidth, mHeight;
	
	private int								mStartX, mStartY;
	
	private final Screen					mScreen;
	
	private byte[][]						mBlocks;
	
	private final HashMap<Integer, Entity>	mEntities;
	
	private final HashSet<Entity>			mAddEntities;
	
	private final HashSet<Integer>			mUpdatableBlocks	= new HashSet<>(), mLiquidBlocks = new HashSet<>();
	
	private Player							mPlayer;
	
	private boolean							mWon;
	
	/**
	 * Creates a world defined by the given id and the given game container.
	 * 
	 * @param aId
	 *            The id of this world.
	 * @param gc
	 *            the containing game container.
	 */
	public World(int aId, GameContainer gc)
	{
		mId = (byte) aId;
		mEntities = new HashMap<>();
		mAddEntities = new HashSet<>();
		reload();
		mWidth = mBlocks.length;
		if (mWidth > 0) mHeight = mBlocks[0].length;
		else mHeight = 0;
		mScreen = new Screen(this, gc.getWidth(), gc.getHeight());
		addPlayer();
		DataManager.nextTitle();
	}
	
	private byte[][] loadBlocks()
	{
		Image image = DataManager.getWorldImage(mId);
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
				else
				{
					final Block block = Block.get(id);
					if (block.isUpdatable()) mUpdatableBlocks.add(x + y * width);
					if (block.isLiquid()) mLiquidBlocks.add(x + y * width);
					if (block == Block.START)
					{
						mStartX = x;
						mStartY = y - 1;
						blocks[x][y] = Block.AIR.getId();
					}
					else blocks[x][y] = id;
				}
			}
		return blocks;
	}
	
	/**
	 * Tests whether the given entity hits any block or other entity when moving in the given direction.
	 * 
	 * @param aXV
	 *            The x velocity of the given entity.
	 * @param aYV
	 *            The y velocity of the given entity.
	 * @param aEntity
	 *            The entity that is moving.
	 * @return {@code Double.NaN} if there is no wall or entity in the way or the distance between the wall or entity and the given entity left.
	 */
	public double isFree(double aXV, double aYV, Entity aEntity)
	{
		double result = Double.NaN;
		Rectangle entity = new Rectangle((aEntity.getX() + aXV), (aEntity.getY() + aYV), aEntity.getWidth(), aEntity.getHeight());
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
					Direction dir = Direction.NONE;
					if (aXV > 0) dir = Direction.LEFT;
					else if (aXV < 0) dir = Direction.RIGHT;
					else if (aYV > 0) dir = Direction.TOP;
					else if (aYV < 0) dir = Direction.BOTTOM;
					aEntity.addTouchingBlock(x, y, dir);
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
				aEntity.hitEntity(aEntity.getXV(), aEntity.getYV(), other);
			}
		return result;
	}
	
	/**
	 * Sets the id of the given positioned block.
	 * 
	 * @param aX
	 *            The x position of the block.
	 * @param aY
	 *            The y position of the block.
	 * @param aId
	 *            The block id to set.
	 */
	public void setBlock(int aX, int aY, byte aId)
	{
		mBlocks[aX][aY] = aId;
	}
	
	/**
	 * Returns the screen position along the x axis.
	 * 
	 * @return the screen x position.
	 */
	public int getScreenX()
	{
		return mScreen.getX();
	}
	
	/**
	 * Returns the screen position along the y axis.
	 * 
	 * @return the screen y position.
	 */
	public int getScreenY()
	{
		return mScreen.getY();
	}
	
	/**
	 * Returns the screen width.
	 * 
	 * @return the screen width.
	 */
	public int getScreenWidth()
	{
		return mScreen.getWidth();
	}
	
	/**
	 * Returns the screen height.
	 * 
	 * @return the screen height.
	 * @return
	 */
	public int getScreenHeight()
	{
		return mScreen.getHeight();
	}
	
	/**
	 * Returns the number of blocks in a row.
	 * 
	 * @return the map width.
	 */
	public int getWidth()
	{
		return mWidth;
	}
	
	/**
	 * Returns the number of blocks in a column.
	 * 
	 * @return the map height.
	 */
	public int getHeight()
	{
		return mHeight;
	}
	
	/**
	 * Returns the id of the block at the given position.
	 * 
	 * @param aX
	 *            The x position.
	 * @param aY
	 *            The y position.
	 * @return the block id.
	 */
	public byte getBlock(int aX, int aY)
	{
		return mBlocks[aX][aY];
	}
	
	/**
	 * Adds the given entity to this world.
	 * 
	 * @param aEntity
	 *            The entity to add.
	 */
	public void addEntity(Entity aEntity)
	{
		aEntity.init(this, generateId());
		mAddEntities.add(aEntity);
	}
	
	/**
	 * Adds the given entity to this world at a specific position.
	 * 
	 * @param aEntity
	 *            The entity to add.
	 * @param aX
	 *            The entity x position
	 */
	public void addEntity(Entity aEntity, int aX, int aY)
	{
		aEntity.init(this, generateId());
		aEntity.setX(aX);
		aEntity.setY(aY);
		mAddEntities.add(aEntity);
	}
	
	private int generateId()
	{
		for (int i = 0;; i++ )
			if ( !mEntities.containsKey(i)) return i;
	}
	
	/**
	 * Updates all entities, blocks and the screen.
	 * 
	 * @param aInput
	 *            The information about keyboard and mouse activity.
	 */
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
		
		if (mPlayer.isRemoved())
		{
			reload();
			addPlayer();
			return;
		}
		
		// Update entities
		for (Entity entity : mEntities.values())
			entity.update(aInput);
		
		// Update blocks
		for (int tile : mUpdatableBlocks)
		{
			final int x = tile % mWidth, y = tile / mWidth;
			Block.get(mBlocks[x][y]).update(x, y, this);
		}
		
		// Update screen
		mScreen.update(mPlayer);
	}
	
	private void addPlayer()
	{
		mPlayer = new Player(mStartX * Block.SIZE, mStartY * Block.SIZE);
		addEntity(mPlayer);
	}
	
	private void reload()
	{
		mBlocks = loadBlocks();
	}
	
	/**
	 * Returns the world id.
	 * 
	 * @return the id.
	 */
	public byte getId()
	{
		return mId;
	}
	
	/**
	 * Returns whether the player has gone through the finishing flag.
	 * 
	 * @return {@code true} if the player has finished and {@code false} if not.
	 */
	public boolean hasWon()
	{
		return mWon;
	}
	
	/**
	 * Makes this world be done so the next world can be loaded.
	 */
	public void win()
	{
		mWon = true;
	}
	
	/**
	 * The current player of this world.
	 * 
	 * @return the player.
	 */
	public Player getPlayer()
	{
		return mPlayer;
	}
	
	/**
	 * Renders the background, all blocks and entities.
	 * 
	 * @param g
	 *            the graphics to draw into.
	 */
	public void render(Graphics g)
	{
		// Render background
		Image background = DataManager.getBackgroundImage(0);
		g.drawImage(background, -(mScreen.getX() / 5 % background.getWidth()), 0);
		g.drawImage(background, background.getWidth() - (mScreen.getX() / 5 % background.getWidth()), 0);
		
		// Render Blocks
		for (int x = Math.max(mScreen.getX() / Block.SIZE, 0); x <= (mScreen.getX() + mScreen.getWidth()) / Block.SIZE && x < mWidth; x++ )
			for (int y = Math.max(mScreen.getY() / Block.SIZE, 0); y <= (mScreen.getY() + mScreen.getHeight()) / Block.SIZE && y < mHeight; y++ )
				renderBlock(x, y, g);
		
		// Render entities
		for (Entity entity : mEntities.values())
			entity.render(g);
		
		// Render liquid blocks
		for (int tile : mLiquidBlocks)
		{
			final int x = tile % mWidth, y = tile / mWidth;
			renderBlock(x, y, g);
		}
	}
	
	private void renderBlock(int aX, int aY, Graphics g)
	{
		final Block block = Block.get(mBlocks[aX][aY]);
		if ( !block.isVisible() && !block.isLiquid()) return;
		Block.render(aX, aY, mBlocks[aX][aY], g, this);
	}
}
