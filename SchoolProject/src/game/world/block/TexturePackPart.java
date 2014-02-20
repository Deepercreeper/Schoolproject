package game.world.block;

import java.util.Collection;
import java.util.HashMap;

public class TexturePackPart
{
	public static final TexturePackPart					NORMAL	= new TexturePackPart(0);
	public static final TexturePackPart					SNOW	= new TexturePackPart(1);
	public static final TexturePackPart					DESERT	= new TexturePackPart(2);
	
	private static final HashMap<Byte, TexturePackPart>	PARTS	= new HashMap<>();
	
	private final byte									mId;
	
	private TexturePackPart(int aId)
	{
		mId = (byte) aId;
		PARTS.put(mId, this);
	}
	
	public byte getId()
	{
		return mId;
	}
	
	public static Collection<TexturePackPart> values()
	{
		return PARTS.values();
	}
	
	public static TexturePackPart get(byte aId)
	{
		return PARTS.get(aId);
	}
}
