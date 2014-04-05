package util;

import java.util.HashMap;

public class InputKeys
{
	private static InputKeys			INSTANCE;
	
	private final HashMap<Key, Integer>	mKeys	= new HashMap<>();
	
	private InputKeys()
	{
		for (final Key key : Key.values())
			mKeys.put(key, key.getDefault());
	}
	
	private InputKeys(final String aData)
	{
		readData(aData);
	}
	
	/**
	 * Creates a new instance if not done yet and returns it.
	 * 
	 * @return the current instance.
	 */
	public static InputKeys instance()
	{
		if (INSTANCE == null) INSTANCE = new InputKeys();
		return INSTANCE;
	}
	
	/**
	 * Creates a instance out of the given data.
	 * 
	 * @param aData
	 *            The input keys data.
	 */
	public static void loadInstance(final String aData)
	{
		INSTANCE = new InputKeys(aData);
	}
	
	public int getKey(final Key aKey)
	{
		return mKeys.get(aKey);
	}
	
	public void setKey(final Key aKey, final int aValue)
	{
		mKeys.put(aKey, aValue);
	}
	
	public String getData()
	{
		final StringBuilder data = new StringBuilder("Input");
		for (final Key key : mKeys.keySet())
			data.append("," + key.toString() + "=" + mKeys.get(key));
		return data.toString();
	}
	
	private void readData(final String aData)
	{
		final String[] data = aData.split(",");
		String[] keyAndValue;
		for (int i = 1; i < data.length; i++ )
		{
			keyAndValue = data[i].split("=");
			mKeys.put(Key.valueOf(keyAndValue[0]), Integer.parseInt(keyAndValue[1]));
		}
		for (final Key key : Key.values())
			if ( !mKeys.containsKey(key)) mKeys.put(key, key.getDefault());
	}
}
