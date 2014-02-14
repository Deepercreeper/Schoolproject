package game.world;

import java.util.HashMap;
import org.newdawn.slick.Image;
import data.DataManager;

public class Block
{
	public static final int					SIZE	= 16;
	private static HashMap<Byte, Block>		BLOCKS	= new HashMap<>();
	private static HashMap<Integer, Byte>	COLORS	= new HashMap<>();
	
	public static final Block				AIR		= new Block(0, 0xffffff).setInVisible().setUnSolid();
	public static final Block				STONE	= new Block(1, 0xc0c0c0);
	public static final Block				GRASS	= new Block(2, 0x4cff00);
	public static final Block				DIRT	= new Block(3, 0x885900);
	public static final Block				COBBLE	= new Block(4, 0x808080);
	public static final Block				SAND	= new Block(5, 0xffe9c2);
	public static final Block				PLANKS	= new Block(6, 0xb79c5b);
	public static final Block				LOG		= new Block(7, 0x653a00);
	public static final Block				IRON	= new Block(8, 0xa0a0a0);
	
	private final byte						mId;
	
	private boolean							mSolid	= true, mVisible = true;
	
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
