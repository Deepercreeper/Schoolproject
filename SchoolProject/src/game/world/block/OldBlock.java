package game.world.block;

import game.entity.Entity;
import game.entity.Player;
import game.world.World;
import java.util.HashMap;
import org.newdawn.slick.Graphics;
import util.Rectangle;
import util.Util;
import data.DataManager;

public class OldBlock
{
	/**
	 * The size of one block.
	 */
	public static final int						SIZE							= 16;
	private static HashMap<Byte, OldBlock>		BLOCKS							= new HashMap<>();
	private static HashMap<Integer, OldBlock>	COLORS							= new HashMap<>();
	
	/**
	 * The empty block that represents no block.
	 */
	public static final OldBlock				AIR								= new OldBlock(0, 0xffffff).setInVisible().setUnSolid();
	/**
	 * The normal brown stone.
	 */
	public static final OldBlock				STONE							= new OldBlock(1, 0xB89040);
	/**
	 * A brick which is able to contain items and to be broken.
	 */
	public static final OldBlock				BRICK							= new BrickBlock(2, 0xB06000);
	/**
	 * The opened block which is set after opening a question block.
	 */
	public static final OldBlock				OPENED							= new OldBlock(3, 0xC03010);
	/**
	 * The question block contains items and is transformed into the opened block after hitting.
	 */
	public static final OldBlock				QUESTION						= new QuestionBlock(4, 0xD8B018);
	/**
	 * The grass at the top left corner of an earth block.
	 */
	public static final OldBlock				GROUND_TOP_LEFT					= new OldBlock(5, 0x00E000);
	/**
	 * The grass at the top of an earth block.
	 */
	public static final OldBlock				GROUND_TOP						= new OldBlock(6, 0x00BA00);
	/**
	 * The grass at the top right corner of an earth block.
	 */
	public static final OldBlock				GROUND_TOP_RIGHT				= new OldBlock(7, 0x009300);
	/**
	 * The left wall of an earth block.
	 */
	public static final OldBlock				GROUND_LEFT						= new OldBlock(8, 0xF0B848);
	/**
	 * The inner block of an earth block.
	 */
	public static final OldBlock				GROUND_MIDDLE					= new OldBlock(9, 0xD3A23F);
	/**
	 * The right wall of an earth block.
	 */
	public static final OldBlock				GROUND_RIGHT					= new OldBlock(10, 0xAD8434);
	/**
	 * The grass block that is the corner between a top and a left wall.
	 */
	public static final OldBlock				GROUND_LEFT_STOP				= new OldBlock(11, 0x8CB848);
	/**
	 * The grass block that is the corner between a top and a right wall.
	 */
	public static final OldBlock				GROUND_RIGHT_STOP				= new OldBlock(12, 0x73963C);
	/**
	 * The left bottom block of an earth block.
	 */
	public static final OldBlock				GROUND_BOTTOM_LEFT				= new OldBlock(13, 0xA07931);
	/**
	 * The middle bottom block of an earth block.
	 */
	public static final OldBlock				GROUND__BOTTOM					= new OldBlock(14, 0x99722F);
	/**
	 * The right bottom block of an earth block.
	 */
	public static final OldBlock				GROUND_BOTTOM_RIGHT				= new OldBlock(15, 0x936C2D);
	/**
	 * The block that is the corner between a bottom and a left wall.
	 */
	public static final OldBlock				GROUND_LEFT_BOTTOM_STOP			= new OldBlock(16, 0xB6B848);
	/**
	 * The block that is the corner between a bottom and a right wall.
	 */
	public static final OldBlock				GROUND_RIGHT_BOTTOM_STOP		= new OldBlock(17, 0xABAD45);
	/**
	 * Spikes that point up.
	 */
	public static final OldBlock				SPIKES_UP						= new SpikeBlock(18, 0xBCBCBC, 0);
	/**
	 * Spikes that point down.
	 */
	public static final OldBlock				SPIKES_DOWN						= new SpikeBlock(19, 0xB5B5B5, 1);
	/**
	 * Spikes that point right.
	 */
	public static final OldBlock				SPIKES_RIGHT					= new SpikeBlock(20, 0xADADAD, 2);
	/**
	 * Spikes that point left.
	 */
	public static final OldBlock				SPIKES_LEFT						= new SpikeBlock(21, 0xA5A5A5, 3);
	/**
	 * The top of water.
	 */
	public static final OldBlock				WATER_TOP						= new LiquidBlock(22, 0x5151FF);
	/**
	 * Simply water.
	 */
	public static final OldBlock				WATER							= new LiquidBlock(23, 0x0000C6);
	/**
	 * An outstanding block of ice.
	 */
	public static final OldBlock				ICE_BLOCK						= new OldBlock(24, 0x00FF7C);
	/**
	 * The normal stone in the snow-world.
	 */
	public static final OldBlock				SNOW_STONE						= new OldBlock(25, 0xC0C0C0).setSnowBlock();
	/**
	 * The normal brick in the snow-world.
	 */
	public static final OldBlock				SNOW_BRICK						= new BrickBlock(26, 0x727272).setSnowBlock();
	/**
	 * An opened block, which gets set after hitting the snow-question-block.
	 */
	public static final OldBlock				SNOW_OPENED						= new OldBlock(27, 0xC03011).setSnowBlock();
	/**
	 * A question block in the snow-world.
	 */
	public static final OldBlock				SNOW_QUESTION					= new QuestionBlock(28, 0xFFD800).setSnowBlock();
	/**
	 * The snow at a top left corner of an earth-block.
	 */
	public static final OldBlock				SNOW_GROUND_TOP_LEFT			= new OldBlock(29, 0x93FFFF).setSnowBlock();
	/**
	 * The snow at a top of an earth-block.
	 */
	public static final OldBlock				SNOW_GROUND_TOP_MIDDLE			= new OldBlock(30, 0x00FFFF).setSnowBlock();
	/**
	 * The snow at a top right corner of an earth-block.
	 */
	public static final OldBlock				SNOW_GROUND_TOP_RIGHT			= new OldBlock(31, 0x00D3D3).setSnowBlock();
	/**
	 * The left wall of an earth block in the snow-world.
	 */
	public static final OldBlock				SNOW_GROUND_LEFT				= new OldBlock(32, 0xAD7854).setSnowBlock();
	/**
	 * The inner block of an earth-block in the snow-world.
	 */
	public static final OldBlock				SNOW_GROUND_MIDDLE				= new OldBlock(33, 0xAF4600).setSnowBlock();
	/**
	 * The right wall of an earth block in the snow-world.
	 */
	public static final OldBlock				SNOW_GROUND_RIGHT				= new OldBlock(34, 0x993D00).setSnowBlock();
	/**
	 * The snow block that is the corner between a top-snow-block and a left wall.
	 */
	public static final OldBlock				SNOW_GROUND_LEFT_STOP			= new OldBlock(35, 0x8EF4D5).setSnowBlock();
	/**
	 * The snow block that is the corner between a top-snow-block and a right wall.
	 */
	public static final OldBlock				SNOW_GROUND_RIGHT_STOP			= new OldBlock(36, 0x00DDD3).setSnowBlock();
	/**
	 * The left bottom block of an earth block in snow-world.
	 */
	public static final OldBlock				SNOW_GROUND_BOTTOM_LEFT			= new OldBlock(37, 0xAD9354).setSnowBlock();
	/**
	 * The bottom block of an earth block in snow-world.
	 */
	public static final OldBlock				SNOW_GROUND_BOTTOM				= new OldBlock(38, 0xAF6D00).setSnowBlock();
	/**
	 * The right bottom block of an earth block in snow-world.
	 */
	public static final OldBlock				SNOW_GROUND_BOTTOM_RIGHT		= new OldBlock(39, 0x995900).setSnowBlock();
	/**
	 * The block that is the corner between a bottom-block and a left wall in snow-world.
	 */
	public static final OldBlock				SNOW_GROUND_LEFT_BOTTOM_STOP	= new OldBlock(40, 0x997F00).setSnowBlock();
	/**
	 * The block that is the corner between a bottom-block and a right wall in snow-world.
	 */
	public static final OldBlock				SNOW_GROUND_RIGHT_BOTTOM_STOP	= new OldBlock(41, 0x7F6A00).setSnowBlock();
	/**
	 * The ice block that is the corner between an top ice block and a right ice wall.
	 */
	public static final OldBlock				ICE_GROUND_RIGHT_STOP			= new OldBlock(42, 0xA5D3B8).setSnowBlock();
	/**
	 * The ice block that is the corner between an top ice block and a left ice wall.
	 */
	public static final OldBlock				ICE_GROUND_LEFT_STOP			= new OldBlock(43, 0x93BCB9).setSnowBlock();
	/**
	 * The top left corner of an ice block.
	 */
	public static final OldBlock				ICE_GROUND_TOP_LEFT				= new OldBlock(44, 0x96FFAA).setSnowBlock();
	/**
	 * The top of an ice block.
	 */
	public static final OldBlock				ICE_GROUND_TOP_MIDDLE			= new OldBlock(45, 0x00FFC9).setSnowBlock();
	/**
	 * The top right corner of an ice block.
	 */
	public static final OldBlock				ICE_GROUND_TOP_RIGHT			= new OldBlock(46, 0x00E2B1).setSnowBlock();
	/**
	 * The left wall of an ice block.
	 */
	public static final OldBlock				ICE_GROUND_LEFT					= new OldBlock(47, 0x96C6AA).setSnowBlock();
	/**
	 * The inner block of ice blocks.
	 */
	public static final OldBlock				ICE_GROUND_MIDDLE				= new OldBlock(48, 0x00FFA7).setSnowBlock();
	/**
	 * The right wall of an ice block.
	 */
	public static final OldBlock				ICE_GROUND_RIGHT				= new OldBlock(49, 0x00BEB1).setSnowBlock();
	/**
	 * The position of the start point of the player.
	 */
	public static final OldBlock				START							= new OldBlock(50, 0x0);
	/**
	 * The position of the finish point of the level.
	 */
	public static final OldBlock				END								= new EndBlock(51, 0x0000FF);
	
