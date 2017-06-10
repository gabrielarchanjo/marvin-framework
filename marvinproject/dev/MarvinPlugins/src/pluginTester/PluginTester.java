/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package pluginTester;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import marvin.color.MarvinColorModelConverter;
import marvin.gui.MarvinFilterWindow;
import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import marvin.plugin.MarvinImagePlugin;
import marvin.plugin.MarvinPlugin;

import org.marvinproject.image.edge.prewitt.Prewitt;

/**
 * Test plug-ins and generate .jar files
 * @author Gabriel Ambrosio Archanjo
 */
public class PluginTester extends JFrame{
	
	// Definitions
	private final static String PACKAGE_NET_FOLDER = "./bin/org/";
	private final static String INITIAL_IMAGE = "./res/senna.jpg";
	
	// Attributes
	private JButton				buttonReset,
								buttonConvertToBinary,
								buttonSaveImage,
								buttonLoadPlugin,
								buttonGenerateJarFiles,
								buttonBenchmark;
	
	private MarvinImage 		originalImage,
								newImage;
	
	private MarvinImagePanel	imagePanel;			
	
	private Benchmark			benchmark;
	
	/*
	 * @Load plug-in to test
	 */
	private void loadPlugin(){
		HashMap<Object,Object> test = new HashMap<Object,Object>();
		test.put("key", null);
		
		
		
		MarvinImagePlugin l_plugin = new Prewitt();
		
		//MarvinImagePlugin l_plugin = new Crop();
		l_plugin.load();
		
		
//		boolean matrix[][] = new boolean[][]{		{false,true,false},
//				{true,true,true},
//				{false,true,false}};
//		
		boolean matrix[][] = new boolean[][]{		{true,true,true,true,true},
				{true,true,true,true,true},
				{true,true,true,true,true},
				{true,true,true,true,true},
				{true,true,true,true,true}
				};
		
		
		
		
//		boolean matrix[][] = new boolean[][]{		{true,true,true,true,true,true,true,true,true,true},
//				{true,true,true,true,true,true,true,true,true,true},
//				{true,true,true,true,true,true,true,true,true,true},
//				{true,true,true,true,true,true,true,true,true,true},
//				{true,true,true,true,true,true,true,true,true,true},
//				{true,true,true,true,true,true,true,true,true,true},
//				{true,true,true,true,true,true,true,true,true,true},
//				{true,true,true,true,true,true,true,true,true,true},
//				{true,true,true,true,true,true,true,true,true,true},
//				{true,true,true,true,true,true,true,true,true,true},
//				
//				};
		
		
		l_plugin.setAttribute("matrix", matrix);
		//l_plugin.setAttribute("neighborhood", 5);
		//l_plugin.setAttribute("range", 15);
		
		
		//l_plugin.setAttribute("color", new Color(51, 102, 153).getRGB());
		
		
		l_plugin.setImagePanel(imagePanel);
		MarvinImage i=null; 
		try{
			//i = MarvinImageIO.loadImage("./res/tile_04.jpg");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		l_plugin.setAttribute("tile", i);
		
		System.out.println("continue running");
		
		//l_plugin.show();
		
		MarvinFilterWindow filterWindow = new MarvinFilterWindow
		(
			"Plugin", 400, 330, l_plugin.getImagePanel(), l_plugin
		);
		
		filterWindow.setVisible(true);
		
		//l_plugin.process(originalImage, originalImage, null, MarvinImageMask.NULL_MASK, false);
		//imagePanel.update();
		
		
		/*
		l_plugin.load();
		l_plugin.show();
		*/
	}
	
	public PluginTester(){
		super("Plug-in Tester");
		
		// GUI
		ButtonHandler l_buttonHandler = new ButtonHandler();
		buttonLoadPlugin = new JButton("Load Plugin");
		buttonSaveImage = new JButton("Save Image");
		buttonReset = new JButton("Reset");	
		buttonConvertToBinary = new JButton("To Binary");
		buttonGenerateJarFiles = new JButton("Generate Jar Files");
		buttonBenchmark = new JButton("Benchmark");
		
		buttonLoadPlugin.addActionListener(l_buttonHandler);
		buttonSaveImage.addActionListener(l_buttonHandler);
		buttonReset.addActionListener(l_buttonHandler);
		buttonConvertToBinary.addActionListener(l_buttonHandler);
		buttonGenerateJarFiles.addActionListener(l_buttonHandler);
		buttonBenchmark.addActionListener(l_buttonHandler);
		
		imagePanel = new MarvinImagePanel();
		JScrollPane scrollPane = new JScrollPane(imagePanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		
		JPanel panelBottom = new JPanel();
		panelBottom.add(buttonLoadPlugin);
		panelBottom.add(buttonSaveImage);
		panelBottom.add(buttonReset);
		panelBottom.add(buttonConvertToBinary);
		panelBottom.add(buttonGenerateJarFiles);		
		panelBottom.add(buttonBenchmark);
		
		// Container
		Container l_con = getContentPane();
		l_con.setLayout(new BorderLayout());
		
		l_con.add(scrollPane, BorderLayout.CENTER);
		l_con.add(panelBottom, BorderLayout.SOUTH);
		
		
		// Load image
		originalImage = MarvinImageIO.loadImage(INITIAL_IMAGE);
		
//		originalImage = new MarvinImage(500, 500, MarvinImage.COLOR_MODEL_RGB);
//		originalImage.fillRect(0, 0, 500, 500, Color.white);
//		originalImage.fillRect(100, 100, 300, 300, Color.red);
//		originalImage.update();
		
		//originalImage = new MarvinImage(50,50);
		newImage = originalImage.clone();
		imagePanel.setImage(newImage);
		
		
		
		
		
		// Benchmark
		benchmark = new Benchmark(); 
		
		int width;
		if(originalImage.getWidth() < 460){
			width = 460;
		}
		else{
			width = originalImage.getWidth();
		}
		setSize(700,500+70);
		
		setVisible(true);
	}
	
	public void generateJarFiles(File a_directory){
		String l_directoryName;
		File[] l_arrFiles;
		String l_path;
		
		if (a_directory.exists() && !a_directory.getName().equals(".svn")){
			l_arrFiles = a_directory.listFiles();

			if (l_arrFiles.length>0){
				for (File l_file : l_arrFiles) {
					if (l_file.isDirectory()){
						l_directoryName = l_file.getName();
						l_directoryName = l_directoryName.substring(0, 1).toUpperCase() + l_directoryName.substring(1).toLowerCase();  
						
						generateJarFiles(l_file);
					}
					else{
						if(isMarvinPlugin(l_file)){
							try{
								l_path = l_file.getParent();
								l_path = l_path.replace("/bin", "");
								l_path = l_path.replace("\\bin", "");
																
								Runtime.getRuntime().exec
								(
									"jar -cvf ../jar/"+getCanonicalName(l_file)+".jar"+" "+l_path,
									null,
									new File("./bin")
								);
							}
							catch(Exception e){
								e.printStackTrace();
							}
						}
					}
				}				
			}
		}
	}
	
	/**
	 * Check if the specified file is a class that implements MarvinPlugin interface
	 * @param a_file supposed MarvinPlugin file
	 * @return true if the file is associated to a MarvinPlugin class
	 */
	private boolean isMarvinPlugin(File a_file){
		Class<?> l_class=null;
		String l_classPath;
		l_classPath = a_file.getPath();
		l_classPath = l_classPath.substring(l_classPath.indexOf("bin")+4);
		l_classPath = l_classPath.replace("/", ".");
		l_classPath = l_classPath.replace("\\", ".");
		l_classPath = l_classPath.replace(".class", "");
		
		// Check if the class file implements MarvinPlugin
		try{
			l_class = Class.forName(l_classPath);							
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		
		if(MarvinPlugin.class.isAssignableFrom(l_class)){
			return true;
		}
		else{
			return false;
		}
	}
	
	private String getCanonicalName(File a_marvinPlugin){
		String l_canonicalName = a_marvinPlugin.getParent();
		l_canonicalName = l_canonicalName.substring(l_canonicalName.indexOf("bin")+4);
		l_canonicalName = l_canonicalName.replace("/", ".");
		l_canonicalName = l_canonicalName.replace("\\", ".");
		return l_canonicalName;
	}
	
	private void saveImage(){
		int result;
		String path=null;
		
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		result = chooser.showSaveDialog(this);
		
		if(result == JFileChooser.CANCEL_OPTION){
			return;
		}

		try{
			path = chooser.getSelectedFile().getCanonicalPath();
			newImage.update();
			MarvinImageIO.saveImage(imagePanel.getImage(), path);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		PluginTester pt = new PluginTester();
		pt.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private class ButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(e.getSource() == buttonReset){
				newImage = originalImage.clone();
				imagePanel.setImage(newImage);
			}
			else if(e.getSource() == buttonConvertToBinary){
				newImage = MarvinColorModelConverter.rgbToBinary(newImage, 200);
				newImage.update();
				imagePanel.setImage(newImage);
			}
			else if(e.getSource() == buttonSaveImage){
				saveImage();
			}
			else if(e.getSource() == buttonLoadPlugin){
				loadPlugin();
			}
			else if(e.getSource() == buttonGenerateJarFiles){
				generateJarFiles(new File(PACKAGE_NET_FOLDER));
			}
			else if(e.getSource() == buttonBenchmark){
				benchmark.process(originalImage);
			}
		}
	}
}
