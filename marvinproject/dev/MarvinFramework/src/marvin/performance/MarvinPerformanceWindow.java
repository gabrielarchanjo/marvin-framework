/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package marvin.performance;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import marvin.statistic.MarvinBarChart;
import marvin.statistic.MarvinBarChartEntry;

/**
 * Window to show statistic information. The events attributes and
 * comparative charts are displayed for performance analysis.
 * 
 * @version 1.0 02/13/08
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinPerformanceWindow extends JFrame
{
	private JPanel		mainPanel,
							leftPanel,
							rightPanel,
							tempPanel;

	private GridLayout	leftPanelLayout,
								rightPanelLayout;

	MarvinBarChart tempTimeBarChart, tempStepsBarChart;
	
	/**
	 * Constructs a {@link MarvinPerformanceWindow} 
	 * @param a_registry {@link MarvinPerformanceRegistry}
	 */
	public MarvinPerformanceWindow(MarvinPerformanceRegistry a_registry){
		super("Performance statistics");
		setSize(800,580);		
		// Panels
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		rightPanel = new JPanel();
		rightPanelLayout = new GridLayout(2,1);
		rightPanelLayout.setVgap(30);
		rightPanel.setLayout(rightPanelLayout);
		tempPanel = new JPanel();
		tempPanel.setLayout(new FlowLayout());
		tempPanel.add(rightPanel);
		mainPanel.add(tempPanel, BorderLayout.EAST);

		leftPanel = new JPanel();
		leftPanelLayout = new GridLayout(0,1);
		leftPanelLayout.setVgap(30);
		leftPanel.setLayout(leftPanelLayout);
		tempPanel = new JPanel();
		tempPanel.setLayout(new FlowLayout());
		tempPanel.add(leftPanel);
		mainPanel.add(tempPanel, BorderLayout.WEST);

		getContentPane().add(mainPanel);

		MarvinPerformanceEntry l_entry;
		MarvinPerformanceEvent l_event;

		tempTimeBarChart = new MarvinBarChart("Process time (milliseconds):");
		tempTimeBarChart.setBarsColor(MarvinBarChart.SEQUENTIAL_BAR_COLOR);

		tempStepsBarChart = new MarvinBarChart("Procedures:");
		tempStepsBarChart.setBarsColor(MarvinBarChart.SEQUENTIAL_BAR_COLOR);
		
		for(int i=0; i<a_registry.size(); i++)
		{
			l_entry = a_registry.getEntry(i);

			leftPanelLayout.setRows(leftPanelLayout.getRows()+1);

			tempPanel = new JPanel();
			tempPanel.setLayout(new GridLayout(3,1));
			tempPanel.add(new JLabel(" Plugin name: "+l_entry.getName()));
			tempPanel.add(new JLabel(" Duration: "+l_entry.getTotalTime()+" ms."));
			tempPanel.add(new JLabel(" Procedures: "+l_entry.getCurrentStep()));
			leftPanel.add(tempPanel);
		
			for(int w=0; w<l_entry.size(); w++){
				l_event = l_entry.getEvent(w);
				leftPanelLayout.setRows(leftPanelLayout.getRows()+1);

				tempTimeBarChart.addEntry(new MarvinBarChartEntry(l_event.getName(), l_event.getTotalTime(), Color.blue));
				tempStepsBarChart.addEntry(new MarvinBarChartEntry(l_event.getName(), l_event.getCurrentStep(), Color.blue));

				tempPanel = new JPanel();
				tempPanel.setLayout(new GridLayout(3,1));
				tempPanel.add(new JLabel(" Step name: "+l_event.getName()));
				tempPanel.add(new JLabel(" Duration: "+l_event.getTotalTime()+" ms."));
				tempPanel.add(new JLabel(" Procedures: "+l_event.getCurrentStep()));
				leftPanel.add(tempPanel);
			}
		}

		rightPanel.add(new JLabel(new ImageIcon(tempTimeBarChart.getImage(500,250))));
		rightPanel.add(new JLabel(new ImageIcon(tempStepsBarChart.getImage(500,250))));

		setVisible(true);
	}	
}