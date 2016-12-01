/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package marvin.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.event.IIOReadWarningListener;
import javax.imageio.stream.ImageInputStream;

import marvin.image.MarvinImage;
import marvin.util.MarvinErrorHandler;

/**
 * Methods to load and save images.
 * @author Danilo Roseto Munoz
 * @author Fabio Andrijaukas
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinImageIO {
	
	/**
	 * Loads a MarvinImage from a filesystem path.
	 * @param a_filePath	- image´s path
	 * @return
	 */
	public static MarvinImage loadImage(String a_filePath){
		MarvinImage l_marvinImage = null;
		BufferedImage l_bufferedImage=null;
		ImageInputStream l_imageInputStream = null;
		
		//1. Load File
		File l_file = new File(a_filePath);
		
		if(!l_file.exists()){
			throw MarvinErrorHandler.handle(MarvinErrorHandler.TYPE.ERROR_FILE_NOT_FOUND,a_filePath);
		}
		
		// 2. Create ImageReader
		Iterator<?> l_ittReaders = ImageIO.getImageReadersByFormatName(a_filePath.substring(a_filePath.lastIndexOf(".") + 1));
		ImageReader l_reader = (ImageReader) l_ittReaders.next();
		
		l_reader.addIIOReadWarningListener(new IIOReadWarningListener() {
			public void warningOccurred(ImageReader source, String warning) {
				MarvinErrorHandler.handleDialog(MarvinErrorHandler.TYPE.BAD_FILE, warning);
			}
		});		
		
		// 3. Load image
		try{
			l_imageInputStream = ImageIO.createImageInputStream(l_file);
			l_reader.setInput(l_imageInputStream);
			l_bufferedImage = l_reader.read(0);
		}catch(Exception e){
			throw MarvinErrorHandler.handle(MarvinErrorHandler.TYPE.ERROR_FILE_OPEN, a_filePath, e);
			
			//MarvinErrorHandler.handle(, e);
			//return null;
		} finally{
			if(l_imageInputStream != null){
				try{	l_imageInputStream.close();	}catch(Exception e){/*nothing to do */};
			}
		}
		
		// 4. Get format
		String l_format = "";
		try{
			l_format = l_reader.getFormatName();
		} catch(IOException e){
			e.printStackTrace();
			return null;
		}
		
		// 5. Create MarvinImage object
		l_marvinImage = new MarvinImage(l_bufferedImage, l_format);
		return l_marvinImage;
	}
	
	/**
	 * Saves a MarvinImage via file system path. 
	 * @param marvinImage	- MarvinImage object
	 * @param filePath	- file path
	 */
	public static void saveImage(MarvinImage marvinImage, String filePath){
		marvinImage.update();
		File l_file = new File(filePath);
		
		try{
			if(filePath.toUpperCase().endsWith(".JPEG") || filePath.toUpperCase().endsWith(".JPG") ){
				ImageIO.write(marvinImage.getBufferedImageNoAlpha(),  "JPEG", l_file);
			}
			else{
				ImageIO.write(marvinImage.getBufferedImage(),  filePath.substring(filePath.lastIndexOf('.')+1), l_file);
			}
			
		} catch(Exception e){
			throw MarvinErrorHandler.handle(MarvinErrorHandler.TYPE.ERROR_FILE_SAVE, filePath, e);
		}
	}
}
