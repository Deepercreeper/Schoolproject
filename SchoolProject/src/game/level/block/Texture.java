package game.level.block;

import java.util.Collection;
import java.util.HashMap;

public class Texture
{
	private static final HashMap<Byte, Texture>	PARTS	= new HashMap<>();
	
	public static final Texture					NORMAL	= new Texture(0, "Normal");
	public static final Texture					SNOW	= new Texture(1, "Snow");
	public static final Texture					DESERT	= new Texture(2, "Desert");
	public static final Texture					CASTLE	= new Texture(3, "Castle");
	// public static final Texture UNDERGROUND = new Texture(4, "Underground");
	
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
	
	/**
	 * The texture id.
	 * 
	 * @return the id.
	 */
	public byte getId()
	{
		return mId;
	}
	
	/**
	 * The suffix of this texture used to load texture images.
	 * 
	 * @return this textures suffix.
	 */
	public String getSuffix()
	{
		return mSuffix;
	}
	
	/**
	 * Returns all defined textures.
	 * 
	 * @return all textures.
	 */
	public static Collection<Texture> values()
	{
		return PARTS.values();
	}
	
	/**
	 * Returns the texture with the given id.
	 * 
	 * @param aId
	 *            The texture id.
	 * @return the texture with the given id and {@code null} if no texture with the given id exists.
	 */
	public static Texture get(byte aId)
	{
		return PARTS.get(aId);
	}
}
