/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package pluginTester;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinImagePlugin;
import marvin.plugin.MarvinPlugin;

import org.marvinproject.image.artistic.mosaic.Mosaic;
import org.marvinproject.image.artistic.television.Television;
import org.marvinproject.image.blur.gaussianBlur.GaussianBlur;
import org.marvinproject.image.blur.pixelize.Pixelize;
import org.marvinproject.image.color.brightnessAndContrast.BrightnessAndContrast;
import org.marvinproject.image.color.grayScale.GrayScale;
import org.marvinproject.image.color.invert.Invert;
import org.marvinproject.image.color.sepia.Sepia;
import org.marvinproject.image.color.thresholding.Thresholding;
import org.marvinproject.image.edge.edgeDetector.EdgeDetector;
import org.marvinproject.image.halftone.circles.Circles;
import org.marvinproject.image.halftone.dithering.Dithering;
import org.marvinproject.image.halftone.errorDiffusion.ErrorDiffusion;
import org.marvinproject.image.halftone.rylanders.Rylanders;
import org.marvinproject.image.statistical.maximum.Maximum;
import org.marvinproject.image.statistical.median.Median;
import org.marvinproject.image.statistical.minimum.Minimum;
import org.marvinproject.image.statistical.mode.Mode;

public class Benchmark {

	private final static int NUM_EXECUTIONS = 5;
	
	List <MarvinPlugin>plugins;
	
	public Benchmark(){
		loadPlugins();
	}
	
	private void loadPlugins(){
		plugins = new ArrayList<MarvinPlugin>();
		
		plugins.add(new Mosaic());
		plugins.add(new Television());
		plugins.add(new Mosaic());
		plugins.add(new GaussianBlur());
		plugins.add(new Pixelize());
		plugins.add(new BrightnessAndContrast());
		plugins.add(new GrayScale());
		plugins.add(new Invert());
		plugins.add(new Sepia());
		plugins.add(new Thresholding());
		plugins.add(new EdgeDetector());
		plugins.add(new Circles());
		plugins.add(new Dithering());
		plugins.add(new ErrorDiffusion());
		plugins.add(new Rylanders());
		//plugins.add(new HarrisPlessey());
		plugins.add(new Maximum());
		plugins.add(new Median());
		plugins.add(new Minimum());
		plugins.add(new Mode());
	}
	
	public void process(MarvinImage image){
		JFrame result = new JFrame("BenchMark");
		JTextArea text = new JTextArea();
		result.add(text);
		
		MarvinImagePlugin tempPlugin;
		MarvinImage imageOut;
		long time, totalTime;
		
		
		text.append(""+new Date(System.currentTimeMillis()).toString());
		text.append("\nimage width:"+image.getWidth());
		text.append("\nimage height:"+image.getHeight());
		text.append("\nNUM_EXECUTIONS:"+NUM_EXECUTIONS);
		text.append("\n\n");
		
		long timeByPlugin[][] = new long[plugins.size()][];
		for(int i=0; i<plugins.size(); i++){
			timeByPlugin[i] = new long[NUM_EXECUTIONS];
		}
		
		totalTime = System.currentTimeMillis();
		for(int i=0; i<plugins.size(); i++){
			for(int e=0; e<NUM_EXECUTIONS; e++){
				tempPlugin = (MarvinImagePlugin)plugins.get(i);
				tempPlugin.load();
				imageOut = new MarvinImage(image.getWidth(), image.getHeight());
		
				time = System.currentTimeMillis();
				tempPlugin.process(image, imageOut, null, MarvinImageMask.NULL_MASK, false);
				timeByPlugin[i][e] = (System.currentTimeMillis()-time);
				System.out.print(".");
			}
			
		}
		System.out.println("FINISHED");
		
		long avgTime;
		for(int i=0; i<plugins.size(); i++){
			avgTime=0;
			for(int e=0; e<NUM_EXECUTIONS; e++){
				avgTime += timeByPlugin[i][e];
			}
			avgTime /= NUM_EXECUTIONS;
			text.append("\nPlugin:"+plugins.get(i).getClass().getSimpleName()+" - Avarage time:"+avgTime);
		}
		
		text.append("\n\n total time:"+(System.currentTimeMillis()-totalTime));
		
		result.setSize(600,500);
		result.setVisible(true);
	}
}