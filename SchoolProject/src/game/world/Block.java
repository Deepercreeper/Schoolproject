package game.world;

import java.util.HashMap;
import org.newdawn.slick.Image;
import data.DataManager;

public class Block
{
	public static final int					SIZE				= 16;
	private static HashMap<Byte, Block>		BLOCKS				= new HashMap<>();
	private static HashMap<Integer, Byte>	COLORS				= new HashMap<>();
	
	public static final Block				AIR					= new Block(0, 0xffffff).setInVisible().setUnSolid();
	public static final Block				STONE				= new Block(1, 0xB89040);
	public static final Block				BRICK				= new Block(2, 0xB06000);
	public static final Block				EMPTY				= new Block(3, 0xC03010);
	public static final Block				QUESTION			= new Block(4, 0xD8B018);
	public static final Block				GROUND_TOP_LEFT		= new Block(5, 0x00E000);
	public static final Block				GROUND_TOP			= new Block(6, 0x00BA00);
	public static final Block				GROUND_TOP_RIGHT	= new Block(7, 0x009300);
	public static final Block				GROUND_LEFT			= new Block(8, 0xF0B848);
	public static final Block				GROUND_MIDDLE		= new Block(9, 0xD3A23F);
	public static final Block				GROUND_RIGHT		= new Block(10, 0xAD8434);
	
	private final byte						mId;
	
	private boolean							mSolid				= true, mVisible = true;
	
	private final Image						mImage;
	
	private Block(int aId, int aRGB)
	{
		mId = (byte) aId;
		BLOCKS.put(mId, this);
		COLORS.put(aRGB, mId);
		mImage = DataManager.get("block" + mId);
	}
	
	private Block setInVisible()
	{
		mVisible = false;
		return this;
	}
	
	private Block setUnSolid()
	{
		mSolid = false;
		return this;
	}
	
	public byte getId()
	{
		return mId;
	}
	
	public Image getImage()
	{
		return mImage;
	}
	
	public boolean isSolid()
	{
		return mSolid;
	}
	
	public boolean isVisible()
	{
		return mVisible;
	}
	
	public static Block get(byte aId)
	{
		return BLOCKS.get(aId);
	}
	
	public static byte get(int aRGB)
	{
		if ( !COLORS.containsKey(aRGB)) return -1;
		return COLORS.get(aRGB);
	}
}
