package game.world.block;

import game.world.World;
import org.newdawn.slick.Graphics;
import util.Rectangle;
import data.DataManager;

public class EndBlock extends OldBlock
{
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
		g.drawImage(DataManager.getImage("flag"), aX * SIZE - aWorld.getScreenX(), (aY - 7) * SIZE - aWorld.getScreenY());
	}
}
