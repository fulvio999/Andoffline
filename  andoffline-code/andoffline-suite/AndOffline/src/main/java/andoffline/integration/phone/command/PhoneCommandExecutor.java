
package andoffline.integration.phone.command;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.Commandline.Argument;

import andoffline.integration.database.dto.JobBean;
import andoffline.server.tomcat.command.StartServerFromCommandline;
import andoffline.server.tomcat.command.StopServerFromCommandLine;

/**
 * Execute a job using the command line of the running OS, a job can
 * start server  : reserved command, NOT necessary save a job for it
 * stop server   : reserved command, NOT necessary save a job for it
 * execute a previously DB stored job
 *
 */
public class PhoneCommandExecutor {
	
	 private static final Logger log = Logger.getLogger(PhoneCommandExecutor.class);

	/**
	 * Constructor
	 */
	public PhoneCommandExecutor() {
		
	}
	
	/**
	 * Start the embedded Tomcat server 
	 */
	public static void startServer(){
		try {			
			StartServerFromCommandline.startServer();
		}catch (CommandLineException e) {
		   log.fatal("Error starting Tomcat embedded server from remote sms, cause: ",e);
		} catch (InterruptedException e) {
			log.fatal("Error starting Tomcat embedded server from remote sms, cause: ",e);
		}
	}
	
	
	/**
	 * Stop the embedded Tomcat server  
	 */
	public static void stopServer(){
		try {
			StopServerFromCommandLine.stopServer();			
		}catch (CommandLineException e) {
		   log.fatal("Error stopping Tomcat embedded server from remote sms, cause: ",e);
		}
	}
	
	
	/**
	 * Execute a database stored job.
	 * NOTE: to start/stop the embedded server is NOT necessary have a database stored job; in that cases are executed the 
	 * methods startServer() or stopServer()
	 * 
	 * @param jobToExecute The {@link JobBean} representing a DB stored job to be executed	
	 */
	public static void executeJob(JobBean jobToExecute){
		
		log.info("Executing stored job with id: "+jobToExecute.getId()+ " Interpreter: "+jobToExecute.getInterpreter()+"Interpreter Arg:"+jobToExecute.getInterpreterArgument()+" Script: "+jobToExecute.getScript()+" Script Argument: "+jobToExecute.getScriptArgument());
		
		try{
			//maybe: the OS type is detected in auto by Plexus library
			Commandline commandLine = new Commandline();
			
			String osType = System.getProperty("os.name");
			log.debug("Executing job on OS type: "+osType);			

			ArrayList<String> argumentsList = new ArrayList<String>(); 
			
			if(!StringUtils.isEmpty(jobToExecute.getScriptArgument()))
			{
				//tokenize the input arguments			
				StringTokenizer scriptArgumentTokenizer = new StringTokenizer(jobToExecute.getScriptArgument(), " ");
				argumentsList.add(jobToExecute.getScript()); //add the script name
				
				while(scriptArgumentTokenizer.hasMoreTokens()){
				   argumentsList.add(scriptArgumentTokenizer.nextToken()); //add the script arguments
				}				
				
				String[] arguments = (String[]) argumentsList.toArray(new String[argumentsList.size()]);
				commandLine.addArguments(arguments); //the script name and his arguments
				
			}else{
				log.debug("The script don't have arguments");				
				String[] arguments = {jobToExecute.getScript()};
				commandLine.addArguments(arguments);
			}
			
			/* Add an argument at the interpreter (eg: '-jar' in case of 'java' interpreter ).
			   Necessary to execute commands like: java -jar /home/fulvio/test.jar 
			*/
			if(!StringUtils.isEmpty(jobToExecute.getInterpreterArgument()))
			{	
			   Commandline.Argument arg = new Argument();
			   arg.setValue(jobToExecute.getInterpreterArgument()); //eg: -jar
			   commandLine.addArg(arg,true);
			}
				
		    commandLine.setExecutable(jobToExecute.getInterpreter());  //eg: python or (/usr/bin/python) or java -jar
		    CommandLineUtils.StringStreamConsumer out = new CommandLineUtils.StringStreamConsumer();
	        CommandLineUtils.StringStreamConsumer err = new CommandLineUtils.StringStreamConsumer();   
	
	        String cmdLine = CommandLineUtils.toString(commandLine.getCommandline());
	        
	        //eg: /usr/bin/python  /home/fulvio/pippo.py argument1 argument2
	        log.info("Executing the job command: "+cmdLine);
	        //System.out.println("Executing the job command: "+cmdLine);
	        
		    int returnCode = CommandLineUtils.executeCommandLine(commandLine, out, err);
		   
		    if(returnCode != 0) {
			    log.fatal("Error executing the job, return code: "+returnCode); 		   
		            	
	           	String output = StringUtils.isEmpty(err.getOutput()) ? null : '\n' + "Error details: "+err.getOutput().trim();	           	
	           	log.fatal("Job execution failed, cause "+output);	
		            	
		    }else{			 
			  log.info("Job executed successfully !"); 				 
		    }
		
	   }catch (Exception e) {		 
		  log.fatal("Error executing the job, cause: ",e);		  
	   }
		
    }

}
