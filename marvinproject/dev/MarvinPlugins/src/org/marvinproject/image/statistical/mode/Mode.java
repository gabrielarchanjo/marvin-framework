/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.statistical.mode;

import java.util.HashMap;
import java.util.Map;

import marvin.gui.MarvinAttributesPanel;
import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.performance.MarvinPerformanceMeter;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/** 
 * @author Fabio Andrijauskas
 */
public class Mode extends MarvinAbstractImagePlugin {

	private MarvinAttributesPanel	attributesPanel;
	MarvinAttributes 				attributes;
	MarvinPerformanceMeter 			performanceMeter;

	public void load() {
		attributes = getAttributes();
		attributes.set("size", 3);
		//attributes.set("shift", 0);
		//attributes.set("circlesDistance", 0);
		performanceMeter = new MarvinPerformanceMeter();
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
		performanceMeter.start("Moda Filter");


		int l_size = (Integer)attributes.get("size");
		int mapSize = l_size * l_size;
		int l_totalRed = 0;
		int l_totalGreen = 0;
		int l_totalBlue = 0;
		int qtd = 0;
		int tmpx = 0;
		int tmpy = 0;
		int width = a_imageIn.getWidth();
		int height = a_imageIn.getHeight();
		
		Map<Integer,Integer> mapModaR;
		Map<Integer,Integer> mapModaG;
		Map<Integer,Integer> mapModaB;
		
		performanceMeter.enableProgressBar("Moda Filter" ,a_imageIn.getWidth() *  a_imageIn.getHeight() );
		performanceMeter.startEvent("Moda Filter");
		
		boolean[][] l_arrMask = a_mask.getMaskArray();

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if(l_arrMask != null && !l_arrMask[x][y]){
					continue;
				}

				tmpx  = x - l_size;
				tmpy  = y - l_size;

				mapModaR = new HashMap<Integer, Integer>(mapSize);
				mapModaG = new HashMap<Integer, Integer>(mapSize);
				mapModaB = new HashMap<Integer, Integer>(mapSize);

				if(tmpx < 0)
					tmpx = 0;
				if(tmpy < 0)
					tmpy = 0;

				int finalX =  x + l_size;

				int finalY = y + l_size;

				if(finalX > width)
					finalX = width;

				if(finalY > height)
					finalY = height;

				for (int xm = tmpx; xm < finalX ; xm++) {
					for (int ym = tmpy; ym <  y + l_size ; ym++) {

						if(xm >= 0 && xm < width && ym >= 0 && ym < height  )
						{
							int rgb = a_imageIn.getIntColor(xm, ym);
							l_totalRed = (int)(rgb & 0x00FF0000) >>> 16;
							l_totalGreen = (int) ((rgb & 0x0000FF00) >>> 8);
							l_totalBlue = (int) (rgb & 0x000000FF);

							Integer l_val = mapModaR.get(l_totalRed);
							if(l_val == null)
								l_val = 0;
							
							mapModaR.put((Integer)l_totalRed, ++l_val);
						
							
							
							l_val = mapModaG.get(l_totalGreen);
							if(l_val == null)
								l_val = 0;
							
							mapModaG.put((Integer)l_totalGreen, ++l_val);
							
							
							
							
							l_val = mapModaB.get(l_totalBlue);
							if(l_val == null)
								l_val = 0;
							
							mapModaB.put((Integer)l_totalBlue, ++l_val);
							

						}
					}
				}
				
				Integer r1 = 0;
				for(Integer it : mapModaR.keySet())
				{
					if( mapModaR.get(it) >= r1)
					{
						r1 = it;
					}
				
				}
				
				Integer g1 = 0;
				for(Integer it : mapModaG.keySet())
				{
					if( mapModaG.get(it) >= g1)
					{
						g1 = it;
					}
				
				}
				
				Integer b1 = 0;
				for(Integer it : mapModaB.keySet())
				{
					if( mapModaB.get(it) >= b1)
					{
						b1 = it;
					}
				
				}
				
				a_imageOut.setIntColor(x, y, a_imageIn.getAlphaComponent(x, y), r1 , g1 , b1 );

				
			}
			performanceMeter.incProgressBar(height);
			performanceMeter.stepsFinished(height);
		}
		performanceMeter.finishEvent();
		performanceMeter.finish();
	}

	public MarvinAttributesPanel getAttributesPanel(){
		if(attributesPanel == null){
			attributesPanel = new MarvinAttributesPanel();
			attributesPanel.addLabel("lblWidth", "Size:");
			attributesPanel.addTextField("txtSize", "size", attributes);
			attributesPanel.newComponentRow();
		}
		return attributesPanel;
	}
}
