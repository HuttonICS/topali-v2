package topali.gui.jobs;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JProgressBar;

import topali.cluster.jobs.AnalysisJob;
import topali.gui.JobsPanel;
import topali.gui.JobsPanelEntry;

public class ProgressBarJobEntry extends JobsPanelEntry {

	JProgressBar pb;
	
	public ProgressBarJobEntry(AnalysisJob job) {
		super(job);
	}
	
	@Override
	public JComponent getProgressComponent() {
		pb = new JProgressBar();
		pb.setStringPainted(true);
		pb.setValue(0);
		pb.setPreferredSize(new Dimension(50, 20));
		pb.setMaximum(100);
		pb.setBorderPainted(false);
		pb.setForeground(new Color(140, 165, 214));
		return pb; 
	}

	@Override
	public void setProgress(float progress, String text) {
		pb.setValue((int)progress);
	}

}
