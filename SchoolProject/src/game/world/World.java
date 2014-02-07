package game.world;

import game.entity.Entity;
import java.util.HashSet;
import org.newdawn.slick.Graphics;

public class World
{
	private final HashSet<Entity>	mEntities, mAddEntities, mRemoveEntities;
	
	public World()
	{
		mEntities = new HashSet<>();
		mAddEntities = new HashSet<>();
		mRemoveEntities = new HashSet<>();
	}
	
	public void update()
	{
		// TODO update
		updateEntities();
	}
	
	private void updateEntities()
	{
		for (Entity entity : mAddEntities)
			mEntities.add(entity);
		for (Entity entity : mEntities)
			if (entity.isRemoved()) mEntities.add(entity);
		for (Entity entity : mRemoveEntities)
			mEntities.remove(entity);
		for (Entity entity : mEntities)
			entity.update();
	}
	
	public void addEntity(Entity aEntity)
	{
		aEntity.init(this);
		mAddEntities.add(aEntity);
	}
	
	public void render(Graphics g)
	{
		// TODO render
	}
}
