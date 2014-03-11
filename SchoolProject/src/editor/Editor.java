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
public class Editor extends JFrame
{
	private static final int	WINDOW_WIDTH	= 800, WINDOW_HEIGHT = 600;
	
	private final ToolBox		mToolBox;
	
	private int					mWorldId		= -1, mLevelId = -1;
	
	private SpinnerModel		mWidth, mHeight;
	
	private final JScrollPane	mScrollPane;
	
	private final Container		mCP;
	
	private short[][]			mMap;
	
	public Editor()
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
		
		mCP = new Container()
		{
			@Override
			public void paint(final Graphics aG)
			{
				render(aG);
			}
		};
		mCP.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		// mCP.setLocation(0, 0);
		mScrollPane = new JScrollPane(mCP);
		getContentPane().add(mScrollPane, null);
		
		mToolBox = new ToolBox(new NewEditor());
		mToolBox.setEnabled(false);
		initMenu();
		
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(final WindowEvent aE)
			{
				close();
			}
		});
		setResizable(false);
		pack();
		setVisible(true);
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
					public void actionPerformed(final ActionEvent aArg0)
					{
						newFile();
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
						openFile();
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
						saveFile();
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
				public void stateChanged(final ChangeEvent aArg0)
				{
					// resizeMap();
				}
			});
			height.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(final ChangeEvent aArg0)
				{
					// resizeMap();
				}
			});
			mWidth = width.getModel();
			mHeight = height.getModel();
			menu.add(width);
			menu.add(height);
			
			final JComboBox<String> tools = new JComboBox<>();
			tools.addItem("Stift");
			tools.addItem("Selektion");
			menu.add(tools);
			
			final JComboBox<String> texturePacks = new JComboBox<>();
			texturePacks.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(final ActionEvent aE)
				{
					EditorDataManager.setTexturePack(texturePacks.getItemAt(texturePacks.getSelectedIndex()));
					mToolBox.repaint();
				}
			});
			for (final String texturePack : EditorDataManager.getTexturePacks())
				texturePacks.addItem(texturePack);
			menu.add(texturePacks);
		}
		setJMenuBar(menu);
	}
	
	private void resizeMap()
	{
		final short[][] newMap = new short[(int) mWidth.getValue()][(int) mHeight.getValue()];
		for (int x = 0; x < mMap.length; x++ )
			for (int y = 0; y < mMap[x].length; y++ )
				newMap[x][y] = mMap[x][y];
		mCP.setPreferredSize(new Dimension((int) mWidth.getValue() * Block.SIZE, (int) mHeight.getValue() * Block.SIZE));
		// TODO repaint();
	}
	
	private int[] getWorldLevelInput()
	{
		String data;
		do
		{
			data = JOptionPane.showInputDialog(this, "Welt und Level Nummer angeben. (Bspl: \"5-4\")", "Welt und Level", JOptionPane.PLAIN_MESSAGE);
		}
		while ( !isOk(data));
		final int worldId = Integer.parseInt(data.split("-")[0]);
		final int levelId = Integer.parseInt(data.split("-")[1]);
		return new int[] { worldId, levelId };
	}
	
	private boolean isOk(final String aData)
	{
		final String[] worldAndLevel = aData.split("-");
		if (worldAndLevel.length != 2) return false;
		try
		{
			if (Integer.parseInt(worldAndLevel[0]) < 0) return false;
			if (Integer.parseInt(worldAndLevel[1]) < 0) return false;
		}
		catch (final NumberFormatException aE)
		{
			return false;
		}
		return true;
	}
	
	private void newFile()
	{
		final int[] worldAndLevel = getWorldLevelInput();
		mWorldId = worldAndLevel[0];
		mLevelId = worldAndLevel[1];
		setTitle("Level:" + mWorldId + "-" + mLevelId);
	}
	
	private void openFile()
	{
		final ArrayList<String> levels = EditorDataManager.getLevels();
		final String[] levelArray = levels.toArray(new String[levels.size()]);
		final String level = (String) JOptionPane.showInputDialog(this, "Level öffnen:", "Öffnen...", JOptionPane.PLAIN_MESSAGE, null, levelArray, levels.get(0));
		mWorldId = Integer.parseInt(level.split("-")[0]);
		mLevelId = Integer.parseInt(level.split("-")[1]);
		// readData(EditorDataManager.getMapImage(levels.indexOf(level)));
	}
	
	private void saveFile()
	{
		EditorDataManager.saveMapImage(mMap, mWorldId, mLevelId);
		System.out.println("Save file!");
	}
	
	private void readData(final BufferedImage aImage)
	{
		final int width = aImage.getWidth(), height = aImage.getHeight();
		mWidth.setValue(width);
		mHeight.setValue(height);
		mMap = new short[width][height];
		final int[] rgb = new int[width * height];
		final int[] alphas = new int[width * height];
		aImage.getRGB(0, 0, width, height, rgb, 0, width);
		aImage.getAlphaRaster().getPixels(0, 0, width, height, alphas);
		for (int x = 0; x < width; x++ )
			for (int y = 0; y < height; y++ )
			{
				final short id = Block.getIdFromCode(rgb[x + y * width]);
				if (id == -1) mMap[x][y] = Block.AIR.getId();
				else
				{
					final Block block = Block.get(id);
					if (block.isItemBlock())
					{
						mMap[x][y] = Block.AIR.getId();
						// (Item.getItem(x * Block.SIZE, y * Block.SIZE, rgb[x + y*width]));
					}
					else mMap[x][y] = id;
				}
			}
		mCP.setPreferredSize(new Dimension((int) mWidth.getValue() * Block.SIZE, (int) mHeight.getValue() * Block.SIZE));
	}
	
	private void render(final Graphics aG)
	{
		aG.setColor(Color.black);
		aG.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		System.out.println(mScrollPane.getHorizontalScrollBar().getValue());
		System.out.println(mScrollPane.getVerticalScrollBar().getValue());
	}
	
	private void close()
	{
		System.out.println("Close!");
		setVisible(false);
		System.exit(0);
	}
}
