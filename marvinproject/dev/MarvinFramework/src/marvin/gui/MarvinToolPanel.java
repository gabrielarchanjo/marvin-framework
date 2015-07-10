/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package marvin.gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import marvin.plugin.MarvinToolPlugin;

public class MarvinToolPanel extends JPanel{
	
	// Definitions
	private final static int 	MAX_TOOLS = 50;
	
	// Interface Components
	private JButton				arrButtons[];

	// Image Panel
	private MarvinImagePanel	currentImagePanel;
	
	
	// Tools
	private MarvinToolPlugin 	arrTools[];
	private int					toolCounter;
	private int					currentTool;
	
	// ButtonHandler
	private ButtonHandler		buttonHandler;
	
	/**
	 * 
	 */
	public MarvinToolPanel(){
		toolCounter = 0;
		currentTool = 0;
		arrTools = new MarvinToolPlugin[MAX_TOOLS];
		arrButtons = new JButton[MAX_TOOLS];
		buttonHandler = new ButtonHandler();
	}
	
	public void setImagePanel(MarvinImagePanel ip){
		if(currentImagePanel != ip){
			currentImagePanel = ip;
		}
	}
	
	/**
	 * 
	 * @param tp
	 */
	public void addTool(MarvinToolPlugin tp){
		arrTools[toolCounter] = tp;
		
		arrButtons[toolCounter] = new JButton(tp.getIcon());
		arrButtons[toolCounter].setPreferredSize
			(new Dimension(tp.getIcon().getImage().getWidth(null)+10, tp.getIcon().getImage().getHeight(null)+10));		
		arrButtons[toolCounter].addActionListener(buttonHandler);
		
		add(arrButtons[toolCounter]);
		
		// Increment counter
		toolCounter++;
	}
	
	public MarvinToolPlugin getCurrentTool(){
		return arrTools[currentTool];
	}
	
	private class ButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent event){
			for(int i=0; i<toolCounter; i++){
				if(event.getSource() == arrButtons[i]){
					
					// Remove current settings panel
					if(arrTools[currentTool].getSettingsWindow() != null){
						remove(arrTools[currentTool].getSettingsWindow().getContentPane());
					}
					
					currentTool = i;
					
					// Set Cursor
					Image img = arrTools[currentTool].getCursorImage();
					if(img != null){
						Toolkit tk = Toolkit.getDefaultToolkit();
						Cursor cursor = tk.createCustomCursor(img, arrTools[currentTool].getCursorHotSpot(), "curstomCursor");
						currentImagePanel.setCursor(cursor);						
					}
					else{
						currentImagePanel.setCursor(Cursor.getDefaultCursor());
					}
					
					// Set settings panel
					if(arrTools[currentTool].getSettingsWindow() != null){
						add(arrTools[currentTool].getSettingsWindow().getContentPane());
					}
					validate();
					repaint();
				}
			}
		}
	}
}
