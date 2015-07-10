/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.difference.differenceGray;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinErrorHandler;
import marvin.util.MarvinFileChooser;

/**
 * Absolute difference between two images considering the gray scale.
 * @author Danilo Rosetto Muñoz
 * @version 1.0 03/28/2008
 */

public class DifferenceGray extends MarvinAbstractImagePlugin{

	public void load(){}

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
		//Selects the other image to apply th difference
		String file = null;
		BufferedImage buffImage2=null;
		
		try {
			//Open the file browser dialog
			file = MarvinFileChooser.select(null, true, MarvinFileChooser.OPEN_DIALOG);
		} catch (Exception e) {
			MarvinErrorHandler.handleDialog(MarvinErrorHandler.TYPE.ERROR_FILE_CHOOSE, e);
			return;
		}
		
		if(file == null) return;

		//Loads the image to the memory and creates an MarvinImage
		try{
			buffImage2 =  ImageIO.read(new File(file));
		}catch (IOException ioe) {
			MarvinErrorHandler.handleDialog(MarvinErrorHandler.TYPE.ERROR_FILE_OPEN, ioe);
			return;
		}

		MarvinImage image2 = new MarvinImage(buffImage2);		
		
		//Gets the minimum width and height
		int minX = Math.min(a_imageIn.getWidth(), image2.getWidth());
		int minY = Math.min(a_imageIn.getHeight(), image2.getHeight());
		
		for (int x = 0; x < minX; x++) {
			for (int y = 0; y < minY; y++) {
				//Calculate the difference
				
				//Gets the gray scale value
				int gray = (int)((a_imageIn.getIntComponent0(x, y)*0.3) + (a_imageIn.getIntComponent1(x, y)*0.11) + (a_imageIn.getIntComponent2(x, y)*0.59));
				int gray1 = (int)((image2.getIntComponent0(x, y)*0.3) + (image2.getIntComponent1(x, y)*0.11) + (image2.getIntComponent2(x, y)*0.59));
				
				//Makes the absolute difference
				int diff = Math.abs(gray - gray1);
	            int v = (diff / 2);
				
	            //Sets the value to the new image
				a_imageOut.setIntColor(x, y, v, v, v);
			}
		}
	}
}
