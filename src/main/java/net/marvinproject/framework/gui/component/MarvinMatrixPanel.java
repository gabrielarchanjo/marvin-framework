/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package net.marvinproject.framework.gui.component;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

public class MarvinMatrixPanel extends JPanel{

	private JFormattedTextField textFields[][];
	private int 				rows;
	private int					columns;
	
	public MarvinMatrixPanel(int rows, int columns){
		this.rows = rows;
		this.columns = columns;
		
		JPanel panelTextFields = new JPanel();
		GridLayout layout = new GridLayout(rows, columns);
		
		panelTextFields.setLayout(layout);
		
		//Format
		DecimalFormat decimalFormat = new DecimalFormat();
		DecimalFormatSymbols dfs = decimalFormat.getDecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		decimalFormat.setDecimalFormatSymbols(dfs);
		
		textFields = new JFormattedTextField[rows][columns];
		for(int r=0; r<rows; r++){
			for(int c=0; c<columns; c++){
				
				textFields[r][c] = new JFormattedTextField(decimalFormat);
				textFields[r][c].setValue(0.0);
				textFields[r][c].setColumns(4);
				panelTextFields.add(textFields[r][c]);
				
			}
		}
		
		setLayout(new FlowLayout(FlowLayout.CENTER));
		add(panelTextFields);
	}
	
	public double[][] getValue(){
		
		double[][] result = new double[rows][columns];
		
		for(int r=0; r<rows; r++){
			for(int c=0; c<columns; c++){
				result[r][c] = Double.parseDouble(textFields[r][c].getText());
			}
		}
		return result;
	}
	
}
