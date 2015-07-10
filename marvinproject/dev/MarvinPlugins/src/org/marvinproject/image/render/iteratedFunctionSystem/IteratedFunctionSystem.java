/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.render.iteratedFunctionSystem;

import java.util.ArrayList;
import java.util.List;

import marvin.gui.MarvinAttributesPanel;
import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

public class IteratedFunctionSystem extends MarvinAbstractImagePlugin{

	private MarvinAttributesPanel	attributesPanel;
	private MarvinAttributes 		attributes;
	
	private List<Rule> rules;
	
	// Testing String
	private final static String EXAMPLE_RULES = 	"0,0,0,0.16,0,0,0.01\n"+
											"0.85,0.04,-0.04,0.85,0,1.6,0.85\n"+
											"0.2,-0.26,0.23,0.22,0,1.6,0.07\n"+
											"-0.15,0.28,0.26,0.24,0,0.44,0.07\n";
	@Override
	public void load() {
		attributes = getAttributes();
		attributes.set("rules", EXAMPLE_RULES);
		attributes.set("iterations", 1000000);
		
		rules = new ArrayList<Rule>();
	}

	@Override
	public void process
	(
		MarvinImage imageIn, 
		MarvinImage imageOut, 
		MarvinAttributes out2,
		MarvinImageMask a_mask, 
		boolean previewMode
	){
		loadRules();
		int iterations = (Integer)attributes.get("iterations");
		
		double x0 = 0;
		double y0 = 0;
		double x,y;
		int startX;
		int startY;
		double factor;
		
		double minX=999999999,minY=999999999,maxX=-999999999,maxY=-99999999;
		
		Rule tempRule;
		double point[] = {x0,y0};
		
		imageOut.clearImage(0xFFFFFFFF);
		
		for(int i=0; i<iterations; i++){
			tempRule = getRule();
			applyRule(point, tempRule);
			
			x = point[0];
			y = point[1];
			
			if(x < minX){	minX = x;	};
			if(x > maxX){	maxX = x;	};
			if(y < minY){	minY = y;	};
			if(y > maxY){	maxY = y;	};
		
		}	
		
		int width = imageOut.getWidth();
		int height = imageOut.getHeight();
		
		double deltaX = Math.abs(maxX-minX);
		double deltaY = Math.abs(maxY-minY); 
		if(deltaX > deltaY){
			factor = (width/deltaX);
			if(deltaY * factor > height){
				factor = factor * (height/(deltaY * factor));
			}
		}
		else{
			factor = (height/deltaY);
			if(deltaX * factor > width){
				factor = factor * (width/(deltaX * factor));
			}
		}
		
		factor *= 0.9;
		
		startX = (int)((width/2)-((minX+((deltaX)/2))*factor));
		startY = (int)(height-((height/2)-((minY+(deltaY/2))*factor)));
		
		point[0] = x0;
		point[1] = y0;
		
		for(int i=0; i<iterations; i++){
			tempRule = getRule();
			applyRule(point, tempRule);
			
			x = (int)(point[0]*factor)+startX;
			y = startY-(int)(point[1]*factor);
			
			if(x >= 0 && x<width && y >= 0 && y < height){
				imageOut.setIntColor((int)x,(int)y , 255, 0);
			}
		}
	}
	

	@Override
	public MarvinAttributesPanel getAttributesPanel(){
		if(attributesPanel == null){
			attributesPanel = new MarvinAttributesPanel();
			attributesPanel.addLabel("lblRules","Rules:");
			attributesPanel.newComponentRow();
			attributesPanel.addTextArea("txtRules","rules", 8, 40, attributes);
		}
		return attributesPanel;
	}
	
	private void loadRules(){
		String r[] = ((String)(attributes.get("rules"))).split("\n");
		
		for(int i=0; i<r.length; i++){
			addRule(r[i]);
		}
	}

	private void addRule(String rule){
		rule = rule.replace(" ", "");
		String attr[] = rule.split(",");
		
		if(attr.length == 7){
			Rule r = new Rule
			(
				Double.parseDouble(attr[0]),
				Double.parseDouble(attr[1]),
				Double.parseDouble(attr[2]),
				Double.parseDouble(attr[3]),
				Double.parseDouble(attr[4]),
				Double.parseDouble(attr[5]),
				Double.parseDouble(attr[6])
			);
			
			rules.add(r);
		}
	}
	
	private Rule getRule(){
		double random = Math.random();
		double sum=0;
		int i;
		for(i=0; i<rules.size(); i++){
			sum += rules.get(i).probability;
			if(random < sum){
				return rules.get(i);
			}
		}
		
		if(i != 0){
			return rules.get(i-1);
		}
		return rules.get(0);
	}
	
	private void applyRule(double point[], Rule rule){
		double nx = rule.a*point[0] + rule.b*point[1]+rule.e;
		double ny = rule.c*point[0] + rule.d*point[1]+rule.f;
		point[0] = nx;
		point[1] = ny;
	}
}

class Rule {
	
	public double	a,
					b,
					c,
					d,
					e,
					f,
					probability;
	
	public Rule
	(
		double a,
		double b,
		double c,
		double d,
		double e,
		double f,
		double probability
	)
	{
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.f = f;
		this.probability = probability;
	}
}
