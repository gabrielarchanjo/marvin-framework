/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.artistic.television;

import marvin.gui.MarvinAttributesPanel;
import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Represent an image using red, green and blue lines.
 * @author Gabriel Ambrosio Archanjo
 * @version 1.0 02/28/2008
 */
public class Television extends MarvinAbstractImagePlugin
{
	public void load(){}

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
		boolean[][] l_arrMask = mask.getMask();
		
		int r,g,b;
		for (int x = 0; x < imageIn.getWidth(); x++) {			
			for (int y = 0; y < imageIn.getHeight(); y+=3) {
				if(l_arrMask != null && !l_arrMask[x][y]){
					continue;
				}
				
				r=0;
				g=0;
				b=0;
					
				for(int w=0; w<3; w++){
					if(y+w < imageIn.getHeight() ){
						r += (imageIn.getIntComponent0(x, y+w))/2;
						g += (imageIn.getIntComponent1(x, y+w))/2;
						b += (imageIn.getIntComponent2(x, y+w))/2;						
					}
				}
				r = getValidInterval(r);
				g = getValidInterval(g);
				b = getValidInterval(b);
						
				for(int w=0; w<3; w++){
					if(y+w < imageOut.getHeight()){
						if(w == 0){
							imageOut.setIntColor(x,y+w,imageIn.getAlphaComponent(x, y), r,0,0);
						}
						else if(w ==1){
							imageOut.setIntColor(x,y+w,imageIn.getAlphaComponent(x, y), 0,g,0);
						}
						else if(w==2){
							imageOut.setIntColor(x,y+w,imageIn.getAlphaComponent(x, y), 0,0,b);
						}
					}
				}				
			}
		}
	}

	public int getValidInterval(int value){
		if(value < 0) return 0;
		if(value > 255) return 255;
		return value;
	}
}