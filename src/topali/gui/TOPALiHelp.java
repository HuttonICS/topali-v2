// (C) 2003-2007 Biomathematics & Statistics Scotland
//
// This package may be distributed under the
// terms of the GNU General Public License (GPL)

package topali.gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.help.*;
import javax.swing.*;

import org.apache.log4j.Logger;

import topali.i18n.Text;

public class TOPALiHelp
{
	 static Logger log = Logger.getLogger(TOPALiHelp.class);
	
	 public static HelpSet hs;

	 public static HelpBroker hb;

	static
	{
		try
		{
			// URL url = HelpSet.findHelpSet(null, "/res/help/help.hs");
			URL url = TOPALiHelp.class.getResource("/res/help/help.hs");
			hs = new HelpSet(null, url);

			hb = hs.createHelpBroker("TOPALi");
			hb.setFont(new Font("SansSerif", Font.PLAIN, 12));

			// ((DefaultHelpBroker)hb).setActivationWindow(scri.commons.gui.TOPALi.winMain);
		} catch (Exception e)
		{
			log.warn("Error loading help",e);
		}
	}

	// Called by a dialog class to enable F1 help to the given helpPage
	public static void enableHelpKey(JRootPane rootPane, String helpPage)
	{
		hb.enableHelpKey(rootPane, helpPage, hs);
	}

	// Associates the given control with the a given online help topic
	public static void enableHelpOnButton(Component comp, String helpPage)
	{
		hb.enableHelpOnButton(comp, helpPage, hs);
	}

	// Associates the given menuitem with the a given online help topic
	public static void enableHelpOnButton(JMenuItem comp, String helpPage)
	{
		hb.enableHelpOnButton(comp, helpPage, hs);
	}

	public static JButton getHelpButton(String helpPage)
	{
		JButton bHelp = new JButton(Text.get("help"));
		bHelp.setMnemonic(KeyEvent.VK_H);
		enableHelpOnButton(bHelp, helpPage);

		return bHelp;
	}
	
	public static JButton getHelpIconButton(String helpPage)
	{
		JButton bHelp = new JButton(Icons.HELP16);
		bHelp.setMnemonic(KeyEvent.VK_H);
		enableHelpOnButton(bHelp, helpPage);

		return bHelp;
	}
}