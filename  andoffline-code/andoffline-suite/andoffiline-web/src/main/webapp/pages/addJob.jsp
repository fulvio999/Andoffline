<%@taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>


<div class="border" id="addjobDiv">

   <div align="center"> <b> ADD JOB </b> </div>

   <table style="width: 100%;">
      
      <tr> 
   		
   		 <td width="50%">
		    <table>	  
		    
				<tr>
					<td>		
					    <s:form id="confirmAddJobForm" name="confirmAddJobForm" action="confirmAddJobAction">						    
						   
						    <s:hidden name="dbmsType" id="dbmsType" value="%{dbmsType}" />	
						
						    <s:textfield id="name" name="name" label="Name" style="width:90em"/>
						    <s:textfield id="interpreter" name="interpreter" label="Interpreter" style="width:90em"/>
						    <s:textfield id="script" name="script" label="Script" style="width:90em"/>
						    <s:textfield id="argument" name="argument" label="Argument" style="width:90em"/>
						    <s:textfield id="description" name="description" label="Description" style="width:90em"/>
						    
					    </s:form>
				 	</td>
				</tr>				
				
				<tr>
				   <td> </td>
				   <td>
				     <sj:submit formIds="confirmAddJobForm"							 
							targets="confirmAddjobResponse" 
							indicator="indicator" 
							button="true" 
							buttonIcon="" 
							value="Add" />
				   </td>		
				</tr>							
		
		  </table>
		    
       </td>
 
   </tr> 
   
   <tr>
		<td>					  
		    <!-- Filled with Ajax response -->
   			<div id="confirmAddjobResponse" align="center">	 </div>				  
		</td>				 	
	</tr>
  
 </table>

</div> 


