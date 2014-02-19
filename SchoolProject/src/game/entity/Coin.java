package game.entity;

public class Coin extends Entity
{
	public Coin(int aX, int aY)
	{
		super(aX, aY, 16, 16);
	}
	
	@Override
	public boolean isSolid()
	{
		return false;
	}
}
