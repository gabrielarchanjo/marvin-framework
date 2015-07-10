/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.render.grayGradient;

import marvin.gui.MarvinAttributesPanel;
import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Render white to black degrade. The Marvin framework do not supports 
 * color choosing component yet, but when it's supported, it will be 
 * used in this filter for degrade color selection.
 * 
 * @version 1.0 02/28/2008
 * @author Gabriel Ambrósio Archanjo
 */

/**
 * @author Gabriel Ambrósio Archanjo
 */
public class GrayGradient extends MarvinAbstractImagePlugin
{
	
	public void load(){}

	public MarvinAttributesPanel getAttributesPanel(){return null;}

	public void process
	(
		MarvinImage a_imageIn, 
		MarvinImage a_imageOut,
		MarvinAttributes a_attributesOut,
		MarvinImageMask a_mask, 
		boolean a_previewMode
	)
	{
		//Start with white color.
		int r = 255,g = 255,b = 255;
		int aux=0;
		double factor;
		
		if(a_imageIn.getWidth() > 255){
			factor = a_imageIn.getWidth()/255.0;
		}
		else{
			factor = 255/a_imageIn.getWidth();
		}

		for (int x = 0; x < a_imageIn.getWidth(); x++) {
			for (int y = 0; y < a_imageIn.getHeight(); y++) {
				try{
					a_imageOut.setIntColor(x,y,a_imageIn.getAlphaComponent(x,y), r,g,b);
				}
				catch(Exception e){System.out.println("Error"); e.printStackTrace();}				
			}
		
			if(a_imageIn.getWidth() > 255){
				aux++;
				
				if(aux > factor){
					r--;
					g--;
					b--;
					aux=0;
				}
			}
			else if(255 > a_imageIn.getWidth()){
				aux+=factor;
				r=255-aux;
				g=255-aux;
				b=255-aux;
			}
			
			if(r<0){r=0;}
			if(g<0){g=0;}
			if(b<0){b=0;}
		}
	}
}