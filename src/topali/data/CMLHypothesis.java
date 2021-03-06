// (C) 2003-2007 Biomathematics & Statistics Scotland
//
// This package may be distributed under the
// terms of the GNU General Public License (GPL)

package topali.data;

import java.util.*;


/**
 * Holds all data of a codeml hypothesis
 */
public class CMLHypothesis
{

	static String nl = System.getProperty("line.separator");
	
	//the model to use (0 for H0, 2 for the other hypothesis)
	public int model;
	//the actual hypothesis
	public String tree;
	public Map<String, String> settings = new HashMap<String, String>();
	
	//result values
	public double likelihood;
	public double[] omegas;
	//tree with added omega values, how it is produced by codeml
	public String omegaTree;
	
	public CMLHypothesis() {
		settings.put("seqfile", "seq.phy");
		settings.put("treefile ", " tree.txt" );
		settings.put("outfile ", " results.txt" );
		settings.put("noisy ", " 9" );
		settings.put("verbose ", " 0" );
		settings.put("runmode ", " 0" );
		settings.put("seqtype ", " 1" );
		settings.put("CodonFreq ", " 2" );
		settings.put("NSsites ", " 0" );
		settings.put("icode ", " 0" );
		settings.put("fix_kappa ", " 0" );
		settings.put("kappa ", " 2" );
		settings.put("fix_omega ", " 0" );
		settings.put("omega ", " 0.2" );
		}

	public String getCTL() {
		StringBuffer sb = new StringBuffer();
		for(String k : settings.keySet()) {
			sb.append(k+" = "+settings.get(k)+nl);
		}
		sb.append("model = "+model+nl);
		return sb.toString();
	}
	
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("<tree>"+tree+"</tree>\n");
		sb.append("Settings:\n");
		for(String k : settings.keySet()) {
			if(!k.contains("file"))
				sb.append(k+" = "+settings.get(k)+"\n");
		}
		return sb.toString();
	}
}
