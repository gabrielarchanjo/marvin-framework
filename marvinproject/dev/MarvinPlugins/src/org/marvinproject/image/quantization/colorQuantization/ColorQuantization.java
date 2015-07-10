/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.quantization.colorQuantization;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

public class ColorQuantization extends MarvinAbstractImagePlugin{

	private MarvinAttributesPanel	attributesPanel;
	
	@Override
	public void load() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public MarvinAttributesPanel getAttributesPanel() {
		if(attributesPanel == null){
			attributesPanel = new MarvinAttributesPanel();
			attributesPanel.addLabel("lblColors", "Colors:");
			attributesPanel.addTextField("txtColors", "colors", getAttributes());
		}
		return attributesPanel;
	}

	@Override
	public void process(MarvinImage imgIn, MarvinImage imgOut,
			MarvinAttributes attrOut, MarvinImageMask mask, boolean previewMode) {
		// TODO Auto-generated method stub
		
	}
}