	private final byte							mId;
	
	private boolean								mSolid							= true, mVisible = true, mUpdatable = false, mSnowBlock = false, mLiquid = false;
	
	/**
	 * Creates a new block with a unique id and a RGB value that identifies this block when loading an image to create a level.
	 * 
	 * @param aId
	 *            The id of this block type.
	 * @param aRGB
	 *            The integer code of the color that you find inside the level image to represent this block.
	 */
	protected OldBlock(int aId, int aRGB)
	{
		mId = (byte) aId;
		BLOCKS.put(mId, this);
		COLORS.put(aRGB, this);
	}
	
	/**
	 * Makes this block invisible so it is not rendered.
	 * 
	 * @return this.
	 */
	protected OldBlock setInVisible()
	{
		mVisible = false;
		return this;
	}
	
	/**
	 * Sets this block to the snow version.
	 * 
	 * @return this.
	 */
	protected OldBlock setSnowBlock()
	{
		mSnowBlock = true;
		return this;
	}
	
	/**
	 * Sets whether this block is solid.
	 * 
	 * @return this.
	 */
	protected OldBlock setUnSolid()
	{
		mSolid = false;
		return this;
	}
	
	/**
	 * Sets whether this block is a liquid block.
	 * 
	 * @return this.
	 */
	protected OldBlock setLiquid()
	{
		mLiquid = true;
		return this;
	}
	
