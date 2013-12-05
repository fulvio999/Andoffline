package andoffline.server.tomcat.command;

import org.apache.log4j.Logger;
import org.codehaus.plexus.util.cli.StreamConsumer;

/**
 * 
 * Receive the Sysstem.out returned from a shell command executed with plexus-util framework 
 * (eg: /bin/bash catalina.sh start) and print them to log file.
 * So the output that is produced executing the command directly in the command line, is captured and
 * placed in the log file.
 *
 */
public class CustomSystemOutConsumer implements StreamConsumer{
	
	private static final Logger log = Logger.getLogger(CustomSystemOutConsumer.class);

	/**
	 * Constructor
	 */
	public CustomSystemOutConsumer() {
		
	}

	/* 
	 * Called for each row produced by the execution of the command
	 */
	public void consumeLine(String str) {		
		log.info("Command System.out output: "+str);		
	}

}
