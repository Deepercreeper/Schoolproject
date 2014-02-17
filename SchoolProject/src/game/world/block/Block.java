package game.world.block;

import game.entity.Entity;
import game.entity.Player;
import game.world.World;
import java.util.HashMap;
import org.newdawn.slick.Graphics;
import util.Rectangle;
import data.DataManager;

public class Block
{
	/**
	 * The size of one block.
	 */
	public static final int					SIZE							= 16;
	private static HashMap<Byte, Block>		BLOCKS							= new HashMap<>();
	private static HashMap<Integer, Byte>	COLORS							= new HashMap<>();
	
	/**
	 * The empty block that represents no block.
	 */
	public static final Block				AIR								= new Block(0, 0xffffff).setInVisible().setUnSolid();
	/**
	 * The normal brown stone.
	 */
	public static final Block				STONE							= new Block(1, 0xB89040);
	/**
	 * A brick which is able to contain items and to be broken.
	 */
	public static final Block				BRICK							= new BrickBlock(2, 0xB06000);
	/**
	 * The opened block which is set after opening a question block.
	 */
	public static final Block				OPENED							= new Block(3, 0xC03010);
	/**
	 * The question block contains items and is transformed into the opened block after hitting.
	 */
	public static final Block				QUESTION						= new QuestionBlock(4, 0xD8B018);
	/**
	 * The grass at the top left corner of an earth block.
	 */
	public static final Block				GROUND_TOP_LEFT					= new Block(5, 0x00E000);
	/**
	 * The grass at the top of an earth block.
	 */
	public static final Block				GROUND_TOP						= new Block(6, 0x00BA00);
	/**
	 * The grass at the top right corner of an earth block.
	 */
	public static final Block				GROUND_TOP_RIGHT				= new Block(7, 0x009300);
	/**
	 * The left wall of an earth block.
	 */
	public static final Block				GROUND_LEFT						= new Block(8, 0xF0B848);
	/**
	 * The inner block of an earth block.
	 */
	public static final Block				GROUND_MIDDLE					= new Block(9, 0xD3A23F);
	/**
	 * The right wall of an earth block.
	 */
	public static final Block				GROUND_RIGHT					= new Block(10, 0xAD8434);
	/**
	 * The grass block that is the corner between a top and a left wall.
	 */
	public static final Block				GROUND_LEFT_STOP				= new Block(11, 0x8CB848);
	/**
	 * The grass block that is the corner between a top and a right wall.
	 */
	public static final Block				GROUND_RIGHT_STOP				= new Block(12, 0x73963C);
	/**
	 * The left bottom block of an earth block.
	 */
	public static final Block				GROUND_BOTTOM_LEFT				= new Block(13, 0xA07931);
	/**
	 * The middle bottom block of an earth block.
	 */
	public static final Block				GROUND__BOTTOM					= new Block(14, 0x99722F);
	/**
	 * The right bottom block of an earth block.
	 */
	public static final Block				GROUND_BOTTOM_RIGHT				= new Block(15, 0x936C2D);
	/**
	 * The block that is the corner between a bottom and a left wall.
	 */
	public static final Block				GROUND_LEFT_BOTTOM_STOP			= new Block(16, 0xB6B848);
	/**
	 * The block that is the corner between a bottom and a right wall.
	 */
	public static final Block				GROUND_RIGHT_BOTTOM_STOP		= new Block(17, 0xABAD45);
	/**
	 * Spikes that point up.
	 */
	public static final Block				SPIKES_UP						= new SpikeBlock(18, 0xBCBCBC, 0);
	/**
	 * Spikes that point down.
	 */
	public static final Block				SPIKES_DOWN						= new SpikeBlock(19, 0xB5B5B5, 1);
	/**
	 * Spikes that point right.
	 */
	public static final Block				SPIKES_RIGHT					= new SpikeBlock(20, 0xADADAD, 2);
	/**
	 * Spikes that point left.
	 */
	public static final Block				SPIKES_LEFT						= new SpikeBlock(21, 0xA5A5A5, 3);
	/**
	 * The top of water.
	 */
	public static final Block				WATER_TOP						= new LiquidBlock(22, 0x5151FF);
	/**
	 * Simply water.
	 */
	public static final Block				WATER							= new LiquidBlock(23, 0x0000C6);
	/**
	 * An outstanding block of ice.
	 */
	public static final Block				ICE_BLOCK						= new Block(24, 0x00FF7C);
	/**
	 * The normal stone in the snow-world.
	 */
	public static final Block				SNOW_STONE						= new Block(25, 0xC0C0C0).setSnowBlock();
	/**
	 * The normal brick in the snow-world.
	 */
	public static final Block				SNOW_BRICK						= new BrickBlock(26, 0x727272).setSnowBlock();
	/**
	 * An opened block, which gets set after hitting the snow-question-block.
	 */
	public static final Block				SNOW_OPENED						= new Block(27, 0xC03011).setSnowBlock();
	/**
	 * A question block in the snow-world.
	 */
	public static final Block				SNOW_QUESTION					= new QuestionBlock(28, 0xFFD800).setSnowBlock();
	/**
	 * The snow at a top left corner of an earth-block.
	 */
	public static final Block				SNOW_GROUND_TOP_LEFT			= new Block(29, 0x93FFFF).setSnowBlock();
	/**
	 * The snow at a top of an earth-block.
	 */
	public static final Block				SNOW_GROUND_TOP_MIDDLE			= new Block(30, 0x00FFFF).setSnowBlock();
	/**
	 * The snow at a top right corner of an earth-block.
	 */
	public static final Block				SNOW_GROUND_TOP_RIGHT			= new Block(31, 0x00D3D3).setSnowBlock();
	/**
	 * The left wall of an earth block in the snow-world.
	 */
	public static final Block				SNOW_GROUND_LEFT				= new Block(32, 0xAD7854).setSnowBlock();
	/**
	 * The inner block of an earth-block in the snow-world.
	 */
	public static final Block				SNOW_GROUND_MIDDLE				= new Block(33, 0xAF4600).setSnowBlock();
	/**
	 * The right wall of an earth block in the snow-world.
	 */
	public static final Block				SNOW_GROUND_RIGHT				= new Block(34, 0x993D00).setSnowBlock();
	/**
	 * The snow block that is the corner between a top-snow-block and a left wall.
	 */
	public static final Block				SNOW_GROUND_LEFT_STOP			= new Block(35, 0x8EF4D5).setSnowBlock();
	/**
	 * The snow block that is the corner between a top-snow-block and a right wall.
	 */
	public static final Block				SNOW_GROUND_RIGHT_STOP			= new Block(36, 0x00DDD3).setSnowBlock();
	/**
	 * The left bottom block of an earth block in snow-world.
	 */
	public static final Block				SNOW_GROUND_BOTTOM_LEFT			= new Block(37, 0xAD9354).setSnowBlock();
	/**
	 * The bottom block of an earth block in snow-world.
	 */
	public static final Block				SNOW_GROUND_BOTTOM				= new Block(38, 0xAF6D00).setSnowBlock();
	/**
	 * The right bottom block of an earth block in snow-world.
	 */
	public static final Block				SNOW_GROUND_BOTTOM_RIGHT		= new Block(39, 0x995900).setSnowBlock();
	/**
	 * The block that is the corner between a bottom-block and a left wall in snow-world.
	 */
	public static final Block				SNOW_GROUND_LEFT_BOTTOM_STOP	= new Block(40, 0x997F00).setSnowBlock();
	/**
	 * The block that is the corner between a bottom-block and a right wall in snow-world.
	 */
	public static final Block				SNOW_GROUND_RIGHT_BOTTOM_STOP	= new Block(41, 0x7F6A00).setSnowBlock();
	/**
	 * The ice block that is the corner between an top ice block and a right ice wall.
	 */
	public static final Block				ICE_GROUND_RIGHT_STOP			= new Block(42, 0xA5D3B8).setSnowBlock();
	/**
	 * The ice block that is the corner between an top ice block and a left ice wall.
	 */
	public static final Block				ICE_GROUND_LEFT_STOP			= new Block(43, 0x93BCB9).setSnowBlock();
	/**
	 * The top left corner of an ice block.
	 */
	public static final Block				ICE_GROUND_TOP_LEFT				= new Block(44, 0x96FFAA).setSnowBlock();
	/**
	 * The top of an ice block.
	 */
	public static final Block				ICE_GROUND_TOP_MIDDLE			= new Block(45, 0x00FFC9).setSnowBlock();
	/**
	 * The top right corner of an ice block.
	 */
	public static final Block				ICE_GROUND_TOP_RIGHT			= new Block(46, 0x00E2B1).setSnowBlock();
	/**
	 * The left wall of an ice block.
	 */
	public static final Block				ICE_GROUND_LEFT					= new Block(47, 0x96C6AA).setSnowBlock();
	/**
	 * The inner block of ice blocks.
	 */
	public static final Block				ICE_GROUND_MIDDLE				= new Block(48, 0x00FFA7).setSnowBlock();
	/**
	 * The right wall of an ice block.
	 */
	public static final Block				ICE_GROUND_RIGHT				= new Block(49, 0x00BEB1).setSnowBlock();
	/**
	 * The position of the start point of the player.
	 */
	public static final Block				START							= new Block(50, 0x0);
	/**
	 * The position of the finish point of the level.
	 */
	public static final Block				END								= new EndBlock(51, 0x0000FF);
	
