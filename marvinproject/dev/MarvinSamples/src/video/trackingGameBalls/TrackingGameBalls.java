/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package video.trackingGameBalls;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.io.MarvinImageIO;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.video.MarvinJavaCVAdapter;
import marvin.video.MarvinVideoInterface;
import marvin.video.MarvinVideoInterfaceException;

import org.marvinproject.image.pattern.findColorPattern.FindColorPattern;
import org.marvinproject.image.render.renderText.RenderText;

/**
 * Tracking game sample
 * @author Gabriel Ambrosio Archanjo
 */

public class TrackingGameBalls extends JFrame implements Runnable{

	private MarvinVideoInterface	videoInterface;
	private MarvinImagePanel 		videoPanel;
	
	private Thread 					thread;
	
	private MarvinImage 			imageIn, 
									imageOut,
									imageHat,
									imageBall;
							
	
	private JPanel					panelSlider;
	
	private JSlider					sliderSensibility;
	
	private JButton					buttonStart;
	
	private JLabel					labelSlider;
	
	private int						regionPx,
									regionPy;
	
	private int						sensibility=30;

	private int 					playerPoints=15;
	
	private boolean					regionSelected=false;
	private int[]					arrInitialRegion;
	
	private Ball[]					arrBall;
	
	private int						imageWidth,
									imageHeight;
	
	private int						maskPx,
									maskPy,
									maskWidth=120,
									maskHeight=60;
	
	private MarvinImagePlugin		pluginColorPattern,
									text;
	
	private MarvinAttributes		attributesOut;
	
	
	// Game Attributes
	private long 					lastBall=0;
	private final static int 		BALL_DELAY = 1000;
	private long 					currentTime;
	
	
	
	public TrackingGameBalls(){
		try{
			videoPanel = new MarvinImagePanel();
			
			videoInterface = new MarvinJavaCVAdapter();
			videoInterface.connect(1);
			
			imageWidth = videoInterface.getImageWidth();
			imageHeight = videoInterface.getImageHeight();
			
			imageOut = new MarvinImage(imageWidth, imageHeight);
			
			imageHat = MarvinImageIO.loadImage("./res/hat.png");
			imageBall = MarvinImageIO.loadImage("./res/ball.png");
			loadGUI();
			
			arrBall = new Ball[5]; 
			for(int l_i=0; l_i<5; l_i++){
				arrBall[l_i] = new Ball();
				arrBall[l_i].used = false;
			}
			
			pluginColorPattern = new FindColorPattern();
			pluginColorPattern.load();
			text				= new RenderText();
			text.load();
			text.setAttribute("fontFile", MarvinImageIO.loadImage("./res/font.png"));
			text.setAttribute("color", 0xFFFFFFFF);
			
			
			attributesOut = new MarvinAttributes(null);
					
			thread = new Thread(this);
			thread.start();	
		}
		catch(MarvinVideoInterfaceException e){
			e.printStackTrace();
		}
	}
	
	private void loadGUI(){	
		setTitle("Video Sample - Tracking Game");
		
		sliderSensibility = new JSlider(JSlider.HORIZONTAL, 0, 60, 30);
		sliderSensibility.setMinorTickSpacing(2);
		sliderSensibility.setPaintTicks(true);
		sliderSensibility.addChangeListener(new SliderHandler());
		
		labelSlider = new JLabel("Sensibility");
		
		panelSlider = new JPanel();
		panelSlider.add(labelSlider);
		panelSlider.add(sliderSensibility);
		
		buttonStart = new JButton("Start");
		buttonStart.addActionListener(new ButtonHandler());
		
		Container l_container = getContentPane();
		l_container.setLayout(new BorderLayout());
		l_container.add(videoPanel, BorderLayout.NORTH);
		l_container.add(panelSlider, BorderLayout.CENTER);
		l_container.add(buttonStart, BorderLayout.SOUTH);
		
		setSize(imageWidth+20,imageHeight+100);
		setVisible(true);
	}
	
