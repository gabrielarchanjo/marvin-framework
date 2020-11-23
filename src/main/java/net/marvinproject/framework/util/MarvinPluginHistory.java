/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package net.marvinproject.framework.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;

import net.marvinproject.framework.image.MarvinImage;

/**
 * Store all plug-ins used on an image. A strip image with all steps can be exported.
 * @version 1.1 02/25/09
 */
public class MarvinPluginHistory extends JFrame
{
	// Constants
	private final static int 				TOP_MARGIN = 50;
	private final static int 				BOTTOM_MARGIN = 10;
	private final static int 				ATTRIBUTES_MARGIN = 200;
	private final static int 				THUMBNAIL_WIDTH = 200;
	private final static String 			STORE_SUCCESS = "History exported sucessfully";
	private final static String 			STORE_FAILED = "Error while exporting the history";

	// Interface Components
	JPanel 									panelButtonHistory;
	JButton 								buttonExportHistortAsImage;
	JButton 								buttonExportHistoryAsImage;
	
	// Image panel
	JPanel 									panelPlugin;
	JScrollPane 							scrollPanelPlugins;

	private LinkedList<String> 				listPluginName;
	private LinkedList<MarvinImage> 		listMarvinImage;
	private LinkedList<MarvinAttributes>	listMarvinAttributes;

	private JFrame 							frameHistory;

