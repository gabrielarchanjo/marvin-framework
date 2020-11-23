/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package net.marvinproject.framework.performance;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * The performance meter provides features to analysis algorithm
 * performance. This module have your own interface components that
 * can be integrated to the application GUI.
 * 
 * @version 1.0 02/13/08
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinPerformanceMeter implements Runnable
{
	private JPanel panel;
	private JLabel labelTime;
	private JProgressBar progressBar;
	private JButton buttonStats;
	
	private MarvinPerformanceRegistry performanceRegistry;
	private MarvinPerformanceEntry currentPerformanceEntry;
	private boolean processFinished;
	private int progressValue;
	private boolean haveStats;
	private boolean finishProcess;
	private boolean enabled;
	
	/**
	 * Empty construtor
	 */
	public MarvinPerformanceMeter(){
		performanceRegistry = new MarvinPerformanceRegistry();
		
		/* old version
		// GUI
		JPanel l_panelNorth;
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		labelTime = new JLabel();
		progressBar = new JProgressBar(0,0);
		buttonStats = new JButton(new ImageIcon("./rsc/statIco.png"));
		buttonStats.setEnabled(false);
		buttonStats.setToolTipText("Show performance status");
		haveStats = false;
		enabled = true;
		buttonStats.addActionListener(new ButtonHandler());
		l_panelNorth = new JPanel();
		l_panelNorth.setLayout(new BorderLayout());
		l_panelNorth.add(labelTime, BorderLayout.WEST);
		l_panelNorth.add(buttonStats, BorderLayout.EAST);

		panel.add(l_panelNorth, BorderLayout.NORTH);
		panel.add(progressBar, BorderLayout.SOUTH);
		*/
	}

	/**
	 * 
	 * @return
	 */
	public JPanel getPanel(){
		return panel;
	}

	/**
	 * 
	 * @return
	 */
	public MarvinPerformanceRegistry getRegistry(){
		return performanceRegistry;
	}

	/**
	 * 
	 */
	public void enable(){
		enabled = true;
	}

	/**
	 * 
	 */
	public void disable(){
		enabled = false;
	}

	/**
	 * 
	 */
	public void reset(){
		if(enabled){
			performanceRegistry = new MarvinPerformanceRegistry();
			progressBar.setString("");
			progressBar.setStringPainted(true);
			labelTime.setText("");
			buttonStats.setEnabled(false);
			haveStats = false;
			finishProcess = false;
			enabled = true;
		}
	}

	/**
	 * 
	 * @param a_name
	 */
	public void start(String a_name){
		start(a_name, a_name);
	}

	/**
	 * 
	 * @param a_id
	 * @param a_name
	 */
	public void start(String a_id, String a_name){
		if(enabled){
			currentPerformanceEntry = new MarvinPerformanceEntry(a_id, a_name);
			performanceRegistry.addEntry(currentPerformanceEntry);
			processFinished = false;
			haveStats = false;
			finishProcess = false;
		}
	}

	/**
	 * 
	 * @param a_text
	 * @param a_steps
	 */
	public void enableProgressBar(String a_text, int a_steps){
		if(enabled){
			// Adjust JProgressBar
			processFinished = false;
			finishProcess = false;
			progressValue = 0;
			progressBar.setString(a_text);
			progressBar.setStringPainted(true);
			progressBar.setMaximum(a_steps);
			// Start the thread that will update the JProgressBar
			new Thread(this).start();
		}
	}

	/**
	 * 
	 */
	public void incProgressBar(){
		incProgressBar(1);
	}

	/**
	 * 
	 * @param a_inc
	 */
	public void incProgressBar(int a_inc){
		if(enabled){
			progressValue+=a_inc;
		}
	}

	/**
	 * 
	 * @param a_name
	 */
	public void startEvent(String a_name){
		startEvent(a_name, a_name);
	}

	/**
	 * 
	 * @param a_id
	 * @param a_name
	 */
	public void startEvent(String a_id, String a_name){
		if(enabled){
			currentPerformanceEntry.startEvent(a_id, a_name);
			haveStats = true;
		}
	}

	/**
	 * 
	 */
	public void finishEvent(){
		if(enabled && currentPerformanceEntry != null){
			currentPerformanceEntry.finishEvent();
		}
	}

	/**
	 * 
	 */
	public void finish(){
		if(enabled){
			labelTime.setText("Total time: " + performanceRegistry.getTotalTime() + " milliseconds");
			progressBar.setValue(0);		
			finishProcess = true;
			
			if(haveStats){
				buttonStats.setEnabled(true);
			}
		}
	}

	/**
	 * 
	 */
	public void stepFinished(){
		if(enabled && currentPerformanceEntry != null){
			currentPerformanceEntry.stepFinished();		
		}
	}

	/**
	 * 
	 * @param a_steps
	 */
	public void stepsFinished(int a_steps){
		if(enabled && currentPerformanceEntry != null){
			currentPerformanceEntry.stepsFinished(a_steps);		
		}
	}

	/**
	 * 
	 */
	public void run(){
		while(!processFinished){
			try{
				progressBar.setValue(progressValue);
				progressBar.getUI().update(progressBar.getGraphics(),progressBar);
				Thread.sleep(200);
			}
			catch(Exception e){
				e.printStackTrace();
			}

			if(finishProcess){
				processFinished = true;
			}
		}
	}

	public void showStats(){
		new MarvinPerformanceWindow(performanceRegistry);
	}
	
	/**
	 * 
	 */
	/* old version
	class ButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e){
			new MarvinPerformanceWindow(performanceRegistry);
		}
	}
	*/
}