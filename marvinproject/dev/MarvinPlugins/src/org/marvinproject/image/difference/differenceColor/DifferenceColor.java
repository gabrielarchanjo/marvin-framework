/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.difference.differenceColor;

import java.awt.Color;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Find the difference between two images considering the pixel´s color.
 * @author Gabriel
 *
 */
public class DifferenceColor extends MarvinAbstractImagePlugin{
	
	private MarvinAttributes 	attributes;
	private MarvinImage 		imageB; 
	
	private int					colorRGB;
	private int					colorRange;
	
	public void load(){
		attributes = getAttributes();
		attributes.set("colorRange", 30);
		attributes.set("differenceColor", Color.green);
	}
	
	public MarvinAttributesPanel getAttributesPanel(){ return null; }
	
	public double diff(MarvinImage imageA, MarvinImage imageB, int colorRange){
		int l_redA,
		l_redB,
		l_greenA,
		l_greenB,
		l_blueA,
		l_blueB;
		
		int total=0;
		for(int y=0; y<imageA.getHeight(); y++){
			for(int x=0; x<imageA.getWidth(); x++){
				
				l_redA = imageA.getIntComponent0(x, y);
				l_greenA = imageA.getIntComponent1(x, y);
				l_blueA = imageA.getIntComponent2(x, y);
				
				l_redB = imageB.getIntComponent0(x, y);
				l_greenB = imageB.getIntComponent1(x, y);
				l_blueB = imageB.getIntComponent2(x, y);
				
				if
				(
					Math.abs(l_redA-l_redB)> colorRange ||
					Math.abs(l_greenA-l_greenB)> colorRange ||
					Math.abs(l_blueA-l_blueB)> colorRange
				)
				{
					if(a_imageOut != null){
						a_imageOut.setIntColor(x, y, colorRGB);
					}
					total++;
				}
				else{
					if(a_imageOut != null){
						a_imageOut.setIntColor(x, y, imageA.getIntColor(x,y));
					}
				}
			}
		}
		
		if(a_attributesOut != null){
			a_attributesOut.set("total", total);
		}
	}
}
