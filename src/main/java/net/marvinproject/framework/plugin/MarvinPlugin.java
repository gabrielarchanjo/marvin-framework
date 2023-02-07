/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package net.marvinproject.framework.plugin;

import net.marvinproject.framework.util.MarvinAttributes;

/**
 * Generic Marvin Plug-in. All application plug-ins must be implement this interface. Currently,
 * it's empty because Marvin suports only one type of plug-in. Nevertheless, different types of
 * plug-ins are expected.
 * @version 1.0 02/13/08
 */
public interface MarvinPlugin{
	
	public void load();
	
	/**
	 * Ensures that this plug-in is working consistently to its attributes. 
	 */
	public void validate();
	
	/**
	 * Invalidate this plug-in. It means that the attributes were changed and the plug-in needs to check whether
	 * or not change its behavior. 
	 */
	public void invalidate();
	
	/**
	 * Determines whether this plug-in is valid. A plug-in is valid when it is correctly configured given a set
	 * of attributes. When an attribute is changed, the plug-in becomes invalid until the method validate() is
	 * called. 
	 * @return
	 */
	public boolean isValid();
	
	/**
	 * @return MarvinAttributes object associated with this plug-in
	 */
	public MarvinAttributes getAttributes();
	
	/**
	 * Set an attribute
	 * @param attrName	attribute name
	 * @param value		attribute value
	 **/
	public void setAttribute(String attrName, Object value);
	
	/**
	 * Set a list of attributes. Format: (String)name, (Object)value...
	 **/
	public void setAttributes(Object... params);
	
	/**
	 * @param attrName		attribute name
	 * @return the attributes value
	 */
	public Object getAttribute(String attrName);
	
}
