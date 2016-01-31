/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.halftone.circles;


import java.awt.Color;
import java.awt.Graphics;

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
 * Halftone using circles.
 * @author Gabriel Ambrósio Archanjo
 * @version 1.0 02/28/2008
 */
public class Circles extends MarvinAbstractImagePlugin
{
	private int circleWidth;
	private int shift;
	private int circlesDistance;

	MarvinPerformanceMeter performanceMeter;
	
	private MarvinAttributesPanel	attributesPanel;
	private MarvinAttributes 		attributes;

	public void load(){
		attributes = getAttributes();
		attributes.set("circleWidth", 6);
		attributes.set("shift", 0);
		attributes.set("circlesDistance", 0);
		performanceMeter = new MarvinPerformanceMeter();
	}

	public MarvinAttributesPanel getAttributesPanel(){
		if(attributesPanel == null){
			attributesPanel = new MarvinAttributesPanel();
			attributesPanel.addLabel("lblWidth", "Circle width:");
			attributesPanel.addTextField("txtCircleWidth", "circleWidth", attributes);
			attributesPanel.newComponentRow();
			attributesPanel.addLabel("lblShift", "Line Shift:");
			attributesPanel.addTextField("txtShift", "shift", attributes);
			attributesPanel.newComponentRow();
			attributesPanel.addLabel("lblDistance", "Circles distance:");
			attributesPanel.addTextField("lblCirclesDistance", "circlesDistance", attributes);
		}
		return attributesPanel;
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
		double l_intensity;

		circleWidth = (Integer)attributes.get("circleWidth");
		shift = (Integer)attributes.get("shift");
		circlesDistance = (Integer)attributes.get("circlesDistance");

		Graphics l_graphics = a_imageOut.getBufferedImage().getGraphics();

		// Gray
		MarvinImagePlugin l_filter = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.color.grayScale.jar");
		l_filter.process(a_imageIn, a_imageIn, a_attributesOut, a_mask, a_previewMode);
		
		performanceMeter.enableProgressBar("Halftone - Circles" , (a_imageIn.getHeight()/(circleWidth+circlesDistance))*(a_imageIn.getWidth()/(circleWidth+circlesDistance)));
		
		boolean[][] l_arrMask = a_mask.getMask();
		
		int l_dif=0;
		for (int y = 0; y < a_imageIn.getHeight(); y+=circleWidth+circlesDistance) {
			for (int x = 0+l_dif; x < a_imageIn.getWidth(); x+=circleWidth+circlesDistance) {
				if(l_arrMask != null && !l_arrMask[x][y]){
					continue;
				}
				l_intensity = getSquareIntensity(x,y,a_imageIn);
				l_intensity+=1.0/circleWidth;
				l_graphics.setColor(Color.white);
				l_graphics.fillRect(x,y,circleWidth+circlesDistance,circleWidth+circlesDistance);
				l_graphics.setColor(Color.black);
				l_graphics.fillArc((int)(x+(circleWidth-(l_intensity*circleWidth))/2), (int)(y+(circleWidth-(l_intensity*circleWidth))/2), (int)(l_intensity*(circleWidth)), (int)(l_intensity*(circleWidth)),1,360);
			}
			l_dif = (l_dif+shift)%circleWidth;
			performanceMeter.stepsFinished((a_imageIn.getWidth()/(circleWidth+circlesDistance)));
		}
		a_imageOut.updateColorArray();
		performanceMeter.finish();
	}

	private double getSquareIntensity(int a_x, int a_y, MarvinImage image){
		double l_totalValue=0;
		for(int y=0; y<circleWidth; y++){
			for(int x=0; x<circleWidth; x++)
			{
				if(a_x+x > 0 && a_x+x < image.getWidth() &&  a_y+y> 0 && a_y+y < image.getHeight()){
					l_totalValue+= 255-(image.getIntComponent0(a_x+x,a_y+y));
				}
			}
		}
		return (l_totalValue/(circleWidth*circleWidth*255));
	}
}