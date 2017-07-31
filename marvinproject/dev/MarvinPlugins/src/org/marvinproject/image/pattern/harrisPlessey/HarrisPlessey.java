/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.pattern.harrisPlessey;

import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.performance.MarvinPerformanceMeter;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;

import org.marvinproject.image.segmentation.crop.Crop;

/**
 * Absolute interst point
 * @author Fabio Andrijauskas
 * @version 1.0 02/04/2008
 */

public class HarrisPlessey extends MarvinAbstractImagePlugin{

	MarvinPerformanceMeter performanceMeter;
	// variaveis com os gradientes de X e Y
	private double Gx = 0;
	private double Gy = 0;

	// variaveis da matriz de auto correalcao
	// A   C
	// C   B
	private double A = 0;
	private double B = 0;
	private double C = 0;

	//valor do limiar
	private double th = 1.7981439005E15;

	// valor do calculo da qualidade do canto
	private double E = 0;

	// valor empirico de sensibilidade
	private double k = 0.2;

	// determinante da matriz de auto correlacao
	private double det = 0;

	// traco da matriz de auto correlocao
	private double trace = 0; 

	// matriz que guarda aonde esta  os cantos
	private int cantos[][];

	// numero de cantos 
	private int numeroCantos;

	private int rn;
	private int gn;
	private int bn;
	private int[][] gx,gy;
	private int r,g,b;
	private int mul8gx[], mul8gy[];
	private String path = null;
	private boolean padrao;
	private boolean modoAnalise;
	
	private MarvinImagePlugin crop;
	
	public void load(){
		performanceMeter = new MarvinPerformanceMeter();
		crop = new Crop();
		crop.load();
	}

	public MarvinAttributesPanel getAttributesPanel(){return null;}

