/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.color.brightnessAndContrast;

import marvin.gui.MarvinAttributesPanel;
import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;


/**
 * Brightness and Constrast manipulation plug-in.
 * @author Gabriel Ambrósio Archanjo
 * @version 1.0 02/28/2008
 */
public class BrightnessAndContrast extends MarvinAbstractImagePlugin
{
	private MarvinAttributesPanel	attributesPanel;
	private MarvinAttributes 		attributes;

	public void load(){
		// Attributes
		attributes = getAttributes();
		attributes.set("brightness", 0);
		attributes.set("contrast", 0);
	}

	public MarvinAttributesPanel getAttributesPanel(){
		if(attributesPanel == null){
			attributesPanel = new MarvinAttributesPanel();
			attributesPanel.addLabel("lblBrightness", "Brightness");
			attributesPanel.addHorizontalSlider("sliderBrightness", "brightness", -127, 127, 0, attributes);
			attributesPanel.newComponentRow();
			attributesPanel.addLabel("lblContrast", "Contrast");
			attributesPanel.addHorizontalSlider("sliderContrast", "contrast", -127, 127, 0, attributes);
		}
		return attributesPanel;
	}

	public void process
	(
		MarvinImage imageIn, 
		MarvinImage imageOut,
		MarvinAttributes attributesOut,
		MarvinImageMask mask, 
		boolean previewMode
	)
	{
		double r,g,b;
		double l_brightness = (Integer)attributes.get("brightness");
		double l_contrast = (Integer)attributes.get("contrast");
		l_contrast = Math.pow((127 + l_contrast)/127, 2);

		// Brightness
		for (int x = 0; x < imageIn.getWidth(); x++) {
			for (int y = 0; y < imageIn.getHeight(); y++) {
				r = imageIn.getIntComponent0(x, y);
				g = imageIn.getIntComponent1(x, y);
				b = imageIn.getIntComponent2(x, y);

				r+= (1-(r/255))*l_brightness;
				g+= (1-(g/255))*l_brightness;
				b+= (1-(b/255))*l_brightness;
				if(r < 0) r=0;
				if(r > 255) r=255;
				if(g < 0) g=0;
				if(g > 255) g=255;
				if(b < 0) b=0;
				if(b > 255) b=255;

				imageOut.setIntColor(x,y,imageIn.getAlphaComponent(x, y), (int)r,(int)g,(int)b);
			}
		}

		// Contrast
		for (int x = 0; x < imageIn.getWidth(); x++) {
			for (int y = 0; y < imageIn.getHeight(); y++) {
				r = imageOut.getIntComponent0(x, y);
				g = imageOut.getIntComponent1(x, y);
				b = imageOut.getIntComponent2(x, y);

				
				r /= 255.0;
				r -= 0.5;
				r *= l_contrast;
				r += 0.5;
				r *= 255.0;

				g /= 255.0;
				g -= 0.5;
				g *= l_contrast;
				g += 0.5;
				g *= 255.0;

				b /= 255.0;
				b -= 0.5;
				b *= l_contrast;
				b += 0.5;
				b *= 255.0;
				

				if(r < 0) r=0;
				if(r > 255) r=255;
				if(g < 0) g=0;
				if(g > 255) g=255;
				if(b < 0) b=0;
				if(b > 255) b=255;

				imageOut.setIntColor(x,y,imageIn.getAlphaComponent(x, y), (int)r,(int)g,(int)b);
			}
		}
	}
}