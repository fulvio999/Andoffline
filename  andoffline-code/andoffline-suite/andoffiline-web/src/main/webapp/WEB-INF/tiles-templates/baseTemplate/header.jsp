<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/struts-tags" prefix="s"%>	

<s:url id="logoutUrl" action="/logout" />

<!-- The header of the jsp pages  -->

   <table style="width: 100%">
		 
			  <tbody>
			  
			    <tr>
			        <td align="Center">             
			          	
			          	<table style="width: 100%; height:40px; background-color: #B9D5D0;">  
 
							 <tr>							    
							      <td class="messageHeader"> 
							       	  Welcome: <s:property value="#session.loggedUser" />          
							      </td>     
							      
							      <td style="vertical-align: middle; margin-left: 142px; text-align: center;" rowspan="2" colspan="1"> 
							      	  <div class="title"> <b> AndOffline - Web Console 1.0 beta </b> </div> 
							      </td>
							            
							      <td align="right" style="vertical-align: middle;">        
								      <span class="messageHeader">Logout</span> 
								      <c:url value="/logout" var="logoutUrl"/>
								      <a href="${logoutUrl}">  <img style="vertical-align: middle;" alt="logout button" src="images/logout.png" height="24" width="24"> </a>       
							      </td>							      
							 </tr> 
							
					    </table>
			          	     
			       </td> 			        
			    </tr>
		 
			    
		 </tbody>
		  
	</table>  
