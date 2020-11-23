/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package net.marvinproject.framework.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.marvinproject.framework.image.MarvinImage;
import net.marvinproject.framework.image.MarvinImageMask;
import net.marvinproject.framework.plugin.MarvinImagePlugin;
import net.marvinproject.framework.plugin.MarvinPlugin;

/**
 * Generic Window for filters. This window includes thumbnail, preview and reset support.
 * @version 1.0 02/13/08
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinFilterWindow extends MarvinPluginWindow
{
	protected JPanel 	panelFixedComponents, panelImage;

	protected JButton 	buttonPreview, buttonApply, buttonReset;
	protected JLabel 	labelImage;

	MarvinImagePanel		imagePanel;
	MarvinAttributesPanel	attributesPanel;
	MarvinImage 			imageThumbnail;
	MarvinImage 			imageResetBuffer;
	MarvinImage				imageOut;
	MarvinImagePlugin 		plugin;	

	// ActionHandler
	protected ActionHandler actionHandler;

	/**
	 * Constructs a {@link MarvinFilterWindow}
	 * @param name Window name
	 * @param width width
	 * @param height height
	 * @param ip {@link MarvinImagePanel}
	 * @param plg {@link MarvinPlugin}
	 */
	public MarvinFilterWindow
	(
		String name, 
		int width, 
		int height,
		MarvinImagePanel ip,
		MarvinImagePlugin filter
	)
	{
		this(name, width, height, 250, 250, ip, filter);
	}

	/**
	 * Constructs a {@link MarvinFilterWindow}
	 * @param name Window name
	 * @param width width
	 * @param height height
	 * @param thumbWidth thumbnail with
	 * @param thumbHeight thumbnail height
	 * @param ip {@link MarvinImagePanel}
	 * @param plg {@link MarvinPlugin}
	 */
	public MarvinFilterWindow
	(
		String name, 
		int width, 
		int height,
		int thumbWidth,
		int thumbHeight,
		MarvinImagePanel ip,
		MarvinImagePlugin plg
	)
	{
		super(name, width, height, plg.getAttributesPanel());
		attributesPanel = plg.getAttributesPanel();
		
		if(attributesPanel != null){
			int newWidth = width;
			if(attributesPanel.getPreferredSize().width > width){
				newWidth = attributesPanel.getPreferredSize().width+20;
			}
			
			setSize(newWidth, height+attributesPanel.getPreferredSize().height);
		}
			
		imagePanel = ip;
		plugin = plg;
		//Buttons
		actionHandler = new ActionHandler();
		buttonPreview = new JButton("Preview");
		buttonReset = new JButton("Reset");
		buttonApply = new JButton("Apply");
		
		buttonPreview.setMnemonic('P');
		buttonReset.setMnemonic('R');
		buttonApply.setMnemonic('A');

		buttonPreview.addActionListener(actionHandler);
		buttonReset.addActionListener(actionHandler);
		buttonApply.addActionListener(actionHandler);

		// Fixed Components
		panelFixedComponents = new JPanel();
		panelFixedComponents.setLayout(new FlowLayout());
		panelFixedComponents.add(buttonPreview);
		panelFixedComponents.add(buttonReset);
		panelFixedComponents.add(buttonApply);

		// Image Panel
		panelImage = new JPanel();
		panelImage.setLayout(new FlowLayout());

		// Image
		if(thumbWidth > 0 && thumbHeight > 0){
			imageThumbnail = new MarvinImage(imagePanel.getImage().getBufferedImage(thumbWidth, thumbHeight, MarvinImage.PROPORTIONAL));			
			imageResetBuffer = imageThumbnail.clone();
			labelImage = new JLabel(new ImageIcon(imageThumbnail.getBufferedImage()));
			panelImage.add(labelImage);
		}
		
		imageOut = new MarvinImage(imagePanel.getImage().getWidth(), imagePanel.getImage().getHeight(), imagePanel.getImage().getColorModel());
		
		container.add(panelImage, BorderLayout.NORTH);
		container.add(panelCenter, BorderLayout.CENTER);
		container.add(panelFixedComponents, BorderLayout.SOUTH);

	}

	/**
	 * Disables preview function.
	 */
	public void disablePreview(){
		panelFixedComponents.remove(buttonPreview);
		panelFixedComponents.remove(buttonReset);
		panelImage.remove(labelImage);
	}
	
	/**
	 * Returns the reference to "Apply" button.
	 * @return a reference to "Apply" button
	 */
	public JButton getApplyButton(){
		return buttonApply;
	}
	
	/**
	 * Preview the plug-in application
	 */
	public void preview(){
		try{
			//marvinApplication.getPerformanceMeter().disable();
			imageThumbnail = imageResetBuffer.clone();
			MarvinImage imgOut = new MarvinImage(imageThumbnail.getWidth(), imageThumbnail.getHeight());
			plugin.process(imageThumbnail, imgOut, null, MarvinImageMask.NULL_MASK, true);
			imgOut.update();
			imageThumbnail = imgOut.clone();
			//marvinApplication.getPerformanceMeter().enable();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		labelImage.setIcon(new ImageIcon(imageThumbnail.getBufferedImage()));
	}
	
	/**
	 * Reset to the original state
	 */
	public void reset(){
		imageThumbnail = new MarvinImage(imageResetBuffer.getNewImageInstance());
		labelImage.setIcon(new ImageIcon(imageThumbnail.getBufferedImage()));
	}

	/**
	 * Apply the plug-in
	 */
	public void apply(){		
		dispose();
		plugin.process(imagePanel.getImage(), imageOut, null, MarvinImageMask.NULL_MASK, false);
		
		if(imagePanel.isHistoryEnabled()){
			imagePanel.getHistory().addEntry(plugin.getClass().getSimpleName(), imageOut, plugin.getAttributes());
		}
		imageOut.update();
		imagePanel.setImage(imageOut);
	}

	/**
	 * Event handler class
	 */
	private class ActionHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e){
			if(e.getSource() == buttonApply){
				if(attributesPanel != null){
					attributesPanel.applyValues();
				}
				apply();
			}			
			else if (e.getSource() == buttonReset){
				reset();	
			}
			else if(e.getSource() == buttonPreview){
				if(attributesPanel != null){
					attributesPanel.applyValues();
				}
				preview();
			}
		}
	}
}