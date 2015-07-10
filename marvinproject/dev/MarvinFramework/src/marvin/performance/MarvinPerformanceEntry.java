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
 * Stores all the image process. An image process can be divided in events,
 * each event store attributes like start time, end time, number of steps etc. 
 * These attributes are used to do statistics analysis.
 * 
 * @version 1.0 02/13/08
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinPerformanceEntry
{
	String id;
	String name;

	private LinkedList<MarvinPerformanceEvent>listEvents;
	private MarvinPerformanceEvent currentEvent;

	/**
	 * Constructs {@link MarvinPerformanceEntry}
	 * @param a_id
	 * @param a_name
	 */
	public MarvinPerformanceEntry(String a_id, String a_name){
		name = a_name;
		id = a_id;
		listEvents = new LinkedList<MarvinPerformanceEvent>();
	}

	/**
	 * 
	 * @param a_index
	 * @return
	 */
	public MarvinPerformanceEvent getEvent(int a_index){
		return listEvents.get(a_index);
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
	 * @return
	 */
	public int size(){
		return listEvents.size();
	}

	/**
	 * 
	 * @param a_id
	 * @param a_name
	 */
	public void startEvent(String a_id, String a_name){
		currentEvent = new MarvinPerformanceEvent(a_id, a_name);
		currentEvent.start();
		listEvents.add(currentEvent);
	}

	/**
	 * 
	 */
	public void finishEvent(){
		if(currentEvent != null){
			currentEvent.finish();
		}
	}

	/**
	 * 
	 */
	public void stepFinished(){
		if(currentEvent != null){
			currentEvent.stepFinished();
		}
	}

	/**
	 * 
	 * @param a_steps
	 */
	public void stepsFinished(int a_steps){
		if(currentEvent != null){
			currentEvent.stepsFinished(a_steps);
		}
	}

	/**
	 * 
	 * @return
	 */
	public long getCurrentStep(){
		long l_currentStep=0;
		Object[] l_events = listEvents.toArray();
		for(int i=0; i<l_events.length; i++){
			l_currentStep+= ((MarvinPerformanceEvent)l_events[i]).getCurrentStep();
		}
		return l_currentStep;
	}

	/**
	 * 
	 * @return
	 */
	public long getTotalTime(){
		long l_totalTime=0;
		Object[] l_events = listEvents.toArray();
		for(int i=0; i<l_events.length; i++){
			l_totalTime+= ((MarvinPerformanceEvent)l_events[i]).getTotalTime();
		}
		return l_totalTime;
	}
}