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
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import net.marvinproject.framework.gui.MarvinPluginWindowComponent.ComponentType;
import net.marvinproject.framework.gui.component.MarvinMatrixPanel;
import net.marvinproject.framework.util.MarvinAttributes;

/**
 * Generic Window for plug-ins. This window supports integrating Swing components 
 * with MarvinAttributes. Components events are automatically handled.
 *
 * @version 1.0 02/13/08
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinPluginWindow extends JFrame
{
	// Components Collections
	//@protected Hashtable<String,MarvinPluginWindowComponent> hashComponents;
	
	//@protected Enumeration <MarvinPluginWindowComponent> enumComponents;

	// Interface
	protected GridLayout dynamicPanelLayout;
	protected Container container;
	protected JPanel	panelDynamicComponents, 
							panelCenter;
							//panelCurrent;
	
	//protected Box 		box;
	/**
	 * Constructs an {@link MarvinPluginWindow}
	 * @param name 		Window name
	 * @param width 	width
	 * @param height 	height
	 */
	public MarvinPluginWindow
	(
		String name, 
		int width, 
		int height,
		MarvinAttributesPanel attrPanel
	){
		super(name);
		
		// Parameters
		setSize(width,height);
		//setLocation(a_marvinApplication.getLocation());

		//@hashComponents = new Hashtable<String, MarvinPluginWindowComponent>(10,5);
		container = getContentPane();
		container.setLayout(new FlowLayout());
		
		// Dynamic Components
		panelCenter = new JPanel();
		panelCenter.setLayout(new FlowLayout());
		//@box = Box.createVerticalBox();
		
		panelDynamicComponents = new JPanel();
		panelDynamicComponents.setLayout(dynamicPanelLayout);
		
		if(attrPanel != null){
			panelCenter.add(attrPanel);
		}
		
		//Window Layout
		container.setLayout(new BorderLayout());
		container.add(panelCenter, BorderLayout.CENTER);
	}
	
//	/**
//	 * Adds new component Line
//	 */
//	public void newComponentRow(){
//		panelCurrent = new JPanel();
//		box.add(panelCurrent);
//	}
	
//	/**
//	 * This method is useful if the developer need to add an external component.
//	 * @return current panel.
//	 */
//	public JPanel getCurrentPanel(){
//		return panelCurrent;
//	}

//	/**
//	 * Adds new component
//	 * @param id
//	 * @param comp
//	 * @param attrID
//	 * @param attr
//	 * @param type
//	 */
//	public void plugComponent(String id, JComponent comp, String attrID, MarvinAttributes attr, ComponentType type){
//		panelCurrent.add(comp);
//		hashComponents.put(id, new MarvinPluginWindowComponent(id, attrID, attr, comp, type));
//	}
	
//	/**
//	 * Returns a component by its id.
//	 * @param MarvinPluginWindowComponent reference. 
//	 */	
//	public MarvinPluginWindowComponent getComponent(String compID){
//		return hashComponents.get(compID);
//	}
	
