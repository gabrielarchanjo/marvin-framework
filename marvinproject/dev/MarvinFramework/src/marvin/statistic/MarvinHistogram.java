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
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Histogram chart.
 * @version 02/13/08
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinHistogram
{
	private String description;
	private double maxX;
	private double maxY;
	private int barWidth;

	private Hashtable <Integer, MarvinHistogramEntry> hashEntries;
	private int[] arrPaintedColumns;
	
	public MarvinHistogram(String a_description){
		description = a_description;
		hashEntries = new Hashtable<Integer, MarvinHistogramEntry>();
		maxX = 0;
		maxY = 0;
	}

	public void setBarWidth(int barW){
		barWidth = barW;
	}

	public int getBarWidth(){
		return barWidth;
	}

	public void addEntry(MarvinHistogramEntry entry){
		hashEntries.put(entry.hashCode(), entry);

		if(entry.getValueX() > maxX){
			maxX = entry.getValueX();
		}
		if(entry.getValueY() > maxY){
			maxY = entry.getValueY();
		}
	}

	public void draw(int px, int py, int width, int height, Graphics g){
		// Fill white rect
		g.setColor(Color.white);
		g.fillRect(px,py,width, height);
		// write the description
		g.setColor(Color.black);
		g.drawString(description, (int)(width*0.05), py+12);
		
		// draw Histo
		drawHisto(px+(int)(width*0.05), py+(int)(height*0.1), (int)(width*0.95), (int)(height*0.8), g);

		//drawLines
		g.setColor(Color.black);
		g.drawLine(px+(int)(width*0.05), py+(int)(height*0.1), px+(int)(width*0.05),(int)(height*0.9));
		g.drawLine(px+(int)(width*0.05), (int)(height*0.9), width,(int)(height*0.9));
	}

	private void drawHisto(int px, int py, int width, int height, Graphics g){
		MarvinHistogramEntry l_entry;

		arrPaintedColumns = new int[width+1];
		for (Enumeration<MarvinHistogramEntry> e = hashEntries.elements(); e.hasMoreElements();){
			l_entry = e.nextElement();
			drawEntry(px+2,py,width-2,height,l_entry,g);			
		}

		for(int i=0; i<width; i++){
			if(arrPaintedColumns[i] == 0){
				if(i > 0){
					l_entry = hashEntries.get(arrPaintedColumns[i-1]);
					redrawEntry(px+2,i,py,height, l_entry,g);
				}
			}
		}
	}

	private void drawEntry(int px, int py, int width, int height, MarvinHistogramEntry entry, Graphics g){
		int l_ePx;
		int l_eHeight;

		if(entry.getColor() != null){
			g.setColor(entry.getColor());
		}
		else{
			g.setColor(Color.black);
		}

		l_ePx = (int)(width*(entry.getValueX()/maxX));
		l_eHeight = (int)(height*(entry.getValueY()/maxY));
		g.fillRect(px+l_ePx, (py+height)-l_eHeight, barWidth, l_eHeight);
		arrPaintedColumns[l_ePx] = entry.hashCode();
	}

	// used to resize the histogram
	private void redrawEntry(int pxHisto, int pxEntry, int py, int height, MarvinHistogramEntry entry, Graphics g){
		int l_eHeight;

		if(entry.getColor() != null){
			g.setColor(entry.getColor());
		}
		else{
			g.setColor(Color.black);
		}

		l_eHeight = (int)(height*(entry.getValueY()/maxY));
		g.fillRect(pxHisto+pxEntry, (py+height)-l_eHeight, barWidth, l_eHeight);
		arrPaintedColumns[pxEntry] = entry.hashCode();
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

};