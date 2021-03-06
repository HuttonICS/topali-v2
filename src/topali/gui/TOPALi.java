// (C) 2003-2007 Biomathematics & Statistics Scotland
//
// This package may be distributed under the
// terms of the GNU General Public License (GPL)

package topali.gui;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.*;
import java.util.*;
import javax.swing.*;
import org.apache.log4j.*;
import scri.commons.multicore.TokenManager;
import scri.commons.gui.*;
import topali.cluster.LocalJobs;
import topali.data.*;
import topali.i18n.Text;
import topali.logging.GracefulShutdownHandler;
import topali.mod.*;
import topali.var.utils.Utils;

import apple.dts.samplecode.osxadapter.*;

public class TOPALi extends Applet implements Application
{
	//enables/disables extensive logging
	public static final boolean debugClient = false;
	public static final boolean debugJobs = false;

	private final String prefsFile = ".topali-2.5.xml";

	 static Logger root;
	 static Logger log;

	static void initstuff() {
		PropertyConfigurator.configure(TOPALi.class.getResource("/res/client.log4j.properties"));

		org.exolab.castor.util.LocalConfiguration.getInstance().getProperties()
		    .setProperty("org.exolab.castor.parser", "org.xml.sax.helpers.XMLReaderAdapter");

		org.exolab.castor.util.LocalConfiguration.getInstance().getProperties()
		    .setProperty("org.exolab.castor.xml.serializer.factory", "org.exolab.castor.xml.XercesJDK5XMLSerializerFactory");

		root = Logger.getRootLogger();
		log = Logger.getLogger(TOPALi.class);
	}

	private boolean isApplet = false;

	private JWindow splash = null;

	private static Prefs prefs = new Prefs();

	public static WinMain winMain;

	public static void main(String[] args)
	{
		// OS X: This has to be set before anything else
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "TOPALi v2.5");

		initstuff();

		Install4j.doStartUpCheck();

		root.info("TOPALi V. "+Install4j.VERSION);
		root.info("Default Locale is " + Locale.getDefault());
		root.info("Running Java " + System.getProperty("java.version"));

		// Let axis know where its config file is
		System.setProperty("axis.ClientConfigFile", "res/client-config.wsdd");

		Utils.createScratch();

		// These don't work (here) - stick em in as -D options on startup
		// System.setProperty("sun.java2d.translaccel", "true");
		// System.setProperty("sun.java2d.ddoffscreen", "false");
		// System.setProperty("sun.java2d.noddraw", "true");
		// System.setProperty("sun.java2d.opengl","True");

		File initialProject = null;
		if (args.length == 1 && args[0] != null)
			initialProject = new File(args[0]);

		Icons.loadIcons();

		new TOPALi(initialProject);
	}

	@Override
	public void init()
	{
		root.info("Applet init()");
		isApplet = true;
		// new TOPALi();
	}

	@Override
	public void destroy()
	{
		shutdown(null);
	}

	private TOPALi(final File initialProject)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager.put("TextArea.font", UIManager.get("TextField.font"));

			// Use the office look for Windows (but not for Vista)
			if (SysPrefs.isWindowsXP)
			{
				UIManager.setLookAndFeel("org.fife.plaf.Office2003.Office2003LookAndFeel");

				// Gives XP the same (nicer) grey background that Vista uses
				UIManager.put("Panel.background", new Color(240, 240, 240));
			}
			else if (SystemUtils.isMacOS())
				handleOSXStupidities();
		}
		catch (Exception e)
		{
			log.warn("Cannot set Look and Feel", e);
		}

		//If there is a GracefulShutdownHandler, tell it about TOPALi
		//and also use it as UncaughtExceptionHandler
		Enumeration<?> en = root.getAllAppenders();
		while(en.hasMoreElements()) {
			Object o = en.nextElement();
			if(o instanceof GracefulShutdownHandler) {
				GracefulShutdownHandler sh = (GracefulShutdownHandler)o;
				sh.setApplication(this);
				Thread.setDefaultUncaughtExceptionHandler(sh);
			}
		}

		if(debugClient) {
			//MemoryMonitor memmon = new MemoryMonitor(MemoryMonitor.UNIT_MEGABYTES, 10);
			//memmon.start();
		}

