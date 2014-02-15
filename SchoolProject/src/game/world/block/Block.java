package game.world.block;

import game.entity.Entity;
import game.world.World;
import java.util.HashMap;
import org.newdawn.slick.Graphics;
import data.DataManager;

public class Block
{
	public static final int					SIZE				= 16;
	private static HashMap<Byte, Block>		BLOCKS				= new HashMap<>();
	private static HashMap<Integer, Byte>	COLORS				= new HashMap<>();
	
	public static final Block				AIR					= new Block(0, 0xffffff).setInVisible().setUnSolid();
	public static final Block				STONE				= new Block(1, 0xB89040);
	public static final Block				BRICK				= new BrickBlock(2, 0xB06000);
	public static final Block				EMPTY				= new Block(3, 0xC03010);
	public static final Block				QUESTION			= new QuestionBlock(4, 0xD8B018);
	public static final Block				GROUND_TOP_LEFT		= new Block(5, 0x00E000);
	public static final Block				GROUND_TOP			= new Block(6, 0x00BA00);
	public static final Block				GROUND_TOP_RIGHT	= new Block(7, 0x009300);
	public static final Block				GROUND_LEFT			= new Block(8, 0xF0B848);
	public static final Block				GROUND_MIDDLE		= new Block(9, 0xD3A23F);
	public static final Block				GROUND_RIGHT		= new Block(10, 0xAD8434);
	public static final Block				GROUND_LEFT_STOP	= new Block(11, 0x8CB848);
	public static final Block				GROUND_RIGHT_STOP	= new Block(12, 0x73963C);
	public static final Block				SPIKES_UP			= new SpikeBlock(13, 0xBCBCBC, 0);
	public static final Block				SPIKES_DOWN			= new SpikeBlock(14, 0xB5B5B5, 1);
	public static final Block				SPIKES_RIGHT		= new SpikeBlock(15, 0xADADAD, 2);
	public static final Block				SPIKES_LEFT			= new SpikeBlock(16, 0xA5A5A5, 3);
	
	private final byte						mId;
	
	private boolean							mSolid				= true, mVisible = true;
	
	protected Block(int aId, int aRGB)
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
	
	public byte getId()
	{
		return mId;
	}
	
	public void render(int aX, int aY, Graphics g, World aWorld)
	{
		g.drawImage(DataManager.getSplittedImage(getImageName(), getImageTile()), aX * SIZE - aWorld.getScreenX(), aY * SIZE - aWorld.getScreenY());
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
	
	protected String getImageName()
	{
		return "blocks";
	}
	
	protected int getImageTile()
	{
		return mId;
	}
	
	public void hit(int aX, int aY, double aXV, double aYV, World aWorld, Entity aEntity)
	{}
}
