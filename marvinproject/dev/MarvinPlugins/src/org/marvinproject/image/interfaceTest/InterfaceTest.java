/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/


package org.marvinproject.image.interfaceTest;

import marvin.gui.MarvinAttributesPanel;
import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Marvin GUI test plug-in. This plug-in do not affect the image.
 * @version 02/28/08
 * @author Gabriel Ambrósio Archanjo
 */
public class InterfaceTest extends MarvinAbstractImagePlugin
{
	private MarvinAttributesPanel	attributesPanel;
	MarvinAttributes 				attributes;
	
	public void load(){
		attributes = getAttributes();
		attributes.set("red", 10);
		attributes.set("green", 50);
		attributes.set("blue", 125);
		attributes.set("filter", "option 1");
		attributes.set("intensity", "option 1");
	}

	public MarvinAttributesPanel getAttributesPanel(){
		if(attributesPanel == null){
			attributesPanel = new MarvinAttributesPanel();
			attributesPanel.addLabel("labelRed", "Red:");
			attributesPanel.addTextField("textRed", "red", attributes);

			attributesPanel.addLabel("labelGreen", "Green:");
			attributesPanel.addTextField("textGreen", "green", attributes);

			attributesPanel.addLabel("labelBlue", "Red:");
			attributesPanel.addTextField("textBlue", "blue", attributes);

			attributesPanel.newComponentRow();
			attributesPanel.addComboBox("comboFilter", "filter", new Object[]{"option 1", "option 2", "option 3"}, attributes);

			attributesPanel.newComponentRow();
			attributesPanel.addLabel("labelIntensity", "Intensity:");
			attributesPanel.addHorizontalSlider("sliderIntensity", "intensity", -100,100,0, attributes);
			
			attributesPanel.newComponentRow();
			attributesPanel.addCheckBox("checkboxTest", "Check Me!", "checkbox1", attributes);
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
		System.out.println("Values received from GUI:");
		System.out.println("red:"+(Integer)attributes.get("red"));
		System.out.println("green:"+(Integer)attributes.get("green"));
		System.out.println("blue:"+(Integer)attributes.get("blue"));
		System.out.println("filter:"+(String)attributes.get("filter"));
		System.out.println("intensity:"+(Integer)attributes.get("intensity"));
		System.out.println("checkbox checked:"+(Boolean)attributes.get("checkbox1"));
		
		a_imageOut.setIntColorArray(a_imageIn.getIntColorArray());
	}
}