/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.render.lindenmayer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.Stack;

import marvin.image.MarvinImage;

/**
 * @author Gabriel Ambrósio Archanjo
 */
public class TurtleGraphics {

	private double walkDistance;
	private double rotationAngle;
	private int startPx;
	private int startPy;
	private double startAngle;
	
	// Current Turtle State
	private double currentPx;
	private double currentPy;
	private double currentAngle;
	
	// State Stack
	private Stack<Double[]> stateStack;
	
	private Polygon polygon = new Polygon();
	private boolean drawingPolygon = false;
	
	public TurtleGraphics(){
		stateStack = new Stack<Double[]>();		
	}
	
	private void reset(){
		currentPx = 0;
		currentPy = 0;
		currentAngle = startAngle;
		stateStack.clear();
	}
	
	public void setStartPosition(int x, int y, double angle){
		startPx = x;
		startPy = y;
		startAngle = angle;
	}
	
	public void setWalkDistance(double distance){
		walkDistance = distance;
	}
	
	public void setRotationAngle(double angle){
		rotationAngle = angle;
	}
	
	public void render(String text, Grammar grammar, int iterations, MarvinImage image){
		
		reset();
		
		for(int i=0; i<iterations; i++){	
			text = grammar.derive(text);						
		}
		setStartPosition(0,0,startAngle);
		walkDistance = 1;
		walk(text, image, false);
		reset();
		walk(text, image, true);
	}
	
	private void walk(String text, MarvinImage image, boolean draw){
		double newPx;
		double newPy;
		
		double minX=999999999,minY=999999999,maxX=-999999999,maxY=-99999999;
		
		
		for(int i=0; i<text.length(); i++){
			switch(text.charAt(i)){
				case 'F':
				case 'G':
				case 'H':
				case 'I':
				case 'J':
				case 'K':
				case 'L':
				case 'M':
				{
					newPx = currentPx+Math.cos(Math.toRadians(currentAngle))*walkDistance;
					newPy = currentPy-Math.sin(Math.toRadians(currentAngle))*walkDistance;
					
					if(!drawingPolygon){
						if(draw){
							drawLine((int)currentPx+startPx, (int)currentPy+startPy, (int)newPx+startPx, (int)newPy+startPy, image);
						}
						else{
							if(currentPx < minX){	minX = currentPx;	};
							if(currentPx > maxX){	maxX = currentPx;	};
							if(currentPy < minY){	minY = currentPy;	};
							if(currentPy > maxY){	maxY = currentPy;	};
						}
					}
					else{
						polygon.addPoint(((int)newPx+startPx), (int)(newPy+startPy));
						
					}
					
					currentPx = newPx;
					currentPy = newPy;
					break;
				}
				case 'g':
				case 'h':
				case 'j':
				case 'k':
				case 'l':
				{
					// do nothing
				}					
				case '+':
				{
					currentAngle -= rotationAngle;
					if(currentAngle < 0){
						currentAngle = 360+currentAngle;
					}
					break;
				}	
				case '-':
				{
					currentAngle = (currentAngle + rotationAngle)%360;
					break;
				}
				case '[':
				{
					stateStack.push(new Double[]{currentPx, currentPy, currentAngle});
					break;
				}
				case ']':
				{
					Double o[] = stateStack.pop();
					currentPx = o[0];
					currentPy = o[1];
					currentAngle = o[2];
					break;
				}
				case '{':
					polygon = new Polygon();
					drawingPolygon = true;
					break;
				case '}':
					//g.fillPolygon(p);
					drawingPolygon = false;
					//g.setColor(Color.black);
					//System.out.println("black");
					break;
				case '#':
					if(text.charAt(i+1) == '0'){
						//g.setColor(Color.black);
					}
					else if(text.charAt(i+1) == '1'){
						//g.setColor(new Color(0,100,0));
					}
					else if(text.charAt(i+1) == '2'){
						//g.setColor(Color.red);
					}
					else if(text.charAt(i+1) == '3'){
						//g.setColor(Color.yellow);
					}
					else if(text.charAt(i+1) == '4'){
						//g.setColor(Color.blue);
					}
					i++;
					break;
				case '&':
					//g.setColor(new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255)));
					i++;
					break;
			}
		}
		
		int width = image.getWidth();
		int height = image.getHeight();
		
		if(currentPx < minX){	minX = currentPx;	};
		if(currentPx > maxX){	maxX = currentPx;	};
		if(currentPy < minY){	minY = currentPy;	};
		if(currentPy > maxY){	maxY = currentPy;	};
		
		double deltaX = Math.abs(maxX-minX);
		double deltaY = Math.abs(maxY-minY); 
		if(deltaX > deltaY){
			walkDistance = (width/deltaX);
			if(deltaY * walkDistance > height){
				walkDistance = walkDistance * (height/(deltaY * walkDistance));
			}
		}
		else{
			walkDistance = (height/deltaY);
			if(deltaX * walkDistance > width){
				walkDistance = walkDistance * (width/(deltaX * walkDistance));
			}
		}
		
		walkDistance *= 0.9;
		
		startPx = (int)((width/2)-((minX+((deltaX)/2))*walkDistance));
		startPy = (int)((height/2)-((minY+(deltaY/2))*walkDistance));
		
	}
	
	private void drawLine(int x0, int y0, int x1, int y1, MarvinImage image){
		boolean steep = (Math.abs(y1 - y0) > Math.abs(x1 - x0));
		int temp;
		if(steep){
			temp = x0;
			x0 = y0;
			y0 = temp;

			temp = x1;
			x1 = y1;
			y1 = temp;
		}
		if(x0 > x1){
			temp = x0;
			x0 = x1;
			x1 = temp;

			temp = y0;
			y0 = y1;
			y1 = temp;
		}

		int deltax = x1 - x0;
		int deltay = Math.abs(y1 - y0);
		int error = deltax / 2;
		int ystep;
		int y = y0;

		if(y0 < y1){
			ystep = 1;
		}
		else{
			ystep = -1;
		}

		for(int x=x0; x<=x1; x++){
         if(steep){
        	 	if(y >= 0 && y < image.getWidth() && x >= 0 && x < image.getHeight()){
        	 		image.setIntColor(y,x,0,0,0);
        	 	}
			}
			else{
				if(x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight()){
					image.setIntColor(x,y,0,0,0);
				}
			}

         error = error - deltay;
         if (error < 0){
             y = y + ystep;
             error = error + deltax;
			}
		}
	}
}