	/**
	 * Sets whether this block needs to be updates every tick.
	 * 
	 * @return this.
	 */
	protected OldBlock setUpdatable()
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
		g.drawImage(DataManager.getSplitImage(DataManager.getTexturePack(true), getImageId()), aX * SIZE - aWorld.getScreenX(), aY * SIZE - aWorld.getScreenY());
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
	public static OldBlock get(byte aId)
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
	public static OldBlock get(int aRGB)
	{
		return COLORS.get(aRGB);
	}
	
	/**
	 * The index that says at which position in the split image lays inside the parent image.
	 * 
	 * @return the image index.
	 */
	protected int getImageId()
	{
		return mId;
	}
	
	protected static boolean isPlayerInside(int aX, int aY, World aWorld)
	{
		return aWorld.getPlayer().getRect().intersects(new Rectangle(aX * SIZE, (aY) * SIZE, SIZE, SIZE));
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
		if (Util.hitsBlockBottom(aX, aY, aEntity)) hitBottom(aX, aY, aWorld, aEntity, cannon);
		if (Util.hitsBlockTop(aX, aY, aEntity)) hitTop(aX, aY, aWorld, aEntity, cannon);
		if (Util.hitsBlockLeft(aX, aY, aEntity)) hitLeft(aX, aY, aWorld, aEntity, cannon);
		if (Util.hitsBlockRight(aX, aY, aEntity)) hitRight(aX, aY, aWorld, aEntity, cannon);
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
