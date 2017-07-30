/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package video.trackingPong;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
import org.marvinproject.image.transform.flip.Flip;

/**
 * Tracking game sample
 * @author Gabriel Ambrosio Archanjo
 */
public class TrackingPong extends JFrame implements Runnable{

	private final static int 		BALL_INITIAL_PX=100;
	private final static int 		BALL_INITIAL_PY=100;
	private final static int 		BALL_INITIAL_SPEED=3;
	
	private MarvinVideoInterface	videoInterface;
	private MarvinImagePanel 		videoPanel;
	
	private Thread 					thread;
	
	private MarvinImage 			imageIn, 
									imageOut;
	
	private JPanel					panelSlider;
	
	private JSlider					sliderSensibility;
	
	private JLabel					labelSlider;

	private int						regionPx,
									regionPy,
									regionWidth,
									regionHeight;
			
	private boolean					regionSelected=false;
	private int[]					arrInitialRegion;
	
	private int						sensibility=30;
	
	
	
	// Pong Game Attributes
	private double					ballPx=BALL_INITIAL_PX,
									ballPy=BALL_INITIAL_PY;
	
	private int						ballSide=15;
								
	
	double							ballIncX=5;
	private double					ballIncY=5;	
	
	private int						imageWidth,
									imageHeight;
	
	private Paddle					paddlePlayer,
									paddleComputer;
	
	private int						playerPoints=0,
									computerPoints=0;
	
	private MarvinImagePlugin 		findColorPattern,
									flip,
									text;
	
	private MarvinImage				imageBall,
									imagePaddlePlayer,
									imagePaddleComputer;
	
	private MarvinAttributes		attributesOut;
	
	public TrackingPong(){
		videoPanel = new MarvinImagePanel();
		
		try{
			// 1. Connect to the camera device.
			videoInterface = new MarvinJavaCVAdapter();
			videoInterface.connect(1);
					
			imageWidth = videoInterface.getImageWidth();
			imageHeight = videoInterface.getImageHeight();
			
			imageOut = new MarvinImage(imageWidth, imageHeight);
			
			// 2. Load Graphical Interface.
			loadGUI();
			
			// 3. Load and set up Marvin plug-ins.
			findColorPattern 	= new FindColorPattern();
			findColorPattern.load();
			flip				= new Flip();
			flip.load();
			text				= new RenderText();
			text.load();
			text.setAttribute("fontFile", MarvinImageIO.loadImage("./res/font.png"));
			text.setAttribute("color", 0xFFFFFFFF);
			
			// 3. Load game images
			imageBall = MarvinImageIO.loadImage("./res/ball.png");
			imagePaddlePlayer = MarvinImageIO.loadImage("./res/paddleA.png");
			imagePaddleComputer = MarvinImageIO.loadImage("./res/paddleB.png");
			
			attributesOut = new MarvinAttributes(null);
			
			// Set up plater and computer paddle properties.
			paddlePlayer = new Paddle();
			paddlePlayer.px=100;
			paddlePlayer.py=420;
			paddlePlayer.width=100;
			paddlePlayer.height=30;
			
			paddleComputer = new Paddle();
			paddleComputer.px=100;
			paddleComputer.py=30;
			paddleComputer.width=100;
			paddleComputer.height=30;
			
			thread = new Thread(this);
			thread.start();
		}
		catch(MarvinVideoInterfaceException e){
			e.printStackTrace();
		}
	}
	
	private void loadGUI(){	
		setTitle("Video Sample - Tracking Pong");
		
		videoPanel.addMouseListener(new MouseHandler());
		
		sliderSensibility = new JSlider(JSlider.HORIZONTAL, 0, 60, 30);
		sliderSensibility.setMinorTickSpacing(2);
		sliderSensibility.setPaintTicks(true);
		sliderSensibility.addChangeListener(new SliderHandler());
		
		labelSlider = new JLabel("Sensibility");
		
		panelSlider = new JPanel();
		panelSlider.add(labelSlider);
		panelSlider.add(sliderSensibility);
		
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		container.add(videoPanel, BorderLayout.NORTH);
		container.add(panelSlider, BorderLayout.SOUTH);
		
		setSize(videoInterface.getImageWidth()+20,videoInterface.getImageHeight()+100);
		setVisible(true);
	}
	
