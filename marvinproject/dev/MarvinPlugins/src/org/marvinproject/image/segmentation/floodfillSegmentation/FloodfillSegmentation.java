package org.marvinproject.image.segmentation.floodfillSegmentation;

import java.awt.Color;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinBlob;
import marvin.image.MarvinBlobSegment;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.image.MarvinSegment;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;

import org.marvinproject.image.fill.boundaryFill.BoundaryFill;


public class FloodfillSegmentation extends MarvinAbstractImagePlugin{

private MarvinImagePlugin floodfill;
	
	public void load(){
		floodfill   = new BoundaryFill();
		floodfill.load();
		setAttribute("returnType", "MarvinSegment");
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
			String returnType = (String)getAttribute("returnType");
			MarvinImage fillBuffer = imageIn.clone();
			MarvinSegment[] segments = floodfillSegmentation(imageIn, fillBuffer);
			
			switch(returnType){
				case "MarvinSegment":
					attributesOut.set("segments", segments);
					break;
				case "MarvinBlobSegment":
					attributesOut.set("blobSegments", blobSegments(fillBuffer, segments));
					break;
			}
		}
	}
	
	private MarvinSegment[] floodfillSegmentation(MarvinImage image, MarvinImage fillBuffer){
		fillBuffer.clear(0xFF000000);
		
		int currentColor=1;
		for(int y=0; y<image.getHeight(); y++){
			for(int x=0; x<image.getWidth(); x++){
				
				int color = fillBuffer.getIntColor(x, y);
				
				if((color & 0x00FFFFFF) == 0 && image.getAlphaComponent(x, y) > 0){
					Color c = new Color(0xFF000000 | (currentColor++));
					floodfill.setAttribute("x", x);
					floodfill.setAttribute("y", y);
					floodfill.setAttribute("color", c);
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
					seg.width = (seg.x2-seg.x1)+1;
					
					// y and height;
					if(seg.y1 == -1 || y < seg.y1)	{		seg.y1 = y;		}
					if(seg.y2 == -1 || y > seg.y2)	{		seg.y2 = y;		}
					seg.height = (seg.y2-seg.y1)+1;
					
					seg.area++;
				}
			}
		}
		
		return segments;
	}
	
	private MarvinBlobSegment[] blobSegments(MarvinImage image, MarvinSegment[] segments){
		
		MarvinBlobSegment[] blobSegments = new MarvinBlobSegment[segments.length];
		
		int colorSegment;
		MarvinSegment seg;
		for(int i=0; i<segments.length; i++){
			seg = segments[i];
			colorSegment = 0xFF000000 + (i+1);
			
			blobSegments[i] = new MarvinBlobSegment(seg.x1, seg.y1);
			MarvinBlob tempBlob = new MarvinBlob(seg.width, seg.height);
			blobSegments[i].setBlob(tempBlob);
			
			for(int y=seg.y1; y<=seg.y2; y++){
				for(int x=seg.x1; x<=seg.x2; x++){
					if(image.getIntColor(x,y) == colorSegment){
						tempBlob.setValue(x-seg.x1, y-seg.y1, true);
					}
				}
			}
			
		}
		return blobSegments;
	}

	@Override
	public MarvinAttributesPanel getAttributesPanel() {
		return null;
	}
}
