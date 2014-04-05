package game.entity.weapon;

import game.entity.Entity;
import game.entity.Player;
import game.level.Map;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import util.Direction;

public abstract class Weapon extends Entity
{
	protected Entity	mParent;
	
	protected int		mTime	= 0;
	
	protected Direction	mDir	= Direction.NONE;
	
	public Weapon(final Entity aParent, final int aWidth, final int aHeight)
	{
		super((int) (aParent.getX() + aParent.getWidth() / 2 - aWidth / 2), (int) (aParent.getY() + aParent.getHeight() / 2 - aHeight / 2), aWidth, aHeight);
		mParent = null;
	}
	
	public Weapon(final int aX, final int aY, final int aWidth, final int aHeight)
	{
		super(aX, aY, aWidth, aHeight);
		mParent = null;
	}
	
	@Override
	public void update(final Input aInput)
	{
		mTime++ ;
		if (mParent != null)
		{
			final int mouseX = aInput.getMouseX() + mLevel.getScreenX();
			if (mouseX < mX) mDir = Direction.LEFT;
			else if (mouseX > mX + mWidth) mDir = Direction.RIGHT;
			
			int xPos = (int) (mParent.getX() + mParent.getWidth() / 2 - mWidth / 2);
			if (mDir == Direction.LEFT) xPos -= getXOffset();
			else xPos += getXOffset();
			mX = xPos;
			mY = (int) (mParent.getY() + mParent.getHeight() / 2 - mHeight / 2) + getYOffset();
		}
		else
		{
			// Reset attributes
			mHurt = mOnIce = false;
			
			move();
			
			mXV *= 0.95f - (mOnGround ? 0.45 : 0) + (mOnIce ? 0.48 : 0) - (mInLiquid ? 0.3 : 0);
			
			final double gravity = Map.GRAVITY - (mInLiquid ? 0.1 : 0), friction = Map.FRICTION - (mInLiquid ? 0.1 : 0);
			
			mYV *= friction;
			mYV += gravity;
		}
		tick();
	}
	
	public static Weapon getWeapon(final Player aPlayer, final String aName)
	{
		switch (aName)
		{
			case "Pistol" :
				return new Pistol(aPlayer);
			default :
				return null;
		}
	}
	
	protected abstract void tick();
	
	@Override
	public void respawn()
	{
		mDead = false;
	}
	
	@Override
	public final boolean isSolid()
	{
		return false;
	}
	
	@Override
	public boolean equals(final Object aO)
	{
		if (aO instanceof Weapon)
		{
			final Weapon e = (Weapon) aO;
			return e.getName().equals(getName());
		}
		return false;
	}
	
	@Override
	public final boolean isParticle()
	{
		return mParent != null;
	}
	
	public void collect(final Player aPlayer)
	{
		mParent = aPlayer;
	}
	
	@Override
	public final void hitEntity(final double aXV, final double aYV, final Entity aEntity)
	{
		if (aEntity instanceof Player)
		{
			final Player p = (Player) aEntity;
			p.addWeapon(this);
		}
	}
	
	public abstract void shoot(Input aInput);
	
	protected abstract int getXOffset();
	
	protected abstract int getYOffset();
	
	public abstract void renderIcon(Graphics aG, int aX, int aY);
	
	public abstract String getName();
	
	@Override
	public void render(final Graphics aG)
	{
		aG.setColor(Color.red);
		aG.fillRect((int) (mX - mLevel.getScreenX()), (int) (mY - mLevel.getScreenY()), mWidth, mHeight);
	}
}
