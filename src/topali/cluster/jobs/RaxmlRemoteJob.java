// (C) 2003-2007 Biomathematics & Statistics Scotland
//
// This package may be distributed under the
// terms of the GNU General Public License (GPL)

package topali.cluster.jobs;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import topali.cluster.JobStatus;
import topali.data.*;
import topali.fileio.Castor;
import topali.gui.Tracker;

public class RaxmlRemoteJob extends RemoteJob
{

	Logger log = Logger.getLogger(this.getClass());

	private SequenceSet ss;
		
	public RaxmlRemoteJob(RaxmlResult result, AlignmentData data) {
		super("topali-rx", result);

		this.data = data;
		this.ss = data.getSequenceSet();

		if (result.startTime == 0)
			result.startTime = System.currentTimeMillis();
	}
	
	
	public AnalysisResult ws_downloadResult() throws Exception
	{
		call = getCall();
		call.setOperationName(new QName("topali-rx", "getResult"));

		String resultXML = (String) call.invoke(new Object[]
		{ result.jobId });
		result = (RaxmlResult) Castor.unmarshall(resultXML);

		result.status = JobStatus.COMPLETING;
		return result;
	}

	
	public JobStatus ws_getProgress() throws Exception
	{
		call = getCall();
		call.setOperationName(new QName("topali-rx", "getPercentageComplete"));

		String statusXML = (String) call.invoke(new Object[]
		{ result.jobId });
		JobStatus status = (JobStatus) Castor.unmarshall(statusXML);

		result.status = status.status;
		return status;
	}

	
	public String ws_submitJob() throws Exception
	{
		determineClusterURL("-parallel");

		call = getCall();
		call.setOperationName(new QName("topali-rx", "submit"));

		String alignmentXML = Castor.getXML(ss);
		String resultXML = Castor.getXML(result);
		
		result.jobId = (String) call.invoke(new Object[]
		{ alignmentXML, resultXML });

		log.info("Job in progress: " + result.jobId);
		Tracker.log("SUBMITTED", "RX", result.jobId, url);

		result.status = JobStatus.QUEUING;
		return result.jobId;
	}

	
}
