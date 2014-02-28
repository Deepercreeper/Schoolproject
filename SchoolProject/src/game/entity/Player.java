package game.entity;

import game.level.Level;
import game.level.block.Block;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import data.DataManager;

public class Player extends Entity
{
	private int	mTime	= 0;
	
	private final int	mMaxLife	= 10, mLifeStep = 2;
	private final double	mSpeed	= 1, mSpeedStep = 0.01;
	private int				mSpeedSkill	= 0, mLifeSkill = 0;
	
	private int				mLife;
	
	private int				mHurtDelay;
	
	private boolean			mCannon;
	
	private int				mCannonTime;
	
	/**
	 * Creates a new player at {@code (0,0)}.
	 */
	public Player()
	{
		super(0, 0, Block.SIZE - 2, Block.SIZE * 2 - 2);
		mLife = mMaxLife + mLifeStep * mLifeSkill;
	}
	
	/**
	 * Creates a new player at {@code (0,0)}.
	 * 
	 * @param aData
	 *            The load data to create out of.
	 */
	public Player(String aData)
	{
		super(0, 0, Block.SIZE - 2, Block.SIZE * 2 - 2);
		String[] data = aData.split(",");
		mLifeSkill = Integer.parseInt(data[0]);
		mSpeedSkill = Integer.parseInt(data[1]);
		mLife = mMaxLife + mLifeStep * mLifeSkill;
	}
	
	@Override
	public void update(Input aInput)
	{
		mTime++ ;
		if (mHurtDelay > 0) mHurtDelay-- ;
		
		// TODO Temporary
		if (aInput.isKeyPressed(Input.KEY_F)) skillLife();
		if (aInput.isKeyPressed(Input.KEY_G)) skillSpeed();
		
		if ( !mHurt)
		{
			if (aInput.isKeyPressed(Input.KEY_S) && !mOnGround && mCannonTime <= 0)
			{
				mCannon = true;
				mCannonTime = 10;
				DataManager.playSound("cannon");
			}
			if (aInput.isKeyPressed(Input.KEY_W) || mOnGround)
			{
				if (mCannon && mOnGround) DataManager.playSound("bomb");
				mCannon = false;
			}
			
			if ( !mCannon)
			{
				mXA = 0;
				if (aInput.isKeyDown(Input.KEY_D)) mXA += 1.36;
				if (aInput.isKeyDown(Input.KEY_A)) mXA -= 1.36;
				if (aInput.isKeyDown(Input.KEY_LSHIFT)) mXA *= 1.5;
				if ( !mOnGround) mXA *= 0.125;
				if (mOnIce) mXA *= 0.08;
				mXA *= mSpeed + mSpeedSkill * mSpeedStep;
				
				if (aInput.isKeyPressed(Input.KEY_SPACE) && (mOnGround || mOnWall))
				{
					if (mOnWall)
					{
						mXV = 10 * (mLeftWall ? 1 : -1);
						mYV = -5;
					}
					else mYV = -6;
					DataManager.playSound("jump");
				}
			}
			else
			{
				mXV = 0;
				if (mCannonTime > 0)
				{
					mCannonTime-- ;
					mYV = 0;
				}
				else mYV = 10;
			}
		}
		mHurt = false;
		
		mXV += mXA;
		
		// Reset attributes
		mOnIce = false;
		
		move();
		
		mXV *= 0.95f - (mOnGround ? 0.45 : 0) + (mOnIce ? 0.48 : 0) - (mInLiquid ? 0.3 : 0);
		
		final double gravity = Level.GRAVITY - (mInLiquid ? 0.1 : 0), friction = Level.FRICTION - (mInLiquid ? 0.1 : 0);
		
		if (mYV < 0 && aInput.isKeyDown(Input.KEY_SPACE))
		{
			mYV *= friction + 0.002;
			mYV += gravity * 0.5f;
		}
		else
		{
			mYV *= friction;
			if (mOnWall) mYV += gravity * 0.3;
			else mYV += gravity;
		}
		
		// Reset attributes
		mInLiquid = false;
	}
	
