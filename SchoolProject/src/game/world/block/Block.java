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
	public static final int						SIZE						= 16;
	private static final HashMap<Byte, Block>	BLOCKS						= new HashMap<>();
	private static final HashMap<Integer, Byte>	COLORS						= new HashMap<>();
	private static final HashMap<Byte, Texture>	TEXTURES					= new HashMap<>();
	
	// Blocks
	public static final Block					AIR							= new Block(0, 0xffffff).setInvisible().setUnsolid();
	public static final Block					STONE						= new Block(1, new int[] { 0xB89040, 0xC0C0C0, 0xB89040 });
	public static final Block					BRICK						= new Block(2, new int[] { 0xB06000, 0x727272, 0xB06000 }).setDestroyable(AIR);
	public static final Block					OPENED						= new Block(3, new int[] { 0xC03010, 0xC03011, 0xC03010 });
	public static final Block					QUESTION					= new Block(4, new int[] { 0xD8B018, 0xFFD800, 0xD8B018 }).setDestroyable(OPENED).setItem(Item.SUPER_BANANA);
	public static final Block					GROUND_TOP_LEFT				= new Block(5, new int[] { 0x00E000, 0x93FFFF, 0xFFA359 });
	public static final Block					GROUND_TOP					= new Block(6, new int[] { 0x00BA00, 0x00FFFF, 0xFF7400 });
	public static final Block					GROUND_TOP_RIGHT			= new Block(7, new int[] { 0x009300, 0x00D3D3, 0xB75200 });
	public static final Block					GROUND_STOP_LEFT			= new Block(8, new int[] { 0x8CB848, 0x8EF4D5, 0x87612D });
	public static final Block					GROUND_STOP_RIGHT			= new Block(9, new int[] { 0x73963C, 0x00DDD3, 0x704A00 });
	public static final Block					GROUND_LEFT					= new Block(10, new int[] { 0xF0B848, 0xAD7854, 0x87512D });
	public static final Block					GROUND_MIDDLE				= new Block(11, new int[] { 0xD3A23F, 0xAF4600, 0x873600 });
	public static final Block					GROUND_RIGHT				= new Block(12, new int[] { 0xAD8434, 0x993D00, 0x702C00 });
	public static final Block					GROUND_BOTTOM_LEFT			= new Block(13, new int[] { 0xA07931, 0xAD9354, 0xBC5F3A });
	public static final Block					GROUND_BOTTOM				= new Block(14, new int[] { 0x99722F, 0xAF6D00, 0xBE3600 });
	public static final Block					GROUND_BOTTOM_RIGHT			= new Block(15, new int[] { 0x936C2D, 0x995900, 0xA02D00 });
	public static final Block					GROUND_BOTTOM_STOP_LEFT		= new Block(16, new int[] { 0xB6B848, 0x997F00, 0xA05500 });
	public static final Block					GROUND_BOTTOM_STOP_RIGHT	= new Block(17, new int[] { 0xABAD45, 0x7F6A00, 0xBC803A });
	public static final Block					SPIKES_UP					= new Block(18, 0xBCBCBC).setHurtPlayer(Direction.TOP);
	public static final Block					SPIKES_DOWN					= new Block(19, 0xB5B5B5).setHurtPlayer(Direction.BOTTOM);
	public static final Block					SPIKES_RIGHT				= new Block(20, 0xADADAD).setHurtPlayer(Direction.RIGHT);
	public static final Block					SPIKES_LEFT					= new Block(21, 0xA5A5A5).setHurtPlayer(Direction.LEFT);
	public static final Block					WATER_TOP					= new Block(22, 0x5151FF).setLiquid();
	public static final Block					WATER						= new Block(23, 0x0000C6).setLiquid();
	public static final Block					ICE							= new Block(24, 0x00FF7C).setIce();
	public static final Block					ICE_TOP_LEFT				= new Block(25, 0x96FFAA).setIce();
	public static final Block					ICE_TOP						= new Block(26, 0x00FFC9).setIce();
	public static final Block					ICE_TOP_RIGHT				= new Block(27, 0x00E2B1).setIce();
	public static final Block					ICE_STOP_LEFT				= new Block(28, 0x93BCB9);
	public static final Block					ICE_STOP_RIGHT				= new Block(29, 0xA5D3B8);
	public static final Block					ICE_LEFT					= new Block(30, 0x96C6AA);
	public static final Block					ICE_MIDDLE					= new Block(31, 0x00FFA7);
	public static final Block					ICE_RIGHT					= new Block(32, 0x00BEB1);
	public static final Block					PLATFORM					= new Block(33, 0x74FF8C);
	public static final Block					BIGGER_ROCK_LEFT			= new Block(34, 0xD34300);
	public static final Block					BIGGER_ROCK_RIGHT			= new Block(35, 0xFF5200);
	public static final Block					BIGGER_ROCK_UP				= new Block(36, 0xFF503D);
	public static final Block					LARGE_ROCK_TOPLEFT			= new Block(37, 0xFF5188);
	public static final Block					LARGE_ROCK_TOP				= new Block(38, 0xFF51D9);
	public static final Block					LARGE_ROCK_TOPRIGHT			= new Block(39, 0xFF88D9);
	public static final Block					LARGE_ROCK_LEFT				= new Block(40, 0xFF0084);
	public static final Block					LARGE_ROCK_MIDDLE			= new Block(41, 0xFF00C6);
	public static final Block					LARGE_ROCK_RIGHT			= new Block(42, 0xFF71C6);
	/*public static final Block					LARGE_ROCK_DOWNLEFT			= new Block(43, 0xB5003E);
	public static final Block					LARGE_ROCK_DOWN				= new Block(44, 0xB5008D);
	public static final Block					LARGE_ROCK_DOWNRIGHT		= new Block(45, 0xB5618D);
	public static final Block					BIGGER_ROCK_DOWN			= new Block(46, 0xCC1400);
	public static final Block					ROCK   					    = new Block(47, 0xFF1C00);
	public static final Block					MUD							= new Block(48, 0x788000);
	public static final Block					MUD_TOP						= new Block(49, 0x7C7F74);*/
	public static final Block					START						= new Block(50, 0x0);
	public static final Block					END							= new Block(51, 0x0000FF).setFlag();
	public static final Block					ITEM						= new Block();
	
	// Attributes
	private final HashSet<UpdateAction>			mUpdateActions				= new HashSet<>();
	private final HashSet<HitAction>			mHitActions					= new HashSet<>();
	private final HashMap<Texture, Byte>		mIds						= new HashMap<>();
	private Item								mItem						= null;
	private Direction							mHurtDirection				= Direction.NONE;
	private Block								mDestination				= null;
	private boolean								mSolid						= true, mVisible = true, mUpdatable = false, mLiquid = false, mFlag = false, mIce = false, mItemBlock = false;
	
	private Block(int aId, int[] aRGBs)
	{
		final int textures = Texture.values().size();
		for (Texture texture : Texture.values())
			mIds.put(texture, (byte) (aId * textures + texture.getId()));
		for (Texture texture : mIds.keySet())
			BLOCKS.put(mIds.get(texture), this);
		if (aRGBs.length != textures) throw new IllegalArgumentException("Not the right number of color codes");
		for (int i = 0; i < textures; i++ )
			COLORS.put(aRGBs[i], mIds.get(Texture.get((byte) i)));
		for (Texture texture : Texture.values())
			TEXTURES.put(mIds.get(texture), texture);
	}
	
	private Block(int aId, int aRGB)
	{
		byte id = (byte) (aId * Texture.values().size());
		for (Texture texture : Texture.values())
			mIds.put(texture, id);
		BLOCKS.put(id, this);
		COLORS.put(aRGB, id);
		TEXTURES.put(id, Texture.NORMAL);
	}
	
	protected Block()
	{
		byte id = Byte.MAX_VALUE;
		for (Texture texture : Texture.values())
			mIds.put(texture, id);
		BLOCKS.put(id, this);
		TEXTURES.put(id, Texture.NORMAL);
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
	 * The texture depending id of this block.
	 * 
	 * @return this blocks id.
	 */
	public byte getId(Texture aTexture)
	{
		return mIds.get(aTexture);
	}
	
	/**
	 * The id of this block.
	 * 
	 * @return this blocks id.
	 */
	public byte getId()
	{
		return mIds.get(Texture.NORMAL);
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
		if ( !COLORS.containsKey(aRGB))
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
		final Texture texture = getBlockTexture(aX, aY, aWorld);
		if (block.mFlag) g.drawImage(DataManager.getImage("flag"), aX * SIZE - aWorld.getScreenX(), (aY - 7) * SIZE - aWorld.getScreenY());
		else g.drawImage(DataManager.getTextureImage(DataManager.getTexturePack(), texture, block.getId(texture) / Texture.values().size()).getScaledCopy(SIZE, SIZE), aX * SIZE - aWorld.getScreenX(),
				aY * SIZE - aWorld.getScreenY());
	}
	
	/**
	 * Checks the texture of the block at the given position.
	 * 
	 * @param aX
	 *            The x position.
	 * @param aY
	 *            The y position.
	 * @param aWorld
	 *            The parent world.
	 * @return the texture of the given block.
	 */
	public static Texture getBlockTexture(int aX, int aY, World aWorld)
	{
		return TEXTURES.get(aWorld.getBlock(aX, aY));
	}
}
