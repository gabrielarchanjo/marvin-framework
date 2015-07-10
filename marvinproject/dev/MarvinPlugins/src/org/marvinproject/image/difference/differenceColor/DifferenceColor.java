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
	private MarvinImage 		comparisonImage; 
	
	private int					colorRGB;
	private int					colorRange;
	
	public void load(){
		attributes = getAttributes();
		attributes.set("colorRange", 30);
		attributes.set("differenceColor", Color.green);
	}
	
	public MarvinAttributesPanel getAttributesPanel(){ return null; }
	
	public void process(MarvinImage a_imageIn, MarvinImage a_imageOut, MarvinAttributes a_attributesOut, MarvinImageMask a_mask, boolean a_previewMode){
		int l_redA,
		l_redB,
		l_greenA,
		l_greenB,
		l_blueA,
		l_blueB;
		
		comparisonImage = (MarvinImage)attributes.get("comparisonImage");
		colorRange = (Integer)attributes.get("colorRange");
		colorRGB = ((Color)attributes.get("differenceColor")).getRGB();
		
		boolean[][] l_arrMask = a_mask.getMaskArray();
		int total=0;
		for(int y=0; y<a_imageIn.getHeight(); y++){
			for(int x=0; x<a_imageIn.getWidth(); x++){
				if(l_arrMask != null && !l_arrMask[x][y]){
					continue;
				}
				
				l_redA = a_imageIn.getIntComponent0(x, y);
				l_greenA = a_imageIn.getIntComponent1(x, y);
				l_blueA = a_imageIn.getIntComponent2(x, y);
				
				l_redB = comparisonImage.getIntComponent0(x, y);
				l_greenB = comparisonImage.getIntComponent1(x, y);
				l_blueB = comparisonImage.getIntComponent2(x, y);
				
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
						a_imageOut.setIntColor(x, y, a_imageIn.getIntColor(x,y));
					}
				}
			}
		}
		
		if(a_attributesOut != null){
			a_attributesOut.set("total", total);
		}
	}
}
