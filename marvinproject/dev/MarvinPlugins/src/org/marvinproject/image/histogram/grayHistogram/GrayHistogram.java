/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.histogram.grayHistogram;

import java.awt.Color;

import marvin.gui.MarvinAttributesPanel;
import marvin.gui.MarvinPluginWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.statistic.MarvinHistogram;
import marvin.statistic.MarvinHistogramEntry;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;

/**
 * Gray histogram is a representation of the gray scale distribution.
 * @author Gabriel Ambrósio Archanjo
 * @version 1.0 02/13/2008
 */
public class GrayHistogram extends MarvinAbstractImagePlugin
{
	MarvinImagePlugin pluginGray;
    public void load(){
    	pluginGray = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.color.grayScale.jar");
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
    	pluginGray.process(a_imageIn, a_imageOut, a_attributesOut, a_mask, a_previewMode);
    	
        MarvinHistogram l_histoGray = new MarvinHistogram("Gray Intensity");
        l_histoGray.setBarWidth(1);

        int l_arrGray[] = new int[256];
        
        for (int x = 0; x < a_imageOut.getWidth(); x++) {
            for (int y = 0; y < a_imageOut.getHeight(); y++) {
                l_arrGray[a_imageOut.getIntComponent0(x, y)]++;
            }
        }

        for(int x=0; x<256; x++){
            l_histoGray.addEntry(new MarvinHistogramEntry(x, l_arrGray[x], new Color(x, x, x)));
        }

        MarvinAttributesPanel panel = new MarvinAttributesPanel();
        panel.addImage("histoGray", l_histoGray.getImage(400,200));
        panel.newComponentRow();
        
        MarvinPluginWindow pluginWindow = new MarvinPluginWindow("Gra Histogram", 400, 200, panel);
        pluginWindow.setVisible(true);
    }
}