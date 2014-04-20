package data.names;

import java.util.HashMap;

public enum Texture
{
	NORMAL(0, "Normal"), SNOW(1, "Snow"), DESERT(2, "Desert"), CASTLE(3, "Castle"), UNDERGROUND(4, "Underground"), CLOUD(5, "Cloud"), JUNGLE(6, "Jungle"), HORROR(7, "Horror"), STONE(8, "Stone");
	
	private static final HashMap<Byte, Texture>	TEXTURES	= new HashMap<>();
	
	private final byte							mId;
	
	private final String						mName;
	
	private Texture(final int aId, final String aName)
	{
		mId = (byte) aId;
		mName = aName;
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
		return mName;
	}
	
	@Override
	public String toString()
	{
		return mName;
	}
	
	/**
	 * Initializes all textures.
	 */
	public static void init()
	{
		for (final Texture texture : values())
			TEXTURES.put(texture.mId, texture);
	}
	
	/**
	 * Returns the texture with the given id.
	 * 
	 * @param aId
	 *            The texture id.
	 * @return the texture with the given id and {@code null} if no texture with the given id exists.
	 */
	public static Texture getTextureWithId(final byte aId)
	{
		return TEXTURES.get(aId);
	}
}
