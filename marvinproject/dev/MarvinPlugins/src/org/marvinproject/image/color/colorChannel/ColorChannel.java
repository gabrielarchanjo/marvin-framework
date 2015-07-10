package org.marvinproject.image.color.colorChannel;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

public class ColorChannel extends MarvinAbstractImagePlugin{

	@Override
	public void load() {
		setAttribute("red", 0);
		setAttribute("green", 0);
		setAttribute("blue", 0);
	}
	
	@Override
	public void process
	(
		MarvinImage imageIn,
		MarvinImage imageOut,
		MarvinAttributes attrOut,
		MarvinImageMask mask,
		boolean preview
	) {
		
		int vr = (Integer)getAttribute("red");
		int vg = (Integer)getAttribute("green");
		int vb = (Integer)getAttribute("blue");
		
		double mr = 1+Math.abs((vr/100.0)*2.5);
		double mg = 1+Math.abs((vg/100.0)*2.5);
		double mb = 1+Math.abs((vb/100.0)*2.5);
		
		mr = (vr > 0? mr : 1.0/mr);
		mg = (vg > 0? mg : 1.0/mg);
		mb = (vb > 0? mb : 1.0/mb);
		
		int red,green,blue;
		for(int y=0; y<imageIn.getHeight(); y++){
			for(int x=0; x<imageIn.getWidth(); x++){
				red = imageIn.getIntComponent0(x, y);
				green = imageIn.getIntComponent1(x, y);
				blue = imageIn.getIntComponent2(x, y);
				
				red 	= (int)Math.min(red * mr, 255);
				green 	= (int)Math.min(green * mg, 255);
				blue	= (int)Math.min(blue * mb, 255);
				
				imageOut.setIntColor(x, y, 255, red, green, blue);
			}
		}
	}

	@Override
	public MarvinAttributesPanel getAttributesPanel() {
		return null;
	}
	
}