//	/**
//	 * Adds label
//	 * @param id	component id.
//	 * @param text	label text attribute.
//	 */
//	public void addLabel(String id, String text){
//		JComponent comp = new JLabel(text);
//		plugComponent(id, comp, null, null, ComponentType.COMPONENT_LABEL);
//	}
//	
//	/**
//	 * Adds image
//	 * @param id	component id.
//	 * @param img	image to be displayed.
//	 */
//	public void addImage(String id, BufferedImage img){
//		JComponent comp = new JLabel(new ImageIcon(img));
//		plugComponent(id, comp, null, null, ComponentType.COMPONENT_IMAGE);
//	}
//	
//	/**
//	 * Adds TextField
//	 * @param id			component id.
//	 * @param attrID		attribute id.
//	 * @param attr			MarivnAttributes Object.
//	 */
//	public void addTextField(String id, String attrID, MarvinAttributes attr)
//	{
//		JComponent comp = new JTextField(5);
//		((JTextField)(comp)).setText(attr.get(attrID).toString());
//		plugComponent(id, comp, attrID, attr, ComponentType.COMPONENT_TEXTFIELD);
//	}
//	
//	/**
//	 * 
//	 * @param id				- component id.
//	 * @param attrID		- attribute id.
//	 * @param lines			- number of lines.
//	 * @param columns			- number of columns.
//	 * @param attr		- MarivnAttributes Object.
//	 */
//	public void addTextArea(String id, String attrID, int lines, int columns, MarvinAttributes attr)
//	{
//		JComponent comp = new JTextArea(lines, columns);
//		JScrollPane scrollPanel = new JScrollPane(comp);
//		((JTextArea)(comp)).setText(attr.get(attrID).toString());
//		
//		// plug manually
//		panelCurrent.add(scrollPanel);
//		//box.add(l_scrollPane);
//		
//		hashComponents.put(id, new MarvinPluginWindowComponent(id, attrID, attr, comp, ComponentType.COMPONENT_TEXTAREA));
//		
//		//plugComponent(id, l_scrollPane, attrID, a_attributes, ComponentType.COMPONENT_TEXTAREA);
//	}
//	
//	/**
//	 * Add ComboBox
//	 * @param id			component´s id.
//	 * @param attrID		attribute id.
//	 * @param items			items array.
//	 * @param attr			MarvinAttributes object.
//	 */
//	public void addComboBox(String id, String attrID, Object[] items, MarvinAttributes attr){
//		JComponent comp = new JComboBox(items);
//		plugComponent(id, comp, attrID, attr, ComponentType.COMPONENT_COMBOBOX);
//	}
//	
//	/**
//	 * Add Slider
//	 * @param id			component id.
//	 * @param attrID		attribute id.
//	 * @param orientation	slider orientation
//	 * @param a_min			minimum value.
//	 * @param a_max			maximum value.
//	 * @param a_value		initial value.
//	 * @param attr			MarvinAttributes object
//	 */
//	protected void addSlider(String id, String attrID, int orientation, int a_min, int a_max, int a_value, MarvinAttributes attr){
//		JComponent comp = new JSlider(orientation, a_min, a_max, a_value);
//		plugComponent(id, comp, attrID, attr, ComponentType.COMPONENT_SLIDER);
//	}
//
//	/**
//	 * Add HorizontalSlider
//	 * @param id		component ID.
//	 * @param attrID	attribute ID.
//	 * @param min		minimum value.
//	 * @param max		maximum value.
//	 * @param value		initial value.
//	 * @param attr		MarvinAttributes object
//	 */
//	public void addHorizontalSlider(String id, String attrID, int min, int max, int value, MarvinAttributes attr){
//		addSlider(id, attrID, SwingConstants.HORIZONTAL, min, max, value, attr);
//	}
//	
//	/**
//	 * Add VerticalSlider
//	 * @param id		component ID
//	 * @param attrID	attribute ID
//	 * @param min		minimum value
//	 * @param max		maximum value
//	 * @param value		initial value
//	 * @param attr		MarvinAttributes object
//	 */
//	public void addVerticalSlider(String id, String attrID, int min, int max, int value, MarvinAttributes attr){
//		addSlider(id, attrID, SwingConstants.VERTICAL, min, max, value, attr);
//	}
//	
//	/**
//	 * Add CheckBox
//	 * @param id				component ID
//	 * @param cbText			CheckBox text attribute  
//	 * @param a_attirbuteID		attribute ID
//	 * @param attr				MarvinAttributes object
//	 */	
//	public void addCheckBox(String id, String cbText, String attrID,MarvinAttributes attr){
//		JComponent comp = new JCheckBox(cbText);
//		plugComponent(id, comp, attrID, attr, ComponentType.COMPONENT_CHECKBOX);
//	}
//	
//	/**
//	 * Add ButtonGroup
//	 */
//	public void addButtonGroup(String id, String attrID, MarvinAttributes attr){}
//
//	
//	/**
//	 * 
//	 * @param id component ID
//	 * @param attrID attribute ID
//	 * @param attr MarvinAttributes object
//	 * @param rows number of rows
//	 * @param columns number of columns
//	 */
//	public void addMatrixPanel(String id, String attrID, MarvinAttributes attr, int rows, int columns){
//		JComponent comp = new MarvinMatrixPanel(rows, columns);
//		plugComponent(id, comp, attrID, attr, ComponentType.COMPONENT_MATRIX_PANEL);
//	}
//	
//	/**
//	 * Update the attributes´ value based on the associated components.
//	 */
//	public void applyValues(){
//		MarvinPluginWindowComponent filterComp;
//		enumComponents = hashComponents.elements();
//		while(enumComponents.hasMoreElements()){
//			filterComp = enumComponents.nextElement();
//			if(filterComp.getAttributes() != null){
//				filterComp.getAttributes().set(filterComp.getAttributeID(), getValue(filterComp));
//			}
//		}
//	}
//
//	/**
//	 * Converts a string to the attribute type.
//	 * @param value		attribute´s value.
//	 * @param type		attribute´s type.
//	 * @return value as the specified type.
//	 */
//	public Object stringToType(String value, Object type){
//		Class<?> l_class = type.getClass();
//		if(l_class == Double.class){
//			return Double.parseDouble(value);
//		}
//		else if(l_class == Float.class){
//			return Float.parseFloat(value);
//		}
//		else if(l_class == Integer.class){
//			return Integer.parseInt(value);
//		}
//		else if(l_class == String.class){
//			return value.toString();
//		}
//		else if(l_class == Boolean.class){
//			return Boolean.parseBoolean(value);
//		}
//		return null;
//	}
//
//	/**
//	 * @param plgComp - graphical component;
//	 * @return the value associated with the specified component
//	 */
//	public Object getValue(MarvinPluginWindowComponent plgComp){
//		String l_id = plgComp.getAttributeID();
//		MarvinAttributes attr = plgComp.getAttributes();
//		JComponent comp = plgComp.getComponent();
//		
//		switch(plgComp.getType()){
//			case COMPONENT_TEXTFIELD:
//				return stringToType( ((JTextField)comp).getText(), attr.get(l_id));
//			case COMPONENT_COMBOBOX:
//				return ( ((JComboBox)comp).getSelectedItem());
//			case COMPONENT_SLIDER:
//				return ( ((JSlider)comp).getValue());
//			case COMPONENT_TEXTAREA:
//				return ( ((JTextArea)comp).getText());
//			case COMPONENT_CHECKBOX:
//				return ( ((JCheckBox)comp).isSelected());
//		}
//		return null;		
//	}
}
