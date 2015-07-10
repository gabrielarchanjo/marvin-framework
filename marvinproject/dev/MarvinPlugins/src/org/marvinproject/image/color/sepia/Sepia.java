/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.color.sepia;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.performance.MarvinPerformanceMeter;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;
/**
 * Sepia plug-in. Reference: {@link http://forum.java.sun.com/thread.jspa?threadID=728795&messageID=4195478}
 * @author Hugo Henrique Slepicka
 * @version 1.0.2 04/10/2008
 */
public class Sepia extends MarvinAbstractImagePlugin implements ChangeListener, KeyListener{

	private MarvinAttributesPanel 	attributesPanel;
	private MarvinAttributes 		attributes;
	private MarvinPerformanceMeter 	performanceMeter;
	
	
	public void load() {
		attributes = getAttributes();
		attributes.set("txtValue", "20");
		attributes.set("intensity", 20);
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
		int r, g, b, depth, corfinal;
		
		//Define a intensidade do filtro...
		depth = Integer.parseInt(attributes.get("intensity").toString());
		
		int width    = imageIn.getWidth();
		int height   = imageIn.getHeight();
		
		//performanceMeter.enableProgressBar("Filtro de Teste", ((height-2)*(width-2)));
		
		boolean[][] l_arrMask = mask.getMaskArray();
		
		for (int x = 0; x < imageIn.getWidth(); x++) {
			for (int y = 0; y < imageIn.getHeight(); y++) {
				if(l_arrMask != null && !l_arrMask[x][y]){
					continue;
				}
				//Captura o RGB do ponto...
				r = imageIn.getIntComponent0(x, y);
				g = imageIn.getIntComponent1(x, y);
				b = imageIn.getIntComponent2(x, y);
				
				//Define a cor como a média aritmética do pixel...
				corfinal = (r + g + b) / 3;
				r = g = b = corfinal;
				 
				r = truncate(r + (depth * 2));
				g = truncate(g + depth);
			
				//Define a nova cor do ponto...
				imageOut.setIntColor(x, y, imageIn.getAlphaComponent(x, y), r, g, b);
			}
			//performanceMeter.incProgressBar(width-2);
		}
		//performanceMeter.finish();
	}

	/**
	 * Sets the RGB between 0 and 255
	 * @param a
	 * @return
	 */
	public int truncate(int a) {
		if      (a <   0) return 0;
		else if (a > 255) return 255;
		else              return a;
	}
	
	public MarvinAttributesPanel getAttributesPanel(){ 
		if(attributesPanel == null){
			attributesPanel = new MarvinAttributesPanel();
			attributesPanel.addLabel("lblIntensidade", "Intensidade do Filtro");
			attributesPanel.addHorizontalSlider("hsIntensidade", "hsIntensidade", 0, 100, 20, attributes);
			attributesPanel.newComponentRow();
			attributesPanel.addTextField("txtValue", "txtValue",attributes);
				
			JTextField txtValue = (JTextField)(attributesPanel.getComponent("txtValue").getComponent());
			JSlider slider = (JSlider)(attributesPanel.getComponent("hsIntensidade").getComponent());
			
			slider.addChangeListener(this);
			txtValue.addKeyListener(this);
		}
		
		return attributesPanel;
	}
	
	//Manipula as alterações da Horizontal Bar
	//Handles the Horizontal Bar changes
	public void stateChanged(ChangeEvent e) {
		JSlider barra = (JSlider) (e.getSource());
		JTextField lbl = (JTextField)(attributesPanel.getComponent("txtValue").getComponent());
		lbl.setText(""+barra.getValue());
	}

	public void keyPressed(KeyEvent e) {
		
	}

	public void keyReleased(KeyEvent e) {
		JTextField txtValor = (JTextField) (attributesPanel.getComponent("txtValue").getComponent());
		JSlider barra = (JSlider) (attributesPanel.getComponent("hsIntensidade").getComponent());
		try {
			barra.setValue(Integer.parseInt(txtValor.getText().trim()));	
		} catch (Exception exception) {
			barra.setValue(0);
			txtValor.setText("0");
		}
			
	}

	public void keyTyped(KeyEvent e) {}
	
}
