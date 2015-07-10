/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class NewFileDialog extends JDialog{
	
	private JLabel			labelWidth,
							labelHeight;
	
	private JTextField		textWidth, 
							textHeight;
	
	private JButton			buttonOk;
	
	private MarvinEditor	marvinEditor;
	
	
	public NewFileDialog(MarvinEditor parent, String title){
		super(parent, title, JDialog.ModalityType.APPLICATION_MODAL);
		this.marvinEditor = parent;
		loadGUI();
	}
	
	private void loadGUI(){
		labelWidth = new JLabel("width:");
		labelHeight = new JLabel("height:");
		
		textWidth = new JTextField(5);
		textHeight = new JTextField(5);
		
		buttonOk = new JButton("OK");
		
		JPanel panelNorth = new JPanel();
		panelNorth.setLayout(new FlowLayout());
		panelNorth.add(labelWidth);
		panelNorth.add(textWidth);
		panelNorth.add(labelHeight);
		panelNorth.add(textHeight);
		
		JPanel panelSouth = new JPanel();
		panelSouth.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelSouth.add(buttonOk);
		
		
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		c.add(panelNorth, BorderLayout.NORTH);
		c.add(panelSouth, BorderLayout.SOUTH);
		
		
		// button Listener
		buttonOk.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				boolean success = false;
				try{
					int width = Integer.parseInt(textWidth.getText());
					int height = Integer.parseInt(textHeight.getText());
					
					if(width >= 0 && height >= 0){
						marvinEditor.newImage(width, height);
						getDialog().dispose();
						success = true;
					}
					
				}
				catch(Exception ex){
					success = false;
				}
				
				if(!success){
					JOptionPane.showMessageDialog(null, "Invalid width or height",
							"Marvin", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		
		setSize(300,100);
		setVisible(true);	
	}
	
	public JDialog getDialog(){
		return this;
	}
}

