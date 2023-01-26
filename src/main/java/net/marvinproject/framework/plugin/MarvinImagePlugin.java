/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package net.marvinproject.framework.plugin;

import java.util.List;

import net.marvinproject.framework.gui.MarvinAttributesPanel;
import net.marvinproject.framework.gui.MarvinImagePanel;
import net.marvinproject.framework.image.MarvinImage;
import net.marvinproject.framework.image.MarvinImageMask;
import net.marvinproject.framework.util.MarvinAttributes;

public interface MarvinImagePlugin extends MarvinPlugin
{	
	/**
	 * Shows a graphical interface 
	 */
	public MarvinAttributesPanel getAttributesPanel();
	
	/**
	 * Executes the algorithm.
	 * @param imgIn				input image.
	 * @param imgOut			output image.
	 * @param attrOut			output attributes.
	 * @param mask				mask containing what pixels should be considered.
	 * @param previewMode		it is or isn't on preview mode.
	 */
	public void process
	(
		MarvinImage imgIn, 
		MarvinImage imgOut, 
		MarvinAttributes attrOut, 
		MarvinImageMask mask, 
		boolean previewMode
	);
	
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
	);
	
	public void process
	(
		MarvinImage imgIn, 
		MarvinImage imgOut, 
		MarvinAttributes attrOut
	);
	
	/**
	 * Executes the algorithm.
	 * @param imgIn				input image.
	 * @param imgOut			output image.
	 */
	public void process
	(
		MarvinImage imgIn, 
		MarvinImage imgOut
	);
	
	/**
	 * Interface for algorithms that use multiple images as input.
	 */
	public void process
	(
		List<MarvinImage> imagesIn,
		MarvinImage imageOut
	);
	
	
	/**
	 * Associates the plug-in with an MarvinImagePanel
	 * @param imgPanel	reference to a MarvinImagePanel object
	 */
	public void setImagePanel(MarvinImagePanel imgPanel);
	
	/**
	 * @return a reference to the associated MarvinImagePanel. If no one MarvinImagePanel is associated with this plug-in,
	 * this method returns null.
	 */
	public MarvinImagePanel getImagePanel();
}