package util;

public class Rectangle
{
	private double	mX, mY, mWidth, mHeight;
	
	public Rectangle(double aX, double aY, double aWidth, double aHeight)
	{
		mX = aX;
		mY = aY;
		mWidth = aWidth;
		mHeight = aHeight;
	}
	
	public boolean intersects(Rectangle aRect)
	{
		return !(mX >= aRect.mX + aRect.mWidth || mX + mWidth <= aRect.mX || mY >= aRect.mY + aRect.mHeight || mY + mHeight <= aRect.mY);
	}
	
	public double getMaxX()
	{
		return mX + mWidth;
	}
	
	public double getMaxY()
	{
		return mY + mHeight;
	}
	
	public double getCenterX()
	{
		return mX + mWidth / 2;
	}
	
	public double getCenterY()
	{
		return mY + mHeight / 2;
	}
	
	public double getX()
	{
		return mX;
	}
	
	public double getY()
	{
		return mY;
	}
	
	public double getWidth()
	{
		return mWidth;
	}
	
	public double getHeight()
	{
		return mHeight;
	}
	
	public void setCenterX(double aX)
	{
		mX = aX - mWidth / 2;
	}
	
	public void setCenterY(double aY)
	{
		mY = aY - mHeight / 2;
	}
	
	public void setX(double aX)
	{
		mX = aX;
	}
	
	public void setY(double aY)
	{
		mY = aY;
	}
	
	public void setWidth(double aWidth)
	{
		mWidth = aWidth;
	}
	
	public void setHeight(double aHeight)
	{
		mHeight = aHeight;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Rectangle)
		{
			Rectangle r = (Rectangle) o;
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
