/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package marvin.plugin;

import java.util.List;

import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.util.MarvinAttributes;

public abstract class MarvinAbstractImagePlugin extends MarvinAbstractPlugin implements MarvinImagePlugin
{
	//private Marvin marvinApplication;
	private MarvinImagePanel 		imagePanel;
	
	private boolean valid;

	/**
	 * Associates the plug-in with an MarvinImagePanel
	 * @param imgPanel reference to a MarvinImagePanel object
	 */
	public void setImagePanel(MarvinImagePanel imgPanel){
		imagePanel = imgPanel;
	}
	
	/**
	 * @return a reference to the associated MarvinImagePanel. If no one MarvinImagePanel is associated with this plug-in,
	 * this method returns null.
	 */
	public MarvinImagePanel getImagePanel(){
		return imagePanel;
	}
	
	/**
	 * Executes the algorithm.
	 * @param imgIn				input image.
	 * @param imgOut			output image.
	 * @param attrOut			output attributes.
	 */
	public void process
	(
		MarvinImage imgIn, 
		MarvinImage imgOut, 
		MarvinImageMask mask
	){
		process(imgIn, imgOut, null, mask, false);
	}
	
	public void process
	(
		MarvinImage imgIn, 
		MarvinImage imgOut, 
		MarvinAttributes attrOut
	){
		process(imgIn, imgOut, attrOut, MarvinImageMask.NULL_MASK, false);
	}
	
	/**
	 * Executes the algorithm.
	 * @param imgIn				input image.
	 * @param imgOut			output image.
	 */
	public void process
	(
		MarvinImage imgIn, 
		MarvinImage imgOut
	){
		process(imgIn, imgOut, null, MarvinImageMask.NULL_MASK, false);
	}
	
	/**
	 * Interface for algorithms that use multiple images as input. This method has to be
	 * overridden by plug-ins that need that behavior.
	 */
	public void process
	(
		List<MarvinImage> imagesIn,
		MarvinImage imageOut
	){ /* An interface to be overridden */}
	
}