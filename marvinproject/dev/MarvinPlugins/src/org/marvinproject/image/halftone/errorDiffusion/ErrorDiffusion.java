/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.halftone.errorDiffusion;

import marvin.gui.MarvinAttributesPanel;
import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.performance.MarvinPerformanceMeter;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;

/**
 * Halftone Error Diffusion implementation.
 * @author Gabriel Ambrósio Archanjo
 * @version 1.0 02/28/2008

 * @author danilo
 *
 */
public class ErrorDiffusion extends MarvinAbstractImagePlugin
{
	int threshold;
	private MarvinPerformanceMeter performanceMeter;

	public void load(){
		performanceMeter = new MarvinPerformanceMeter();
		threshold = 128;
	}

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
		int color;
		double dif;

		// Gray
		MarvinImagePlugin l_filter = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.color.grayScale.jar");
		l_filter.process(a_imageIn, a_imageOut, a_attributesOut, a_mask, a_previewMode);
		
		performanceMeter.start("Halftone - Error Diffusion");
		performanceMeter.enableProgressBar("Halftone - Error Diffusion", a_imageOut.getHeight()*a_imageOut.getWidth());
		performanceMeter.startEvent("Error Diffusion");

		boolean[][] l_arrMask = a_mask.getMask();
		
		for (int y = 0; y < a_imageOut.getHeight(); y++) {
			for (int x = 0; x < a_imageOut.getWidth(); x++) {
				if(l_arrMask != null && !l_arrMask[x][y]){
					continue;
				}
				
				color = a_imageOut.getIntComponent0(x, y);
				if(color > threshold){
					a_imageOut.setIntColor(x,y,a_imageIn.getAlphaComponent(x,y), 255,255,255);
					dif = -(255-color);
				}
				else{
					a_imageOut.setIntColor(x,y,a_imageIn.getAlphaComponent(x,y), 0,0,0);
					dif = color;
				}

				// Pixel Right
				if(x+1 < a_imageOut.getWidth()){
					color = a_imageOut.getIntComponent0(x+1,y);
					color+=(int)(0.4375*dif);
					color = getValidGray(color); 
					a_imageOut.setIntColor(x+1,y,a_imageIn.getAlphaComponent(x+1,y), color,color,color);

					// Pixel Right Down
					if(y+1 < a_imageOut.getHeight()){
						color = a_imageOut.getIntComponent0(x+1,y+1);
						color+=(int)(0.0625*dif);
						color = getValidGray(color); 
						a_imageOut.setIntColor(x+1,y+1,a_imageIn.getAlphaComponent(x+1,y+1), color,color,color);
					}
				}

				// Pixel Down
				if(y+1 < a_imageOut.getHeight()){
					color = a_imageOut.getIntComponent0(x,y+1);
					color+=(int)(0.3125*dif);
					color = getValidGray(color); 
					a_imageOut.setIntColor(x,y+1,a_imageIn.getAlphaComponent(x,y+1), color,color,color);

					// Pixel Down Left
					if(x-1 >= 0){
						color = a_imageOut.getIntComponent0(x-1,y+1);
						color+=(int)(0.1875*dif);
						color = getValidGray(color); 
						a_imageOut.setIntColor(x-1,y+1,a_imageIn.getAlphaComponent(x-1,y+1), color,color,color);
					}
				}
			}
			performanceMeter.stepsFinished(a_imageOut.getWidth());
			performanceMeter.incProgressBar(a_imageOut.getWidth());
		}
		performanceMeter.finishEvent();
		performanceMeter.finish();
	}

	private int getValidGray(int a_value){
		if(a_value < 0) return 0;
		if(a_value > 255) return 255;
		return a_value;
	}
}