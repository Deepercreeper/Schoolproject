package game.level.block;

import java.util.Collection;
import java.util.HashMap;

public class Texture
{
	private static final HashMap<Byte, Texture>	PARTS	= new HashMap<>();
	
	public static final Texture					NORMAL	= new Texture(0, "Normal");
	public static final Texture					SNOW	= new Texture(1, "Snow");
	public static final Texture DESERT = new Texture(2, "Desert");
	
	private final byte							mId;
	
	private final String						mSuffix;
	
	private Texture(int aId, String aSuffix)
	{
		mId = (byte) aId;
		mSuffix = aSuffix;
		PARTS.put(mId, this);
	}
	
	@Override
	public String toString()
	{
		return mSuffix;
	}
	
	public byte getId()
	{
		return mId;
	}
	
	public String getSuffix()
	{
		return mSuffix;
	}
	
	public static Collection<Texture> values()
	{
		return PARTS.values();
	}
	
	public static Texture get(byte aId)
	{
		return PARTS.get(aId);
	}
}
