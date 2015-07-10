/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package marvin.statistic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 * Bar Chart.
 * @version 02/13/08
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinBarChart
{
	// Constants
	public final static int ORIGINAL_BAR_COLOR = 0;
	public final static int SEQUENTIAL_BAR_COLOR = 1;

	private final static int BAR_REFERENCE_HEIGHT = 15;
	private final static int CHARACTER_WIDTH = 8;
	private final static int CHARACTER_HEIGHT = 15;


	private final static Color[] BAR_COLORS = {	Color.blue, Color.red, new Color(0,125,0), 
																Color.orange, Color.green, Color.pink, 
																Color.cyan, Color.yellow
															};

	private String description;
	private int barsColorType;
	private double maxValue;
	private double maxHeight;	

	private LinkedList<MarvinBarChartEntry>listEntries;

	public MarvinBarChart(String desc){
		description = desc;
		barsColorType = ORIGINAL_BAR_COLOR;
		maxValue = 0;
		maxHeight=0;
		listEntries = new LinkedList<MarvinBarChartEntry>();
	}

	public void addEntry(MarvinBarChartEntry entry){
		listEntries.add(entry);

		if(entry.getValue() > maxValue){
			maxValue = entry.getValue();
			maxHeight = entry.getValue()-CHARACTER_HEIGHT;			
		}
	}

	public void setBarsColor(int type){
		barsColorType = type;
	}

	private Color getBarColor(MarvinBarChartEntry entry, int barIndex){
		switch(barsColorType){
			case ORIGINAL_BAR_COLOR:
				return entry.getColor();
			case SEQUENTIAL_BAR_COLOR:
				return BAR_COLORS[barIndex%(BAR_COLORS.length)];
		}
		return null;
	}

	public void draw(int px, int py, int width, int height, Graphics graphics){
		int chartLeftDistance = ((""+maxValue).length()*CHARACTER_WIDTH)+5;

		// Fill white rect with the chart dimension
		graphics.setColor(Color.white);
		graphics.fillRect(px, py, width, height);
		// write the description
		graphics.setColor(Color.black);
		graphics.drawString(description, chartLeftDistance, 12);
		// draw chart
		drawChart(px+(int)(chartLeftDistance), py+(int)(height*0.1), (int)(width-chartLeftDistance), (int)(height*0.60), graphics);
		drawBarReference(px+(int)(chartLeftDistance), (int)(height*0.75), (int)(width-chartLeftDistance), (int)(height*0.25), graphics);
		drawIntervals(px+(int)(chartLeftDistance), py+(int)(height*0.1), (int)(width-chartLeftDistance), (int)(height*0.60), graphics);	
	}

	private void drawChart(int px, int py, int width, int height, Graphics g){
		Object[] arrEntries;
		MarvinBarChartEntry entry; 
		int l_px, l_py;
		int l_height;
		int l_numEntries;
		int barWidth;
		int barDistance;
		
		// draw chart Lines
		g.setColor(Color.black);
		g.drawLine(px, py+height,px+width, py+height);
		g.drawLine(px, py, px, py+height);

		arrEntries = listEntries.toArray();
		l_numEntries = listEntries.size();
		// Chart design Attributes		
		barWidth = (int) ((width*0.7)/l_numEntries);
		barDistance = (int) ((width*0.3)/(l_numEntries+1));

		for(int i=0; i<l_numEntries; i++){
			entry = (MarvinBarChartEntry)arrEntries[i];

			g.setColor(getBarColor(entry,i));

			l_height = (int)(height*(entry.getValue()/maxHeight));
			if(l_height == 0 && entry.getValue() > 0){
				l_height = 1;
			}

			l_px = px+(barDistance+((barDistance+barWidth)*i));
			l_py = py+(height-l_height);
			// render bar
			g.fillRect(l_px, l_py, barWidth, l_height);
			g.setColor(Color.black);
		}
	}

	private void drawBarReference(int px, int py, int width, int height, Graphics g){
		Object[] arrEntries;
		MarvinBarChartEntry entry;
		int l_numEntries;
		int l_px, l_py;
		int l_barReferenceWidth;
		int l_barReferenceStringLength;

		l_py = py;
		l_px = px;
		l_barReferenceWidth = width/3;
		l_barReferenceStringLength = (l_barReferenceWidth-12)/CHARACTER_WIDTH;
		arrEntries = listEntries.toArray();
		l_numEntries = listEntries.size();
		for(int i=0; i<l_numEntries; i++){
			entry = (MarvinBarChartEntry)arrEntries[i];

			l_px=px+(i*l_barReferenceWidth)%width;
			l_py=py+(((i*l_barReferenceWidth)/width)*BAR_REFERENCE_HEIGHT);
			
			g.setColor(getBarColor(entry,i));
			g.fillRect(l_px, l_py, 10,10);

			g.setColor(Color.black);

			if(entry.getName().length() > l_barReferenceStringLength){
				g.drawString(entry.getName().substring(0,l_barReferenceStringLength)+"." , l_px+12,l_py+10);
			}
			else{
				g.drawString(entry.getName(), l_px+12,l_py+10);
			}
		}
	}

	/*
		Draw from px to px-(width of the value as String)
	*/
	private void drawIntervals(int px, int py, int width, int height, Graphics g){
		int intervalMaxValue = (int)(maxValue/10)*10;
		double numIntervals = (height)/(CHARACTER_HEIGHT*2);
		double intervalHeight = (height/numIntervals);
		int intervalValue = (int)(intervalMaxValue/numIntervals);
		int l_value;
		int l_py;

		l_value = intervalMaxValue;
		g.setColor(Color.black);
		for(int i=0; i<numIntervals+1; i++){

			if(i == numIntervals){
				l_py = py+height;
				l_value = 0;
			}
			else{
				l_py = py+(int)(i*intervalHeight);
			}

			g.drawLine(px, l_py, px-5, l_py);
			g.drawString((""+l_value), px-5-((""+l_value).length()*CHARACTER_WIDTH), l_py);
			l_value = l_value - intervalValue;
		}
	}

	public BufferedImage getImage(int width, int height){
		BufferedImage l_buf;
		Graphics2D l_g2d;
		l_buf = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
		l_g2d = (Graphics2D) l_buf.getGraphics();
		l_g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		draw(0,0,width,height,l_g2d);
		return l_buf;
	}
}