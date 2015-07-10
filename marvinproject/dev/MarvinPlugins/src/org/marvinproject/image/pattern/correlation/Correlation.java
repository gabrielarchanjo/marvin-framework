/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.pattern.correlation;

import java.io.File;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.performance.MarvinPerformanceMeter;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinFileChooser;

public class Correlation extends MarvinAbstractImagePlugin {

	private MarvinImage img_padrao,img_teste;
	private MarvinPerformanceMeter performanceMeter;
	private double somaX,somaY,somaXY,n,somaX2,somaY2,acima,abaixo,r,somaX2R,somaY2R;
	private String novaImagem;
	private boolean modoAnalise = false;
	private double cr;

	public void setNovaImagem(String novaImagem)
	{
		this.novaImagem = novaImagem;
	}
	public void setModoAnalise(boolean b)
	{
		this.modoAnalise = b;
	}

	public double getCr(){

		return cr;
	}

	public void load() {
		if(!modoAnalise)
		{
			performanceMeter = new MarvinPerformanceMeter();
			process(getImagePanel().getImage(), null, null, MarvinImageMask.NULL_MASK, true);
			
		}

	}

	public void process
	(
		MarvinImage a_imageIn, 
		MarvinImage a_imageOut,
		MarvinAttributes a_attributesOut,
		MarvinImageMask a_mask, 
		boolean a_previewMode
	) 
	{
		img_padrao = a_imageIn;
		try
		{
			if(!modoAnalise)
			{
				novaImagem = MarvinFileChooser.select(null, true,JFileChooser.OPEN_DIALOG);
			}
			img_teste = new MarvinImage(ImageIO.read(new File(novaImagem)));
		}catch(Exception e)
		{
			System.out.println("Error while selecting image");
		}
		// the image have to same size
		if(img_teste.getWidth() == a_imageIn.getWidth() && img_teste.getHeight() == a_imageIn.getHeight()){

			if(!modoAnalise)
			{
				performanceMeter.start("Correlation");
				performanceMeter.enableProgressBar("Correlation" , 5 * ( img_padrao.getWidth() * img_padrao.getHeight()));
			}
			n = img_padrao.getWidth() * img_padrao.getHeight();

			
			if(!modoAnalise)
			{
				performanceMeter.startEvent("Add X");
			}
			// soma X
			somaX = 0;

			for (int x = 1; x < img_padrao.getWidth()-1; x++) 
			{
				for (int y =  1; y < img_padrao.getHeight()-1; y++) 
				{
					somaX = somaX + img_padrao.getIntComponent0(x, y);
				}
				if(!modoAnalise)
				{
					performanceMeter.stepsFinished(a_imageIn.getHeight());
					performanceMeter.incProgressBar(a_imageIn.getHeight());
				}
			}	
			if(!modoAnalise)
			{
				performanceMeter.finishEvent();
			}

			if(!modoAnalise)
			{
				performanceMeter.startEvent("Add Y");
			}
			// soma Y
			somaY = 0;

			for (int x = 1; x < img_teste.getWidth()-1; x++) 
			{
				for (int y =  1; y < img_teste.getHeight()-1; y++) 
				{
					somaY = somaY + img_teste.getIntComponent0(x, y);
				}
				if(!modoAnalise)
				{
					performanceMeter.stepsFinished(a_imageIn.getHeight());
					performanceMeter.incProgressBar(a_imageIn.getHeight());
				}
			}	
			
			if(!modoAnalise)
			{
				performanceMeter.finishEvent();
			}


			if(!modoAnalise)
			{
				performanceMeter.startEvent("Add XR");
			}
			// soma XR
			somaX = 0;

			for (int x = 1; x < img_padrao.getWidth()-1; x++) 
			{
				for (int y =  1; y < img_padrao.getHeight()-1; y++) 
				{
					somaX2R = somaX2R + Math.pow (img_padrao.getIntComponent0(x, y),2);
				}
				if(!modoAnalise)
				{
					performanceMeter.stepsFinished(a_imageIn.getHeight());
					performanceMeter.incProgressBar(a_imageIn.getHeight());
				}
			}	
			
			if(!modoAnalise)
			{
				performanceMeter.finishEvent();
				performanceMeter.startEvent("Add XR");
			}
			// soma Yr
			somaY = 0;
			for (int x = 1; x < img_teste.getWidth()-1; x++) 
			{
				for (int y =  1; y < img_teste.getHeight()-1; y++) 
				{
					somaY2R = somaY2R +  Math.pow (img_teste.getIntComponent0(x, y),2);
				}		
				if(!modoAnalise)
				{
					performanceMeter.stepsFinished(a_imageIn.getHeight());
					performanceMeter.incProgressBar(a_imageIn.getHeight());
				}
			}	
			if(!modoAnalise)
			{
				performanceMeter.finishEvent();
				

				performanceMeter.startEvent("Add z y");
			}
			// soma x . y
			for (int x = 1; x < img_teste.getWidth()-1; x++) 
			{
				for (int y =  1; y < img_teste.getHeight()-1; y++) 
				{
					somaXY = somaXY + (img_teste.getIntComponent0(x, y) * img_padrao.getIntComponent0(x, y) );
				}
				//performanceMeter.stepsFinished(a_image.getHeight());
				if(!modoAnalise)
				{
					performanceMeter.stepsFinished(a_imageIn.getHeight());
					performanceMeter.incProgressBar(a_imageIn.getHeight());
				}
			}
			if(!modoAnalise)
			{
				performanceMeter.finishEvent();
				performanceMeter.startEvent("Adding all");
			}
			somaX2 = Math.sqrt(somaX);
			somaY2 = Math.sqrt(somaY);


			acima = n*(somaXY) - (somaX * somaY);
			abaixo = (Math.sqrt( n * (somaX2R) - somaX2 )) * (Math.sqrt(  n * (somaY2R) - somaY2));
			r = acima / abaixo;
			if(!modoAnalise)
			{
				performanceMeter.finishEvent();
				performanceMeter.finish();
			}

			cr = r;
			DecimalFormat myFormatter = new DecimalFormat("##.#");
			String output = myFormatter.format(r);
			if(!modoAnalise)
			{
				performanceMeter.finishEvent();
				performanceMeter.finish();
			}

			if(a_previewMode)
				JOptionPane.showMessageDialog(null, "The correlation is: " + cr);
		}else{
			if(!modoAnalise)
				JOptionPane.showMessageDialog(null, "The image have to the same size");
		}

	}

	public MarvinAttributesPanel getAttributesPanel(){
		return null;
	}

}
