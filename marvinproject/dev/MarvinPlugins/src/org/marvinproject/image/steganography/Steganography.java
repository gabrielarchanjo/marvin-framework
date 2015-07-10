/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.steganography;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import marvin.gui.MarvinAttributesPanel;
import marvin.gui.MarvinPluginWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinFileChooser;

/**
 * Steganography plug-in
 * @author Hugo Henrique Slepicka
 */
public class Steganography extends MarvinAbstractImagePlugin implements ActionListener{
	
	private enum Action{
		MODE_READ, MODE_WRITE
	};

	private enum Type{
		TYPE_TEXT, TYPE_FILE
	};
	
	private final int numArgs = 3;

	private final int RED   = 0;
	private final int GREEN = 1;
	private final int BLUE  = 2;

	
	private int currentColor = RED;
	private int currentPixel = 0;
	
	private MarvinAttributes attributes;	
	private MarvinAttributesPanel attributesPanel;
	//private MarvinPluginWindow pluginWindow;
	private JButton buttonOK;
	private byte[] put;
	private byte[] get;
	
	private Type type;
	private long pixels;
	private String ext;
	private int xH, yH = 0;
	
	public void load() {
		attributes = getAttributes();
		attributes.set("txtLines", "");
		attributes.set("cbSelection", "Write on Image");
		attributes.set("cbType", "Store a file");
	}
	
	
		
	public Action getAction(){
		String s = attributes.get("cbSelection").toString();
		if(s.equals("Write on Image")){
			return (Action.MODE_WRITE);
		}else{
			return (Action.MODE_READ);
		}
	}
	
	public Type getType(){
		String s = attributes.get("cbType").toString();
		if(s.equals("Store a file")){
			return (Type.TYPE_FILE);
		}else{
			return (Type.TYPE_TEXT);
		}
	}
		
	public void getHEADER(MarvinImage a_image){
		//Cabeçalho do arquivo no seguinte formato:
		//Tipo_do_armazenamento/Extensao/Qtde_Pixels
		//F/docx/12345
		int cont = 0;
		String binpar = "";
		String pixaux = "";
		ext = "";
		type = null;
		pixels = 0L;
		
		currentColor = RED;
		currentPixel = 0;
		
		while(cont != numArgs){
			binpar += readBit(a_image);	
			if(binpar.length()==8){				
				//If equals, increment cont
				if(binpar.equals("00101111")){
					cont++;
				}else{		

					switch(cont){
					//File Type
					case 0:
						if(binpar.equals("01010100")){
							type = Type.TYPE_TEXT;
						}else{
							type = Type.TYPE_FILE;
						}
						break;
						//File Extension
					case 1:
						ext += (char) Integer.parseInt(binpar,2); 
						break;
						//Pixels
					case 2:
						pixaux += (char) Integer.parseInt(binpar,2);
						break;						
					}
				}	
				binpar = "";
			}
		}
		xH = currentPixel/a_image.getHeight();
		yH = currentPixel%a_image.getHeight();		
		pixels = Long.valueOf(pixaux);			
	}
	
	public String setHEADER(File fileOrigin){
		
		//File header in the following format:
		//Storage type/Extension/Pixel amount
		//F/docx/12345
		String rec = "";
		switch(getType()){
		  case TYPE_FILE:
			  rec += "F";
		  break;
		  case TYPE_TEXT:
			  rec += "T";
		  break;				 
		}
		
		rec += "/";
		rec += fileOrigin.getName().substring(fileOrigin.getName().lastIndexOf('.')+1);
		rec += "/";
		rec += fileOrigin.length()*8;
		rec += "/";
		
		return rec;
	}

	public String setHEADER(String Message){
		
		//File header in the following format:
		//Storage type/Extension/Pixel amount
		//F/docx/12345/
		String rec = "";
		switch(getType()){
		  case TYPE_FILE:
			  rec += "F";
		  break;
		  case TYPE_TEXT:
			  rec += "T";
		  break;				 
		}
		
		rec += "/";
		rec += "txt";
		rec += "/";
		rec += Message.length()*8;
		rec += "/";
		
		return rec;
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
		int l_result = 0;
		String name = "";		
		
		//Get the action - Write File/Read File
		switch(getAction()){
			case MODE_WRITE:
				//Get the Type of record to store in image...
				switch(getType()){
					case TYPE_FILE:
						try{
							JOptionPane.showMessageDialog(null, "Select a file to store on image");
							JFileChooser jfc = new JFileChooser("./img");
							jfc.setAcceptAllFileFilterUsed(true);
							l_result = jfc.showOpenDialog(attributesPanel);					
		
							if(l_result != JFileChooser.CANCEL_OPTION){
								name = jfc.getSelectedFile().getCanonicalPath();
							}
						} catch(IOException ex){
							ex.printStackTrace();
						}
						if((l_result != JFileChooser.CANCEL_OPTION)&&(name != null)&&(name.length() > 0)){
							File arq = new File(name);
							PrepareFile(arq);					
						}else{
							JOptionPane.showMessageDialog(null, "Invalid file name!");
							return ;
						}
						break;
					case TYPE_TEXT:
						prepareFile(((JTextArea) attributesPanel.getComponent("txtLines").getComponent()).getText());
						break;
					}				
				if(storeOnImage(a_imageIn, put)){
					a_imageIn.update();
					JOptionPane.showMessageDialog(null, "Image modified successfully");
					attributesPanel.getParent().setVisible(false);
				}
				break;	
			case MODE_READ:				
				if(isPng(a_imageIn)){					
					switch(StorageType(a_imageIn)){
						case TYPE_TEXT:
							JOptionPane.showMessageDialog(null, "Text found!");
							ReadText(a_imageIn);
							break;
						case TYPE_FILE:
							JOptionPane.showMessageDialog(null, "File found!");
							ReadFile(a_imageIn);
							break;
						default:
							JOptionPane.showMessageDialog(null, "This file does not contain any content!");
					}
				}else{
					JOptionPane.showMessageDialog(null, "The image must be in PNG format!");
					return ;
				}				
				break;
		}
	}
	
