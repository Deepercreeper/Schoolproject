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
	public static final Block				STONE	= new Block(1, 0x0).setImage();
	
	private final byte						mId;
	
	private boolean							mSolid	= true, mVisible = true;
	
	private Image							mImage;
	
	private Block(int aId, int aRGB)
	{
		mId = (byte) aId;
		BLOCKS.put(mId, this);
		COLORS.put(aRGB, mId);
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
	
	private Block setImage()
	{
		mImage = DataManager.get("block" + mId);
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
		return COLORS.get(aRGB);
	}
}
