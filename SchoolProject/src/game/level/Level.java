package game.level;

import game.Stats;
import game.entity.Entity;
import game.entity.Player;
import game.level.block.Block;
import game.level.block.Item;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import util.Direction;
import util.Rectangle;
import util.Util;
import data.DataManager;

public class Level
{
	/**
	 * Friction and gravity of the level.
	 */
	public static final double	FRICTION	= 0.99, GRAVITY = 0.3;
	
	private final byte			mLevelId, mWorldId;
	
	private final int			mWidth, mHeight;
	
	private int					mStartX, mStartY;
	
	private final Screen		mScreen;
	
	private short[][]			mBlocks;
	
	private short[][]			mAlphas;
	
	private final HashMap<Integer, Entity>	mEntities	= new HashMap<>(), mAddEntities = new HashMap<>();
	
	private final HashSet<Integer>			mUpdatableBlocks	= new HashSet<>(), mTransparentBlocks = new HashSet<>();
	
	private final Player					mPlayer;
	
	private boolean							mWon;
	
	/**
	 * Creates a new level.
	 * 
	 * @param aWorldId
	 *            The world id.
	 * @param aLevelId
	 *            The level id.
	 * @param aGC
	 *            The containing game container.
	 * @param aPlayer
	 *            The player of this level.
	 */
	public Level(int aWorldId, int aLevelId, GameContainer aGC, Player aPlayer)
	{
		mWorldId = (byte) aWorldId;
		mLevelId = (byte) aLevelId;
		mScreen = new Screen(aGC.getWidth(), aGC.getHeight());
		mScreen.init(this);
		mPlayer = aPlayer;
		mPlayer.respawn();
		reload();
		mWidth = mBlocks.length;
		if (mWidth > 0) mHeight = mBlocks[0].length;
		else mHeight = 0;
		addPlayer(mPlayer);
		DataManager.playMusic("world" + (mWorldId % 6));
	}
	
