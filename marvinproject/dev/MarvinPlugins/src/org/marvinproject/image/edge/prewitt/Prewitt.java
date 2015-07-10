/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.edge.prewitt;

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
public class Prewitt extends MarvinAbstractImagePlugin{

	// Definitions
	double[][] matrixPrewittX = new double[][]{
			{1,		0,		-1},
			{1,		0,		-1},
			{1,		0,		-1}
	};
	
	double[][] matrixPrewittY = new double[][]{
			{1,		1,		1},
			{0,		0,		0},
			{-1,	-1,		-1}
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
		convolution.setAttribute("matrix", matrixPrewittX);
		convolution.process(imageIn, imageOut, null, mask, previewMode);
		
		convolution.setAttribute("matrix", matrixPrewittY);
		convolution.process(imageIn, imageOut, null, mask, previewMode);
    }
}
