package game.entity;

import game.Stats;
import game.entity.enemy.Enemy;
import game.entity.weapon.Weapon;
import game.level.Map;
import game.level.block.Block;
import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import util.Direction;
import util.InputKeys;
import util.Key;
import data.DataManager;

public class Player extends Entity
{
	private int						mTime		= 0;
	
	private final int				mMaxLife	= 10, mLifeStep = 2;
	private final double			mSpeed		= 1, mSpeedStep = 0.01;
	private int						mSpeedSkill	= 0, mLifeSkill = 0;
	
	private Direction				mDir		= Direction.NONE;
	
	private final ArrayList<Weapon>	mWeapons	= new ArrayList<>();
	
	private int						mLife;
	
	private int						mHurtDelay;
	
	private boolean					mCannon, mJumping, mRunning, mFast;
	
	private int						mCannonTime, mWeapon;
	
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
	public Player(final String aData)
	{
		super(0, 0, Block.SIZE - 2, Block.SIZE * 2 - 2);
		final String[] data = aData.split(",");
		mLifeSkill = Integer.parseInt(data[0]);
		mSpeedSkill = Integer.parseInt(data[1]);
		mLife = mMaxLife + mLifeStep * mLifeSkill;
	}
	
	@Override
	public void update(final Input aInput)
	{
		mTime++ ;
		if (mHurtDelay > 0) mHurtDelay-- ;
		
		if ( !mWeapons.isEmpty())
		{
			mWeapons.get(mWeapon).update(aInput);
			
			if (aInput.isKeyPressed(InputKeys.instance().getKey(Key.NEXT_WEAPON))) mWeapon = (mWeapon + 1) % mWeapons.size();
			if (aInput.isKeyPressed(InputKeys.instance().getKey(Key.PREVIOUS_WEAPON))) mWeapon = (mWeapon - 1 + mWeapons.size()) % mWeapons.size();
			
			if ( !mWeapons.isEmpty() && aInput.isMousePressed(Input.MOUSE_LEFT_BUTTON)) mWeapons.get(mWeapon).shoot(aInput);
		}
		
		// TODO Temporary
		{
			if (aInput.isKeyPressed(Input.KEY_F)) skillLife();
			if (aInput.isKeyPressed(Input.KEY_G)) skillSpeed();
		}
		
		mDir = Direction.NONE;
		mRunning = mFast = false;
		
		if ( !mHurt)
		{
			final int mouseX = aInput.getMouseX() + mLevel.getScreenX();
			if (mouseX < mX) mDir = Direction.LEFT;
			else if (mouseX > mX + mWidth) mDir = Direction.RIGHT;
			
			if (aInput.isKeyPressed(InputKeys.instance().getKey(Key.DOWN)) && !mOnGround && mCannonTime <= 0)
			{
				mCannon = true;
				mCannonTime = 10;
				DataManager.playSound("cannon");
			}
			if (aInput.isKeyPressed(InputKeys.instance().getKey(Key.UP)) || mOnGround)
			{
				if (mCannon && mOnGround) DataManager.playSound("bomb");
				mCannon = false;
			}
			
			if ( !mCannon)
			{
				mXA = 0;
				if (aInput.isKeyDown(InputKeys.instance().getKey(Key.RIGHT)))
				{
					mXA += 1.36;
					mRunning = true;
				}
				if (aInput.isKeyDown(InputKeys.instance().getKey(Key.LEFT)))
				{
					mXA -= 1.36;
					mRunning = true;
				}
				if (aInput.isKeyDown(InputKeys.instance().getKey(Key.FAST)))
				{
					mXA *= 1.5;
					mFast = true;
				}
				if ( !mOnGround) mXA *= 0.125;
				if (mOnIce) mXA *= 0.08;
				mXA *= mSpeed + mSpeedSkill * mSpeedStep;
				
				if ((aInput.isKeyPressed(InputKeys.instance().getKey(Key.JUMP)) || mJumping && !mOnWall) && (mOnGround || mOnWall))
				{
					if (mOnWall)
					{
						mXV = 10 * (mLeftWall ? 1 : -1);
						mYV = -5;
					}
					else mYV = -6;
					mJumping = false;
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
		
		mXV += mXA;
		
		// Reset attributes
		mHurt = mOnIce = false;
		
		move();
		
		mXV *= 0.95f - (mOnGround ? 0.45 : 0) + (mOnIce ? 0.48 : 0) - (mInLiquid ? 0.3 : 0);
		
		final double gravity = Map.GRAVITY - (mInLiquid ? 0.1 : 0), friction = Map.FRICTION - (mInLiquid ? 0.1 : 0);
		
		if (mYV < 0 && aInput.isKeyDown(InputKeys.instance().getKey(Key.JUMP)))
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
	
	public void addWeapon(final Weapon aWeapon)
	{
		if ( !mWeapons.contains(aWeapon)) mWeapons.add(aWeapon);
		mWeapon = mWeapons.indexOf(aWeapon);
		aWeapon.remove();
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
	public void render(final Graphics aG)
	{
		// Player
		Image image;
		if (mDir == Direction.NONE) image = DataManager.getSplitImage("player" + DataManager.getTexturePack(), 0).getScaledCopy(mWidth, mHeight);
		else
		{
			int id = 0;
			if (mRunning)
			{
				final int delay = 6 - (mFast ? 2 : 0);
				id = mTime % (delay * 4) / delay;
				if (Math.signum(mXV) != Math.signum(mDir.XD)) id = 4 - id;
			}
			else id = -4;
			image = DataManager.getSplitImage("player" + DataManager.getTexturePack(), 5 + id).getScaledCopy(mWidth, mHeight);
			if (mDir == Direction.LEFT) image = image.getFlippedCopy(true, false);
		}
		if (mHurtDelay > 0 && mTime % 10 < 5) aG.drawImage(image, (float) mX - mLevel.getScreenX(), (float) mY - mLevel.getScreenY(), new Color(1, 1, 1, 0.5f));
		else aG.drawImage(image, (float) mX - mLevel.getScreenX(), (float) mY - mLevel.getScreenY());
		
		// Weapon
		if ( !mWeapons.isEmpty())
		{
			mWeapons.get(mWeapon).render(aG);
		}
		
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
	public void hitEntity(final double aXV, final double aYV, final Entity aEntity)
	{
		if (aEntity instanceof Gore) ((Gore) aEntity).hit(this);
		if (aEntity instanceof Banana) ((Banana) aEntity).collect();
		if (aEntity instanceof Heart) ((Heart) aEntity).collect();
		if (aEntity instanceof Enemy)
		{
			if (mHurtDelay <= 0 && mY + mHeight < aEntity.getY() + 5)
			{
				((Enemy) aEntity).hitTop(mCannon, this);
				mJumping = true;
			}
		}
	}
	
	@Override
	public void hurt(final int aAmount, final double aXV, final double aYV)
	{
		if (mHurtDelay <= 0)
		{
			mLife -= aAmount;
			mHurtDelay = 100;
			mHurt = true;
			mXV = aXV;
			mYV = aYV;
			Stats.instance().addHit();
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
		final StringBuilder data = new StringBuilder();
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
	public void addLife(final int aAmount)
	{
		mLife += aAmount;
		if (mLife > mMaxLife + mLifeStep * mLifeSkill) mLife = mMaxLife + mLifeStep * mLifeSkill;
	}
	
	@Override
	public boolean canDestroyBlock(final Direction aDirection)
	{
		return aDirection == Direction.BOTTOM || aDirection == Direction.TOP && isCannonBall();
	}
	
	@Override
	public boolean isSolid()
	{
		return mHurtDelay <= 0;
	}
}
