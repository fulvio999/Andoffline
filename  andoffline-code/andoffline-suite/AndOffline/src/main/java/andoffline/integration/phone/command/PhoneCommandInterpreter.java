
package andoffline.integration.phone.command;

import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import andoffline.constant.backend.BackendConstantEnum;
import andoffline.constant.backend.sms.command.SmsCommandEnum;
import andoffline.constant.backend.sms.command.SmsPredicateEnum;
import andoffline.integration.database.dao.JobDao;
import andoffline.integration.database.dto.JobBean;

/**
 * Process a line read from the swap file written by Android 'logcat' utility
 * 
 * The input line are like this: 
 *  I/AndOfflineSmsProcessor(275): New message: +395678123456 : Oct-02-2013-15_10_39 : textOfSms  
 *
 */
public class PhoneCommandInterpreter {
	
	private static final Logger log = Logger.getLogger(PhoneCommandInterpreter.class);
	
	private JobDao jobDao = new JobDao();

	/**
	 * Constructor
	 */
	public PhoneCommandInterpreter() {
		
	}
	
	/**
	 * Method that processes the input messages: execute the job requested or execute a reserved command (eg start/stop web server)
	 * 
	 * NOTE: despite Android logs are filtered by tag name, 'logcat' put in the destination file also some unwanted messages.
	 * To filter them is used the log prefix set in the Android source class that handle the incoming sms;
	 * that prefix is equal at the class name (ie: AndOfflineSmsProcessor )
	 *  
	 * The text of the sms is WITH the prefix set in AndOffline was NOT removed by AndOfflineSmsProcessor class in the Android application
	 * 
	 * The general syntax of a command (the prefix at the beginning was removed) is:
	 * <command>space<predicate>space<argument>
	 * 
	 * - Execute an existing job stored on the DB
	 * execute job 1
	 * 
	 * - Execute a reserved command (ie start/stop embedded web server)
	 * execute server start 
	 * execute server stop
	 *  
	 * @param command A line coming from the log like this:	 I/AndOfflineSmsProcessor(275): New message: +395678123456 : Oct-02-2013-15_10_39 : prefix textOfSms
	 * @throws Exception 
     *
	 */
	public void processCommand(String inputCommand){		
		
		// Check if the input line contains the LOG tag inserted by Android Application
		if(StringUtils.contains(inputCommand, BackendConstantEnum.ANDOFFLINE_APP_FILTER_TAG.getParamValue()))
		{	
			try{
				//eg: I/AndOfflineSmsProcessor( 2473): New message: +393490xxxxx : dic-01-2013-12_39_40 : And execute job 1
				log.info("Processing input command: "+inputCommand);
				
				// STEP 1: extract base information (ie: msisdn, time stamp...) used to update the job on the DB
				StringTokenizer baseTokenizer = new StringTokenizer(inputCommand, ":");			
				baseTokenizer.nextToken(); //skip I/AndOfflineSmsProcessor(275)
				baseTokenizer.nextToken(); //skip "New message" String
				
				String senderMsisdn = baseTokenizer.nextToken(); //the msisdn of the phone who send the command sms
				String timeAndDate = baseTokenizer.nextToken();  // nov-17-2013-16_39_40 The date when the sms is received by phone
				String smsContent = baseTokenizer.nextToken();		
				
				// STEP 2: analyze the smsContent ("textOfSms") (ie the command to execute)
				StringTokenizer smsContentTokenizer = new StringTokenizer(smsContent, " ");	
				smsContentTokenizer.nextToken(); //skip the sms prefix
					
				// must be "execute" the only allowed command
				String command = smsContentTokenizer.nextToken(); 
				
				// must be "job" or "server"
				String predicate = smsContentTokenizer.nextToken(); 
				
				// can be a number if predicate='job', or 'start'/'stop' if predicate='server'
				String argument = smsContentTokenizer.nextToken(); 
				
				// STEP 3: validate inputCommand
				if(checkCommandSyntax(command,predicate,argument)) {					
				
					JobBean jobToExecute = null;
					/* STEP 5: execute the job
					 * NOTE: the execution is NOT done in a separate Thread because a job can last some time which is unknown, depends on the user
					 */
					if(predicate.equalsIgnoreCase("job"))
					{
						if(StringUtils.isNumeric(argument))
						{
							log.info("Loading stored job with id: "+argument);
							jobToExecute = jobDao.loadJobById(argument);						
							
							if(!StringUtils.isEmpty(jobToExecute.getId())) 
							{
							   // STEP 6: execute the job and update execution time and msisdn executor on the database
							   PhoneCommandExecutor.executeJob(jobToExecute);
						       jobDao.updateJobExecutionInfo(argument, senderMsisdn, timeAndDate);
					        } 
						
						}else
							log.fatal("The 'job' must be followed by a numeric value representing the id of the job");
						
					}else{
						//"predicate" is "server" decide if start or stop Tomcat: NO stored job is necessary to run/stop the server
						if(argument.equalsIgnoreCase("start"))
							PhoneCommandExecutor.startServer();
						else
							PhoneCommandExecutor.stopServer();
					}
					
				}else{
				  log.fatal("Input sms command: "+inputCommand+"  is invalid");
				}
			
			}catch (Exception e) {
				log.fatal("Error processing the command: "+inputCommand +" Cause: ",e);
			}			
		}
		
		//else: skip input row because don't come from the AndofflinreApp sms processor	
	}
	
	
	/**
	 * Utility method that check the validity of the tokens that compose the received sms
	 * @param command
	 * @param predicate
	 * @param argument
	 * @return true if is everything OK
	 */
	private boolean checkCommandSyntax(String command,String predicate,String argument)
	{	
		// check if all the necessary tokens are present
		if(StringUtils.isNotEmpty(command) && StringUtils.isNotEmpty(predicate) && StringUtils.isNotEmpty(argument))
		{			
			if(SmsCommandEnum.containsIgnoreCase(command)) //the 'command' is valid
			{				
				if(SmsPredicateEnum.containsIgnoreCase(predicate))  //the 'predicate' is valid
				{ 					
					// 'predicate' token can be "job" or "server"
					if(predicate.equalsIgnoreCase("job"))
					{												
					   if(StringUtils.isNumeric(argument)) //the job 'argument' is valid	
					   { 						
						  return true;
					   }else{
						  log.fatal("Input sms command is invalid, cause: the 'argument' for 'job' must be a numeric value");
						  return false;
					   }	
						
					}else if (predicate.equalsIgnoreCase("server"))
					{						
						 // the server 'argument' is valid
						if(StringUtils.equalsIgnoreCase(argument, "start") || StringUtils.equalsIgnoreCase(argument, "stop")){
							return true;
						}else{
							log.fatal("Input sms command is invalid, cause: the 'argument' must be 'start' or 'stop'");
							return false;
						}	
					}else{
						log.fatal("Input sms command is invalid, cause: the 'argument' must be 'job' or 'server'");
						return false;
					}
					
				}else{
					log.fatal("Input sms command is invalid, cause: the 'predicate' must be 'server' or 'job'");					
					return false;
				}
			} else{
				log.fatal("Input sms 'command' is invalid, cause: must be 'execute'");
				return false;
			}				
				
		}else{
		  log.fatal("Input sms command is invalid, cause: missing mandatory parameter(s)");
		  return false;
		}
    }
	
	
}	