	/**
	 * Constructs a new PluginHistory.
	 */
	public MarvinPluginHistory()
	{
		frameHistory = this;
		
		this.setLayout(new BorderLayout ());

		setResizable(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Plug-ins history");
		
		listMarvinImage = new LinkedList<MarvinImage>();
		listMarvinAttributes = new LinkedList<MarvinAttributes>();
		listPluginName = new LinkedList<String>();
		
		panelPlugin = new JPanel();
		
		scrollPanelPlugins = new JScrollPane
		(
			panelPlugin, 
			ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
		);
		
		
		scrollPanelPlugins.setLayout(new ScrollPaneLayout());		
		add(scrollPanelPlugins);
		
		panelButtonHistory = new JPanel();
				
		buttonExportHistortAsImage = new JButton("Export as Image");
		buttonExportHistortAsImage.addActionListener(new ExportButtonHandler());
		buttonExportHistortAsImage.setMnemonic('I');
		
		// the action listener is still not created
		// so no button appears
		//btnExportHistoricText = new JButton ("Export as Text");
		//btnExportHistoricText.addActionListener (new ExportTexButtonHandler ());
		// ActionListener "ExportTextButtonHandler" need to be created!!!
		//btnExportHistoricText.setMnemonic('T');

		panelButtonHistory.add(buttonExportHistortAsImage);
		//jpBtnHistoric.add (btnExportHistoricText);

		this.add(panelButtonHistory, BorderLayout.PAGE_END);
	}

	/**
	 * Add a new entry to the history.
	 *
	 * @param plgName Effect's name
	 * @param img The modified {@code MarvinImage}
	 * @param attr The {@code MarvinAttributes} applied
	 *
	 * @see MarvinImage
	 * @see MarvinAttributes
	 */
	public void addEntry
	(
		String plgName, 
		MarvinImage img, 
		MarvinAttributes attr
	){
		listPluginName.add(plgName);
		
		MarvinImage l_image=null;
		MarvinAttributes l_attributes=null;
		
		if(img != null){
			l_image = img.clone();
		}
		
		if(attr != null){
			l_attributes = attr.clone();
		}
		
		listMarvinImage.add(l_image);
		listMarvinAttributes.add(l_attributes);
	}

	/**
	 * Shows the history dialog.
	 */
	public void showThumbnailHistory()
	{	
		panelPlugin.removeAll();
		panelPlugin.setLayout(new GridLayout(1,listMarvinImage.size()));
		
		// displays the plugins
		MarvinImage l_image;
		MarvinAttributes l_pluginAttributes;
		String l_name;
		for(int l_pos=0; l_pos<listMarvinImage.size(); l_pos++)	
		{
			l_name = listPluginName.get(l_pos);
			l_image = listMarvinImage.get(l_pos);
			l_pluginAttributes = listMarvinAttributes.get(l_pos);
			
			JLabel l_labelIcon = new JLabel (new ImageIcon(l_image.getBufferedImage(THUMBNAIL_WIDTH, THUMBNAIL_WIDTH)));
			
			// creates text box for plugin information
			TextArea l_textPluginInfo = new TextArea("", 5, 25, TextArea.SCROLLBARS_VERTICAL_ONLY);	
			l_textPluginInfo.setEditable(false);
			
			l_textPluginInfo.append(l_name+"\n\n");
			
			// if plugin's attributes not empty
			if(l_pluginAttributes != null){
				if (l_pluginAttributes.toStringArray().length > 0){
					// appends them to the info box
					l_textPluginInfo.append ("Attributes:\n");
				}
	
				for (int j = 0; j < l_pluginAttributes.toStringArray().length; j += 2){
					l_textPluginInfo.append(l_pluginAttributes.toStringArray()[j] + ": ");
					l_textPluginInfo.append(l_pluginAttributes.toStringArray()[j + 1] + "\n");
				}
			}
		
			JPanel l_panelPlugin = new JPanel();
			l_panelPlugin.add(l_labelIcon);
			l_panelPlugin.add(l_textPluginInfo);
			panelPlugin.add(l_panelPlugin);			
		}
		
		// adjust the screen size considering the number of entries
		panelPlugin.setPreferredSize (new Dimension (listMarvinImage.size()* (THUMBNAIL_WIDTH+20), 350));
		int l_windowWidth = listMarvinImage.size() * (THUMBNAIL_WIDTH+20);
		if(l_windowWidth > 1024){
			l_windowWidth = 1024;
		}
		setSize(l_windowWidth, 430);
		
		setVisible(true);
	}

	/**
	 * Clear the history.
	 */
	public void clear(){
		listPluginName = new LinkedList<String>();
		listMarvinImage = new LinkedList<MarvinImage>();
		listMarvinAttributes = new LinkedList<MarvinAttributes>();
	}

	/**
	 * Export history as image
	 */
	private void exportAsImage(){
		
		System.out.println("listMarvinImage.size():"+listMarvinImage.size());
		System.out.println("listMarvinAttributes.size():"+listMarvinAttributes.size());
		
		try {
			String l_pluginName;
			MarvinImage l_image;
			MarvinAttributes l_attributes;
			String[] l_attributesAsString=null;
			int l_pos;
			int l_imagePx = 5;
			int l_attributePy=0;
			int width=0;
			int height=0;
			int i;


			// Calc the history image width and height
			for(MarvinImage img : listMarvinImage){
				width+=img.getWidth()+ATTRIBUTES_MARGIN;

				// The image height will be the biggest height from the listMarvinImage
				if(img.getHeight() > height){
					height = img.getHeight();
				}
			}
			height+=TOP_MARGIN+BOTTOM_MARGIN;

			String arq = MarvinFileChooser.select(frameHistory, false, MarvinFileChooser.SAVE_DIALOG);

			if (arq != null) {
				BufferedImage bufExport = new BufferedImage(width+5,height, listMarvinImage.get(0).getType());
				Graphics2D g = bufExport.createGraphics();
				height =  listMarvinImage.get(0).getHeight();

				for(l_pos=0; l_pos<listMarvinImage.size(); l_pos++){
					l_pluginName = listPluginName.get(l_pos);
					l_image = listMarvinImage.get(l_pos);
					l_attributes = listMarvinAttributes.get(l_pos);

					if(l_attributes != null){
						l_attributesAsString = l_attributes.toStringArray();
					}

					g.drawImage(l_image.getBufferedImage(), null, l_imagePx, TOP_MARGIN);
					g.setColor(Color.white);
					g.drawRect(l_imagePx+l_image.getWidth()+5, TOP_MARGIN, ATTRIBUTES_MARGIN-10, l_image.getHeight());

					if(l_pluginName.lastIndexOf('.') != -1){
						g.drawString(l_pluginName.substring(l_pluginName.lastIndexOf('.')+1, l_pluginName.length()), l_imagePx+l_image.getWidth()+10, TOP_MARGIN+15);
					}
					else{
						g.drawString(l_pluginName, l_imagePx+l_image.getWidth()+10, TOP_MARGIN+15);
					}

					g.drawString("ATTRIBUTES:", l_imagePx+l_image.getWidth()+10, TOP_MARGIN+45);
					l_attributePy = TOP_MARGIN+30+45;

					if(l_attributes != null){
						g.setColor(Color.white);
						for(i=0; i<l_attributesAsString.length; i+=2){
							// Attribute name
							g.drawString(l_attributesAsString[i]+": "+l_attributesAsString[i+1], l_imagePx+l_image.getWidth()+10, l_attributePy);
							l_attributePy+=15;
						}
					}

					l_imagePx+=l_image.getWidth()+ATTRIBUTES_MARGIN;
				}

				// Draw Header
				g.setFont(new Font("Courier",Font.BOLD, 30));
				g.drawString("Marvin 1.2 - Plug-ins history", 5, 30);
				g.dispose();

				if(ImageIO.write(bufExport, "jpg", new File(arq)))
				{
					JOptionPane.showMessageDialog(frameHistory, STORE_SUCCESS);

				}else
				{
					JOptionPane.showMessageDialog(frameHistory, STORE_FAILED);
				}
				frameHistory.setVisible(false);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 *	Invoked when export button is performed.
	 */
	private class ExportButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == buttonExportHistortAsImage){
				exportAsImage();
			}
		}
	}
}


