package andoffline.phone;

import andoffline.integration.database.dto.JobBean;
import andoffline.integration.phone.command.PhoneCommandExecutor;

public class PhoneCommandExecutorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		    PhoneCommandExecutor phoneCommandExecutor = new PhoneCommandExecutor();
			
			JobBean jobBean = new JobBean();	
			jobBean.setId("1");
			jobBean.setName("primo");
			
			//execute a python script which execute a jar file
			jobBean.setInterpreter("java"); 
			jobBean.setScript("/home/fulvio/testJob.jar");
			
			
			//------ Add an argument at the interpreter (eg: -jar): necessary to execute command like: java -jar /home/fulvio/test.jar
//			Commandline.Argument arg = new Argument();
//			arg.setValue("-jar");
//			commandLine.addArg(arg,true);			
			//----------------

			
			//OK execute a python script which execute a jar file
//			jobBean.setInterpreter("/usr/bin/python");  //or python
//			jobBean.setScript("/home/fulvio/pippo.py");
//			jobBean.setArgument("uno due tre quattro"); //optional field, can be empty
			
			//OK: execute a .sh script which execute a jar file
//			jobBean.setInterpreter("/bin/bash");  //doens't work with -jar Solution: create a script with the right command java -jar 
//			jobBean.setScript("/home/fulvio/jar-executor.sh");
//			jobBean.setArgument(""); //optional field, can be empty
			
			jobBean.setDescription("this is the job description");		
			jobBean.setLastExecutionDate("");		
			jobBean.setExecutorMsisdn("");		
			
			phoneCommandExecutor.executeJob(jobBean);
		}	

 }
