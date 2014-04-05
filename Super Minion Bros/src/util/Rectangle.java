package util;

public class Rectangle
{
	private double	mX, mY, mWidth, mHeight;
	
	/**
	 * Creates a new rectangle.
	 * 
	 * @param aX
	 *            The x position.
	 * @param aY
	 *            The y position.
	 * @param aWidth
	 *            The width.
	 * @param aHeight
	 *            The height.
	 */
	public Rectangle(final double aX, final double aY, final double aWidth, final double aHeight)
	{
		mX = aX;
		mY = aY;
		mWidth = aWidth;
		mHeight = aHeight;
	}
	
	/**
	 * Returns whether this rectangle intersects with the given one.
	 * 
	 * @param aRect
	 *            The intersecting rectangle.
	 * @return {@code true} if this rectangle intersects and {@code false} if not.
	 */
	public boolean intersects(final Rectangle aRect)
	{
		return !(mX >= aRect.mX + aRect.mWidth || mX + mWidth <= aRect.mX || mY >= aRect.mY + aRect.mHeight || mY + mHeight <= aRect.mY);
	}
	
	/**
	 * The maximum x value.
	 * 
	 * @return {@code mX + mWidth}.
	 */
	public double getMaxX()
	{
		return mX + mWidth;
	}
	
	/**
	 * The maximum y value.
	 * 
	 * @return {@code mY + mHeight}.
	 */
	public double getMaxY()
	{
		return mY + mHeight;
	}
	
	/**
	 * The center x value.
	 * 
	 * @return {@code mX + mWidth/2}.
	 */
	public double getCenterX()
	{
		return mX + mWidth / 2;
	}
	
	/**
	 * The center y value.
	 * 
	 * @return {@code mY + mHeight/2}.
	 */
	public double getCenterY()
	{
		return mY + mHeight / 2;
	}
	
	/**
	 * The x value.
	 * 
	 * @return {@code mX}.
	 */
	public double getX()
	{
		return mX;
	}
	
	/**
	 * The y value.
	 * 
	 * @return {@code mY}.
	 */
	public double getY()
	{
		return mY;
	}
	
	/**
	 * The width.
	 * 
	 * @return {@code mWidth}.
	 */
	public double getWidth()
	{
		return mWidth;
	}
	
	/**
	 * The height.
	 * 
	 * @return {@code mHeight}.
	 */
	public double getHeight()
	{
		return mHeight;
	}
	
	/**
	 * Sets the center x position of this rectangle.
	 * 
	 * @param aX
	 *            The center x position.
	 */
	public void setCenterX(final double aX)
	{
		mX = aX - mWidth / 2;
	}
	
	/**
	 * Sets the center y position of this rectangle.
	 * 
	 * @param aY
	 *            The center y position.
	 */
	public void setCenterY(final double aY)
	{
		mY = aY - mHeight / 2;
	}
	
	/**
	 * Sets the x position of this rectangle.
	 * 
	 * @param aX
	 *            The new x position.
	 */
	public void setX(final double aX)
	{
		mX = aX;
	}
	
	/**
	 * Sets the y position of this rectangle.
	 * 
	 * @param aY
	 *            The new y position.
	 */
	public void setY(final double aY)
	{
		mY = aY;
	}
	
	/**
	 * Sets the width of this rectangle.
	 * 
	 * @param aWidth
	 *            The new width.
	 */
	public void setWidth(final double aWidth)
	{
		mWidth = aWidth;
	}
	
	/**
	 * Sets the height of this rectangle.
	 * 
	 * @param aHeight
	 *            The new height.
	 */
	public void setHeight(final double aHeight)
	{
		mHeight = aHeight;
	}
	
	@Override
	public boolean equals(final Object aO)
	{
		if (aO instanceof Rectangle)
		{
			final Rectangle r = (Rectangle) aO;
			return r.mX == mX && r.mY == mY && r.mWidth == mWidth && r.mHeight == mHeight;
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return "Rectangle:" + mX + ":" + mY + " " + mWidth + ":" + mHeight;
	}
}