	public MarvinAttributesPanel getAttributesPanel(){
	
		attributesPanel = new MarvinAttributesPanel();
		//Create the objects to receive the text that will be masked on the image...				
		attributesPanel.newComponentRow();
		attributesPanel.addComboBox("cbSelection", "cbSelection", new String[] {"Read from Image","Write on Image"}, attributes);
		attributesPanel.newComponentRow();
		attributesPanel.addComboBox("cbType", "cbType", new String[] {"Store a file","Store a text"}, attributes);
		attributesPanel.newComponentRow();
		attributesPanel.addLabel("lblTexto", "Type below the text to be stored:");
		attributesPanel.newComponentRow();
		attributesPanel.addTextArea("txtLines", "txtLines", 6, 40, attributes);
		
		attributesPanel.newComponentRow();
		
		buttonOK = new JButton("OK");
		buttonOK.addActionListener(this);
		attributesPanel.getCurrentPanel().add(buttonOK);
		
		JComboBox cbSel = (JComboBox) attributesPanel.getComponent("cbSelection").getComponent();	
		cbSel.addActionListener(this);
		
		JComboBox cbType = (JComboBox) attributesPanel.getComponent("cbType").getComponent();
		cbType.setVisible(false);
		
		return attributesPanel;
		
	}
	
	
		
	public boolean storeOnImage(MarvinImage a_image, byte[] Put){
		
//		if((Put.length*8) > (a_image.getWidth()*a_image.getHeight()*3)){
//			JOptionPane.showMessageDialog(null, "Espaço insulficiente na imagem para armazenar informações solicitadas.","Marvin", JOptionPane.ERROR_MESSAGE);
//			return false;
//		}else{
			for (int l_pos = 0; l_pos < put.length; l_pos++){				
				for (int l_bit = 0; l_bit < 8; l_bit++){
					storeBit(a_image, put[l_pos], 7-l_bit);				
				}					
			}				
//		}		
		return true;				
	}
	
	private Type StorageType(MarvinImage a_image){
		getHEADER(a_image);
		return type;
	}
	
	private void ReadText(MarvinImage a_image){
		get = new byte[(int) (pixels/8)];
		String msgbin = "";
		String msg = "";
		int cont = 0;
		
		for(int i = 0; i < pixels; i++){
			msgbin += readBit(a_image);
			if(msgbin.length() == 8){
				get[cont] = (byte) Integer.parseInt(msgbin,2);
				msgbin = "";
				cont++;
			}
		}
		
		for(int i = 0; i < get.length; i++){
			msg += (char) get[i];
		}
		
		((JTextArea) attributesPanel.getComponent("txtLines").getComponent()).setText(msg);
	}
	
