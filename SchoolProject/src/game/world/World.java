package game.world;

import game.entity.Entity;
import game.entity.Player;
import java.util.HashMap;
import java.util.HashSet;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class World
{
	private int			mWidth, mHeight;
	
	private byte[][]	mBlocks;
	
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
		mWidth = aWidth / Block.SIZE;
		mHeight = aHeight / Block.SIZE;
		mBlocks = new byte[aWidth][aHeight];
		for (int i = 0; i < 100; i++ )
			mBlocks[(int) (Math.random() * mWidth)][(int) (Math.random() * mHeight)] = Block.STONE.getId();
		mScreen.move(0, 0);
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
		while (mPlayer.isRemoved())
			createPlayer();
		mScreen.move((int) (mPlayer.getX() - mScreen.getWidth() / 2), (int) (mPlayer.getY() - mScreen.getHeight() / 2));
		mPlayer.updateInput(aInput);
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
	
	public void createPlayer()
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
	
	public Screen getScreen()
	{
		return mScreen;
	}
	
	public void render(Graphics g)
	{
		for (int x = 0; x < mWidth; x++ )
			for (int y = 0; y < mHeight; y++ )
				if (Block.get(mBlocks[x][y]).isVisible()) renderBlock(g, x, y);
		for (Entity entity : mEntities.values())
			if (mScreen.intersectsWith(entity)) entity.render(g);
	}
	
	private void renderBlock(Graphics g, int aX, int aY)
	{
		Block block = Block.get(mBlocks[aX][aY]);
		g.setColor(block.getColor());
		g.fillRect(aX * Block.SIZE - mScreen.getX(), aY * Block.SIZE - mScreen.getY(), Block.SIZE, Block.SIZE);
	}
}
