package game.level.block;

import game.entity.Entity;
import game.level.Level;
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
	public static final int							SIZE						= 16;
	private static final HashMap<Short, Block>		BLOCKS						= new HashMap<>();
	private static final HashMap<Integer, Short>	COLORS						= new HashMap<>();
	private static final HashMap<Short, Texture>	TEXTURES					= new HashMap<>();
	
	// Blocks
	public static final Block						AIR							= new Block(0, 0xffffff).setInvisible().setUnsolid();
	public static final Block						STONE						= new Block(1, new int[] { 0xB89040, 0xC0C0C0, 0xB89041, 0x2A7CE0, 0x5000DB, 0xB89042, 0xB89043, 0x593797, 0xB89044 });
	public static final Block						BRICK						= new Block(2, new int[] { 0xB06000, 0x727272, 0xB06001, 0x014EB2, 0x014EB3, 0xB06002, 0xB06003, 0x432A72, 0xB06004 })
																						.setDestroyable(AIR);
	public static final Block						OPENED						= new Block(3, new int[] { 0xC03010, 0xC03011, 0xC03014, 0xC03012, 0xC03015, 0xC03016, 0xC03017, 0xC03013, 0xC03018 });
	public static final Block						QUESTION					= new Block(4, new int[] { 0xD8B018, 0xFFD800, 0xD8B019, 0x14466D, 0x14466E, 0xD8B020, 0xD8B021, 0x9B00CD, 0xD8B022 })
																						.setDestroyable(OPENED).setItem(255, Item.SUPER_BANANA).setItem(254, Item.BANANA).setItem(253, Item.HEART)
																						.addRenderBlock(OPENED);
	public static final Block						GROUND_TOP_LEFT				= new Block(5, new int[] { 0x00E000, 0x93FFFF, 0xFFA359, 0x3D2C77, 0x564E75, 0x00E096, 0x00AA71, 0xD32C77, 0x005B3B });
	public static final Block						GROUND_TOP					= new Block(6, new int[] { 0x00BA00, 0x00FFFF, 0xFF7400, 0x291D4F, 0x39334C, 0x00BA96, 0x00846A, 0xBF1D4F, 0x00352A });
	public static final Block						GROUND_TOP_RIGHT			= new Block(7, new int[] { 0x009300, 0x00D3D3, 0xB75200, 0x1D1438, 0x272335, 0x009396, 0x005F60, 0xB31438, 0x001111 });
	public static final Block						GROUND_STOP_LEFT			= new Block(8, new int[] { 0x8CB848, 0x8EF4D5, 0x87612D, 0x3D4677, 0x606375, 0x7CB8DE, 0x6D8FAA, 0xD34677, 0x3B4D5B });
	public static final Block						GROUND_STOP_RIGHT			= new Block(9, new int[] { 0x73963C, 0x00DDD3, 0x704A00, 0x1D2338, 0x2B2E35, 0x7396D2, 0x56729E, 0xB32338, 0x2B394F });
	public static final Block						GROUND_LEFT					= new Block(10, new int[] { 0xF0B848, 0xAD7854, 0x87512D, 0x1D0077, 0x362375, 0xF0B8DE, 0x8CB8DE, 0xB30077, 0x5D7991 });
	public static final Block						GROUND_MIDDLE				= new Block(11, new int[] { 0xD3A23F, 0xAF4600, 0x873600, 0x140050, 0x25174F, 0xD3A2D5, 0x6FA2D5, 0xAA0050, 0x476787 });
	public static final Block						GROUND_RIGHT				= new Block(12, new int[] { 0xAD8434, 0x993D00, 0x702C00, 0x0D0035, 0x170F33, 0xAD84CA, 0x4984CA, 0xA30035, 0x2E527C });
	public static final Block						GROUND_BOTTOM_LEFT			= new Block(13, new int[] { 0xA07931, 0xAD9354, 0xBC5F3A, 0x25234C, 0x393849, 0xA079C7, 0x3C79C7, 0xBB234C, 0x254B7A });
	public static final Block						GROUND_BOTTOM				= new Block(14, new int[] { 0x99722F, 0xAF6D00, 0xBE3600, 0x06004F, 0x1A164C, 0x9972C5, 0x3572C5, 0x9C004F, 0x204677 });
	public static final Block						GROUND_BOTTOM_RIGHT			= new Block(15, new int[] { 0x936C2D, 0x995900, 0xA02D00, 0x04003D, 0x13113A, 0x936CC3, 0x2F6CC3, 0x9A003D, 0x1D4175 });
	public static final Block						GROUND_BOTTOM_STOP_LEFT		= new Block(16, new int[] { 0xB6B848, 0x997F00, 0xA05500, 0x04273D, 0x152C3A, 0xB6B8DE, 0x52B8DE, 0x9A273D, 0x357891 });
	public static final Block						GROUND_BOTTOM_STOP_RIGHT	= new Block(17, new int[] { 0xABAD45, 0x7F6A00, 0xBC803A, 0x253E4C, 0x3A4449, 0xABADDB, 0x47ADDB, 0xBB3E4C, 0x2E708C });
	public static final Block						SPIKES_UP					= new Block(18, new int[] { 0xBCBCBC, 0xBCBCBD, 0xBCBCBE, 0xBCBCBF, 0xBCBCC0, 0xBCBCC1, 0xBCBCC2, 0xBA5D5D, 0xBCBCC3 })
																						.setHurtPlayer(Direction.TOP);
	public static final Block						SPIKES_DOWN					= new Block(19, new int[] { 0xB5B5B5, 0xB5B5B6, 0xB5B5B7, 0xB5B5B8, 0xB5B5B9, 0xB5B5C0, 0xB5B5C1, 0xB25959, 0xB5B5C2 })
																						.setHurtPlayer(Direction.BOTTOM);
	public static final Block						SPIKES_RIGHT				= new Block(20, new int[] { 0xADADAD, 0xADADAE, 0xADADAF, 0xADADB0, 0xADADB1, 0xADADB2, 0xADADB3, 0xAA5555, 0xADADB4 })
																						.setHurtPlayer(Direction.RIGHT);
	public static final Block						SPIKES_LEFT					= new Block(21, new int[] { 0xA5A5A5, 0xA5A5A6, 0xA5A5A7, 0xA5A5A8, 0xA5A5A9, 0xA5A5B0, 0xA5A5B1, 0xA35151, 0xA5A5B2 })
																						.setHurtPlayer(Direction.LEFT);
	public static final Block						WATER_TOP					= new Block(22, 0x5151FF).setLiquid();
	public static final Block						WATER						= new Block(23, 0x0000C6).setLiquid();
	public static final Block						ICE							= new Block(24, 0x00FF7C).setIce();
	public static final Block						ICE_TOP_LEFT				= new Block(25, 0x96FFAA).setIce();
	public static final Block						ICE_TOP						= new Block(26, 0x00FFC9).setIce();
	public static final Block						ICE_TOP_RIGHT				= new Block(27, 0x00E2B1).setIce();
	public static final Block						ICE_STOP_LEFT				= new Block(28, 0x93BCB9);
	public static final Block						ICE_STOP_RIGHT				= new Block(29, 0xA5D3B8);
	public static final Block						ICE_LEFT					= new Block(30, 0x96C6AA);
	public static final Block						ICE_MIDDLE					= new Block(31, 0x00FFA7);
	public static final Block						ICE_RIGHT					= new Block(32, 0x00BEB1);
	public static final Block						PLATFORM					= new Block(33, new int[] { 0x74FF8C, 0x74FF8D, 0x74FF8E, 0x88FFFF, 0x88A3FF, 0x74FF8F, 0x74FF90, 0xFF1414, 0x74FF91 })
																						.setSolidSide(Direction.TOP);
	public static final Block						SMALL_ROCK_LEFT				= new Block(34, 0xD34300);
	public static final Block						SMALL_ROCK_RIGHT			= new Block(35, 0xFF5200);
	public static final Block						SMALL_ROCK_TOP				= new Block(36, 0xFF503D);
	public static final Block						LARGE_ROCK_TOP_LEFT			= new Block(37, 0xFF5188);
	public static final Block						LARGE_ROCK_TOP				= new Block(38, 0xFF51D9);
	public static final Block						LARGE_ROCK_TOP_RIGHT		= new Block(39, 0xFF88D9);
	public static final Block						LARGE_ROCK_LEFT				= new Block(40, 0xFF0084);
	public static final Block						LARGE_ROCK_MIDDLE			= new Block(41, 0xFF00C6);
	public static final Block						LARGE_ROCK_RIGHT			= new Block(42, 0xFF71C6);
	public static final Block						LARGE_ROCK_BOTTOM_LEFT		= new Block(43, 0xB5003E);
	public static final Block						LARGE_ROCK_BOTTOM			= new Block(44, 0xB5008D);
	public static final Block						LARGE_ROCK_BOTTOM_RIGHT		= new Block(45, 0xB5618D);
	public static final Block						SMALL_ROCK_BOTTOM			= new Block(46, 0xCC1400);
	public static final Block						ROCK						= new Block(47, 0xFF1C00);
	public static final Block						MUD							= new Block(48, 0x788000).setLiquid();
	public static final Block						MUD_TOP						= new Block(49, 0x7C7F74).setLiquid();
	public static final Block						START						= new Block(50, 0x0);
	public static final Block						END							= new Block(51, 0x0000FF).setFlag();
	public static final Block						ITEM						= new Block();
	
	// Attributes
	private final HashSet<UpdateAction>				mUpdateActions				= new HashSet<>();
	private final HashSet<HitAction>				mHitActions					= new HashSet<>();
	private final HashMap<Texture, Short>			mIds						= new HashMap<>();
	private final HashMap<Short, Item>				mItems						= new HashMap<>();
	private final HashSet<Block>					mRenderBlocks				= new HashSet<>();
	private Direction								mHurtDirection				= Direction.NONE;
	private Direction								mSolid						= Direction.NONE;
	private Block									mDestination				= null;
	private boolean									mVisible					= true, mUpdatable = false, mLiquid = false, mFlag = false, mIce = false, mItemBlock = false;
	
	private Block(int aId, int[] aRGBs)
	{
		final int textures = Texture.values().size();
		for (Texture texture : Texture.values())
			mIds.put(texture, (short) (aId * textures + texture.getId()));
		for (Texture texture : mIds.keySet())
			BLOCKS.put(mIds.get(texture), this);
		if (aRGBs.length != textures) throw new IllegalArgumentException("Not the right number of color codes");
		for (int i = 0; i < textures; i++ )
			COLORS.put(aRGBs[i], mIds.get(Texture.get((byte) i)));
		for (Texture texture : Texture.values())
			TEXTURES.put(mIds.get(texture), texture);
		mRenderBlocks.add(this);
	}
	
	private Block(int aId, int aRGB)
	{
		short id = (short) (aId * Texture.values().size());
		for (Texture texture : Texture.values())
			mIds.put(texture, id);
		BLOCKS.put(id, this);
		COLORS.put(aRGB, id);
		TEXTURES.put(id, Texture.NORMAL);
		mRenderBlocks.add(this);
	}
	
	private Block()
	{
		short id = Short.MAX_VALUE;
		for (Texture texture : Texture.values())
			mIds.put(texture, id);
		BLOCKS.put(id, this);
		TEXTURES.put(id, Texture.NORMAL);
		mItemBlock = true;
		mRenderBlocks.add(this);
	}
	
	private Block addRenderBlock(Block aBlock)
	{
		mRenderBlocks.add(aBlock);
		return this;
	}
	
	private Block setUpdatable()
	{
		mUpdatable = true;
		return this;
	}
	
	private Block setUnsolid()
	{
		mSolid = null;
		return this;
	}
	
	private Block setSolidSide(Direction aDirection)
	{
		mSolid = aDirection;
		return this;
	}
	
	private Block setInvisible()
	{
		mVisible = false;
		mRenderBlocks.remove(this);
		return this;
	}
	
	private Block setFlag()
	{
		mFlag = true;
		setUpdatable();
		setUnsolid();
		mRenderBlocks.remove(this);
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
	
	private Block setItem(int aAlpha, Item aItem)
	{
		mItems.put((short) aAlpha, aItem);
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
	
	Item getItem(short aAlpha)
	{
		return mItems.get(aAlpha);
	}
	
	/**
	 * The texture depending id of this block.
	 * 
	 * @return this blocks id.
	 */
	public short getId(Texture aTexture)
	{
		return mIds.get(aTexture);
	}
	
	/**
	 * The id of this block.
	 * 
	 * @return this blocks id.
	 */
	public short getId()
	{
		return mIds.get(Texture.NORMAL);
	}
	
	/**
	 * Returns whether this block allows to move through.
	 * 
	 * @return {@code true} if this block is solid and {@code false} if not.
	 */
	public boolean isSolid(int aX, int aY, Entity aEntity)
	{
		if (mSolid == null) return false;
		switch (mSolid)
		{
			case NONE :
				return true;
			case TOP :
				return aEntity.getY() + aEntity.getHeight() <= aY * SIZE;
			case BOTTOM :
				return aEntity.getY() >= (aY + 1) * SIZE;
			case LEFT :
				return aEntity.getX() + aEntity.getWidth() <= aX * SIZE;
			case RIGHT :
				return aEntity.getX() >= (aX + 1) * SIZE;
			default :
				return true;
		}
	}
	
	/**
	 * Whether this block represents a flag.
	 * 
	 * @return {@code true} if this block is a flag and {@code false} if not.
	 */
	public boolean isFlag()
	{
		return mFlag;
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
	 * Loads the used image into the cache.
	 * 
	 * @param aX
	 *            The x position.
	 * @param aY
	 *            The y position.
	 * @param aLevel
	 *            The parent level.
	 */
	public void initImage(int aX, int aY, Level aLevel)
	{
		Texture texture = getBlockTexture(aX, aY, aLevel);
		DataManager.loadTexture(DataManager.getTexturePack(), texture, getId(texture) / Texture.values().size());
	}
	
	/**
	 * All blocks that can be created by doing actions with this block so they should be loaded, too.
	 * 
	 * @return all blocks, able to build out of this.
	 */
	public HashSet<Block> getRenderBlocks()
	{
		return mRenderBlocks;
	}
	
	/**
	 * Returns the block with the given id.
	 * 
	 * @param aId
	 *            The id.
	 * @return The block.
	 */
	public static Block get(short aId)
	{
		return BLOCKS.get(aId);
	}
	
	/**
	 * Returns the id of the block with the given color code.
	 * 
	 * @param aRGB
	 *            The color of any pixel inside a level map image.
	 * @return the id of the searched block.
	 */
	public static short getIdFromCode(int aRGB)
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
	 * @param aG
	 *            The graphics to draw into.
	 * @param aLevel
	 *            The parent level.
	 */
	public static void render(int aX, int aY, short aId, Graphics aG, Level aLevel)
	{
		final Block block = get(aId);
		final Texture texture = getBlockTexture(aX, aY, aLevel);
		if (block.mFlag) aG.drawImage(DataManager.getImage("flag"), aX * SIZE - aLevel.getScreenX(), (aY - 7) * SIZE - aLevel.getScreenY());
		else aG.drawImage(DataManager.getTextureImage(DataManager.getTexturePack(), texture, block.getId(texture) / Texture.values().size()).getScaledCopy(SIZE, SIZE),
				aX * SIZE - aLevel.getScreenX(), aY * SIZE - aLevel.getScreenY());
	}
	
	/**
	 * Checks the texture of the block at the given position.
	 * 
	 * @param aX
	 *            The x position.
	 * @param aY
	 *            The y position.
	 * @param aLevel
	 *            The parent level.
	 * @return the texture of the given block.
	 */
	public static Texture getBlockTexture(int aX, int aY, Level aLevel)
	{
		return TEXTURES.get(aLevel.getBlock(aX, aY));
	}
	
	/**
	 * If this block is updatable this method is invoked each tick.
	 * 
	 * @param aX
	 *            the x index of this block.
	 * @param aY
	 *            the y index of this block.
	 * @param aLevel
	 *            the parent level.
	 */
	public void update(int aX, int aY, Level aLevel)
	{
		for (UpdateAction action : mUpdateActions)
			action.execute(aX, aY, aLevel);
	}
	
	/**
	 * Invoked, when any entity hits this block.
	 * 
	 * @param aX
	 *            the x position.
	 * @param aY
	 *            the y position.
	 * @param aLevel
	 *            the parent level.
	 * @param aEntity
	 *            the hitting entity.
	 * @param aDirection
	 *            The direction where this block is hit from.
	 * @param aOtherBlocks
	 *            All other hit blocks by the given entity at this tick.
	 */
	public void hit(int aX, int aY, Level aLevel, Entity aEntity, Direction aDirection, HashMap<Block, Direction> aOtherBlocks)
	{
		for (HitAction action : mHitActions)
			action.execute(aX, aY, aLevel, aEntity, this, aDirection, aOtherBlocks);
	}
	
	@Override
	public int hashCode()
	{
		return getId();
	}
	
	@Override
	public boolean equals(Object aObj)
	{
		if (aObj instanceof Block)
		{
			Block b = (Block) aObj;
			return b.getId() == getId();
		}
		return false;
	}
}
