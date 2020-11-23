/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package net.marvinproject.framework.video;

import net.marvinproject.framework.image.MarvinImage;

public interface MarvinVideoInterface {

	// Connection
	void connect(int deviceIndex) 							throws MarvinVideoInterfaceException;
	void connect(int deviceIndex, int width, int height) 	throws MarvinVideoInterfaceException;
	void loadResource(String path) 							throws MarvinVideoInterfaceException;
	void disconnect() 										throws MarvinVideoInterfaceException;
	
	// Video file
	int getFrameNumber();
	void setFrameNumber(int number)							throws MarvinVideoInterfaceException;
	
	// Image Width / Height
	int getImageWidth();
	int getImageHeight();
	
	// Frame request
	MarvinImage getFrame() 									throws MarvinVideoInterfaceException;
}
