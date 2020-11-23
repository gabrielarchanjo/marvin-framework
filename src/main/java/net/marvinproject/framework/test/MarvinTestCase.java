/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package net.marvinproject.framework.test;

import java.util.Arrays;

import junit.framework.TestCase;
import net.marvinproject.framework.image.MarvinImage;
import net.marvinproject.framework.util.MarvinAttributes;



public class MarvinTestCase extends TestCase {

	// Errors
	private final static String IMAGES_NOT_EQUAL 		= "The images are different and should be equal.";
	private final static String IMAGES_NOT_SIMILAR 		= "The images are not similar.";
	private final static String ATTRIBUTES_NOT_EQUAL 	= "The attributes are different and should be equal.";
	
	/**
	 * Check if two images are equal.
	 * @param imageA image to be compared.
	 * @param imageB image to be compared.
	 */
	public void assertEquals(MarvinImage imageA, MarvinImage imageB){
		if(imageA.getWidth() != imageB.getWidth() || imageA.getHeight() != imageB.getHeight()){
			fail(IMAGES_NOT_EQUAL);
		}
		
		for(int y=0; y<imageA.getHeight(); y++){
			for(int x=0; x<imageA.getWidth(); x++){
				if(imageA.getIntColor(x, y) != imageB.getIntColor(x, y)){
					fail(IMAGES_NOT_EQUAL);
				}
			}
		}
	}
	
	
	/**
	 * Check if two attributes are equal.
	 * @param attrA MarvinAttribute to be compared. 
	 * @param attrB MarvinAttribute to be compared.
	 */
	public void assertEquals(MarvinAttributes attrA, MarvinAttributes attrB){
		if(!Arrays.equals(attrA.getValues(),attrB.getValues())){
			fail(ATTRIBUTES_NOT_EQUAL);
		}
	}
	
	public void assertSimilar(MarvinImage imageA, MarvinImage imageB){
		if(imageA.getWidth() != imageB.getWidth() || imageA.getHeight() != imageB.getHeight()){
			fail(IMAGES_NOT_EQUAL);
		}

		// TODO
	}
}
