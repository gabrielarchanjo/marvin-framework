/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package image.history;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinPluginHistory;

import org.marvinproject.image.color.brightnessAndContrast.BrightnessAndContrast;
import org.marvinproject.image.color.invert.Invert;
import org.marvinproject.image.edge.edgeDetector.EdgeDetector;

/**
 * History sample
 * @author Gabriel Ambrosio Archanjo
 *
 */
public class History extends JFrame {
	
	// GUI
	JButton buttonShowHistory;
	JButton buttonApply;
	
	// Marvin Objects
	MarvinPluginHistory history;
	MarvinImagePlugin 	tempPlugin;
	MarvinImage 		originalImage;
	MarvinImage			resultImage;
	
	MarvinImagePanel 	imagePanelOriginal,
						imagePanelNew;
	
	public History(){
		super("Plug-in History Sample");
		
		ButtonHandler buttonHandler = new ButtonHandler();
		buttonShowHistory = new JButton("Show History");
		buttonShowHistory.addActionListener(buttonHandler);
		buttonApply = new JButton("Apply");
		buttonApply.addActionListener(buttonHandler);
		
		JPanel l_panelBottom = new JPanel();
		l_panelBottom.add(buttonApply);
		l_panelBottom.add(buttonShowHistory);
		
		imagePanelOriginal = new MarvinImagePanel();
		imagePanelNew = new MarvinImagePanel();
		
		JPanel l_panelTop = new JPanel();
		l_panelTop.add(imagePanelOriginal);
		l_panelTop.add(imagePanelNew);		
		
		Container l_c = getContentPane();
		l_c.setLayout(new BorderLayout());
		l_c.add(l_panelTop, BorderLayout.NORTH);
		l_c.add(l_panelBottom, BorderLayout.SOUTH);
		
		originalImage = MarvinImageIO.loadImage("./res/tucano.jpg");
		imagePanelOriginal.setImage(originalImage);
		imagePanelNew.setPreferredSize(imagePanelOriginal.getPreferredSize());
		
		setSize(765,630);
		setVisible(true);		
	}
	
	private void process(){
		history = new MarvinPluginHistory();
	
		resultImage = originalImage.clone();
		MarvinImage l_tempImage = new MarvinImage(resultImage.getWidth(), resultImage.getHeight());
		
		history.addEntry("Original", resultImage, null);
		
		tempPlugin = new EdgeDetector();
		tempPlugin.load();
		tempPlugin.process(resultImage, l_tempImage);
		l_tempImage.update();
		resultImage = l_tempImage.clone();
		history.addEntry("Edge", resultImage, tempPlugin.getAttributes());
		
		tempPlugin = new BrightnessAndContrast();
		tempPlugin.load();
		tempPlugin.setAttribute("brightness", -127);
		tempPlugin.setAttribute("contrast", 127);
		
		tempPlugin.process(resultImage, resultImage);
		resultImage.update();
		history.addEntry("BrightenessContrast", resultImage, tempPlugin.getAttributes());
		
		tempPlugin = new Invert();
		tempPlugin.load();
		tempPlugin.process(resultImage, resultImage);
		resultImage.update();
		history.addEntry("Invert", resultImage, tempPlugin.getAttributes());
		
		
		imagePanelNew.setImage(resultImage);
	}
	
	public static void main(String args[]){
		History s = new History();
		s.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private class ButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(e.getSource() == buttonApply){
				process();
			}
			else if(e.getSource() == buttonShowHistory){
				if(history != null){
					history.showThumbnailHistory();
				}
			}			
		}		
	}
}
