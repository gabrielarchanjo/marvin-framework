/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.render.lindenmayer;

import java.util.HashMap;

/**
 * @author Gabriel Ambrósio Archanjo
 */
public class Grammar {
	HashMap<String, String> rules;
	
	public Grammar(){
		rules = new HashMap<String, String>();
	}
	
	public void addRule(String predecessor, String sucessor){
		rules.put(predecessor, sucessor);
	}
	
	public String derive(String text){
		StringBuffer result = new StringBuffer();
		String temp;
		for(int i=0; i<text.length(); i++){
			temp = rules.get(""+text.charAt(i));
			if(temp == null){
				result.append(text.charAt(i)); 
			}
			else{
				result.append(temp);
			}
		}
		return result.toString();
	}
}