	public void run(){
		long time = System.currentTimeMillis();
		int ticks=0;
		
		// The game loop.
		try{
			while(true){
				
				ticks++;
				if(System.currentTimeMillis() - time > 1000){
					System.out.println("FPS: "+ticks+"       ");
					ticks=0;
					time = System.currentTimeMillis();					
				}
				
				// 1. Get the current video frame.
				imageIn = videoInterface.getFrame();
				MarvinImage.copyColorArray(imageIn, imageOut);
				
				// 2. Flip the frame horizontally so the player will see him on the screen like looking at the mirror.
				flip.process(imageOut, imageOut);
				
				if(regionSelected){
					
					// 3. Find the player paddle position.
					findColorPattern.setAttribute("differenceColorRange", sensibility);
					findColorPattern.process(imageOut, imageOut, attributesOut, MarvinImageMask.NULL_MASK, false);
					regionPx 		= (Integer)attributesOut.get("regionPx");
					regionPy 		= (Integer)attributesOut.get("regionPy");
					regionWidth 	= (Integer)attributesOut.get("regionWidth");
					regionHeight	= (Integer)attributesOut.get("regionHeight");
					
					// 4. Invoke the game logic
					pongGame();
					
					// 5. Draw the detected region
					imageOut.drawRect(regionPx, regionPy, regionWidth, regionHeight, Color.red);
					
					// 6. Draw the player and computer points.
					text.setAttribute("x", 105);
					text.setAttribute("y", 3);
					text.setAttribute("text", "PLAYER:"+playerPoints);
					text.process(imageOut, imageOut);
					
					text.setAttribute("x", 105);
					text.setAttribute("y", 460);
					text.setAttribute("text", "COMPUTER:"+computerPoints);
					text.process(imageOut, imageOut);
				}
	
				
				videoPanel.setImage(imageOut);
			}
		}
		catch(MarvinVideoInterfaceException e){
			e.printStackTrace();
		}
	}
	
	private void pongGame(){
		// 1. Move the ball
		ballIncX*=1.001;
		ballIncY*=1.001;
		ballPx+=ballIncX;
		ballPy+=ballIncY;
		
		// 2. Set the player paddle position to the the coordinates of the detected region.
		paddlePlayer.px = regionPx+((regionWidth-paddlePlayer.width)/2);
		
		// 3. Invoke simple computer AI
		computerAI();
		
		// 4. Check object positions and collisions.
		checkPaddlePosition(paddlePlayer);
		checkPaddlePosition(paddleComputer);
		collisionScreen();
		collisionTap();
		
		// 5. Draw the game elements.
		imageOut.fillRect(horizontalMargin, 0, 5, imageHeight, Color.black);
		imageOut.fillRect(imageWidth-horizontalMargin, 0, 5, imageHeight, Color.black);
		
		combineImage(imagePaddlePlayer, paddlePlayer.px, paddlePlayer.py);
		combineImage(imagePaddleComputer, paddleComputer.px, paddleComputer.py);
		combineImage(imageBall,(int)ballPx, (int)ballPy);
	}
	
	private void checkPaddlePosition(Paddle a_paddle){
		if(a_paddle.px < horizontalMargin){
			a_paddle.px = horizontalMargin;
		}
		if(a_paddle.px+a_paddle.width > imageWidth-horizontalMargin){
			a_paddle.px = imageWidth-horizontalMargin-a_paddle.width;
		}		
	}
	
	private void computerAI(){
		if(ballPx < paddleComputer.px+(paddleComputer.width/2)-10){
			paddleComputer.px-=4;
		}
		if(ballPx > paddleComputer.px+(paddleComputer.width/2)+10){
			paddleComputer.px+=4;
		}
	}
	
