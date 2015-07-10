/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.histogram.colorHistogram;

import java.awt.Color;

import marvin.gui.MarvinAttributesPanel;
import marvin.gui.MarvinPluginWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.statistic.MarvinHistogram;
import marvin.statistic.MarvinHistogramEntry;
import marvin.util.MarvinAttributes;

/**
 * Color histogram is a representation of the RGB colors distribution.
 * @author Gabriel Ambrósio Archanjo
 * @version 1.0 02/13/2008
 */
public class ColorHistogram extends MarvinAbstractImagePlugin
{
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
        MarvinHistogram l_histoRed = new MarvinHistogram("Red Intensity");
        l_histoRed.setBarWidth(1);

        MarvinHistogram l_histoGreen = new MarvinHistogram("Green Intensity");
        l_histoGreen.setBarWidth(1);

        MarvinHistogram l_histoBlue = new MarvinHistogram("Blue Intensity");
        l_histoBlue.setBarWidth(1);

        int l_arrRed[] = new int[256];
        int l_arrGreen[] = new int[256];
        int l_arrBlue[] = new int[256];

        for (int x = 0; x < a_imageIn.getWidth(); x++) {
            for (int y = 0; y < a_imageIn.getHeight(); y++) {
                l_arrRed[a_imageIn.getIntComponent0(x, y)]++;
                l_arrGreen[a_imageIn.getIntComponent1(x, y)]++;
                l_arrBlue[a_imageIn.getIntComponent2(x, y)]++;
            }
        }

        for(int x=0; x<256; x++){
            l_histoRed.addEntry(new MarvinHistogramEntry(x, l_arrRed[x], new Color(x, 0, 0)));
            l_histoGreen.addEntry(new MarvinHistogramEntry(x, l_arrGreen[x], new Color(0, x, 0)));
            l_histoBlue.addEntry(new MarvinHistogramEntry(x, l_arrBlue[x], new Color(0, 0, x)));
        }

        MarvinAttributesPanel panel = new MarvinAttributesPanel();
        panel.addImage("histoRed", l_histoRed.getImage(400,200));
        panel.newComponentRow();
        panel.addImage("histoGreen", l_histoGreen.getImage(400,200));
        panel.newComponentRow();
        panel.addImage("histoBlue", l_histoBlue.getImage(400,200));
        panel.setVisible(true);
        
        MarvinPluginWindow pluginWindow = new MarvinPluginWindow("Color Histogram", 440,580, panel);
        pluginWindow.setVisible(true);
        
    }
}