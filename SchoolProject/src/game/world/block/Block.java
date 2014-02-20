package game.world.block;

import game.entity.Entity;
import game.world.World;
import java.util.HashMap;
import java.util.HashSet;
import org.newdawn.slick.Graphics;
import util.Direction;
import data.DataManager;

public class Block
{
	/**
	 * The size of one block.
	 */
	public static final int											SIZE						= 16;
	private static final HashMap<Byte, Block>						BLOCKS						= new HashMap<>();
	private static final HashMap<Integer, Byte>						COLORS						= new HashMap<>();
	private static final HashMap<TexturePackPart, HashSet<Byte>>	SNOW_IDS					= new HashMap<>();
	
	// Blocks
	public static final Block										AIR							= new Block(0, 0xffffff).setInvisible().setUnsolid();
	public static final Block										STONE						= new Block(1, 0xB89040, 0xC0C0C0);
	public static final Block										BRICK						= new Block(2, 0xB06000, 0x727272).setDestroyable(AIR);
	public static final Block										OPENED						= new Block(3, 0xC03010, 0xC03011);
	public static final Block										QUESTION					= new Block(4, 0xD8B018, 0xFFD800).setDestroyable(OPENED).setItem(Item.BANANA);
	public static final Block										GROUND_TOP_LEFT				= new Block(5, 0x00E000, 0x93FFFF);
	public static final Block										GROUND_TOP					= new Block(6, 0x00BA00, 0x00FFFF);
	public static final Block										GROUND_TOP_RIGHT			= new Block(7, 0x009300, 0x00D3D3);
	public static final Block										GROUND_STOP_LEFT			= new Block(8, 0x8CB848, 0x8EF4D5);
	public static final Block										GROUND_STOP_RIGHT			= new Block(9, 0x73963C, 0x00DDD3);
	public static final Block										GROUND_LEFT					= new Block(10, 0xF0B848, 0xAD7854);
	public static final Block										GROUND_MIDDLE				= new Block(11, 0xD3A23F, 0xAF4600);
	public static final Block										GROUND_RIGHT				= new Block(12, 0xAD8434, 0x993D00);
	public static final Block										GROUND_BOTTOM_LEFT			= new Block(13, 0xA07931, 0xAD9354);
	public static final Block										GROUND_BOTTOM				= new Block(14, 0x99722F, 0xAF6D00);
	public static final Block										GROUND_BOTTOM_RIGHT			= new Block(15, 0x936C2D, 0x995900);
	public static final Block										GROUND_BOTTOM_STOP_LEFT		= new Block(16, 0xB6B848, 0x997F00);
	public static final Block										GROUND_BOTTOM_STOP_RIGHT	= new Block(17, 0xABAD45, 0x7F6A00);
	public static final Block										SPIKES_UP					= new Block(18, 0xBCBCBC).setHurtPlayer(Direction.TOP);
	public static final Block										SPIKES_DOWN					= new Block(19, 0xB5B5B5).setHurtPlayer(Direction.BOTTOM);
	public static final Block										SPIKES_RIGHT				= new Block(20, 0xADADAD).setHurtPlayer(Direction.RIGHT);
	public static final Block										SPIKES_LEFT					= new Block(21, 0xA5A5A5).setHurtPlayer(Direction.LEFT);
	public static final Block										WATER_TOP					= new Block(22, 0x5151FF).setLiquid();
	public static final Block										WATER						= new Block(23, 0x0000C6).setLiquid();
	public static final Block										ICE							= new Block(24, 0x00FF7C).setIce();
	public static final Block										ICE_TOP_LEFT				= new Block(25, 0x96FFAA).setIce();
	public static final Block										ICE_TOP						= new Block(26, 0x00FFC9).setIce();
	public static final Block										ICE_TOP_RIGHT				= new Block(27, 0x00E2B1).setIce();
	public static final Block										ICE_STOP_LEFT				= new Block(28, 0x93BCB9);
	public static final Block										ICE_STOP_RIGHT				= new Block(29, 0xA5D3B8);
	public static final Block										ICE_LEFT					= new Block(30, 0x96C6AA);
	public static final Block										ICE_MIDDLE					= new Block(31, 0x00FFA7);
	public static final Block										ICE_RIGHT					= new Block(32, 0x00BEB1);
	public static final Block										START						= new Block(33, 0x0);
	public static final Block										END							= new Block(34, 0x0000FF).setFlag();
	public static final Block										ITEM						= new Block();
	
	// Attributes
	private final HashSet<UpdateAction>								mUpdateActions				= new HashSet<>();
	private final HashSet<HitAction>								mHitActions					= new HashSet<>();
	private final HashMap<TexturePackPart, Byte>					mIds						= new HashMap<>();
	private Item													mItem						= null;
	private Direction												mHurtDirection				= Direction.NONE;
	private Block													mDestination				= null;
	private boolean													mSolid						= true, mVisible = true, mUpdatable = false, mLiquid = false, mFlag = false,
			mIce = false, mItemBlock = false;
	
