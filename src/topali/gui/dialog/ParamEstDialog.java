// (C) 2003-2006 Iain Milne
//
// This package may be distributed under the
// terms of the GNU General Public License (GPL)

package topali.gui.dialog;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import pal.alignment.*;

import topali.analyses.*;
import topali.gui.*;

import doe.*;

public class ParamEstDialog extends JDialog
{
	private ParamEstimateThread estThread;
	
	public ParamEstDialog(SimpleAlignment alignment)
	{
		super(MsgBox.frm, Text.Analyses.getString("ParamEstDialog.gui01"), true);		
		
		JPanel p1 = new JPanel(new BorderLayout());
		p1.add(new JLabel(Text.Analyses.getString("ParamEstDialog.gui02")),
			BorderLayout.NORTH);
		p1.add(new JLabel(Text.Analyses.getString("ParamEstDialog.gui03")),
			BorderLayout.SOUTH);
		p1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(p1, BorderLayout.CENTER);
		
		
		estThread = new ParamEstimateThread(alignment);
		
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent event)
			{
				Runnable r = new Runnable() {
					public void run()
					{				
						estThread.start();
				
						while (estThread.isAlive())
							try { Thread.sleep(250); }
							catch (InterruptedException e) {}
										
						setVisible(false);
					}
				};
				
				new Thread(r).start();
			}
		});
		
		pack();
		setResizable(false);
		setLocationRelativeTo(MsgBox.frm);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}
	
	public double getRatio()
		{ return estThread.getRatio(); }
	
	public double getAlpha()
		{ return estThread.getAlpha(); }
	
	public double getKappa()
		{ return estThread.getKappa(); }
		
	public double getAvgDistance()
		{ return estThread.getAvgDistance(); }
	
	public double[] getFreqs()
		{ return estThread.getFreqs(); }	
}
