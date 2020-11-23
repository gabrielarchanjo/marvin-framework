/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package net.marvinproject.framework.thread;

import net.marvinproject.framework.plugin.MarvinPlugin;

/**
 * Thread Event.
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinThreadEvent {
	
	private long			threadId;		
	private MarvinPlugin 	plugin;
	
	
	/**
	 * Constructor.
	 * @param plg	- plug-in associated with the event.
	 */
	MarvinThreadEvent(long threadId, MarvinPlugin plg){
		this.threadId = threadId;
		plugin = plg;
	}
	
	public long getThreadId(){
		return threadId;
	}
	
	/**
	 * @return the plug-in associated with the event.
	 */
	public MarvinPlugin getPlugin(){
		return plugin;
	}
}
