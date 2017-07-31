/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.test.determineGround;

import java.awt.Color;
import java.awt.Point;
import java.util.LinkedList;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;

import org.marvinproject.image.color.brightnessAndContrast.BrightnessAndContrast;
import org.marvinproject.image.edge.edgeDetector.EdgeDetector;

public class DetermineGround extends MarvinAbstractImagePlugin{
	
	private MarvinImagePlugin 	l_pluginEdgeDetector,
								l_pluginBC;
	
	public void load(){
		l_pluginEdgeDetector = new EdgeDetector();
		l_pluginEdgeDetector.load();
		l_pluginBC = new BrightnessAndContrast();
		l_pluginBC.load();
		l_pluginBC.setAttribute("contrast", 255);
	}
	
	public MarvinAttributesPanel getAttributesPanel(){ return null; }
    
    
    public void process
    (
    	MarvinImage a_imageIn, 
    	MarvinImage a_imageOut, 
    	MarvinAttributes a_attributesOut, 
    	MarvinImageMask a_mask,
    	boolean a_previewMode
    )
    {
    	MarvinImage l_originalImage = a_imageIn.clone();
    	l_pluginEdgeDetector.process(a_imageIn, a_imageOut, a_attributesOut, a_mask, a_previewMode);
    	l_pluginBC.process(a_imageOut, a_imageOut, a_attributesOut, a_mask, a_previewMode);
    	
    	// remove black border
    	a_imageOut.drawRect(0, 0,a_imageOut.getWidth(), a_imageOut.getHeight(), Color.white);
    	
    	// Copy a_imageOut content to a_imageIn
    	a_imageOut.copyColorArray(a_imageOut, a_imageIn);
    	
    	max(a_imageIn, a_imageOut);
    	
    	//long time = System.currentTimeMillis();
    	floodFill(a_imageOut, 10,400,0xFFFFFFFF, 0xFF00FF00);
    	//System.out.println("time:"+(System.currentTimeMillis()-time));
    	
    	blend(a_imageOut, l_originalImage);
    	a_imageOut.copyColorArray(l_originalImage, a_imageOut);
	}
    
    private void blend(MarvinImage a_imageIn, MarvinImage a_imageOut){
    	for(int l_y=0; l_y<a_imageIn.getHeight(); l_y++){
    		for(int l_x=0; l_x<a_imageIn.getWidth(); l_x++){
    			if(a_imageIn.getIntColor(l_x,l_y) == 0xFF00FF00){
    				a_imageOut.setIntColor(l_x, l_y, 0xFF00FF00);
    			}
    		}
    	}
    }
    
    private void max(MarvinImage a_imageIn, MarvinImage a_imageOut){
    	
    	for(int l_y=0; l_y<a_imageIn.getHeight(); l_y++){
    		for(int l_x=0; l_x<a_imageIn.getWidth(); l_x++){
    			if(a_imageIn.getIntColor(l_x,l_y) != 0xFFFFFFFF){
    				paintPixel(a_imageOut, l_x-1,l_y);
    				paintPixel(a_imageOut, l_x-2,l_y);
    				paintPixel(a_imageOut, l_x-3,l_y);
    				paintPixel(a_imageOut, l_x-4,l_y);
    				paintPixel(a_imageOut, l_x-5,l_y);
    				paintPixel(a_imageOut, l_x+1,l_y);
    				paintPixel(a_imageOut, l_x+2,l_y);
    				paintPixel(a_imageOut, l_x+3,l_y);
    				paintPixel(a_imageOut, l_x+4,l_y);
    				paintPixel(a_imageOut, l_x+5,l_y);
    				/*
    				paintPixel(a_imageOut, l_x,l_y-1);
    				paintPixel(a_imageOut, l_x,l_y-2);
    				paintPixel(a_imageOut, l_x,l_y-3);
    				paintPixel(a_imageOut, l_x,l_y-4);
    				paintPixel(a_imageOut, l_x,l_y+1);
    				paintPixel(a_imageOut, l_x,l_y+2);
    				paintPixel(a_imageOut, l_x,l_y+3);
    				paintPixel(a_imageOut, l_x,l_y+4);
    				*/
    			}
    		}
    	}
    }
    