	private void combineImage(MarvinImage a_image, int x, int y){
		int l_rgb;
		int l_width = a_image.getWidth();
		int l_height = a_image.getHeight();
		
		for(int iy=0; iy<l_height; iy++){
			for(int ix=0; ix<l_width; ix++){
				if
				(
					ix+x > 0 && ix+x < imageWidth &&
					iy+y > 0 && iy+y < imageHeight
				)
				{
					l_rgb=a_image.getIntColor(ix, iy);				
					if(l_rgb != 0xFFFFFFFF){
						imageOut.setIntColor(ix+x, iy+y, l_rgb);
					}
				}
			}
		}		
	}
	
	
	private void updateBalls(){
				
		currentTime = System.currentTimeMillis();
		if(currentTime - lastBall > BALL_DELAY){
			launchBall();
			lastBall = currentTime;
		}
		
		for(int l_i=0; l_i<5; l_i++){
			if(arrBall[l_i].used){
				arrBall[l_i].py *= 1.1;
				combineImage(imageBall,(int)arrBall[l_i].px,(int)arrBall[l_i].py);
			}
		}
		
	}
	
	private void launchBall(){
		for(int l_i=0; l_i<5; l_i++){
			if(!arrBall[l_i].used){
				arrBall[l_i].py = 1;
				arrBall[l_i].px = (Math.random()*(imageWidth-300))+150;
				arrBall[l_i].used = true;
				return;
			}
		}
	}
	
	public void run(){
		long time = System.currentTimeMillis();
		int ticks=0;
		
		try{
			while(true){
				
				ticks++;
				if(System.currentTimeMillis() - time > 1000){
					System.out.println("FPS: "+ticks+"       ");
					ticks=0;
					time = System.currentTimeMillis();					
				}
				
				imageIn = videoInterface.getFrame();
				MarvinImage.copyColorArray(imageIn, imageOut);	
							
				MarvinImage.copyColorArray(imageIn, imageOut);
				
				if(regionSelected){
					pluginColorPattern.setAttribute("differenceColorRange", sensibility);
					pluginColorPattern.process(imageIn, imageOut, attributesOut, MarvinImageMask.NULL_MASK, false);
					regionPx 		= (Integer)attributesOut.get("regionPx");
					regionPy 		= (Integer)attributesOut.get("regionPy");
					gameLoop();				
				}
				else{
					combineImage(imageHat,300,200);
				}
				
				
				text.setAttribute("y", 5);
				text.setAttribute("text", "POINTS:"+playerPoints);
				text.process(imageOut, imageOut);
				
				videoPanel.setImage(imageOut);
			}
		}
		catch(MarvinVideoInterfaceException e){
			e.printStackTrace();
		}
	}
	
	private void gameLoop(){
		
		maskPx = regionPx-12;
		maskPy = regionPy-58;
		
		updateBalls();
		collisionDetection();
		combineImage(imageHat,regionPx-14,regionPy-95);
	}
	
	public void collisionDetection(){
		for(int l_i=0; l_i<5; l_i++){
			if(arrBall[l_i].used){
				collisionBallMask(arrBall[l_i]);
				collisionBallScreen(arrBall[l_i]);
			}
		}
	}
	
	private void collisionBallScreen(Ball a_ball){
		if(a_ball.py > imageHeight){
			a_ball.used = false;
		}
	}
	
	private void collisionBallMask(Ball a_ball){
		if
		(
			(
				a_ball.px >= maskPx && a_ball.px <= maskPx+maskWidth ||
				a_ball.px <= maskPx && a_ball.px+a_ball.width >= maskPx
			)
			&&
			(
				a_ball.py >= maskPy && a_ball.py <= maskPy+maskHeight ||
				a_ball.py <= maskPy && a_ball.py+a_ball.height >= maskPy
			)
		)
		{
			a_ball.used = false;
		}
			
	}
	
	public static void main(String args[]){
		TrackingGameBalls l_trackingBalls = new TrackingGameBalls();
		l_trackingBalls.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private class SliderHandler implements ChangeListener{
		public void stateChanged(ChangeEvent a_event){
			sensibility = (60-sliderSensibility.getValue());
		}
	}

	private class ButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent a_event){
			pluginColorPattern.setAttribute("regionPx", 310);
			pluginColorPattern.setAttribute("regionPy", 295);
			pluginColorPattern.setAttribute("regionWidth", 118);
			pluginColorPattern.setAttribute("regionHeight", 134);
			
			regionSelected = true;
		}
	}
	
	private class Ball{
		public boolean used;
		public double px,py;
		public int width,height;
	}
}