	private int horizontalMargin = 100;
	private void collisionScreen(){
			
		if(ballPx < horizontalMargin){
			ballPx = horizontalMargin;
			ballIncX*=-1;
		}
		if(ballPx+ballSide >= imageWidth-horizontalMargin){
			ballPx=(imageWidth-horizontalMargin)-ballSide;
			ballIncX*=-1;
		}
		if(ballPy < 0){
			playerPoints++;
			ballPx = BALL_INITIAL_PX;
			ballPy = BALL_INITIAL_PY;
			ballIncY=BALL_INITIAL_SPEED;
			ballIncX=BALL_INITIAL_SPEED;
		} else if(ballPy+ballSide >= imageHeight){
			computerPoints++;
			ballPx = BALL_INITIAL_PX;
			ballPy = BALL_INITIAL_PY;
			ballIncY=BALL_INITIAL_SPEED;
			ballIncX=BALL_INITIAL_SPEED;
		}
	}
	
	private void collisionTap(){
		if(ballCollisionTap(paddlePlayer)){
			ballIncY*=-1;
			ballPy = paddlePlayer.py-ballSide;
		}
		if(ballCollisionTap(paddleComputer)){
			ballIncY*=-1;
			ballPy = paddleComputer.py+paddleComputer.height;
		}
	}
	
	private boolean ballCollisionTap(Paddle a_tap){
		if
		(
			(
				ballPx >= a_tap.px && ballPx <= a_tap.px+a_tap.width ||
				ballPx <= a_tap.px && ballPx+ballSide >= a_tap.px
			)
			&&
			(
				ballPy >= a_tap.py && ballPy <= a_tap.py+a_tap.height ||
				ballPy <= a_tap.py && ballPy+ballSide >= a_tap.py
			)
		)
		{
			return true;
		}
		return false;
	}
	
	private void combineImage(MarvinImage img, int x, int y){
		int rgb;
		int width = img.getWidth();
		int height = img.getHeight();
		
		for(int iy=0; iy<height; iy++){
			for(int ix=0; ix<width; ix++){
				if
				(
					ix+x > 0 && ix+x < imageWidth &&
					iy+y > 0 && iy+y < imageHeight
				)
				{
					rgb=img.getIntColor(ix, iy);				
					if(rgb != 0xFFFFFFFF){
						imageOut.setIntColor(ix+x, iy+y, rgb);
					}
				}
			}
		}		
	}
	
	public static void main(String args[]){
		TrackingPong trackingPong = new TrackingPong();
		trackingPong.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private class SliderHandler implements ChangeListener{
		public void stateChanged(ChangeEvent a_event){
			sensibility = (60-sliderSensibility.getValue());
		}
	}
	
	private class MouseHandler implements MouseListener{
		public void mouseEntered(MouseEvent a_event){}
		public void mouseExited(MouseEvent a_event){}
		public void mousePressed(MouseEvent a_event){}
		public void mouseClicked(MouseEvent a_event){}
		
		public void mouseReleased(MouseEvent event){
			if(!regionSelected){
				if(arrInitialRegion == null){
					arrInitialRegion = new int[]{event.getX(), event.getY(),0,0};
				}
				else{
					arrInitialRegion[2] = event.getX()-arrInitialRegion[0];
					arrInitialRegion[3] = event.getY()-arrInitialRegion[1];
					
					findColorPattern.setAttribute("regionPx", arrInitialRegion[0]);
					findColorPattern.setAttribute("regionPy", arrInitialRegion[1]);
					findColorPattern.setAttribute("regionWidth", arrInitialRegion[2]);
					findColorPattern.setAttribute("regionHeight", arrInitialRegion[3]);
					
					regionSelected = true;
				}	
			}
		}		
	}
	
	private class Paddle{
		public int px,py,width,height;
	}
}
