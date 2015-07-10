/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.render.text;

import java.util.HashMap;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

public class Text extends MarvinAbstractImagePlugin{

	private MarvinAttributes attributes;
	private HashMap<Character, CharacterDescription> chars;
	
	@Override
	public void load() {
		
		attributes = getAttributes();
		attributes.set("x", 0);
		attributes.set("y", 0);
		attributes.set("color", 0);
		attributes.set("text", "");
		
		
		
		// Character description
		chars = new HashMap<Character, CharacterDescription>();
		
		chars.put('A', new CharacterDescription(0,2,19,17));
		chars.put('B', new CharacterDescription(24,2,17,17));
		chars.put('C', new CharacterDescription(47,2,17,17));
		chars.put('D', new CharacterDescription(70,2,17,17));
		chars.put('E', new CharacterDescription(93,2,16,17));
		chars.put('F', new CharacterDescription(115,2,15,17));
		chars.put('G', new CharacterDescription(136,2,18,17));
		chars.put('H', new CharacterDescription(161,2,18,17));
		chars.put('I', new CharacterDescription(186,2,8,17));
		chars.put('J', new CharacterDescription(200,2,15,17));
		chars.put('K', new CharacterDescription(222,2,20,17));
		chars.put('L', new CharacterDescription(247,2,15,17));
		chars.put('M', new CharacterDescription(268,2,17,17));
		chars.put('N', new CharacterDescription(295,2,18,17));
		chars.put('O', new CharacterDescription(320,2,18,17));
		chars.put('P', new CharacterDescription(345,2,16,17));
		chars.put('Q', new CharacterDescription(367,2,19,17));
		chars.put('R', new CharacterDescription(392,2,17,17));
		chars.put('S', new CharacterDescription(414,2,17,17));
		chars.put('T', new CharacterDescription(436,2,17,17));
		chars.put('U', new CharacterDescription(459,2,18,17));
		chars.put('V', new CharacterDescription(483,2,18,17));
		chars.put('W', new CharacterDescription(507,2,22,17));
		chars.put('X', new CharacterDescription(534,2,19,17));
		chars.put('Y', new CharacterDescription(557,2,19,17));
		chars.put('Z', new CharacterDescription(580,2,17,17));
		chars.put('0', new CharacterDescription(602,2,16,17));
		chars.put('1', new CharacterDescription(624,2,12,17));
		chars.put('2', new CharacterDescription(644,2,16,17));
		chars.put('3', new CharacterDescription(665,2,16,17));
		chars.put('4', new CharacterDescription(686,2,16,17));
		chars.put('5', new CharacterDescription(707,2,16,17));
		chars.put('6', new CharacterDescription(728,2,16,17));
		chars.put('7', new CharacterDescription(749,2,16,17));
		chars.put('8', new CharacterDescription(770,2,16,17));
		chars.put('9', new CharacterDescription(791,2,16,17));
		chars.put(':', new CharacterDescription(813,2,8,17));
	}

	@Override
	public void process
	(
		MarvinImage imageIn, 
		MarvinImage imageOut, 
		MarvinAttributes attrOut,
		MarvinImageMask mask, 
		boolean mode
	)
	{
		MarvinImage font = (MarvinImage)attributes.get("fontFile");
		String text = (String)attributes.get("text");
		int x = (Integer)attributes.get("x");
		int y = (Integer)attributes.get("y");
		int color = (Integer)attributes.get("color");
		CharacterDescription charDesc;
		
		for(int i=0; i<text.length(); i++){
			charDesc = chars.get(text.charAt(i));
			drawChar(font, x, y, color, charDesc, imageOut);
			x+= charDesc.width+3;
		}
	}
	
	private void drawChar(MarvinImage font, int x, int y, int color, CharacterDescription charDesc, MarvinImage imageOut){
		int fontColor;
		for(int j=0; j<charDesc.height; j++){
			for(int i=0; i<charDesc.width; i++){	
				fontColor = font.getIntColor(i+charDesc.x, j+charDesc.y);
			
				if(fontColor == 0xFF000000){
					imageOut.setIntColor(x+i, y+j, color);
				}
			}
		}
	}

	@Override
	public MarvinAttributesPanel getAttributesPanel(){ return null; }
	
	
}

class CharacterDescription{
	public int x,y,width,height;
	
	public CharacterDescription(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
}
