package editor;

import game.level.block.Block;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.KeyStroke;
import javax.swing.SpinnerModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class NewEditor extends JFrame
{
	private boolean				mSaved, mChangesMade, mSizeModelChanged;
	
	private int					mWidth, mHeight, mWorld, mLevel, mMouseX, mMouseY;
	
	private final Container		mCP;
	
	private final JScrollPane	mScrollPane;
	
	private final ToolBox		mToolBox;
	
	private SpinnerModel		mWidthModel, mHeightModel;
	
	private short[][]			mMap;
	
	public NewEditor()
	{
		EditorDataManager.init();
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(final WindowEvent aE)
			{
				close();
			}
		});
		
		// Initiate fields
		{
			mCP = new Container()
			{
				@Override
				public void paint(final Graphics aG)
				{
					render(aG);
				}
			};
			mCP.addMouseMotionListener(new MouseMotionListener()
			{
				@Override
				public void mouseMoved(final MouseEvent aE)
				{
					mMouseX = aE.getX() / Block.SIZE;
					mMouseY = aE.getY() / Block.SIZE;
					repaint();
				}
				
				@Override
				public void mouseDragged(final MouseEvent aE)
				{
					mMouseX = aE.getX() / Block.SIZE;
					mMouseY = aE.getY() / Block.SIZE;
					repaint();
					click();
				}
			});
			mCP.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mousePressed(final MouseEvent aE)
				{
					click();
				}
			});
			mScrollPane = new JScrollPane(mCP);
			getContentPane().add(mScrollPane);
			
			initMenu();
			mChangesMade = false;
			mSaved = false;
			mToolBox = new ToolBox(this);
		}
		
		newMap();
		pack();
		setVisible(true);
	}
	
	private void newMap()
	{
		if (mChangesMade && !mSaved) saveMap();
		mChangesMade = mSaved = false;
		mWidth = 100;
		mHeight = 30;
		mMap = new short[mWidth][mHeight];
		resizeCP();
		setTitle("Level: " + mWorld + "-" + mLevel);
		/*
		 * TODO
		 * - ...
		 */
	}
	
	private void openMap()
	{
		if (mChangesMade && !mSaved) saveMap();
		final int[] worldAndLevel = showOpenDialog();
		if (worldAndLevel == null) return;
		mChangesMade = mSaved = false;
		mWorld = worldAndLevel[0];
		mLevel = worldAndLevel[1];
		
		final BufferedImage data = EditorDataManager.getMapImage(mWorld, mLevel);
		mWidth = data.getWidth();
		mHeight = data.getHeight();
		
		mMap = new short[mWidth][mHeight];
		final int[] rgb = new int[mWidth * mHeight];
		final int[] alphas = new int[mWidth * mHeight];
		data.getRGB(0, 0, mWidth, mHeight, rgb, 0, mWidth);
		data.getAlphaRaster().getPixels(0, 0, mWidth, mHeight, alphas);
		for (int x = 0; x < mWidth; x++ )
			for (int y = 0; y < mHeight; y++ )
			{
				final short id = Block.getIdFromCode(0xffffff & rgb[x + y * mWidth]);
				if (id == -1) mMap[x][y] = Block.AIR.getId();
				else
				{
					final Block block = Block.get(id);
					if (block.isItemBlock())
					{
						mMap[x][y] = Block.AIR.getId();
						// TODO load items
						// (Item.getItem(x * Block.SIZE, y * Block.SIZE, rgb[x + y*width]));
					}
					else mMap[x][y] = id;
				}
			}
		
		resizeCP();
		setTitle("Level: " + mWorld + "-" + mLevel);
		repaint();
		/*
		 * TODO
		 * - load items
		 * - ...
		 */
	}
	
	private void click()
	{
		if (mMouseX < 0 || mMouseX >= mWidth || mMouseY < 0 || mMouseY >= mHeight) return;
		mMap[mMouseX][mMouseY] = mToolBox.getBlockId();
	}
	
	private void saveMap()
	{
		/*
		 * TODO
		 * - show dialog
		 * - load world and level
		 * - save mMap
		 * - set title
		 */
		mSaved = true;
	}
	
	private void close()
	{
		if (mChangesMade && !mSaved) saveMap();
		setVisible(false);
		System.exit(0);
	}
	
	private void resizeMap()
	{
		if (mSizeModelChanged) return;
		final int oldWidth = mWidth, oldHeight = mHeight;
		mWidth = (int) mWidthModel.getValue();
		mHeight = (int) mHeightModel.getValue();
		final short[][] oldMap = mMap;
		mMap = new short[mWidth][mHeight];
		for (int x = 0; x < oldWidth && x < mWidth; x++ )
			for (int y = 0; y < oldHeight && y < mHeight; y++ )
				mMap[x][y] = oldMap[x][y];
		resizeCP();
		/*
		 * TODO
		 * - ...
		 */
	}
	
	private void render(final Graphics aG)
	{
		aG.setColor(Color.black);
		aG.fillRect(0, 0, mWidth * Block.SIZE, mHeight * Block.SIZE);
		
		for (int x = 0; x < mWidth; x++ )
			for (int y = 0; y < mHeight; y++ )
				Block.render(x, y, mMap[x][y], aG, this);
		
		aG.setColor(Color.red);
		aG.drawRect(mMouseX * Block.SIZE, mMouseY * Block.SIZE, Block.SIZE, Block.SIZE);
		/*
		 * TODO
		 * - render selection (selection tool)
		 */
	}
	
	private int[] showOpenDialog()
	{
		final ArrayList<String> levels = EditorDataManager.getLevels();
		final String[] levelArray = levels.toArray(new String[levels.size()]);
		final String level = (String) JOptionPane.showInputDialog(this, "Level öffnen:", "Öffnen...", JOptionPane.PLAIN_MESSAGE, null, levelArray, levels.get(0));
		if (level == null) return null;
		final int[] worldAndLevel = new int[2];
		worldAndLevel[0] = Integer.parseInt(level.split("-")[0]);
		worldAndLevel[1] = Integer.parseInt(level.split("-")[1]);
		return worldAndLevel;
	}
	
	private boolean showSaveDialog()
	{
		return false;
	}
	
	private void resizeCP()
	{
		mCP.setPreferredSize(new Dimension(mWidth * Block.SIZE, mHeight * Block.SIZE));
		mScrollPane.revalidate();
		mSizeModelChanged = true;
		mWidthModel.setValue(mWidth);
		mHeightModel.setValue(mHeight);
		mSizeModelChanged = false;
		repaint();
	}
	
	private void initMenu()
	{
		final JMenuBar menu = new JMenuBar();
		{
			final JMenu file = new JMenu("Datei");
			{
				final JMenuItem newFile = new JMenuItem("Neu", 0);
				newFile.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(final ActionEvent aE)
					{
						newMap();
					}
				});
				newFile.setMnemonic('N');
				newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK));
				file.add(newFile);
				
				final JMenuItem openFile = new JMenuItem("Öffnen", 1);
				openFile.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(final ActionEvent aE)
					{
						openMap();
					}
				});
				openFile.setMnemonic('f');
				openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
				file.add(openFile);
				
				file.addSeparator();
				
				final JMenuItem saveFile = new JMenuItem("Speichern", 0);
				saveFile.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(final ActionEvent aE)
					{
						saveMap();
					}
				});
				saveFile.setMnemonic('S');
				saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
				file.add(saveFile);
				
				file.addSeparator();
				
				final JMenuItem close = new JMenuItem("Beenden", 0);
				close.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(final ActionEvent aE)
					{
						close();
					}
				});
				close.setMnemonic('B');
				close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, Event.ALT_MASK));
				file.add(close);
			}
			file.setMnemonic('D');
			menu.add(file);
			
			final JLabel size = new JLabel("Map größe:");
			menu.add(size);
			
			final JSpinner width = new JSpinner(), height = new JSpinner();
			new NumberEditor(width);
			new NumberEditor(height);
			width.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(final ChangeEvent aE)
				{
					resizeMap();
				}
			});
			height.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(final ChangeEvent aE)
				{
					resizeMap();
				}
			});
			mWidthModel = width.getModel();
			mHeightModel = height.getModel();
			menu.add(width);
			menu.add(height);
			
			final JComboBox<String> tools = new JComboBox<>();
			tools.addItem("Stift");
			tools.addItem("Selektion");
			menu.add(tools);
			
			final JComboBox<String> texturePacks = new JComboBox<>();
			for (final String texturePack : EditorDataManager.getTexturePacks())
				texturePacks.addItem(texturePack);
			texturePacks.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(final ActionEvent aE)
				{
					EditorDataManager.setTexturePack(texturePacks.getItemAt(texturePacks.getSelectedIndex()));
					mToolBox.repaint();
				}
			});
			menu.add(texturePacks);
		}
		setJMenuBar(menu);
	}
}
