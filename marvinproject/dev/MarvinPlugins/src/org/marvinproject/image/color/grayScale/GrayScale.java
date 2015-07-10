/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.color.grayScale;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.performance.MarvinPerformanceMeter;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Represents an image in gray scale.
 * @author Fábio Andrijauskas
 * @version 1.0 02/28/2008
 */
public class GrayScale extends MarvinAbstractImagePlugin
{
	MarvinPerformanceMeter performanceMeter;
	MarvinAttributes attributes;
	
	public void load(){
		performanceMeter = new MarvinPerformanceMeter();
		attributes = getAttributes();
	}
	
	public MarvinAttributesPanel getAttributesPanel(){
		return null;
	}

	public void process
	(
		MarvinImage imageIn, 
		MarvinImage imageOut,
		MarvinAttributes attributesOut,
		MarvinImageMask mask, 
		boolean previewMode
	)
	{
		// Mask
		boolean[][] l_arrMask = mask.getMaskArray();
		
		performanceMeter.start("Gray");
		performanceMeter.startEvent("Gray");
		int r,g,b,finalColor;
		for (int x = 0; x < imageIn.getWidth(); x++) {
			for (int y = 0; y < imageIn.getHeight(); y++) {
				if(l_arrMask != null && !l_arrMask[x][y]){
					continue;
				}
				//Red - 30% / Green - 59% / Blue - 11%
				r = imageIn.getIntComponent0(x, y);
				g = imageIn.getIntComponent1(x, y);
				b = imageIn.getIntComponent2(x, y);
				finalColor = (int)((r*0.3)+(g*0.59)+(b*0.11));
				imageOut.setIntColor(x,y,imageIn.getAlphaComponent(x, y), finalColor,finalColor,finalColor);
								
			}
			performanceMeter.stepsFinished(imageIn.getHeight());
		}
		performanceMeter.finishEvent();
		performanceMeter.finish();
	}
}

