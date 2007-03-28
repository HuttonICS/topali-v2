// (C) 2003-2007 Biomathematics & Statistics Scotland
//
// This package may be distributed under the
// terms of the GNU General Public License (GPL)

package topali.gui;

import static topali.cluster.JobStatus.*;

import javax.swing.SwingUtilities;

import topali.cluster.JobStatus;
import topali.cluster.jobs.AnalysisJob;
import topali.data.AnalysisResult;

public class JobsThread extends Thread
{
	private final JobsPanel jobsPanel;

	private TimerThread timerThread;

	JobsThread(JobsPanel jobsPanel)
	{
		this.jobsPanel = jobsPanel;
		timerThread = new TimerThread();
		timerThread.start();
	}

	public void run()
	{
		while (true)
		{

			try
			{
				Thread.sleep(Prefs.web_check_secs * 1000);
			} catch (InterruptedException e)
			{
				// Interupts are generated when a new job is submitted so it
				// can be dealt with immediately
				timerThread.interrupt();
			}

			WinMainStatusBar.resetIcon = true;

			for (int i = 0; i < jobsPanel.jobs.size(); i++)
			{
				final JobsPanelEntry entry = jobsPanel.jobs.get(i);
				final AnalysisJob job = entry.getJob();

				if (job.getStatus() != FATAL_ERROR)
					job.errorInfo = null;

				try
				{
					switch (job.getStatus())
					{
					case STARTING:
					{
						job.ws_submitJob();
						SwingUtilities.invokeLater(new Runnable()
						{
							public void run()
							{
								entry.setJobId(job.getJobId());
							}
						});
						break;
					}

						// For all these states, just continue to check progress
					case UNKNOWN:
					case QUEUING:
					case HOLDING:
					case RUNNING:
					case COMMS_ERROR:
					{
						final JobStatus status = job.ws_getProgress();
						SwingUtilities.invokeLater(new Runnable()
						{
							public void run()
							{
								entry.setJobStatus(status);
							}
						});

						// When a job is finished, the "old" outofdate results
						// object must be removed from the dataset and replaced
						// with the new one
						if (status.progress >= 100f)
						{
							// Retrieve the result
							AnalysisResult oldR = job.getResult();
							AnalysisResult newR = job.ws_downloadResult();

							// Then tell the service to cleanup tmp files
							job.ws_cleanup();

							job.getAlignmentData().replaceResult(oldR, newR);

							SwingUtilities.invokeLater(new Runnable()
							{
								public void run()
								{
									jobsPanel.removeJobEntry(entry, true);
								}
							});
						}

						break;
					}

					case CANCELLING:
					{
						// Catch any errors here, because we want to cancel the
						// job regardless of what happens at the server end
						try
						{
							job.ws_cancelJob();
						} catch (Exception e)
						{
							TOPALi.log.warning(e.toString());
						}

						SwingUtilities.invokeLater(new Runnable()
						{
							public void run()
							{
								jobsPanel.removeJobEntry(entry, false);
							}
						});
						break;
					}
					}
				} catch (org.apache.axis.AxisFault e)
				{
					String fault = e.getFaultString();

					final int error = (fault.startsWith("java.net.")) ? COMMS_ERROR
							: FATAL_ERROR;
					SwingUtilities.invokeLater(new Runnable()
					{
						public void run()
						{
							entry.getJob().setStatus(error);
						}
					});
					job.errorInfo = e.dumpToString();
					TOPALi.log.warning(e.toString());
				} catch (Exception e)
				{
					SwingUtilities.invokeLater(new Runnable()
					{
						public void run()
						{
							entry.getJob().setStatus(FATAL_ERROR);
						}
					});
					job.errorInfo = e.toString();
					TOPALi.log.warning(e.toString());
				}

				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						entry.updateStatus();
					}
				});
			}

			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					jobsPanel.setStatusPanel();
				}
			});

		}
	}

	class TimerThread extends Thread
	{
		public void run()
		{
			while (true)
			{
				final long current = System.currentTimeMillis();
				for (final JobsPanelEntry entry : jobsPanel.jobs)
				{
					AnalysisJob job = entry.getJob();
					final long start = job.getResult().startTime;
					SwingUtilities.invokeLater(new Runnable()
					{
						public void run()
						{
							entry.setTimeLabel(start, current);
						}
					});
				}

				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						jobsPanel.repaint();
					}
				});

				try
				{
					Thread.sleep(100);
				} catch (InterruptedException e)
				{
				}
			}
		}
	}
}