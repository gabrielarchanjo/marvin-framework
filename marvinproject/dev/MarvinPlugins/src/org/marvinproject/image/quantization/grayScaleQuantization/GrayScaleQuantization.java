/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.quantization.grayScaleQuantization;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;

public class GrayScaleQuantization extends MarvinAbstractImagePlugin{

	private MarvinAttributesPanel	attributesPanel;
	private MarvinImagePlugin 		gray;
	
	@Override
	public void load() {
		gray = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.color.grayScale.jar");
		
		setAttribute("shades", 10);
	}
	
	@Override
	public MarvinAttributesPanel getAttributesPanel() {
		if(attributesPanel == null){
			attributesPanel = new MarvinAttributesPanel();
			attributesPanel.addLabel("lblShades", "Shades:");
			attributesPanel.addTextField("txtShades", "shades", getAttributes());
		}
		return attributesPanel;
	}

	@Override
	public void process
	(
		MarvinImage imageIn,
		MarvinImage imageOut,
		MarvinAttributes attrOut, 
		MarvinImageMask mask,
		boolean preview)
	{
		
		int colors = (Integer)getAttribute("shades");
		int range = 255/colors;
		gray.process(imageIn.clone(), imageIn);
		int c;
		int c2;
		for(int y=0; y<imageIn.getHeight(); y++){
			for(int x=0; x<imageIn.getWidth(); x++){
				c = imageIn.getIntComponent0(x, y);
				c2 = (c/range)*range;
				
				imageOut.setIntColor(x, y, 255, c2,c2,c2);
			}
		}
	}
}
