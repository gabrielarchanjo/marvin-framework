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

/**
 * Histogram entry.
 * @version 02/13/08
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinHistogramEntry
{
	protected String name;
	protected double valueX;
	protected double valueY;
	protected Color color;

	public MarvinHistogramEntry(){
		this("",0,0,Color.black);
	}

	public MarvinHistogramEntry(double valueX, double valueY){
		this("", valueX, valueY, Color.black);
	}

	public MarvinHistogramEntry(double valueX, double valueY, Color a_color){
		this("", valueX, valueY, a_color);
	}

	public MarvinHistogramEntry(String a_name, double valueX, double valueY){
		this(a_name, valueX, valueY, Color.black);
	}

	public MarvinHistogramEntry(String a_name, double valueX, double valueY, Color a_color){
		name = a_name;
		this.valueX = valueX;
		this.valueY = valueY;
		color = a_color;
	}

	public String getName(){
		return name;
	}

	public double getValueX(){
		return valueX;
	}

	public double getValueY(){
		return valueY;
	}

	public Color getColor(){
		return color;
	}
};