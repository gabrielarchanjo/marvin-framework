/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.halftone.dithering;


import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;

import org.marvinproject.image.color.grayScale.GrayScale;

/**
 * Halftone dithering implementation.
 * @author Gabriel Ambrósio Archanjo
 * @version 1.0 02/28/2008
 */
public class Dithering extends MarvinAbstractImagePlugin
{
	private static final int DOT_AREA = 5;
	private static final int arrDither[] = {	167,200,230,216,181,
															94,72,193,242,232,
															36,52,222,167,200,
															181,126,210,94,72,
															232,153,111,36,52
														};

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
		// Gray
		MarvinImagePlugin l_filter = new GrayScale();
		l_filter.load();
		l_filter.process(a_imageIn, a_imageIn, a_attributesOut, a_mask, a_previewMode);
		
		boolean[][] l_arrMask = a_mask.getMask();
		
		for (int x = 0; x < a_imageIn.getWidth(); x+=DOT_AREA) {
			for (int y = 0; y < a_imageIn.getHeight(); y+=DOT_AREA) {
				if(l_arrMask != null && !l_arrMask[x][y]){
					continue;
				}
				drawTone(x,y,a_imageIn, a_imageOut);
			}
		}
	}

	private void drawTone(int a_x, int a_y, MarvinImage a_imageIn, MarvinImage a_imageOut){
		int l_grayIntensity;
		int l_x;
		int l_y;

		for(int x=0; x<DOT_AREA*DOT_AREA; x++){
			l_x = x%DOT_AREA;
			l_y = x/DOT_AREA;
			
			if(a_x+l_x < a_imageIn.getWidth() && a_y+l_y < a_imageIn.getHeight()){

				l_grayIntensity = 255-(a_imageIn.getIntComponent0(a_x+l_x, a_y+l_y));

				if(l_grayIntensity > arrDither[x]){
					a_imageOut.setIntColor(a_x+l_x, a_y+l_y, a_imageIn.getAlphaComponent(a_x+l_x, a_y+l_y), 0,0,0);
				}
				else{
					a_imageOut.setIntColor(a_x+l_x, a_y+l_y, a_imageIn.getAlphaComponent(a_x+l_x, a_y+l_y), 255,255,255);
				}
			}
		}
	}
}