	private final byte						mId;
	
	private boolean							mSolid							= true, mVisible = true, mUpdatable = false, mSnowBlock = false, mLiquid = false;
	
	/**
	 * Creates a new block with a unique id and a RGB value that identifies this block when loading an image to create a level.
	 * 
	 * @param aId
	 *            The id of this block type.
	 * @param aRGB
	 *            The integer code of the color that you find inside the level image to represent this block.
	 */
	protected Block(int aId, int aRGB)
	{
		mId = (byte) aId;
		BLOCKS.put(mId, this);
		COLORS.put(aRGB, mId);
	}
	
	/**
	 * Makes this block invisible so it is not rendered.
	 * 
	 * @return this.
	 */
	protected Block setInVisible()
	{
		mVisible = false;
		return this;
	}
	
	/**
	 * Sets this block to the snow version.
	 * 
	 * @return this.
	 */
	protected Block setSnowBlock()
	{
		mSnowBlock = true;
		return this;
	}
	
	/**
	 * Sets whether this block is solid.
	 * 
	 * @return this.
	 */
	protected Block setUnSolid()
	{
		mSolid = false;
		return this;
	}
	
	/**
	 * Sets whether this block is a liquid block.
	 * 
	 * @return this.
	 */
	protected Block setLiquid()
	{
		mLiquid = true;
		return this;
	}
	
