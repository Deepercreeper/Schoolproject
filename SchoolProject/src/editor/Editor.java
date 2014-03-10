package editor;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

@SuppressWarnings("serial")
public class Editor extends JFrame
{
	private static final int	WINDOW_WIDTH	= 800, WINDOW_HEIGHT = 600;
	
	private final ToolBox		mToolBox;
	
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
		final Container cp = new Container()
		{
			@Override
			public void paint(final Graphics aG)
			{
				aG.setColor(Color.black);
				aG.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
			}
		};
		cp.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		cp.setLocation(0, 0);
		
		setResizable(false);
		
		mToolBox = new ToolBox(this);
		initMenu();
		getContentPane().add(cp, null);
		
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
	
	private void newFile()
	{
		System.out.println("New file!");
	}
	
	private void openFile()
	{
		System.out.println("Open file!");
	}
	
	private void saveFile()
	{
		System.out.println("Save file!");
	}
	
	private void close()
	{
		System.out.println("Close!");
		System.exit(0);
	}
}
