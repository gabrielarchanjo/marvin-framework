/**
Marvin Project <2007-2017>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
@author Anshul Katta
*/
package net.marvinproject.framework.util;

import java.awt.image.BufferedImage;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter.ToIplImage;

public class ConverterUtil { 
	
	public static BufferedImage IplImageToBufferedImage(IplImage src) {
		OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
		Java2DFrameConverter paintConverter = new Java2DFrameConverter();
		Frame frame = grabberConverter.convert(src);
		return paintConverter.getBufferedImage(frame, 1);
	}
	
	public static IplImage bufferedToIplImage(BufferedImage bufImage) {

		ToIplImage iplConverter = new OpenCVFrameConverter.ToIplImage();
		Java2DFrameConverter java2dConverter = new Java2DFrameConverter();
		IplImage iplImage = iplConverter.convert(java2dConverter
				.convert(bufImage));
		return iplImage;
	}
	
	public static BufferedImage frametoBufferedImage(Frame frame) {
		Java2DFrameConverter paintConverter = new Java2DFrameConverter();
		return paintConverter.getBufferedImage(frame, 1);
	}
	
	public static IplImage frametoIplImage(Frame frame) {
		OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
		return converter.convert(frame);
	}

}
