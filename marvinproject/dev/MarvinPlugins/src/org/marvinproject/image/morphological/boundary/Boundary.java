/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.morphological.boundary;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;

import org.marvinproject.image.morphological.erosion.Erosion;

public class Boundary extends MarvinAbstractImagePlugin{

	private MarvinImagePlugin	pluginErosion;
	private boolean[][]			matrix;
	
	
	@Override
	public void load() {
		matrix = new boolean[][]
		{
			{true,true,true},
			{true,true,true},
			{true,true,true},
		};
		
		pluginErosion = new Erosion();
		pluginErosion.load();
		pluginErosion.setAttribute("matrix", matrix);
	}
	
	public void process
	(
		MarvinImage imgIn, 
		MarvinImage imgOut,
		MarvinAttributes attrOut, 
		MarvinImageMask mask, 
		boolean previewMode
	)
	{	
		if(imgIn.getColorModel() == MarvinImage.COLOR_MODEL_BINARY){
			pluginErosion.process(imgIn, imgOut, attrOut, mask, previewMode);
			diff(imgIn, imgOut);
		}
	}
	
	private void diff(MarvinImage imgIn, MarvinImage imgOut){
		for(int y=0; y<imgIn.getHeight(); y++){
			for(int x=0; x<imgIn.getWidth(); x++){
				if(imgIn.getBinaryColor(x, y) != imgOut.getBinaryColor(x, y)){
					imgOut.setBinaryColor(x, y, true);
				}
				else{
					imgOut.setBinaryColor(x, y, false);
				}
			}
		}
	}
	
	public MarvinAttributesPanel getAttributesPanel(){
		return null;
	}
	
}
