package andoffline.server.tomcat.command;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;



/**
 * Class the uses Plexus-util framework to Stop Tomcat start/stop script from java.
 * 
 */
public class StopServerFromCommandLine {	
	
	private static final Logger log = Logger.getLogger(StopServerFromCommandLine.class);
	
	private static String SERVER_CONF_FILE_NAME = "conf/server.properties";

	/**	
	 * @throws CommandLineException 
	 */
	//public static void main(String[] args) throws CommandLineException {
	public static void stopServer() throws CommandLineException {
		
		final Commandline commandLine = new Commandline();
		String[] arguments = null;
		
		String osType = System.getProperty("os.name");
		log.info("Stopping server on OS type: "+osType);
		
	    //the folder where is placed the script to execute	 
        String tomcatWorkingDir = loadServerConfig()+File.separator+"web-app"+File.separator+"apache-tomcat"+File.separator+"bin";

        //set the executable according with the OS used by the user
      	if(osType.equalsIgnoreCase("Linux") || osType.contains("Unix"))
      	{      	    
      	     commandLine.setExecutable("/bin/bash");
	         arguments = new String[]{"catalina.sh", "stop"};
      	        
      	     commandLine.setWorkingDirectory(tomcatWorkingDir);	    
      		 commandLine.addArguments(arguments);	    
      	        
      	     String cmdLine = CommandLineUtils.toString(commandLine.getCommandline());
      	     log.info("Stopping the server with command: "+cmdLine);
      	       
      		 /* eg:  /bin/bash catalina.sh start
      	     CustomStreamConsumer capture the output coming from the above command */
      	         
      	     // on Windows open a cmd window and this is a blocking instruction, it continue when the cmd window is closed, but this stop Tomcat :(
      		 int returnCode = CommandLineUtils.executeCommandLine(commandLine, new CustomSystemOutConsumer(), new CustomSystemErrorConsumer(),10);
      		 log.info("Start server CommandLineUtils, return code: "+returnCode);
      		    
      		 if (returnCode != 0)
      		 {
      		    log.fatal("Error stopping Tomcat, return code: "+returnCode); 			   	            	
      		 }else{
      		    log.info("Tomcat stopped successfully !"); 
      		 }	    
      	    
      	 }else{	 
      		   log.info("Stopping Tomcat on Windows....");
      			 
      		   commandLine.setExecutable("catalina.bat");	
      		   arguments = new String[]{"stop"};				
      				
      		   commandLine.setWorkingDirectory(tomcatWorkingDir);	    
      		   commandLine.addArguments(arguments);	    
      		        
      		   String cmdLine = CommandLineUtils.toString(commandLine.getCommandline());
      		   log.info("Stopping the server on Windows with command: "+cmdLine);
       		  
      		   Thread stopServerOnWindowsThread = new Thread()
      		   {
      		      public void run()
      		      {	
      		         try{
      		      	     /* On Windows open this instruction open a cmd window and the execution is blocked on this instruction.
      		       		    This is the reason why a new Thread is used
      		       		 */
      		       		 CommandLineUtils.executeCommandLine(commandLine, new CustomSystemOutConsumer(), new CustomSystemErrorConsumer(),10);
      		        		   
      		        	}catch (Exception exception) {							
      					   log.fatal("Error stopping the server on Windows: ",exception);							  
      					}			    
      		       }						   
      		   };
      		  
      		   stopServerOnWindowsThread.start();
      		   log.info("Tomcat stopped successfully on windows !"); 
      	   } 
	 }
	
	/**
     * Utility method used to load the Tomcat server base installation directory from an external properties file
     * filled by IZpack during the installation because is a value known only at runtime.
     * 	
     * @return Tomcat server base installation directory
    */
   private static String loadServerConfig(){	   
        
	   try{		  
		   File serverConfigFile = new File(SERVER_CONF_FILE_NAME);
		   FileReader fr = new FileReader(serverConfigFile.getAbsolutePath());
			  
		   Properties serverProperties = new Properties();
		   serverProperties.load(fr);
					 
		   String serverBaseDir = serverProperties.getProperty("serverBaseDir");
		   log.info("Tomcat server installation base dir: "+serverBaseDir); //eg:  /home/fulvio/AndOffline
			 
		   return serverBaseDir;
		
	   }catch (Exception e) {
		  //problem reading the configuration or the server was not started		   
		  log.fatal("Error getting the tomcat installation dir, (maybe server was not started) cause: "+e.getMessage());
		  return null;
	   }
   }

}
