package game.entity;

import game.world.NewWorld;

public class NewEntity
{
	private byte		mId;
	
	protected NewWorld	mWorld;
	
	public byte getId()
	{
		return mId;
	}
	
	public void init(NewWorld aWorld)
	{
		mWorld = aWorld;
	}
}
