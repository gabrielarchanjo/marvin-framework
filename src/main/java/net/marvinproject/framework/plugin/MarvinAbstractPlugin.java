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

public abstract class MarvinAbstractPlugin implements MarvinPlugin{

	private MarvinAttributes marvinAttributes;
	
	private boolean valid;
	
	protected MarvinAbstractPlugin(){
		marvinAttributes = new MarvinAttributes(this);
	}
	
	/**
	 * Ensures that this plug-in is working consistently to its attributes. 
	 */
	public void validate(){
		valid = true;
	}
	
	/**
	 * Invalidate this plug-in. It means that the attributes were changed and the plug-in needs to check whether
	 * or not change its behavior. 
	 */
	public void invalidate(){
		valid = false;
	}
	
	/**
	 * Determines whether this plug-in is valid. A plug-in is valid when it is correctly configured given a set
	 * of attributes. When an attribute is changed, the plug-in becomes invalid until the method validate() is
	 * called. 
	 * @return
	 */
	public boolean isValid(){
		return valid;
	}
	

	/**
	 * @return MarvinAttributes object associated with this plug-in
	 */
	public MarvinAttributes getAttributes(){
		return marvinAttributes;
	}
	
	/**
	 * Set an attribute
	 * @param a_attrName	attributes name
	 * @param value			attributes value
	 **/
	public void setAttribute(String label, Object value){
		marvinAttributes.set(label, value);
	}
	
	/**
	 * Set a list of attributes. Format: (String)name, (Object)value...
	 */
	public void setAttributes(Object... params){
		marvinAttributes.set(params);
	}
	
	/**
	 * @param a_attrName	attributes name
	 * @return the attributes value
	 */
	public Object getAttribute(String label){
		return marvinAttributes.get(label);
	}
}
