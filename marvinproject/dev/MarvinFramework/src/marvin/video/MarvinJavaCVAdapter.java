/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package marvin.video;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

import marvin.image.MarvinImage;

import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.VideoInputFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class MarvinJavaCVAdapter implements MarvinVideoInterface{
	
	//OpenCVFrameConverter.ToIplImage converterToIpl = new OpenCVFrameConverter.ToIplImage();
	//Java2DFrameConverter paintConverter = new Java2DFrameConverter();
	
	private FrameGrabber	grabber;
	private IplImage		image;
	//Mat						image;
	private int				width;
	private int				height;
	private boolean			connected;
	private int[]			intArray;
	private MarvinImage 	marvinImage;
	
	private enum MODE{DEVICE, FILE};
	
	private MODE			mode;
	
	@Override
	public void connect(int deviceIndex) throws MarvinVideoInterfaceException {
		connect(deviceIndex, 640,480);
	}
	
	@Override
	public void connect(int deviceIndex, int width, int height) throws MarvinVideoInterfaceException {
		mode = MODE.DEVICE;
		this.width = width;
		this.height = height;
		marvinImage = new MarvinImage(width, height);
		intArray = new int[height*width*4];
		
		
		try{
			grabber= new VideoInputFrameGrabber(deviceIndex);
			grabber.setImageWidth(width);
			grabber.setImageHeight(height);
			grabber.start();
			grabber.grab();
			connected = true;
		}
		catch(Exception e){
			throw new MarvinVideoInterfaceException("Error while trying to connect to the device", e);
		}
	}
	
	
	@Override
	public void loadResource(String path) throws MarvinVideoInterfaceException{
		mode = MODE.FILE;
		try{
			grabber= OpenCVFrameGrabber.createDefault(new File(path));
			//grabber = new OpenCVFrameGrabber(new File(path));
			//grabber = new FFmpegFrameGrabber(new File(path));
			//grabber.setImageWidth(width);
			//grabber.setImageHeight(height);
			grabber.start();
			BufferedImage bufImage = grabber.grab().getBufferedImage();
			//image = converterToIpl.convert(grabber.grab());
			this.width = bufImage.getWidth();
			this.height = bufImage.getHeight();
			//this.width = image.width();
			//this.height = image.height();
			marvinImage = new MarvinImage(width, height);
			intArray = new int[height*width*4];
			connected = true;
		}
		catch(Exception e){
			throw new MarvinVideoInterfaceException("Error while trying to load resource", e);
		}
	}
	
	@Override
	public void disconnect() throws MarvinVideoInterfaceException {
		try{
			grabber.stop();
			connected = false;
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public int getImageWidth(){
		return this.width;
	}
	
	@Override
	public int getImageHeight(){
		return this.height;
	}
	
	int frame=1;
	@Override
	
	public MarvinImage getFrame() throws MarvinVideoInterfaceException{
		
		if(connected){
			image=null;
			
			try
			{
				if(mode == MODE.DEVICE || grabber.getFrameNumber() < grabber.getLengthInFrames()-1){
					 image=grabber.grab();
					 convertToIntArray(image, intArray);
					 marvinImage.setIntColorArray(intArray);
					 return marvinImage;
				}
			}
			catch(Exception e){
				throw new MarvinVideoInterfaceException("Error while trying to grab a new frame", e);
			}
		}
		return null;
	}
	
	private void convertToIntArray(IplImage img, int[] arr){
		ByteBuffer buffer = img.getByteBuffer();
		for(int ii=0, bi=0; bi<buffer.limit()-3; ii++, bi+=3){
			arr[ii] = 0xFF000000 + (buffer.get(bi+2) << 16) + (buffer.get(bi+1) << 8) + buffer.get(bi);
		}
	}
	

	@Override
	public int getFrameNumber() {
		if(connected){
			return grabber.getFrameNumber();
		}
		return -1;
	}

	@Override
	public void setFrameNumber(int number) throws MarvinVideoInterfaceException {
		if(connected){
			try{
				grabber.setFrameNumber(number);
			}
			catch(Exception e){
				throw new MarvinVideoInterfaceException("Error while setting frame number", e);
			}
		}
	}
	
	
}
