/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package image.filters;

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

import static marvinplugins.MarvinPluginCollection.*;

/**
 * Filter application sample
 * @author Gabriel Ambrosio Archanjo
 */
public class Filters extends JFrame
{
	private MarvinImagePanel 	imagePanel;
	private MarvinImage 		image, 
								backupImage;
	
	private JPanel 				panelBottom;
	
	
	private JButton 			buttonGray, 
								buttonSepia, 
								buttonInvert, 
								buttonReset;
	
	private MarvinImagePlugin 	imagePlugin;
	
	public Filters()
	{
		super("Filters Sample");
		// Create Graphical Interface
		ButtonHandler buttonHandler = new ButtonHandler();
		buttonGray = new JButton("Gray");
		buttonGray.addActionListener(buttonHandler);
		buttonSepia = new JButton("Sepia");
		buttonSepia.addActionListener(buttonHandler);
		buttonInvert = new JButton("Invert");
		buttonInvert.addActionListener(buttonHandler);
		buttonReset = new JButton("Reset");
		buttonReset.addActionListener(buttonHandler);
		
		panelBottom = new JPanel();
		panelBottom.add(buttonGray);
		panelBottom.add(buttonSepia);
		panelBottom.add(buttonInvert);
		panelBottom.add(buttonReset);
		
		// ImagePanel
		imagePanel = new MarvinImagePanel();
		
		Container l_c = getContentPane();
		l_c.setLayout(new BorderLayout());
		l_c.add(imagePanel, BorderLayout.NORTH);
		l_c.add(panelBottom, BorderLayout.SOUTH);
		
		// Load image
		loadImage();
		
		imagePanel.setImage(image);
		
		setSize(320,600);
		setVisible(true);	
	}
	
	private void loadImage(){
		image = MarvinImageIO.loadImage("./res/arara.jpg");
		backupImage = image.clone();
	}
	
	public static void main(String args[]){
		Filters t = new Filters();
		t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private class ButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent a_event){
			image = backupImage.clone();
			if(a_event.getSource() == buttonGray){
				grayScale(image);
			}
			else if(a_event.getSource() == buttonSepia){
				sepia(image, 50);
			}
			else if(a_event.getSource() == buttonInvert){
				invertColors(image);
			}
			image.update();
			imagePanel.setImage(image);
		}
	}
}