	/**
	 * Sets whether this block needs to be updates every tick.
	 * 
	 * @return this.
	 */
	protected Block setUpdatable()
	{
		mUpdatable = true;
		return this;
	}
	
	/**
	 * The id of this block.
	 * 
	 * @return this blocks id.
	 */
	public byte getId()
	{
		return mId;
	}
	
	/**
	 * Renders this block. the position is only the position index of this block, so it should be rendered at {@code SIZE *} the position.<br>
	 * By default draws the block image with the id as index at the given position.
	 * 
	 * @param aX
	 *            the x index.
	 * @param aY
	 *            the y index.
	 * @param g
	 *            the graphics to render into.
	 * @param aWorld
	 *            the parent world.
	 */
	public void render(int aX, int aY, Graphics g, World aWorld)
	{
		g.drawImage(DataManager.getSplitImage(DataManager.getTexturePack(), getImageIndex()), aX * SIZE - aWorld.getScreenX(), aY * SIZE - aWorld.getScreenY());
	}
	
	/**
	 * If this block is updatable this method is invoked each tick.
	 * 
	 * @param aX
	 *            the x index of this block.
	 * @param aY
	 *            the y index of this block.
	 * @param aWorld
	 *            the parent world.
	 */
	public void update(int aX, int aY, World aWorld)
	{}
	
	/**
	 * If this block needs to be updated every tick this returns {@code true}.
	 * 
	 * @return {@code true} if this block has to be updated and {@code false} if not.
	 */
	public boolean isUpdatable()
	{
		return mUpdatable;
	}
	
	/**
	 * If this block is a block used in snow worlds.
	 * 
	 * @return {@code true} if this is a snow block or {@code false} if not.
	 */
	public boolean isSnowBlock()
	{
		return mSnowBlock;
	}
	
	/**
	 * Returns whether this block allows to move through.
	 * 
	 * @return {@code true} if this block is solid and {@code false} if not.
	 */
	public boolean isSolid()
	{
		return mSolid;
	}
	
	/**
	 * Returns whether this block is a liquid block
	 * 
	 * @return {@code true} if this block is liquid and {@code false} if not.
	 */
	public boolean isLiquid()
	{
		return mLiquid;
	}
	
	/**
	 * Returns whether this block has to be rendered.
	 * 
	 * @return {@code true} if this block is visible and {@code false} if not.
	 */
	public boolean isVisible()
	{
		return mVisible;
	}
	
	/**
	 * Returns the block with the given id.
	 * 
	 * @param aId
	 *            The id of the block you want.
	 * @return the block with the given id or {@code null} if no block has this id.
	 */
	public static Block get(byte aId)
	{
		return BLOCKS.get(aId);
	}
	
