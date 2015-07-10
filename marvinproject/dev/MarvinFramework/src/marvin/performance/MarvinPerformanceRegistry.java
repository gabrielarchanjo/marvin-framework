/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package marvin.performance;

import java.util.LinkedList;

/**
 * Stores performance entries.
 * @version 1.0 02/13/08
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinPerformanceRegistry
{
	private LinkedList<MarvinPerformanceEntry>listEntries;

	/**
	 * empty constructor
	 */
	public MarvinPerformanceRegistry(){
		listEntries = new LinkedList<MarvinPerformanceEntry>();
	}

	/**
	 * 
	 * @param a_entry
	 */
	public void addEntry(MarvinPerformanceEntry a_entry){
		listEntries.add(a_entry);
	}

	/**
	 * 
	 * @param a_index
	 * @return
	 */
	public MarvinPerformanceEntry getEntry(int a_index){
		return listEntries.get(a_index);
	}

	/**
	 * 
	 * @return
	 */
	public int size(){
		return listEntries.size();
	}

	/**
	 * 
	 * @return
	 */
	public long getTotalTime(){
		long l_totalTime=0;
		Object[] l_entries = listEntries.toArray();
		for(int i=0; i<l_entries.length; i++){
			l_totalTime+= ((MarvinPerformanceEntry)l_entries[i]).getTotalTime();
		}
		return l_totalTime;
	}
}