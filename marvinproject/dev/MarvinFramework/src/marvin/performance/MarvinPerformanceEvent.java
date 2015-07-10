/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package marvin.performance;

/**
 * Algorithms can be divided in events, each event store your own attributes for future analysis.
 * 
 * @version 1.0 02/13/08
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinPerformanceEvent
{
	private String id;
	private String name;
	private long startTime;
	private long endTime;
	private int currentStep;
	
	/**
	 * 
	 * @param a_name
	 */
	public MarvinPerformanceEvent(String a_name){
		this(a_name, a_name);
	}

	/**
	 * 
	 * @param a_id
	 * @param a_name
	 */
	public MarvinPerformanceEvent(String a_id, String a_name){
		id = a_id;
		name = a_name;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getID(){
		return id;
	}

	/**
	 * 
	 * @return
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * 
	 */
	public void start(){
		startTime = System.currentTimeMillis();
		currentStep=0;
	}

	/**
	 * 
	 */
	public void finish(){
		endTime = System.currentTimeMillis();
	}

	/**
	 * 
	 */
	public void stepFinished(){
		currentStep++;
	}

	/**
	 * 
	 * @param a_steps
	 */
	public void stepsFinished(int a_steps){
		currentStep+=a_steps;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getCurrentStep(){
		return currentStep;
	}

	/**
	 * 
	 * @return
	 */
	public long getTotalTime(){
		return endTime-startTime;
	}
}