package game.world;

import org.newdawn.slick.Color;

public enum Block
{
	AIR(0, false), STONE(1, true, Color.white);
	
	public static final int	SIZE	= 16;
	
	private final boolean	mSolid, mVisible;
	
	private final byte		mId;
	
	private final Color		mColor;
	
	private Block(int aId, boolean aSolid, Color aColor)
	{
		mId = (byte) aId;
		mSolid = aSolid;
		mVisible = true;
		mColor = aColor;
	}
	
	private Block(int aId, boolean aSolid)
	{
		mId = (byte) aId;
		mSolid = aSolid;
		mVisible = false;
		mColor = null;
	}
	
	public byte getId()
	{
		return mId;
	}
	
	public Color getColor()
	{
		return mColor;
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
		for (Block block : values())
			if (block.mId == aId) return block;
		return null;
	}
}