    private void paintPixel(MarvinImage a_image, int a_x, int a_y){
    	if(a_x > 0 && a_x < a_image.getWidth() && a_y > 0 && a_y < a_image.getHeight()){
    		a_image.setIntColor(a_x, a_y, 0,0,0);
    	}
    }
    
    
    /**
     * Flood fill - not recursively
     * @param a_image
     * @param a_x
     * @param a_y
     * @param a_regionRGB
     * @param a_regionNewRGB
     */
    private void floodFill(MarvinImage a_image, int a_x, int a_y, int a_regionTargetRGB, int a_regionNewRGB){
    	LinkedList<Point> l_list = new LinkedList<Point>();
    	Point 	l_point,
    			l_pointW,
    			l_pointE;
    	
    	
    	if(a_image.getIntColor(a_x, a_y) != a_regionTargetRGB){
    		return;
    	}
    	
    	l_list.add(new Point(a_x, a_y));
    	
    	//for(int l_i=0; l_i<l_list.size(); l_i++){
    	while(l_list.size() > 0){
    		l_point = l_list.poll();
    		l_pointW = new Point(l_point.x, l_point.y);
    		l_pointE = new Point(l_point.x, l_point.y);
    		
    		// west
    		while(true){
    			if(l_pointW.x-1 > 0 && a_image.getIntColor(l_pointW.x-1, l_pointW.y) == a_regionTargetRGB){
    				l_pointW.x--;
    			}
    			else{
    				break;
    			}
    		 }
    		
    		// east
    		while(true){
    			if(l_pointE.x+1 < a_image.getWidth() && a_image.getIntColor(l_pointE.x+1, l_pointE.y) == a_regionTargetRGB){
    				l_pointE.x++;
    			}
    			else{
    				break;
    			}
    		 }
    		
    		if(l_pointE.x == l_pointW.x && l_pointE.y == l_pointW.y){
    			continue;
    		}
    		
    		// set color of pixels between pointW and pointE
    		for(int l_px=l_pointW.x; l_px<=l_pointE.x; l_px++){
    			a_image.setIntColor(l_px, l_point.y, a_regionNewRGB);
    			
    			if(l_point.y-1 > 0 && a_image.getIntColor(l_px, l_point.y-1) == a_regionTargetRGB){
    				l_list.add(new Point(l_px, l_point.y-1));
    			}
    			if(l_point.y+1 < a_image.getHeight() && a_image.getIntColor(l_px, l_point.y+1) == a_regionTargetRGB){
    				l_list.add(new Point(l_px, l_point.y+1));
    			}
    		}
    	}    	
    }
    
    private void foo(MarvinImage a_image, int a_x, int a_y, int a_regionRGB, int a_regionNewRGB){
    	a_image.setIntColor(a_x, a_y, a_regionNewRGB);
    	
    	if(a_x-1 > 0 && a_image.getIntColor(a_x-1, a_y) == a_regionRGB){
    		foo(a_image, a_x-1, a_y, a_regionRGB, a_regionNewRGB);
    	}
    	if(a_x+1 < a_image.getWidth() && a_image.getIntColor(a_x+1, a_y) == a_regionRGB){
    		foo(a_image, a_x+1, a_y, a_regionRGB, a_regionNewRGB);
    	}
    	
    	if(a_y-1 > 0 && a_image.getIntColor(a_x, a_y-1) == a_regionRGB){
    		foo(a_image, a_x, a_y-1, a_regionRGB, a_regionNewRGB);
    	}
    	if(a_y+1 < a_image.getHeight() && a_image.getIntColor(a_x, a_y+1) == a_regionRGB){
    		foo(a_image, a_x, a_y+1, a_regionRGB, a_regionNewRGB);
    	}
	}    
}