	/**
	 * Tests whether the given entity hits any block or other entity when moving
	 * in the given direction.
	 * 
	 * @param aXV
	 *            The x velocity of the given entity.
	 * @param aYV
	 *            The y velocity of the given entity.
	 * @param aEntity
	 *            The entity that is moving.
	 * @return {@code Double.NaN} if there is no wall or entity in the way or
	 *         the distance between the wall or entity and the given entity
	 *         left.
	 */
	public double isFree(double aXV, double aYV, Entity aEntity)
	{
		double result = Double.NaN;
		Rectangle entity = new Rectangle((aEntity.getX() + aXV), (aEntity.getY() + aYV), aEntity.getWidth(), aEntity.getHeight());
		for (int x = (int) (entity.getX() / Block.SIZE); x <= (int) (entity.getX() + entity.getWidth() - 0.1) / Block.SIZE && x < mWidth; x++ )
			for (int y = (int) (entity.getY() / Block.SIZE); y <= (int) (entity.getY() + entity.getHeight()) / Block.SIZE && y < mHeight; y++ )
			{
				if (Block.get(mBlocks[x][y]).isSolid(x, y, aEntity) && new Rectangle(x * Block.SIZE, y * Block.SIZE, Block.SIZE, Block.SIZE).intersects(entity))
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
		if (aEntity.isParticle()) return result;
		for (Entity other : mEntities.values())
			if ( !other.isParticle() && other != aEntity && mScreen.contains(other) && other.getRect().intersects(entity))
			{
				if (aEntity.isSolid() && other.isSolid())
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
	public void setBlock(int aX, int aY, short aId)
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
	public short getBlock(int aX, int aY)
	{
		return mBlocks[aX][aY];
	}
	
	/**
	 * Returns the alpha value of the given position.
	 * 
	 * @param aX
	 *            The x position.
	 * @param aY
	 *            The y position.
	 * @return an alpha value between 0 and 255.
	 */
	public short getAlpha(int aX, int aY)
	{
		return mAlphas[aX][aY];
	}
	
	/**
	 * Adds the given entity to this level.
	 * 
	 * @param aEntity
	 *            The entity to add.
	 */
	public void addEntity(Entity aEntity)
	{
		aEntity.init(this, generateId());
		mAddEntities.put(aEntity.getId(), aEntity);
	}
	
	/**
	 * Adds the given entity to this level at a specific position.
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
		mAddEntities.put(aEntity.getId(), aEntity);
	}
	
	/**
	 * Updates all entities, blocks and the screen.
	 * 
	 * @param aInput
	 *            The information about keyboard and mouse activity.
	 */
	public void update(Input aInput)
	{
		if (mPlayer.isDead())
		{
			Stats.instance().die();
			reload();
			mPlayer.respawn();
			addPlayer(mPlayer);
			return;
		}
		
		for (Iterator<Entry<Integer, Entity>> iterator = mEntities.entrySet().iterator(); iterator.hasNext();)
		{
			Entity entity = iterator.next().getValue();
			if (entity.isRemoved()) iterator.remove();
		}
		for (Entity entity : mAddEntities.values())
			if ( !entity.isRemoved()) mEntities.put(entity.getId(), entity);
		mAddEntities.clear();
		
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
	
	/**
	 * Adds the player of this level.
	 * 
	 * @param aPlayer
	 *            The player.
	 */
	public void addPlayer(Player aPlayer)
	{
		mPlayer.setX(mStartX * Block.SIZE);
		mPlayer.setY(mStartY * Block.SIZE);
		addEntity(mPlayer);
	}
	
	/**
	 * Resets entities, statistics and blocks.
	 */
	public void reload()
	{
		mEntities.clear();
		Stats.instance().setBananaLevel();
		loadBlocks();
	}
	
	/**
	 * Returns the level id.
	 * 
	 * @return the id.
	 */
	public byte getLevelId()
	{
		return mLevelId;
	}
	
	/**
	 * Returns the world id.
	 * 
	 * @return the id.
	 */
	public byte getWorldId()
	{
		return mWorldId;
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
	 * Makes this level be done so the next level or world can be loaded.
	 */
	public void win()
	{
		mWon = true;
	}
	
	/**
	 * The current player of this level.
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
	 * @param aG
	 *            the graphics to draw into.
	 */
	public void render(Graphics aG)
	{
		// Render background
		Image background = DataManager.getBackgroundImage(0);
		aG.drawImage(background, -(mScreen.getX() / 5 % background.getWidth()), 0);
		aG.drawImage(background, background.getWidth() - (mScreen.getX() / 5 % background.getWidth()), 0);
		
		// Render Blocks
		for (int x = Math.max(mScreen.getX() / Block.SIZE, 0); x <= (mScreen.getX() + mScreen.getWidth()) / Block.SIZE && x < mWidth; x++ )
			for (int y = Math.max(mScreen.getY() / Block.SIZE, 0); y <= (mScreen.getY() + mScreen.getHeight()) / Block.SIZE && y < mHeight; y++ )
				renderBlock(x, y, aG);
		
		// Render entities
		for (Entity entity : mEntities.values())
			entity.render(aG);
		
		// Render transparent blocks
		for (int tile : mTransparentBlocks)
		{
			final int x = tile % mWidth, y = tile / mWidth;
			Block.render(x, y, mBlocks[x][y], aG, this);
		}
	}
	
	private int generateId()
	{
		for (int i = 0;; i++ )
			if ( !mEntities.containsKey(i) && !mAddEntities.containsKey(i)) return i;
	}
	
	private void loadBlocks()
	{
		Image image = DataManager.getLevelImage(mWorldId, mLevelId);
		final int width = image.getWidth(), height = image.getHeight();
		final int redInt = (int) Math.pow(2, 16), greenInt = (int) Math.pow(2, 8);
		mBlocks = new short[width][height];
		mAlphas = new short[width][height];
		Color color;
		int rgb;
		for (int x = 0; x < width; x++ )
			for (int y = 0; y < height; y++ )
			{
				color = image.getColor(x, y);
				rgb = color.getRed() * redInt + color.getGreen() * greenInt + color.getBlue();
				mAlphas[x][y] = (short) color.getAlpha();
				short id = Block.getIdFromCode(rgb);
				if (id == -1) mBlocks[x][y] = Block.AIR.getId();
				else
				{
					final Block block = Block.get(id);
					if (block.isUpdatable()) mUpdatableBlocks.add(x + y * width);
					if (block.isLiquid() || block.isFlag()) mTransparentBlocks.add(x + y * width);
					if (block.isItemBlock())
					{
						mBlocks[x][y] = Block.AIR.getId();
						addEntity(Item.getItem(x * Block.SIZE, y * Block.SIZE, rgb));
					}
					else if (block == Block.START)
					{
						mStartX = x;
						mStartY = y - 1;
						mBlocks[x][y] = Block.AIR.getId();
					}
					else mBlocks[x][y] = id;
					for (Block renderBlock : block.getRenderBlocks())
						renderBlock.initImage(x, y, this);
				}
			}
	}
	
	private void renderBlock(int aX, int aY, Graphics aG)
	{
		final Block block = Block.get(mBlocks[aX][aY]);
		if ( !block.isVisible() || block.isLiquid() || block.isFlag()) return;
		Block.render(aX, aY, mBlocks[aX][aY], aG, this);
	}
}
