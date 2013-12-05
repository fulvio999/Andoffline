<%@taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>


<div class="border" id="editjobDiv">

   <div align="center"> <b> Edit selected JOB </b> </div>

   <table style="width: 100%;">
      
      <tr> 
   		
   		<td width="50%">
		   <table>	  
		    
				<tr>
					<td>		
					    <s:form id="editJobForm" name="editJobForm" action="confirmEditJobAction">
						    
						    <s:hidden id="jobId" name="jobId" value="%{jobId}"/>
						    <s:hidden name="dbmsType" id="dbmsType" value="%{dbmsType}"/>	
						
						    <s:textfield id="name" name="name" label="Name" value="%{jobToEdit.name}" size="50"/>
						    <s:textfield id="interpreter" name="interpreter" label="Interpreter" value="%{jobToEdit.interpreter}" size="90"/>
						    <s:textfield id="interpreterArgument" name="interpreterArgument" label="Interpreter Arg." value="%{jobToEdit.interpreterArgument}" size="90"/>
						    <s:textfield id="script" name="script" label="Script" value="%{jobToEdit.script}" size="90"/>
						    <s:textfield id="scriptArgument" name="scriptArgument" label="Script Argument" value="%{jobToEdit.scriptArgument}" size="90"/>
						    <s:textfield id="description" name="description" label="Description" value="%{jobToEdit.description}" size="100"/>
						    
					    </s:form>
				 	</td>
				</tr>
				
			
				
				<tr>
				   <td> </td>
				   <td>
				     <sj:submit formIds="editJobForm"							 
							targets="editjobResponse" 
							indicator="indicator" 
							button="true" 
							buttonIcon="" 
							value="Confirm" />
					</td>		
				</tr>							
		
		  </table>
		    
       </td>
 
   </tr> 
   
   <tr>
		<td>					  
		    <!-- Filled with Ajax response -->
   			<div id="editjobResponse" align="center">	 </div>				  
		</td>				 	
	</tr>
  
 </table>

</div> 