	private Block(int aId, int aRGB, int[] aRGBs)
	{
		final int texturePackParts = TexturePackPart.values().size();
		for (TexturePackPart texture : TexturePackPart.values())
			mIds.put(texture, (byte) (aId * texturePackParts + texture.getId()));
		for (TexturePackPart texture : mIds.keySet())
			BLOCKS.put(mIds.get(texture), this);
		if (aRGBs.length != texturePackParts) throw new IllegalArgumentException("Not the right number of color codes");
		for (int i = 0; i < texturePackParts; i++)
			COLORS.put(aRGBs[i], mIds.get(TexturePackPart.get((byte) i)));
		for (TexturePackPart texture : TexturePackPart.values())
		{
			HashSet<Byte> ids = SNOW_IDS.get(texture);
		}
		SNOW_IDS.add(mSnowId);
	}
	
	private Block(int aId, int aRGB)
	{
		mId = (byte) aId;
		mSnowId = mId;
		BLOCKS.put(mId, this);
		COLORS.put(aRGB, mId);
	}
	
	protected Block()
	{
		mId = Byte.MAX_VALUE;
		mSnowId = Byte.MAX_VALUE;
		BLOCKS.put(mId, this);
		mItemBlock = true;
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
	{
		for (UpdateAction action : mUpdateActions)
			action.execute(aX, aY, aWorld);
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
	public void hit(int aX, int aY, World aWorld, Entity aEntity, Direction aDirection, HashMap<Block, Direction> aOtherBlocks)
	{
		for (HitAction action : mHitActions)
			action.execute(aX, aY, aWorld, aEntity, this, aDirection, aOtherBlocks);
	}
	
	private Block setUpdatable()
	{
		mUpdatable = true;
		return this;
	}
	
	private Block setUnsolid()
	{
		mSolid = false;
		return this;
	}
	
	private Block setInvisible()
	{
		mVisible = false;
		return this;
	}
	
	private Block setFlag()
	{
		mFlag = true;
		setUpdatable();
		setUnsolid();
		mUpdateActions.add(UpdateAction.WIN);
		return this;
	}
	
	private Block setLiquid()
	{
		setUpdatable();
		setUnsolid();
		mLiquid = true;
		mUpdateActions.add(UpdateAction.LIQUID);
		return this;
	}
	
	private Block setIce()
	{
		mIce = true;
		mHitActions.add(HitAction.ICE);
		return this;
	}
	
	private Block setItem(Item aItem)
	{
		mItem = aItem;
		return this;
	}
	
	private Block setDestroyable(Block aDestination)
	{
		mDestination = aDestination;
		mHitActions.add(HitAction.DESTROY);
		return this;
	}
	
	private Block setHurtPlayer(Direction aDirection)
	{
		mHurtDirection = aDirection;
		mHitActions.add(HitAction.HURT);
		return this;
	}
	
	Direction getHurtDirection()
	{
		return mHurtDirection;
	}
	
	Block getDestination()
	{
		return mDestination;
	}
	
	Item getItem()
	{
		return mItem;
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
	 * The snow id of this block.
	 * 
	 * @return this blocks snow id.
	 */
	public byte getSnowId()
	{
		return mSnowId;
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
	 * Returns whether entities flow over this block.
	 * 
	 * @return return {@code true} if this block is an ice block and {@code false} if not.
	 */
	public boolean isIce()
	{
		return mIce;
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
	 * If this block needs to be updated every tick this returns {@code true}.
	 * 
	 * @return {@code true} if this block has to be updated and {@code false} if not.
	 */
	public boolean isUpdatable()
	{
		return mUpdatable;
	}
	
	/**
	 * Returns whether this block has to be replaces by an item.
	 * 
	 * @return {@code true} if this block represents an item and {@code false} if not.
	 */
	public boolean isItemBlock()
	{
		return mItemBlock;
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
	 * Returns the block with the given id.
	 * 
	 * @param aId
	 *            The id.
	 * @return The block.
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
		if (!COLORS.containsKey(aRGB))
		{
			if (Item.containsCode(aRGB)) return ITEM.getId();
			return -1;
		}
		return COLORS.get(aRGB);
	}
	
	/**
	 * Renders the specific block at the given position.
	 * 
	 * @param aX
	 *            The x position.
	 * @param aY
	 *            The y position.
	 * @param aId
	 *            The block id.
	 * @param g
	 *            The graphics to draw into.
	 * @param aWorld
	 *            The parent world.
	 */
	public static void render(int aX, int aY, byte aId, Graphics g, World aWorld)
	{
		final Block block = get(aId);
		final boolean isSnow = isSnowBlock(aX, aY, aWorld);
		if (block.mFlag) g.drawImage(DataManager.getImage("flag"), aX * SIZE - aWorld.getScreenX(), (aY - 7) * SIZE - aWorld.getScreenY());
		else g.drawImage(DataManager.getSplitImage(DataManager.getTexturePack(isSnow), block.getId()).getScaledCopy(SIZE, SIZE), aX * SIZE - aWorld.getScreenX(), aY * SIZE
				- aWorld.getScreenY());
	}
	
	/**
	 * Checks whether the block at the given position is a snow version.
	 * 
	 * @param aX
	 *            The x position.
	 * @param aY
	 *            The y position.
	 * @param aWorld
	 *            The parent world.
	 * @return {@code true} if the block is snow and {@code false} if not.
	 */
	public static boolean isSnowBlock(int aX, int aY, World aWorld)
	{
		return SNOW_IDS.contains(aWorld.getBlock(aX, aY));
	}
}