	/**
	 * Increases the speed slightly.
	 */
	public void skillSpeed()
	{
		if (mSpeedSkill < 10) mSpeedSkill++ ;
	}
	
	/**
	 * Increases the life.
	 */
	public void skillLife()
	{
		if (mLifeSkill < 10) mLifeSkill++ ;
	}
	
	/**
	 * Returns whether this player has started to do a cannon ball (ass bomb).
	 * 
	 * @return {@code true} if doing and {@code false} if not.
	 */
	public boolean isCannonBall()
	{
		return mCannon;
	}
	
	@Override
	public void render(Graphics aG)
	{
		// Player
		if (mHurtDelay > 0 && mTime % 10 < 5) aG.drawImage(DataManager.getSplitImage("player", 0), (float) mX - mLevel.getScreenX(), (float) mY - mLevel.getScreenY(), new Color(1, 1, 1, 0.5f));
		else aG.drawImage(DataManager.getSplitImage("player", 0).getScaledCopy(mWidth, mHeight), (float) mX - mLevel.getScreenX(), (float) mY - mLevel.getScreenY());
		
		// HUD
		aG.setColor(Color.red);
		aG.fillRect(10, mLevel.getScreenHeight() - 20, 100, 10);
		aG.setColor(Color.green);
		aG.fillRect(10, mLevel.getScreenHeight() - 20, 100 * mLife / (mMaxLife + mLifeStep * mLifeSkill), 10);
		aG.setColor(Color.white);
		aG.drawString(mLife + "/" + (mMaxLife + mLifeStep * mLifeSkill), 120, mLevel.getScreenHeight() - 25);
		
		// Skills
		aG.setColor(Color.white);
		aG.drawString("Speed: " + mSpeedSkill, mLevel.getScreenWidth() - 200, 10);
		aG.drawString("Life: " + mLifeSkill, mLevel.getScreenWidth() - 200, 25);
	}
	
	@Override
	public void hitEntity(double aXV, double aYV, Entity aEntity)
	{
		if (aEntity instanceof Gore) ((Gore) aEntity).hit(this);
		if (aEntity instanceof Banana) ((Banana) aEntity).collect();
		if (aEntity instanceof Heart) ((Heart) aEntity).collect();
		if (aEntity.isSolid()) hitWall(aXV, aYV);
	}
	
	@Override
	public void hurt(int aAmount, float aXV, float aYV)
	{
		if (mHurtDelay <= 0)
		{
			mLife -= aAmount;
			mHurtDelay = 100;
			mHurt = true;
			mXV = aXV;
			mYV = aYV;
			for (int i = (int) (Math.random() * 100 + 50 + (mLife <= 0 ? 50 : 0)); i > 0; i-- )
				mLevel.addEntity(new Blood((int) (mX + mWidth / 2), (int) (mY + mHeight / 2)));
			for (int i = (int) (Math.random() * 3 + (mLife <= 0 ? 10 : 0)); i > 0; i-- )
				mLevel.addEntity(new Gore((int) (mX + mWidth / 2), (int) (mY + mHeight / 2)));
			if (mLife <= 0) die();
		}
	}
	
	@Override
	public void respawn()
	{
		mDead = false;
		mLife = mMaxLife + mLifeStep * mLifeSkill;
		mHurtDelay = 0;
		mXV = mYV = 0;
	}
	
	/**
	 * Creates a string that contains all data needed to create a player out of save files.
	 * 
	 * @return a representative string.
	 */
	public String getData()
	{
		StringBuilder data = new StringBuilder();
		data.append(mLifeSkill).append("," + mSpeedSkill);
		return data.toString();
	}
	
	@Override
	public void die()
	{
		mDead = true;
	}
	
	/**
	 * Increases the current life by the given amount.
	 * 
	 * @param aAmount
	 *            The amount of life to add.
	 */
	public void addLife(int aAmount)
	{
		mLife += aAmount;
		if (mLife > mMaxLife + mLifeStep * mLifeSkill) mLife = mMaxLife + mLifeStep * mLifeSkill;
	}
	
	@Override
	public boolean canDestroyBlocks()
	{
		return true;
	}
	
	@Override
	public boolean isSolid()
	{
		return true;
	}
}
