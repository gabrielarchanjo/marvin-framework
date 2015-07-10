/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.color.emboss;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Invert the pixels color to create an emboss effect.
 * @author Chris Mack
 * @version 1.0 12/07/2011
 */
public class Emboss extends MarvinAbstractImagePlugin
{	
	public void load(){}

	public MarvinAttributesPanel getAttributesPanel(){
		return null;
	}
	
	public void process
	(
		MarvinImage a_imageIn, 
		MarvinImage a_imageOut,
		MarvinAttributes a_attributesOut,
		MarvinImageMask a_mask, 
		boolean a_previewMode
	)
	{
		boolean[][] l_arrMask = a_mask.getMaskArray();
		
		for (int x = 0; x < a_imageIn.getWidth(); x++) {
			for (int y = 0; y < a_imageIn.getHeight(); y++) {
				if(l_arrMask != null && !l_arrMask[x][y]){
					a_imageOut.setIntColor(x, y, 255, a_imageIn.getIntColor(x, y));
					continue;
				}
				             
				int rDiff=0; 
				int gDiff=0;
				int bDiff=0;
				
	             if (y > 0 && x > 0){
     
		             // Red component difference between the current and the upperleft pixels
	            	 rDiff = a_imageIn.getIntComponent0(x, y) - a_imageIn.getIntComponent0(x-1, y-1);
		             
		             // Green component difference between the current and the upperleft pixels
	            	 gDiff = a_imageIn.getIntComponent1(x, y) - a_imageIn.getIntComponent1(x-1, y-1);
		             
		             // Blue component difference between the current and the upperleft pixels
	            	 bDiff = a_imageIn.getIntComponent2(x, y) - a_imageIn.getIntComponent2(x-1, y-1);
		             
	             }
	             else{
	            	 rDiff = 0;
	            	 gDiff = 0;
	            	 bDiff = 0;
	             }
		    
		         int diff = rDiff;
		         if (Math.abs (gDiff) > Math.abs (diff))
		              diff = gDiff;
		         if (Math.abs (bDiff) > Math.abs (diff))
		              diff = bDiff;
	
		         int grayLevel = Math.max (Math.min (128 + diff, 255),0);
	
		        a_imageOut.setIntColor(x, y, 255, grayLevel, grayLevel, grayLevel);
			}
		}
	}
}