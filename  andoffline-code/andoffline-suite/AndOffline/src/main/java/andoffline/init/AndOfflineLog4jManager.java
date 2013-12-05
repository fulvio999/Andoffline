
package andoffline.init;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Initialize the log4j system with a dummy Console appender in case of the log4j.properties file is not found;
 * So that the log.xxx are "transformed" in System.out 
 * It is used in case of the local development, when for path reason the log4j.properties can't be found 
 */
public class AndOfflineLog4jManager {
	
	private final static Logger logger = Logger.getLogger(AndOfflineLog4jManager.class);

	private  Properties configProperties;

	/**
	 * Constructor
	 */
	public AndOfflineLog4jManager() {		
		this.configProperties = new Properties();
	}
	
	/**
	 * Initialize the logging system manually with a Console Appenders, so that the log.xxx are "transformed" in System.out 
	 * This method is used when the log4j.properties file is not found (when the application is execute in the IDE)
	 * @throws IOException If some error happen 
	 */
	public void initializeOnlyConsoleLogging() {		
		
		configProperties.setProperty("log4j.appender.stdout","org.apache.log4j.ConsoleAppender");
		configProperties.setProperty("log4j.appender.stdout.layout","org.apache.log4j.PatternLayout");		 
		configProperties.setProperty("log4j.appender.stdout.layout.ConversionPattern","%5p (%F:%L) - %m%n");
		configProperties.setProperty("log4j.logger.andoffline","DEBUG,stdout");
		
		PropertyConfigurator.configure(configProperties);
				
		logger.info("Logging System initialized successfully !");		
	}
	

	public Properties getConfigProperties() {
		return configProperties;
	}

	public void setConfigProperties(Properties configProperties) {
		this.configProperties = configProperties;
	}

}
