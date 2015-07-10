package image.unitTesting;

import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import marvin.test.MarvinTestCase;

public class UnitTesting extends MarvinTestCase {

	private MarvinImage imageA, imageB, imageC;
	
	public UnitTesting(){
		imageA = MarvinImageIO.loadImage("./res/penguin.jpg");
		imageB = MarvinImageIO.loadImage("./res/penguin.jpg");
		imageC = MarvinImageIO.loadImage("./res/penguin_dark.jpg");
	}
	
	public void testSameImage(){
		assertEquals(imageA, imageB);
	}
	
	public void testDistinctImages(){
		assertEquals(imageA, imageC);
	}
}
