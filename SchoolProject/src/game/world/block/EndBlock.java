package game.world.block;

import game.world.World;
import org.newdawn.slick.Graphics;
import util.Rectangle;

public class EndBlock extends Block
{
	private int	mTile	= 0;
	
	/**
	 * Creates an end block that makes the player win the level at stepping through this block.
	 * 
	 * @param aId
	 *            This blocks id.
	 * @param aRGB
	 *            The RGB code of this block.
	 */
	protected EndBlock(int aId, int aRGB)
	{
		super(aId, aRGB);
		setUnSolid();
		setUpdatable();
	}
	
	@Override
	public void update(int aX, int aY, World aWorld)
	{
		if (aWorld.getPlayer().getRect().intersects(new Rectangle(aX * SIZE, (aY - 7) * SIZE, SIZE * 2, SIZE * 8))) aWorld.win();
	}
	
	@Override
	public void render(int aX, int aY, Graphics g, World aWorld)
	{
		mTile = -1;
		super.render(aX, aY, g, aWorld);
		mTile++ ;
		super.render(aX + 1, aY, g, aWorld);
		mTile++ ;
		super.render(aX, aY - 1, g, aWorld);
		super.render(aX, aY - 2, g, aWorld);
		super.render(aX, aY - 3, g, aWorld);
		super.render(aX, aY - 4, g, aWorld);
		super.render(aX, aY - 5, g, aWorld);
		super.render(aX, aY - 6, g, aWorld);
		mTile++ ;
		super.render(aX + 1, aY - 1, g, aWorld);
		super.render(aX + 1, aY - 2, g, aWorld);
		super.render(aX + 1, aY - 3, g, aWorld);
		super.render(aX + 1, aY - 4, g, aWorld);
		super.render(aX + 1, aY - 5, g, aWorld);
		super.render(aX + 1, aY - 6, g, aWorld);
		mTile++ ;
		super.render(aX, aY - 7, g, aWorld);
		mTile++ ;
		super.render(aX + 1, aY - 7, g, aWorld);
	}
	
	@Override
	protected int getImageIndex()
	{
		return getId() + mTile;
	}
}
