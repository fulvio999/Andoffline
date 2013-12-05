<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net" %>

<%@page import="andoffline.web.decorator.JobTableDecorator" %>

  <form id="detailJobForm" name="detailJobForm" action="editJobAction">
  
      <!-- set during after the successfully login  -->
  	  <s:hidden name="dbmsType" id="dbmsType" value="%{dbmsType}"/>	
  	  <s:hidden name="contentType" id="contentType" value="%{contentType}"/>	
				    	
	  <display:table name="requestScope.jobList" decorator="andoffline.web.decorator.JobTableDecorator" requestURI="getStoredContentAction.action" pagesize="5" id="jobTable" export="true" style="width:100%">
				
			<display:setProperty name="export.csv" value="true"/>		
			<display:setProperty name="export.pdf" value="true"/>
			<display:setProperty name="export.csv" value="false"/>
				
			<!-- Decorated column, note: the "property" value must be equal at the column name -->
			<display:column property="decoratorJobId" title="" media="html"/> <!-- With media=html the column will be shown only in jsp page (default is all) -->	
							
			<display:column property="id" title="Id" style="text-align:center" />	
			<display:column property="name" title="Name" media="html" style="text-align:center"/>
			<display:column property="interpreter" title="Interpreter" style="text-align:center"/>
			<display:column property="interpreterArgument" title="Interpreter Argument" style="text-align:center"/>
			<display:column property="script" title="Script" escapeXml="true" style="text-align:center"/>
			<display:column property="scriptArgument" title="Script Argument" escapeXml="true" style="text-align:center"/>
			<display:column property="executorMsisdn" title="Last Executor Msisdn" escapeXml="true" />
			<display:column property="lastExecutionDate" title="Last Execution Date (mm/dd/yyyy)" escapeXml="true" sortable="true"/>
			<display:column property="description" title="Description" escapeXml="true" style="text-align:left"/>									
						    					
	  </display:table>			    	
				    	
  </form>

   <!-- The table with the buttons for the CRUD operations  -->
   <table style="width: 100%">  
    
	  <tr>
	     <!-- ^^^^^^^ Edit job ^^^^^^^^ -->
	   	 <td align="right">
	   	   
	   	   	<sj:submit formIds="detailJobForm"							 
					   targets="jobdiv" 
					   indicator="indicator" 
					   button="true" 
					   buttonIcon="" 
					   value="Edit Selected" 
					   cssClass="customButton"
					   />	   	   
	  	 </td>
	  	 
	  	  <!-- ^^^^^^^ Delete job ^^^^^^^^ -->
	  	  <td>	
			 <s:form id="deleteJobForm" action="deleteJobAction" >  	 	

				 <!-- Value set with Javascript (function 'getJobSelected') -->
		  	 	 <s:hidden name="jobId" id="selectedJob" value=""/>	
		  	 	 <s:hidden name="dbmsType" id="dbmsType" value="%{dbmsType}"/>		  	 	
	  	 	
			 </s:form>	
			
			 <sj:submit formIds="deleteJobForm"						
						targets="jobdiv" 
						indicator="indicator" 
						button="true" 
						buttonIcon=""
						value="Delete Selected" 
						cssClass="customButton"
						onclick="JavaScript: getJobSelected()" 
						/>							  	 
	  	  </td>	  
	  	  
	  	  <!-- ^^^^^^^ Add job ^^^^^^^^ -->	 
	  	  <td>
	  	  	 <s:form id="addJobForm" action="addJobAction" > 				
		  	 	 <s:hidden name="dbmsType" id="dbmsType" value="%{dbmsType}"/>		  	 	
			 </s:form>	
			
			 <sj:submit formIds="addJobForm"						
						targets="jobdiv" 
						indicator="indicator" 
						button="true" 
						buttonIcon=""
						value="Add Job"
						cssClass="customButton"
						/>	  	  
	  	  </td>
	  	  
	  	  <!-- ^^^^^^^ Reload job ^^^^^^^^ -->
	  	  <td>	  	 	
	  	 	<s:form id="formReloadjob" action="getStoredContentAction" theme="simple" cssClass="yform">	
	  	 	  	
	  	 	  	<s:hidden name="dbmsType" id="dbmsType" value="%{dbmsType}"/>	
	  	 	  	<s:hidden name="contentType" id="contentType" value="%{contentType}"/>	
	  	 	  	 	
		  	 	<sj:submit formIds="formReloadjob" 	    	
			    	       effect="pulsate" 
			    	       value="(Re)Load Jobs" 
			    	       indicator="indicator" 
			    	       button="true" 
			    	       cssClass="customButton"
			    	       />		    
		    </s:form> 
	  	 
	  	 </td>
	  	 
	  </tr>
	  
  </table>
  
  <br/> <br/>
  
   <!-- Filled with Ajax response -->
   <div id="jobdiv" align="center"> 
   
   </div>
   
   <br/> <br/> <br/>
  
   <div align="center">
	  	<img id="indicator" src="images/loading.gif" alt="Loading..." style="display:none"/>
   </div>
  
  
 
  