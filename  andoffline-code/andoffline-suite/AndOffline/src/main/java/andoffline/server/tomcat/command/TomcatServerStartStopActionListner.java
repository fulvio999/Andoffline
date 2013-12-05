package andoffline.server.tomcat.command;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import andoffline.gui.common.utility.BusyLabelPanel;
import andoffline.gui.panel.server.WebServerCommandPanel;


/**
 * Listener that manage the actions on the start/stop buttons in the "Embedded Server Configuration".
 * 
 * It call the API exposed by "web-app/andoffline-server-mngr.jar" (artifact built with the module "andoffline-web") to start/stop the 
 * TOMCAT server.
 *
 */
public class TomcatServerStartStopActionListner implements ActionListener {

	private static final Logger log = Logger.getLogger(TomcatServerStartStopActionListner.class);
	
	/* The label to update that show the server status */
	private JLabel errorLabel;
	private JLabel serverStatusLabel;
	private JButton startServerButton;
	private JButton stopServerButton;	
	private WebServerCommandPanel webServerCommandPanel;
	
	/**
	 * Constructor 
	 * Update the status label and enable/disable the buttons according with the server status
	 * 
	 * @param errorLabel Error description during the starting
	 * @param serverStatusLabel start/stop/running/starting...wait
	 * @param startServerButton
	 * @param stopServerButton
	 * @param serverCommandPanel The panel with the buttons to start/stop the server
	 */
	public TomcatServerStartStopActionListner(JLabel errorLabel,JLabel serverStatusLabel,JButton startServerButton,JButton stopServerButton,WebServerCommandPanel webServerCommandPanel) {
		
		this.errorLabel = errorLabel;
		this.serverStatusLabel = serverStatusLabel;		
		this.startServerButton = startServerButton;
		this.stopServerButton = stopServerButton;	
		this.webServerCommandPanel = webServerCommandPanel;
	}	
	
	/**
	 * Start/Stop the server, depending on the input ActionEvent coming form the "server Tab" panel on the GUI
	 * Don't care about the server instance. Is about the ServerManager API provide only a single instance 
	 */
	public void actionPerformed(ActionEvent actionEvent) {		
			  
		 if (actionEvent.getSource() instanceof JButton)  
		 {
		    final BusyLabelPanel busyLabelPanel = webServerCommandPanel.getBusyLabelPanel();
			   
			if(actionEvent.getActionCommand().equals("Start Server"))
			{    	
		   	   /* check for minimum requirements to start Tomcat: env variable JAVA_HOME or JRE_HOME must be set */
		       if(StringUtils.isEmpty(System.getenv("JAVA_HOME")) && StringUtils.isEmpty(System.getenv("JRE_HOME")))
		       {			      			
		          log.fatal("ERROR: to start Tomcat is necessary set JRE_HOME or JAVA_HOME environment variable !");		      		    
		          errorLabel.setForeground(Color.red);
		          errorLabel.setText("To start the Server is necessary set JRE_HOME or JAVA_HOME environment variable");
				  
		       }else{  
					 log.debug("minimum requirements to start Tomcat are satisfied (ie: JAVA_HOME or JRE_HOME)"); 
		      		
					 if(!busyLabelPanel.getJxBusyLabel().isEnabled())
				   	 {
				   	    busyLabelPanel.getJxBusyLabel().setEnabled(true);		       	  
				   	 }
				       	
				 	 if (busyLabelPanel.getJxBusyLabel().isBusy()) //true if task finished
				 	 {	      	    
				         busyLabelPanel.getJxBusyLabel().setBusy(false);
				         busyLabelPanel.getJxBusyLabel().setVisible(false);		       	   
				       	   
				     }else{	        	    
				           busyLabelPanel.getJxBusyLabel().setBusy(true);
				           busyLabelPanel.getJxBusyLabel().setVisible(true); 		       	      			       	      
				       	      
				       	   serverStatusLabel.setText("STARTING...WAIT");
				       	   
						   Thread startServerThread = new Thread()
						   {			                    
						      public void run() {							   
								
							      try{
									   log.info("Starting the server...");
										   
									   StartServerFromCommandline.startServer();
									
									   //dummy solution to wait the startup because plexus-utils execute an async call								
									   Thread.sleep(1200);
									  									   
									   // stop and hide the animation
								       busyLabelPanel.getJxBusyLabel().setBusy(false);
								   	   busyLabelPanel.getJxBusyLabel().setVisible(false);	
								   		   
								   	   startServerButton.setEnabled(false);
									   stopServerButton.setEnabled(true); 
								   		  
								   	   log.info("Server started !");
								   	   serverStatusLabel.setText("STARTED");	
						   		       
								   }catch (Exception exception) {
										
									  log.fatal("Error Starting the server: ",exception);
									  serverStatusLabel.setForeground(Color.red);
									  serverStatusLabel.setText("ERORR");
									   
									  // stop and hide the animation
									  busyLabelPanel.getJxBusyLabel().setBusy(false);
							   		  busyLabelPanel.getJxBusyLabel().setVisible(false);	
								   }							  
							    }						   
						    };
						  
						    startServerThread.start();				         
				        }      		  
		      	   }		  
		      }
		    	  
		      if (actionEvent.getActionCommand().equals("Stop Server"))
		      {		    			   
		    	  if(!busyLabelPanel.getJxBusyLabel().isEnabled())
			       	 {
			       	    busyLabelPanel.getJxBusyLabel().setEnabled(true);		       	  
			       	 }
			       	
			       	 if (busyLabelPanel.getJxBusyLabel().isBusy()) {	   //true if task finished   	    
			       	     busyLabelPanel.getJxBusyLabel().setBusy(false);
			       	     busyLabelPanel.getJxBusyLabel().setVisible(false);		       	   
			       	   
			       	  }else{	        	    
			       	      busyLabelPanel.getJxBusyLabel().setBusy(true);
			       	      busyLabelPanel.getJxBusyLabel().setVisible(true); 
					
					      serverStatusLabel.setText("STOPPING...WAIT");				    
					  
					      Thread stoptServerThread = new Thread() {
		                    
						     public void run() {
							   
							   try {
								   log.info("Stopping the server...");
								   
								   StopServerFromCommandLine.stopServer();	
								   //dummy solution to wait the startup because plexus-utils execute an async call
								   Thread.sleep(12000);							   
								   
								   // stop and hide the animation
						     	   busyLabelPanel.getJxBusyLabel().setBusy(false);
						   		   busyLabelPanel.getJxBusyLabel().setVisible(false);
						   		   
						   		   startServerButton.setEnabled(true);
								   stopServerButton.setEnabled(false); 
						   		   
						   		  log.info("Server stopped !");
						   		  serverStatusLabel.setText("STOPPED");	
					   		   
							   }catch (Exception exception) {
									
								  log.fatal("Error Stopping the server: ",exception);
								  serverStatusLabel.setForeground(Color.red);
								  serverStatusLabel.setText("ERORR");
								   
								  // stop and hide the animation
								  busyLabelPanel.getJxBusyLabel().setBusy(false);
						   		  busyLabelPanel.getJxBusyLabel().setVisible(false);	
								}
						      }						   
					      };
					  
					      stoptServerThread.start();					  
			        } 						    		  
	    	  }	      		 
	     }
    }	
   

}