	/**
	 * Returns the id of the block with the given color code.
	 * 
	 * @param aRGB
	 *            The color of any pixel inside a world map image.
	 * @return the id of the searched block.
	 */
	public static byte get(int aRGB)
	{
		if ( !COLORS.containsKey(aRGB)) return -1;
		return COLORS.get(aRGB);
	}
	
	/**
	 * The index that says at which position in the split image lays inside the parent image.
	 * 
	 * @return the image index.
	 */
	protected int getImageIndex()
	{
		return mId;
	}
	
	protected boolean isPlayerInside(int aX, int aY, World aWorld)
	{
		return aWorld.getPlayer().getRect().intersects(new Rectangle(aX * SIZE, (aY) * SIZE, SIZE, SIZE));
	}
	
	/**
	 * If any entity hits this block, this method returns whether this block was hit from the bottom side.
	 * 
	 * @param aX
	 *            the x position.
	 * @param aY
	 *            the y position.
	 * @param aEntity
	 *            the hitting entity.
	 * @return {@code true} if the entity hit this block with its head and {@code false} if not.
	 */
	private boolean hitsBottom(int aX, int aY, Entity aEntity)
	{
		return aEntity.getY() >= (aY + 1) * SIZE && aEntity.getX() + aEntity.getWidth() > aX * SIZE && aEntity.getX() < (aX + 1) * SIZE;
	}
	
	/**
	 * If any entity hits this block, this method returns whether this block was hit from the left side.
	 * 
	 * @param aX
	 *            the x position.
	 * @param aY
	 *            the y position.
	 * @param aEntity
	 *            the hitting entity.
	 * @return {@code true} if the entity hit this block with its head and {@code false} if not.
	 */
	private boolean hitsLeft(int aX, int aY, Entity aEntity)
	{
		return aEntity.getX() + aEntity.getWidth() <= aX * SIZE && aEntity.getY() + aEntity.getHeight() > aY * SIZE && aEntity.getY() < (aY + 1) * SIZE;
	}
	
	/**
	 * If any entity hits this block, this method returns whether this block was hit from the right side.
	 * 
	 * @param aX
	 *            the x position.
	 * @param aY
	 *            the y position.
	 * @param aEntity
	 *            the hitting entity.
	 * @return {@code true} if the entity hit this block with its head and {@code false} if not.
	 */
	private boolean hitsRight(int aX, int aY, Entity aEntity)
	{
		return aEntity.getX() <= (aX + 1) * SIZE && aEntity.getY() + aEntity.getHeight() > aY * SIZE && aEntity.getY() < (aY + 1) * SIZE;
	}
	
	/**
	 * If any entity hits this block, this method returns whether this block was hit from the top side.
	 * 
	 * @param aX
	 *            the x position.
	 * @param aY
	 *            the y position.
	 * @param aEntity
	 *            the hitting entity.
	 * @return {@code true} if the entity hit this block with its head and {@code false} if not.
	 */
	private boolean hitsTop(int aX, int aY, Entity aEntity)
	{
		return aEntity.getY() + aEntity.getHeight() <= aY * SIZE && aEntity.getX() + aEntity.getWidth() > aX * SIZE && aEntity.getX() < (aX + 1) * SIZE;
	}
	
	/**
	 * Invoked, when any entity hits this block.
	 * 
	 * @param aX
	 *            the x position.
	 * @param aY
	 *            the y position.
	 * @param aWorld
	 *            the parent world.
	 * @param aEntity
	 *            the hitting entity.
	 */
	public void hit(int aX, int aY, World aWorld, Entity aEntity)
	{
		final boolean cannon = (aEntity instanceof Player) && ((Player) aEntity).isCannonBall();
		if (hitsBottom(aX, aY, aEntity)) hitBottom(aX, aY, aWorld, aEntity, cannon);
		if (hitsTop(aX, aY, aEntity)) hitTop(aX, aY, aWorld, aEntity, cannon);
		if (hitsLeft(aX, aY, aEntity)) hitLeft(aX, aY, aWorld, aEntity, cannon);
		if (hitsRight(aX, aY, aEntity)) hitRight(aX, aY, aWorld, aEntity, cannon);
	}
	
	protected void hitBottom(int aX, int aY, World aWorld, Entity aEntity, boolean aCannon)
	{}
	
	protected void hitTop(int aX, int aY, World aWorld, Entity aEntity, boolean aCannon)
	{}
	
	protected void hitLeft(int aX, int aY, World aWorld, Entity aEntity, boolean aCannon)
	{}
	
	protected void hitRight(int aX, int aY, World aWorld, Entity aEntity, boolean aCannon)
	{}
}