	public void process
	(
		MarvinImage a_imageIn, 
		MarvinImage a_imageOut,
		MarvinAttributes a_attributesOut,
		MarvinImageMask a_mask, 
		boolean a_previewMode
	)
	{

		if(a_previewMode || modoAnalise){
			path = "";
			File dir = new File("pattern/tmp/");							
			boolean trf = dir.delete();
			if(!trf)
			{
				File tmpFiles[] = dir.listFiles();
				if(tmpFiles != null){
					for(int a = 0; a < tmpFiles.length;a++)
					{
						boolean rr = tmpFiles[a].delete();
					}
				}

			}
			dir.mkdir();
		}else{
			path = JOptionPane.showInputDialog(null,"","Type the class of this image",JOptionPane.QUESTION_MESSAGE);
		}

		int l_processSteps=0;
		l_processSteps+=(a_imageIn.getWidth() *a_imageIn.getHeight())*2;
		l_processSteps+=((a_imageIn.getWidth()-2) * (a_imageIn.getHeight()-2));
		l_processSteps+=((a_imageIn.getWidth()-3) * (a_imageIn.getHeight()-3));
		l_processSteps+=((a_imageIn.getWidth()-2));

		performanceMeter.start("InterestPoint");
		performanceMeter.enableProgressBar("InterestPoint" , l_processSteps);

		performanceMeter.startEvent("Gray scale");
		int r,g,b,corfinal;
		for (int x = 0; x < a_imageIn.getWidth(); x++) {
			for (int y = 0; y < a_imageIn.getHeight(); y++) {
				//Red - 30% / Blue - 59% / Green - 11%
				r = a_imageIn.getIntComponent0(x, y);
				g = a_imageIn.getIntComponent1(x, y);
				b = a_imageIn.getIntComponent2(x, y);
				corfinal = (int)((r*0.3)+(b*0.59)+(g*0.11));
				a_imageOut.setIntColor(x,y,corfinal,corfinal,corfinal);
			}
			performanceMeter.stepsFinished(a_imageIn.getHeight());
			performanceMeter.incProgressBar(a_imageIn.getHeight());			
		}
		performanceMeter.finishEvent();

		//matrizes do sobelhr
		gx = new int[3][3];
		gy = new int[3][3];

		// matrizes utilizadas para detetar as bordas para o eixo x
		gx[0][0] = -1;gx[1][0] = 0;gx[2][0] = 1;
		gx[0][1] = -2;gx[1][1] = 0;gx[2][1] = 2;
		gx[0][2] = -1;gx[1][2] = 0;gx[2][2] = 1;

		// matrizes utilizadas para detetar as bordas para o eixo Y		
		gy[0][0] = 1;gy[1][0] = 2;gy[2][0] = 1;
		gy[0][1] = 0;gy[1][1] = 0;gy[2][1] = 0;
		gy[0][2] = -1;gy[1][2] = -2;gy[2][2] = -1;

		performanceMeter.startEvent("Border detector");
		for (int x = 1; x < a_imageOut.getWidth()-1; x++) {
			for (int y =  1; y < a_imageOut.getHeight()-1; y++) {

				// pega o r , g ,e b
				r = a_imageIn.getIntComponent0(x, y);
				g = a_imageIn.getIntComponent1(x, y);
				b = a_imageIn.getIntComponent2(x, y);

				//a b c
				// d e f
				// g h i

				// metodo que multiplic a matriz pela janela 
				mul8gx = a_imageIn.Multi8p(x, y, gx);
				mul8gy = a_imageIn.Multi8p(x, y, gy);

				// soma os gradientes
				rn = Math.abs(mul8gx[0]) + Math.abs(mul8gy[0]);
				gn = Math.abs(mul8gx[1]) + Math.abs(mul8gy[1]);
				bn = Math.abs(mul8gx[2]) + Math.abs(mul8gy[2]);

				// re arranja os rgb
				rn = a_imageOut.boundRGB(rn);
				gn = a_imageOut.boundRGB(gn);
				bn = a_imageOut.boundRGB(bn);

				// defini os rgb novos
				a_imageOut.setIntColor(x, y, rn,gn,bn);

			}
			performanceMeter.stepsFinished(a_imageOut.getHeight()-2);
			performanceMeter.incProgressBar(a_imageOut.getHeight()-2);
		}
		performanceMeter.finishEvent();

		performanceMeter.startEvent("Binarization");
		int limiar = 150;
		for (int x = 0; x < a_imageOut.getWidth(); x++) {
			for (int y =  0; y < a_imageOut.getHeight(); y++) {

				if(a_imageOut.getIntComponent0(x, y) > limiar ||a_imageOut.getIntComponent0(x, y)+20 > limiar ||a_imageOut.getIntComponent0(x, y)-20 > limiar ){

					if(a_imageOut.getIntComponent1(x, y) > limiar ||a_imageOut.getIntComponent1(x, y)+20 > limiar ||a_imageOut.getIntComponent1(x, y)-20 > limiar ){

						if(a_imageOut.getIntComponent2(x, y) > limiar ||a_imageOut.getIntComponent2(x, y)+20 > limiar ||a_imageOut.getIntComponent2(x, y)-20 > limiar ){

							a_imageOut.setIntColor(x, y, 255,255,255);

						}
					}

				}else{

					a_imageOut.setIntColor(x, y, 0,0,0);
				}


			}
			performanceMeter.stepsFinished(a_imageOut.getHeight());
			performanceMeter.incProgressBar(a_imageOut.getHeight());
		}
		performanceMeter.finishEvent();

		numeroCantos = 0;

		// matriz que tem o mesmo tamnho da imagem para terminar quais os cantos
		cantos = new int[a_imageOut.getWidth()][a_imageOut.getHeight()];

		performanceMeter.startEvent("Harris/Plessey");
		for (int x = 2; x < a_imageOut.getWidth()-1; x++) {
			for (int y =  2; y < a_imageOut.getHeight()-1; y++) {

				//gradiente rem relação a x
				//matriz 3 x 3 (janela) 
				// 3  3  3 
				// 3  3  3 * (-1 ,0 ,1)  matriz (janela da imagem, o valor dos pixels mesmo) convulacionaod com (-1,0,1)
				// 3  3  3  
				Gx = (a_imageOut.getIntComponent0(x-1,y-1) * 1) + (a_imageOut.getIntComponent0(x-1,y) * 0) + (a_imageOut.getIntComponent0(x-1,y+1) * -1);
				Gx = (a_imageOut.getIntComponent0(x,y-1) * 1) + (a_imageOut.getIntComponent0(x,y) * 0) + (a_imageOut.getIntComponent0(x,y+1) * -1);
				Gx = Gx + (a_imageOut.getIntComponent0(x+1,y-1) * 1) + (a_imageOut.getIntComponent0(x+1,y) * 0) + (a_imageOut.getIntComponent0(x+1,y+1) * -1);



				//gradiente em relação a y
				//matriz 3 x 3 (janela) 
				// 3  3  3 
				// 3  3  3 * (-1 ,0 ,1)  matriz (janela da imagem, o valor dos pixels mesmo) convulacionaod com (-1,0,1)
				// 3  3  3  
				Gy = (a_imageOut.getIntComponent0(x-1,y-1) * -1) + (a_imageOut.getIntComponent0(x,y-1) * 0) + (a_imageOut.getIntComponent0(x+1,y-1) * 1);
				Gy = (a_imageOut.getIntComponent0(x-1,y) * -1) + (a_imageOut.getIntComponent0(x,y) * 0) + (a_imageOut.getIntComponent0(x+1,y) * 1);
				Gy = Gy + (a_imageOut.getIntComponent0(x-1,y+1) * -1) + (a_imageOut.getIntComponent0(x,y+1) * 0) + (a_imageOut.getIntComponent0(x+1,y+1) * 1);


				// calcula o valor da matriz de auto correlacao
				A = a_imageOut.multi8p(x, y,Math.pow(Gx,2.0));
				B = a_imageOut.multi8p(x, y,Math.pow(Gy,2.0));
				C = a_imageOut.multi8p(x, y,Gx+Gy);

				// determinante da matriz de autocorrelacao
				det = (A * B) - Math.pow(C,2.0);

				// traco da matriz de auto correlacao
				trace = A + B;

				// valor que se descobre se e canto , ou borda ou nada
				E = det - (k * Math.pow(trace,2.0)); 

				// se E for maior que o valor de limiar e canto
				if( E > th ){
					cantos[x][y] = 1;
					numeroCantos++;
					a_imageOut.setIntColor(x, y, 255, 0, 0);
				}

				// zera todos os itens da matriz de auto correlação
				Gx = 0;
				Gy = 0;
				A = 0;
				B = 0;
				C = 0;
				E = 0;
				det = 0;
				trace = 0; 
			}
			performanceMeter.stepsFinished(a_imageIn.getHeight()-3);
			performanceMeter.incProgressBar(a_imageIn.getHeight()-3);
		}
		performanceMeter.finishEvent();
		int corners = 0;

		performanceMeter.startEvent("Segmentation");
		for (int x = 1; x < a_imageOut.getWidth()-1; x++) 
		{
			for (int y =  1; y < a_imageOut.getHeight()-1; y++) 
			{

				if(cantos[x][y] == 1)
				{

					if(x+40 < a_imageOut.getWidth() && y+40 < a_imageOut.getHeight())
					{

						a_imageOut.setIntColor(x, y, 255,0,0);
						crop.setAttribute("x", x);
						crop.setAttribute("y", y);
						crop.setAttribute("width", 40);
						crop.setAttribute("height", 40);
						MarvinImage imgTmp = a_imageIn.clone();
						crop.process(a_imageIn, imgTmp);
						if(!a_previewMode){
							try{
								if(modoAnalise)
								{
									File tmp = new File("pattern/"+"tmp/"+corners+".jpg");
									while(tmp.exists())
									{
										corners++;
										tmp = new File("pattern/"+path+"/"+corners+".jpg");
									}
									ImageIO.write(imgTmp.getBufferedImage(),"jpg",tmp);
									imgTmp = null;
									corners++;				

								}else{

									File dir = new File("pattern/"+path+"/");							
									dir.mkdirs();
									File tmp = new File("pattern/"+path+"/"+corners+".jpg");
									while(tmp.exists())
									{
										corners++;
										tmp = new File("pattern/"+path+"/"+corners+".jpg");
									}
									ImageIO.write(imgTmp.getBufferedImage(),"jpg",tmp);
									imgTmp = null;
									corners++;
								}

							}catch (Exception e) {
								// TODO: handle exception
							}
						}

						for (int xN = x -10; xN <= x+40; xN++)
						{
							for (int yN = y -10; yN <= y+40; yN++)
							{
								//if(xN < imagemOriginal.getx() && yN < imagemOriginal.gety())
								//{
								try
								{
									cantos[xN][yN] = 0;
								}catch (Exception e) {

								}
								//}
							}
						}
					}
				}			
			}
			performanceMeter.stepsFinished(a_imageOut.getHeight()-1);
			performanceMeter.incProgressBar(1);
		}
		performanceMeter.finishEvent();

		a_imageIn.setBufferedImage(a_imageOut.getBufferedImage());

		performanceMeter.finish();

	}


	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isModoAnalise() {
		return modoAnalise;
	}

	public void setModoAnalise(boolean modoAnalise) {
		this.modoAnalise = modoAnalise;
	}
}
