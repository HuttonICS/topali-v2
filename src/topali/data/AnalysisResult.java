// (C) 2003-2007 Biomathematics & Statistics Scotland
//
// This package may be distributed under the
// terms of the GNU General Public License (GPL)

package topali.data;

import topali.cluster.JobStatus;

public class AnalysisResult extends DataObject
{

	// Simple name for this result (used by navpanel)
	public String guiName;

	// The job's friendly name (used by jobspanel)
	public String jobName;

	// Is the job local or server side
	public boolean isRemote = true;

	// (If it is remote), the URL of the server where the job will be run
	public String url;

	// Location on disk of the scratch file area
	public String tmpDir;

	// The job's unique ID (can be server or client assigned)
	public String jobId;

	// The times the job was started and finished at
	public long startTime, endTime;

	// The job's current status
	public int status = JobStatus.STARTING;

	// If the user should get warned about something:
	public String warning = "";
	
	protected boolean isResubmittable = false;
	
	public AnalysisResult()
	{
		super();
	}


	public AnalysisResult(int id)
	{
		super(id);
	}


	
	public String toString()
	{
		return guiName;
	}
	
	public boolean isResubmittable() {
		return isResubmittable;
	}
}