//		showSplash();

		// Load the preferences
		prefs.loadPreferences(new File(System.getProperty("user.home"),
				prefsFile), Prefs.class);


		doEncryption(true);


		setProxy();
		LocalJobs.manager = new TokenManager(Prefs.gui_max_cpus);


		// Create and initialise the main window
		winMain = new WinMain();

		winMain.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				shutdown(null);
			}

			@Override
			public void windowOpened(WindowEvent e)
			{
//				hideSplash();

				// If this is the first time TOPALi has been loaded, show help
				if (Prefs.gui_first_run)
				{
					JButton b = new JButton();
					TOPALiHelp.enableHelpOnButton(b, "intro");
					b.doClick();
					Prefs.gui_first_run = false;
				}

				// Do we want to open an initial project?
				if (initialProject != null)
					winMain.menuFileOpenProject(initialProject.getPath());
			}

			public void windowIconified(WindowEvent e) {
				WinMainMenuBar.mWndTOPALi.setSelected(false);
			}

			public void windowDeiconified(WindowEvent e) {
				WinMainMenuBar.mWndTOPALi.setSelected(true);
			}
		});

		scri.commons.gui.MsgBox.initialize(winMain, Text.get("title"));
		scri.commons.gui.TaskDialog.initialize(winMain, Text.get("title"));
		winMain.setVisible(true);
	}

	public static void setProxy()
	{
		if (Prefs.web_proxy_enable)
		{
			System.setProperty("http.proxyHost", Prefs.web_proxy_server);
			System.setProperty("http.proxyPort", "" + Prefs.web_proxy_port);
			// System.setProperty("http.auth.ntlm.domain", "SIMS");

			Authenticator.setDefault(new Authenticator()
			{
				@Override
				protected PasswordAuthentication getPasswordAuthentication()
				{
					return new PasswordAuthentication(Prefs.web_proxy_username,
							Prefs.web_proxy_password.toCharArray());
				}
			});
		} else
		{
			System.setProperty("http.proxyHost", "");
			System.setProperty("http.proxyPort", "");
		}
	}

	public void shutdown(String errorLog)
	{
		log.info("Shutting down TOPALi");

		// Check it's ok to exit
		if (!winMain.okToContinue())
			return;
		winMain.setVisible(false);

		//check if there is a vamsas session
		if(winMain.vamsas!=null)
			winMain.vamsasDisconnect();

		if (winMain.getExtendedState() == Frame.MAXIMIZED_BOTH)
		{
			Prefs.gui_maximized = true;
			winMain.setExtendedState(Frame.NORMAL);
		} else
			Prefs.gui_maximized = false;

		Prefs.gui_win_width = winMain.getWidth();
		Prefs.gui_win_height = winMain.getHeight();
		Prefs.gui_splits_loc = WinMain.splits.getDividerLocation();
		WinMain.ovDialog.exit();
		WinMain.rDialog.exit();

		// Save the preferences
		doEncryption(false);
		prefs.savePreferences(new File(System.getProperty("user.home"),
				prefsFile), Prefs.class);
		// Remove tmp files
		// Utils.emptyScratch();

		if(errorLog!=null) {
//		    try {
//			Utils.openMailclient("help@topali.org", "TOPALi Bug Report", errorLog);
//		    } catch (Exception e) {
//			e.printStackTrace();
//			Utils.copyToClipboard(errorLog);
//			String msg = "Could not launch eMail client...\n" +
//					"The error information has been copied to the clipboard.\n" +
//					"Please send an eMail manually to help@topali.org,\n" +
//					"pasting in your clipboard content. Thank you!";
//			JOptionPane.showMessageDialog(null, msg, "Error opening eMail client", JOptionPane.ERROR_MESSAGE);
//		    }
		}

		// And exit
		if (isApplet == false)
			System.exit(0);
		else
			winMain.dispose();
	}

	private void showSplash()
	{
		JLabel label = new JLabel(new ImageIcon(getClass().getResource(
				"/res/icons/splash.png")));

		splash = new JWindow();
		splash.add(label);
		splash.pack();
		splash.setLocationRelativeTo(null);
		splash.setVisible(true);
	}

	private void hideSplash()
	{
		splash.setVisible(false);
		splash.dispose();
	}

	// Decrypts/encrypts passwords that have been written to disk by TOPALi
	private void doEncryption(boolean decrypt)
	{
		if(Prefs.web_proxy_password==null || Prefs.web_proxy_password.equals(""))
			return;

		// About as secure as a chocolate teapot is functional...
		String key = "287e283d5737552c5a72277561745452";
		String scheme = StringEncrypter.DESEDE_ENCRYPTION_SCHEME;

		try
		{
			StringEncrypter s = new StringEncrypter(scheme, key);

			if (decrypt)
			{
				try
				{
					Prefs.web_proxy_password = s
							.decrypt(Prefs.web_proxy_password);
				} catch (EncryptionException e)
				{
					log.warn("Cannot decrypt.\n", e);
				}
			} else
			{
				try
				{
					Prefs.web_proxy_password = s
							.encrypt(Prefs.web_proxy_password);
				} catch (EncryptionException e)
				{
					log.warn("Cannot encrypt.", e);
				}
			}
		} catch (Exception e)
		{
			log.warn("En/Decryption failed", e);
		}
	}


	// --------------------------------------------------
	// Methods required for better native support on OS X

	private void handleOSXStupidities()
	{
		try
		{
			// Register handlers to deal with the System menu about/quit options
			OSXAdapter.setAboutHandler(this,
				getClass().getDeclaredMethod("aboutOSX", (Class[])null));
			OSXAdapter.setQuitHandler(this,
				getClass().getDeclaredMethod("shutdownOSX", (Class[])null));
			OSXAdapter.setPreferencesHandler(this,
				getClass().getDeclaredMethod("preferencesOSX", (Class[])null));

			// Dock the menu bar at the top of the screen
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		}
		catch (Exception e) { System.out.println(e); }
	}

	/** "About Flapjack" on the OS X system menu. */
	public void aboutOSX()
	{
		winMain.menuHelpAbout();
	}

	/** Called by a forced CMD-Q quit from the OS. Return false to cancel. */
	public boolean shutdownOSX()
	{
		if (winMain.okToContinue() == false)
			return false;

		shutdown(null);
		return true;
	}

	public void preferencesOSX()
	{
		winMain.menuAnlsSettings();
	}

	static void osxMinimize()
	{
		winMain.setExtendedState(Frame.ICONIFIED);
	}

	static void osxZoom()
	{
		if (winMain.getExtendedState() == Frame.NORMAL)
			winMain.setExtendedState(Frame.MAXIMIZED_BOTH);
		else
			winMain.setExtendedState(Frame.NORMAL);
	}

	static void osxTOPALi()
	{
		if (Prefs.gui_maximized)
			winMain.setExtendedState(Frame.MAXIMIZED_BOTH);
		else
			winMain.setExtendedState(Frame.NORMAL);

		WinMainMenuBar.mWndTOPALi.setSelected(true);
	}
}