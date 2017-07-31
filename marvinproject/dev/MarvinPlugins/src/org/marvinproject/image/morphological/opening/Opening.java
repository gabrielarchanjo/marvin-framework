/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.morphological.opening;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;

import org.marvinproject.image.morphological.dilation.Dilation;
import org.marvinproject.image.morphological.erosion.Erosion;

public class Opening extends MarvinAbstractImagePlugin{

	private boolean 			matrix[][];
	
	private MarvinImagePlugin 	pluginDilation,
								pluginErosion;
								
	
	@Override
	public void load() {
		setAttribute("matrix", matrix);
		
		pluginDilation = new Dilation();
		pluginDilation.load();
		pluginErosion = new Erosion();
		pluginErosion.load();
		
	}

	@Override
	public void process
	(
		MarvinImage imgIn, 
		MarvinImage imgOut,
		MarvinAttributes attrOut, 
		MarvinImageMask mask, 
		boolean previewMode
	)
	{	
		matrix = (boolean[][])getAttribute("matrix");
		
		if(imgIn.getColorModel() == MarvinImage.COLOR_MODEL_BINARY && matrix != null){
			
			pluginErosion.setAttribute("matrix", (boolean[][])getAttribute("matrix"));
			pluginErosion.process(imgIn, imgOut, attrOut, mask, previewMode);
			
			MarvinImage.copyColorArray(imgOut, imgIn);
			
			pluginDilation.setAttribute("matrix", (boolean[][])getAttribute("matrix"));
			pluginDilation.process(imgIn, imgOut, attrOut, mask, previewMode);
		}
	}

	@Override
	public MarvinAttributesPanel getAttributesPanel(){
		return null;
	}

}
