/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.test.plugin;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import marvin.gui.MarvinAttributesPanel;
import marvin.gui.MarvinFilterWindow;
import marvin.gui.MarvinPluginWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;
/**
 * Sepia plug-in. Reference: {@link http://forum.java.sun.com/thread.jspa?threadID=728795&messageID=4195478}
 * @author Hugo Henrique Slepicka
 * @version 1.0.2 04/10/2008
 */
public class Plugin extends MarvinAbstractImagePlugin implements ChangeListener, KeyListener{

	private MarvinAttributesPanel	attributesPanel;
	private MarvinAttributes 		attributes;
	private MarvinPluginWindow 		tela;
		
	public void load() {
		attributes = getAttributes();
		attributes.set("hsR", 0);
		attributes.set("hsG", 0);
		attributes.set("hsB", 0);
		attributes.set("txtR", "0");
		attributes.set("txtG", "0");
		attributes.set("txtB", "0");
	}

	public int truncate(int a) {
		if      (a <   0) return 0;
		else if (a > 255) return 255;
		else              return a;
	}
	public int positive(int a) {
		if      (a <   0) return -a;
		else              return a;
	}
	
	
	public void process(
		MarvinImage a_imageIn, 
		MarvinImage a_imageOut,
		MarvinAttributes a_attributesOut,
		MarvinImageMask a_mask, 
		boolean a_previewMode
	)
	{
		//RGB rgb = new RGB();
		RGB rgbImagem = new RGB();
	
		int l_red = (Integer)attributes.get("hsR");
		int l_green = (Integer)attributes.get("hsG");
		int l_blue = (Integer)attributes.get("hsB");
		
		int width  = a_imageIn.getWidth();
		int height = a_imageIn.getHeight();
		
		boolean[][] l_arrMask = a_mask.getMask();
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if(l_arrMask != null && !l_arrMask[x][y]){
					continue;
				}
				
				rgbImagem.setR(a_imageIn.getIntComponent0(x, y));
				rgbImagem.setG(a_imageIn.getIntComponent1(x, y));
				rgbImagem.setB(a_imageIn.getIntComponent2(x, y));
				
				rgbImagem.setR((int)(rgbImagem.getR()*(255.0/(255.0-l_red))));
				rgbImagem.setG((int)(rgbImagem.getG()*(255.0/(255.0-l_green))));
				rgbImagem.setB((int)(rgbImagem.getB()*(255.0/(255.0-l_blue))));
				
				a_imageOut.setIntColor(x, y, rgbImagem.getR(),rgbImagem.getG(),rgbImagem.getB());
			}
		}
	}

	public MarvinAttributesPanel getAttributesPanel(){
		if(attributesPanel == null){
			attributesPanel = new MarvinAttributesPanel();
			attributesPanel.addLabel("lblC", "Cyan");
			attributesPanel.addHorizontalSlider("hsR", "hsR", -100, 100, 0, attributes);
			attributesPanel.addLabel("lblR", "Red");
			attributesPanel.addTextField("txtR", "txtR",attributes);
			
			attributesPanel.newComponentRow();
			attributesPanel.addLabel("lblM", "Magenta");
			attributesPanel.addHorizontalSlider("hsG", "hsG", -100, 100, 0, attributes);
			attributesPanel.addLabel("lblG", "Green");
			attributesPanel.addTextField("txtG", "txtG",attributes);
			
			attributesPanel.newComponentRow();
			attributesPanel.addLabel("lblY", "Yellow");
			attributesPanel.addHorizontalSlider("hsB", "hsB", -100, 100, 0, attributes);
			attributesPanel.addLabel("lblB", "Blue");
			attributesPanel.addTextField("txtB", "txtB",attributes);
			
			attributesPanel.newComponentRow();
			
				
			JSlider sliderR = (JSlider)(attributesPanel.getComponent("hsR").getComponent());
			JSlider sliderG = (JSlider)(attributesPanel.getComponent("hsG").getComponent());
			JSlider sliderB = (JSlider)(attributesPanel.getComponent("hsB").getComponent());
			
			JTextField txtR = (JTextField)(attributesPanel.getComponent("txtR").getComponent());
			JTextField txtG = (JTextField)(attributesPanel.getComponent("txtG").getComponent());
			JTextField txtB = (JTextField)(attributesPanel.getComponent("txtB").getComponent());
			
			sliderR.addChangeListener(this);
			sliderG.addChangeListener(this);
			sliderB.addChangeListener(this);
			txtR.addKeyListener(this);
			txtG.addKeyListener(this);
			txtB.addKeyListener(this);
		}
		return attributesPanel;
	}
	
	//Manipula as alterações da Horizontal Bar
	//Handles the Horizontal Bar changes
	public void stateChanged(ChangeEvent e) {
		JSlider barra1 = (JSlider) (attributesPanel.getComponent("hsR").getComponent());
		JSlider barra2 = (JSlider) (attributesPanel.getComponent("hsG").getComponent());
		JSlider barra3 = (JSlider) (attributesPanel.getComponent("hsB").getComponent());
		JSlider barra = (JSlider) (e.getSource());
		JTextField lbl = null;
		
		if(e.getSource()==barra1){
				lbl = (JTextField)(attributesPanel.getComponent("txtR").getComponent());
			}
			else{ 
				if(e.getSource()==barra2){
					lbl = (JTextField)(attributesPanel.getComponent("txtG").getComponent());
				}
				else {
					if(e.getSource()==barra3){
					}
					lbl = (JTextField)(attributesPanel.getComponent("txtB").getComponent());
					}
			}
		lbl.setText(""+barra.getValue());
	}

	public void keyPressed(KeyEvent e) {
		
	}

	public void keyReleased(KeyEvent e) {
		JTextField txtR = (JTextField) (attributesPanel.getComponent("txtR").getComponent());
		JTextField txtG = (JTextField) (attributesPanel.getComponent("txtG").getComponent());
		JTextField txtB = (JTextField) (attributesPanel.getComponent("txtB").getComponent());
		
		JSlider barra1 = (JSlider) (attributesPanel.getComponent("hsR").getComponent());
		JSlider barra2 = (JSlider) (attributesPanel.getComponent("hsG").getComponent());
		JSlider barra3 = (JSlider) (attributesPanel.getComponent("hsB").getComponent());
		
		try {
			if(e.getSource()==txtR){
			barra1.setValue(Integer.parseInt(txtR.getText().toString()));
			}
			else{
				if(e.getSource()==txtG){
					barra2.setValue(Integer.parseInt(txtG.getText().toString()));
				}
				else
					if(e.getSource()==txtB){
						barra3.setValue(Integer.parseInt(txtB.getText().toString()));
					}
			}
			} catch (Exception exception) {
				if(e.getSource()==txtR){
					barra1.setValue(0);	
					txtR.setText("0");
					}
				else {
						if(e.getSource()==txtG){
							barra2.setValue(0);
							txtG.setText("0");
						}
						else{ 
								if(e.getSource()==txtB){
									barra3.setValue(0);
									txtB.setText("0");
								}
							}
					}
				}
	}

	public void keyTyped(KeyEvent e) {
	
	}

	private class RGB{
		private int r;
		private int g;
		private int  b;
		public RGB(){
			this.r=0;
			this.g=0;
			this.b=0;
		}
		public RGB(int r, int g, int b){
			this.r=r;
			this.g=g;
			this.b=b;
		}
		public int getR() {
			return r;
		}
		public void setR(int r) {
			this.r = truncate(r);
		}
		public int getG() {
			return g;
		}
		public void setG(int g) {
			this.g = truncate(g);
		}
		public int getB() {
			return b;
		}
		public void setB(int b) {
			this.b = truncate(b);
		}
	}
}
