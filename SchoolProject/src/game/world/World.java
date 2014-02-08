package game.world;

import game.entity.Entity;
import game.entity.Player;
import java.util.HashMap;
import java.util.HashSet;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class World
{
	private int	mWidth, mHeight;
	
	private final HashMap<Integer, Entity>	mEntities, mAddEntities;
	
	private final HashSet<Integer>			mRemoveEntities;
	
	private Player							mPlayer;
	
	private final Screen					mScreen;
	
	public World()
	{
		mScreen = new Screen();
		mEntities = new HashMap<>();
		mAddEntities = new HashMap<>();
		mRemoveEntities = new HashSet<>();
	}
	
	public void init(int aWidth, int aHeight)
	{
		mWidth = aWidth;
		mHeight = aHeight;
		mScreen.move(0, 0);
		mScreen.resize(mWidth, mHeight);
	}
	
	public int getWidth()
	{
		return mWidth;
	}
	
	public int getHeight()
	{
		return mHeight;
	}
	
	public void update(Input aInput)
	{
		// TODO update
		if (mPlayer != null) mPlayer.updateInput(aInput);
		updateEntities();
	}
	
	private void updateEntities()
	{
		mEntities.putAll(mAddEntities);
		for (int id : mEntities.keySet())
			if (mEntities.get(id).isRemoved()) mEntities.put(id, mEntities.get(id));
		for (int id : mRemoveEntities)
			mEntities.remove(id);
		for (Entity entity : mEntities.values())
			entity.update();
	}
	
	public void addPlayer()
	{
		mPlayer = new Player();
		addEntity(mPlayer);
	}
	
	public void addEntity(Entity aEntity)
	{
		aEntity.init(this);
		mAddEntities.put(aEntity.getId(), aEntity);
	}
	
	public int createId()
	{
		for (int i = 0;; i++ )
			if ( !mEntities.containsKey(i)) return i;
	}
	
	public void render(Graphics g)
	{
		for (Entity entity : mEntities.values())
			if (mScreen.intersectsWith(entity)) entity.render(g);
	}
}
