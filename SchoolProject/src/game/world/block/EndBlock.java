package game.world.block;

import game.world.World;
import org.newdawn.slick.Graphics;
import util.Rectangle;

public class EndBlock extends Block
{
	private int	mTile	= 0;
	
	protected EndBlock(int aId, int aRGB)
	{
		super(aId, aRGB);
		setUnSolid();
		setUpdatable();
	}
	
	@Override
	public void update(int aX, int aY, World aWorld)
	{
		if (aWorld.getPlayer().getRect().intersects(new Rectangle(aX * SIZE, (aY - 2) * SIZE, SIZE, SIZE * 3))) aWorld.win();
	}
	
	@Override
	public void render(int aX, int aY, Graphics g, World aWorld)
	{
		mTile = 0;
		super.render(aX, aY, g, aWorld);
		mTile++ ;
		super.render(aX, aY - 1, g, aWorld);
		mTile++ ;
		super.render(aX, aY - 2, g, aWorld);
	}
	
	@Override
	protected int getImageTile()
	{
		return getId() + mTile;
	}
}
