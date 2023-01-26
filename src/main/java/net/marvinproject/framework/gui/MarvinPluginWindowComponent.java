/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package net.marvinproject.framework.gui;

import javax.swing.JComponent;

import net.marvinproject.framework.util.MarvinAttributes;

/**
 * Generic component for PluginWindow.
 * @version 02/13/08
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinPluginWindowComponent
{
	
	public enum ComponentType{
		COMPONENT_TEXTFIELD, COMPONENT_SLIDER, COMPONENT_COMBOBOX, 
		COMPONENT_LABEL, COMPONENT_IMAGE, COMPONENT_TEXTAREA, COMPONENT_CHECKBOX,
		COMPONENT_MATRIX_PANEL
	};
	
	protected String id;
	protected String attributeID;
	protected MarvinAttributes attributes;
	protected JComponent component;
	ComponentType type;
	
	/**
	 * Constructs a new {@link MarvinPluginWindowComponent}
	 * @param a_id
	 * @param attrID 
	 * @param attr {@link MarvinAttributes}
	 * @param comp {@link JComponent}
	 * @param type
	 */
	public MarvinPluginWindowComponent(String id, String attrID, MarvinAttributes attr, JComponent comp, ComponentType type){
		this.id = id;
		attributeID = attrID;
		attributes = attr;
		component = comp;
		this.type = type;
	}

	/**
	 * Returns the components ID.
	 * @return the components ID
	 */
	public String getID(){
		return id;
	}

	/**
	 * Returns the ID of the attribute associated with the component.
	 * @return attributes ID.
	 */
	public String getAttributeID(){
		return attributeID;
	}

	/**
	 * Returns Atribute objects reference.
	 * @return MarvinAttribute reference.
	 */
	public MarvinAttributes getAttributes(){
		return attributes;
	}

	/**
	 * Returns the swing component representation of this component.
	 * @return the Swing component
	 */
	public JComponent getComponent(){
		return component;
	}

	/**
	 * Returns the type of the component.
	 * @return the components type.
	 */
	public ComponentType getType(){
		return type;
	}
}