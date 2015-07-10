/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.edge.sobel;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;

/**
 * @author Gabriel Ambrósio Archanjo
 */
public class Sobel extends MarvinAbstractImagePlugin{

	// Definitions
	double[][] matrixSobelX = new double[][]{
			{1,		0,	-1},
			{2,		0,	-2},
			{1,		0,	-1}
	};
	
	double[][] matrixSobelY = new double[][]{
			{-1,	-2,		-1},
			{0,		0,		0},
			{1,		2,		1}
	};
	
	private MarvinImagePlugin 	convolution;
	
	public void load(){
		convolution = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.convolution.jar");
	}
	
	public MarvinAttributesPanel getAttributesPanel(){
		return null;
	}
	
	public void process
	(
		MarvinImage imageIn, 
		MarvinImage imageOut,
		MarvinAttributes attrOut,
		MarvinImageMask mask, 
		boolean previewMode
	)
    {
		convolution.setAttribute("matrix", matrixSobelX);
		convolution.process(imageIn, imageOut, null, mask, previewMode);
		
		convolution.setAttribute("matrix", matrixSobelY);
		convolution.process(imageIn, imageOut, null, mask, previewMode);
    }
}