	private void ReadFile(MarvinImage a_image){
		get = new byte[(int) (pixels/8)];
		String msgbin = "";
		int cont = 0;
		
		for(int i = 0; i < pixels; i++){
			msgbin += readBit(a_image);
			if(msgbin.length() == 8){
				get[cont] = (byte) Integer.parseInt(msgbin,2);
				msgbin = "";
				cont++;
			}
		}
		
		try {			
			String name = "";
			JOptionPane.showMessageDialog(null, "Select the file destination.");
			FileNameExtensionFilter[] vExt = new FileNameExtensionFilter[]{new FileNameExtensionFilter("File - *."+ext,ext)};
			name = MarvinFileChooser.select(attributesPanel,false,MarvinFileChooser.SAVE_DIALOG,vExt);			

			if((name != null)&&(name.length() > 0)){
				File fileOutput = new File(name);

				FileOutputStream out = new FileOutputStream(fileOutput);
				BufferedOutputStream on = new BufferedOutputStream(out);

				for(int i = 0; i < get.length; i++){
					on.write((int) get[i]);
				}
				on.close();
			}else{
				JOptionPane.showMessageDialog(null, "Invalid file name.");
			}
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
						
	}
	
	private void prepareFile(String Message){

		String header = "";
		int cont = 0;
		
		header = setHEADER(Message);
						
		put = new byte[header.length()+Message.length()];
		
		for (int j = 0; j < header.length(); j++){
			put[cont] = (byte) header.charAt(j);
			cont++;
		}
			
		for (int j = 0; j < Message.length(); j++){
			put[cont] = (byte) Message.charAt(j);
			cont++;			
		}				
	}
	
	private void PrepareFile(File FileName){
		String header = "";
		int cont = 0;
		
		try{
			File fileOrigin = FileName;

			
			FileInputStream i = new FileInputStream(fileOrigin);
			BufferedInputStream in = new BufferedInputStream(i);			
			byte[] arq = new byte[(int) fileOrigin.length()];	
			
			
			in.read(arq);

			header = setHEADER(FileName);
		
			put = new byte[header.length() + arq.length];
		
			for (int j = 0; j < header.length(); j++){
				put[cont] = (byte) header.charAt(j);
				cont++;
			}
					
			for(int r=0;r<arq.length;r++){
				put[cont] = arq[r];
				cont++;
			}									
			
			in.close();						
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Sets the RGB between 0 and 255
	 * @param a
	 * @return
	 */
	public int truncate(int a) {
		if      (a <   0) return 0;
		else if (a > 255) return 255;
		else              return a;
	}

	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == buttonOK){
			attributesPanel.applyValues();
			process(getImagePanel().getImage(), null);
		}
		else{
			JComboBox cbSel = (JComboBox) attributesPanel.getComponent("cbSelection").getComponent();
			JComboBox cbType = (JComboBox) attributesPanel.getComponent("cbType").getComponent();
	
			cbType.setVisible(cbSel.getSelectedItem() == "Write on Image");
		}
	}

	private boolean isPng(MarvinImage a_image){
		String ext = a_image.getFormatName();		
        if((ext != null)&&(ext.equals("png"))){
			return true;
		}else{
			return false;
		}
	}

	private int getBit(int dado, int bit){			
		  return ((dado & (0x1 << bit)) >> bit);
	}
	
	private void storeBit(MarvinImage image, byte dado, int bit){
		int currX, currY;
		int vbit;
		int r,g,b;

		currX = currentPixel/image.getHeight();
		currY = currentPixel%image.getHeight();
		 		
		vbit = getBit(dado, bit);

		r = image.getIntComponent0(currX, currY);
		g = image.getIntComponent1(currX, currY);
		b = image.getIntComponent2(currX, currY);		
		
		switch(currentColor){
		    case RED:
		       if(vbit == 0){
					//If r is odd
					if(r % 2 !=0){
						//If r+i is less than 255
						if(r + 1 <255){ r += 1; }else{ r -= 1; }
					}
				}else{
					//If r is even
					if(r % 2 ==0){
						//If r+1 is less than 255
						if(r + 1 < 255){ r += 1; }else{ r -= 1; }
					}
			   }
		       		       
		       image.setIntColor(currX, currY, r, g, b);
		       currentColor++;
		       break;
		   
		   case GREEN:
		       if(vbit == 0){
					//If g is odd
					if(g % 2 !=0){
						//If g+i is less than 255
						if(g + 1 <255){ g += 1; }else{ g -= 1; }
					}
				}else{
					//If g is even
					if(g % 2 ==0){
						//If g+1 is less than 255
						if(g + 1 < 255){ g += 1; }else{ g -= 1; }
					}
			   }
		       		       
		       image.setIntColor(currX, currY, r, g, b);

		       currentColor++;
		       break;
		   
		   case BLUE:
		       if(vbit == 0){
					//If b is odd
					if(b % 2 !=0){
						//If b+1 is less than 255
						if(b + 1 <255){ b += 1; }else{ b -= 1; }
					}
				}else{
					//If b is even
					if(b % 2 ==0){
						//If b+1 is less than 255
						if(b + 1 < 255){ b += 1; }else{ b -= 1; }
					}
			   }
		       		       
		       image.setIntColor(currX, currY, r, g, b);
		       currentColor = RED;
		       currentPixel++;
		       break;
		  
		 }
	}
	
	private char readBit(MarvinImage image){
		int currX, currY;
		char result = '0';
		int r,g,b;
		

		currX = currentPixel/image.getHeight();
		currY = currentPixel%image.getHeight();		 				

		r = image.getIntComponent0(currX, currY);
		g = image.getIntComponent1(currX, currY);
		b = image.getIntComponent2(currX, currY);		
		
		switch(currentColor){
		    case RED:
				if(r%2==0)
				    result = '0';
				else
					result = '1';				
		       currentColor++;
		       break;
		   
		   case GREEN:
			   if(g%2==0)
				    result = '0';
				else
					result = '1';
			   currentColor++;
		       break;
		   
		   case BLUE:
			   if(b%2==0)
				    result = '0';
				else
					result = '1';
			   currentColor = RED;
		       currentPixel++;
		       break;		 
		 }
		return result;
	}
}