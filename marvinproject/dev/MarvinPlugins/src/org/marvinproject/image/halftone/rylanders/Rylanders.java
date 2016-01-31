/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.halftone.rylanders;


import marvin.gui.MarvinAttributesPanel;
import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;

/**
 * Halftone Rylanders implementation.
 * @author Gabriel Ambrósio Archanjo
 * @version 1.0 02/28/2008
 */
public class Rylanders extends MarvinAbstractImagePlugin
{
	private static final int DOT_AREA = 4;
	private static final int arrPattern[] = {	1,9,3,11,
												5,13,7,15,
												4,12,2,10,
												8,16,6,14
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
		double l_intensity;

		// Gray
		MarvinImagePlugin l_filter = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.color.grayScale.jar");
		l_filter.process(a_imageIn, a_imageIn, a_attributesOut, a_mask, a_previewMode);
		
		boolean[][] l_arrMask = a_mask.getMask();
		
		for (int x = 0; x < a_imageIn.getWidth(); x+=DOT_AREA) {
			for (int y = 0; y < a_imageIn.getHeight(); y+=DOT_AREA) {
				if(l_arrMask != null && !l_arrMask[x][y]){
					continue;
				}
				
				l_intensity = getSquareIntensity(x,y,a_imageIn);
				drawTone(x,y,a_imageIn, a_imageOut, l_intensity);
			}
		}
	}

	private void drawTone(int a_x, int a_y, MarvinImage a_imageIn, MarvinImage a_imageOut, double a_intensity){
		double l_factor = 1.0/15;
		int l_toneIntensity = (int)Math.floor(a_intensity/l_factor);
		int l_x;
		int l_y;

		for(int x=0; x<DOT_AREA*DOT_AREA; x++){
			l_x = x%DOT_AREA;
			l_y = x/DOT_AREA;
			
			if(a_x+l_x < a_imageIn.getWidth() && a_y+l_y < a_imageIn.getHeight()){
				if(l_toneIntensity >= arrPattern[x]){
					a_imageOut.setIntColor(a_x+l_x, a_y+l_y, 0,0,0);
				}
				else{
					a_imageOut.setIntColor(a_x+l_x, a_y+l_y, 255,255,255);
				}
			}
		}
	}

	private double getSquareIntensity(int a_x, int a_y, MarvinImage image){
		double l_totalValue=0;
		for(int y=0; y<DOT_AREA; y++){
			for(int x=0; x<DOT_AREA; x++)
			{
				if(a_x+x < image.getWidth() && a_y+y < image.getHeight()){
					l_totalValue+= 255-(image.getIntComponent0(a_x+x,a_y+y));
				}
			}
		}
		return (l_totalValue/(DOT_AREA*DOT_AREA*255));
	}
}