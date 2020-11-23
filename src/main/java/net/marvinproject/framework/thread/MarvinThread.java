/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package net.marvinproject.framework.thread;

import net.marvinproject.framework.image.MarvinImage;
import net.marvinproject.framework.image.MarvinImageMask;
import net.marvinproject.framework.plugin.MarvinImagePlugin;
import net.marvinproject.framework.plugin.MarvinPlugin;

/**
 * Thread to process a segment or an entire image.
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinThread implements Runnable{
	
	private static long currentId = 0;
	
	private enum PluginType{
		PLUGIN_IMAGE
	}
	
	private long					id;
	private MarvinThreadListener 	listener;
	private Thread 					thread;
	private MarvinPlugin 			plugin;
	private MarvinImage 			imageIn,
									imageOut;
	private MarvinImageMask 		imageMask;
	PluginType 						eType;
	
	/**
	 * Constructor.
	 * @param plg		plugin associated with this thread.
	 * @param imgIn		plug-in input image.
	 * @param imgOut	plug-in output image.
	 * @param mask		image mask.
	 */
	public MarvinThread
	(
		MarvinImagePlugin plg,
		MarvinImage imgIn,
		MarvinImage imgOut,
		MarvinImageMask mask
	)
	{
		id = currentId++;
		plugin = plg;
		imageIn = imgIn;
		imageOut = imgOut;
		imageMask = mask;
		eType = PluginType.PLUGIN_IMAGE;
		thread = new Thread(this);
	}
	
	/**
	 * @return MarvinThread id.
	 */
	public long getId(){
		return id;
	}
	
	/**
	 * Starts the thread.
	 */
	public void start(){
		thread.start();
	}
	
	/**
	 * Set a thread listener.
	 * {@link TODO} 			- Support a list of listeners.
	 * @param a_listener		- listener object.
	 */
	public void addThreadListener(MarvinThreadListener l){
		listener = l;
	}
	
	/**
	 * Thread´s run method.
	 */
	public void run(){
		switch(eType){
			case PLUGIN_IMAGE:
				((MarvinImagePlugin)plugin).process(imageIn, imageOut, null, imageMask, false);
				if(listener != null){
					listener.threadFinished(new MarvinThreadEvent(id, plugin));
				}
				break;
		}
	}
}
