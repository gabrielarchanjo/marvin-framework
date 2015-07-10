package org.marvinproject.image.segmentation.floodfillSegmentation;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.image.MarvinSegment;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;


public class FloodfillSegmentation extends MarvinAbstractImagePlugin{

	private MarvinImagePlugin floodfill;
	
	public void load(){
		floodfill   = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.fill.boundaryFill");
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
		if(attributesOut != null){
			attributesOut.set("segments", floodfillSegmentation(imageIn));
		}
	}
	
	private MarvinSegment[] floodfillSegmentation(MarvinImage image){
		MarvinImage fillBuffer = image.clone();
		fillBuffer.clearImage(0xFF000000);
		
		int currentColor=1;
		for(int y=0; y<image.getHeight(); y++){
			for(int x=0; x<image.getWidth(); x++){
				
				int color = fillBuffer.getIntColor(x, y);
				
				if((color & 0x00FFFFFF) == 0){
					floodfill.setAttribute("x", x);
					floodfill.setAttribute("y", y);
					floodfill.setAttribute("color", 0xFF000000 | (currentColor++));
					floodfill.process(image, fillBuffer);
					
				}
			}
		}
		
		MarvinSegment[] segments = new MarvinSegment[currentColor-1];
		MarvinSegment seg;
		for(int y=0; y<fillBuffer.getHeight(); y++){
			for(int x=0; x<fillBuffer.getWidth(); x++){
				int color = (fillBuffer.getIntColor(x, y) & 0x00FFFFFF);
				
				if(color != 0x00FFFFFF && color > 0){
					
					seg = segments[color-1];
					
					if(seg == null){
						seg = new MarvinSegment();
						segments[color-1] = seg;
					}
					
					// x and width
					if(seg.x1 == -1 || x < seg.x1)	{		seg.x1 = x;		}
					if(seg.x2 == -1 || x > seg.x2)	{		seg.x2 = x;		}
					seg.width = seg.x2-seg.x1;
					
					// y and height;
					if(seg.y1 == -1 || y < seg.y1)	{		seg.y1 = y;		}
					if(seg.y2 == -1 || y > seg.y2)	{		seg.y2 = y;		}
					seg.height = seg.y2-seg.y1;
					
					seg.mass++;
				}
			}
		}
		
		return segments;
	}

	@Override
	public MarvinAttributesPanel getAttributesPanel() {
		return null;
	}
}
