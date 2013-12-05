
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>


<html>

   <head>
		<title>AndOffline - Web Console 1.0</title>
				
		<link href="css/custom.css" rel="stylesheet" type="text/css" media="all"/>
   </head> 

<body>

<table style="width: 100%" >
  
    <tr>
        <td>         
          	 <!-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ HEADER ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ -->
			   <div align="center">
			   
				  <table class="headerStyle" >
			
				    <tbody>
					   <tr>
						  <td style="vertical-align: middle; margin-left: 142px; text-align: center;" rowspan="2" colspan="1">
							  
							  <div class="title">
							  	  <b>AndOffline - Web Console 1.0</b>
							  </div>		
							  			  
						  </td>						  
						 </tr>					
				      </tbody>
				    
				   </table>
			
			   </div>
	 		<!-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ -->        
        </td>      
    </tr>
   
    <tr>
          <td>
	    	 	<!-- ^^^^^^^^^^^^^^^^^^^^^^^^^^ CONTENT ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ -->
	    	 	
	    	 	<!-- 
					"j_spring_security_check" is a default url where are placed Spring filters already created					
					 NOTE: also the fields names must have a standard name					
				-->
				<form action="j_spring_security_check" method="post" id="loginForm">
	           
				<p align="center">
				
		    		<br/>
		    		    <!-- "login_error" is a parameter flag that indicates if some login error happen, See applicationContext-security.xml -->
		    		    <%						
						    if (request.getParameter("login_error") != null) {							
						%>			       
							<span id="infomessage" class="errorMessageSpring">
							   	Login failed !
							</span>	
						<%	    	
						   }
						%>  
						
					<br/>
				</p>	
				
				<div align="center"> 
				<table>
					<tr>
					 	<td>
							<label for="j_username">Login</label>:
					 	</td>
					 	<td>
					 	    <!-- In case of bad login re-show the wrong username inserted -->
					 		<input id="j_username" name="j_username" size="20" maxlength="50" type="text" value=""/>
					 	</td>
					</tr>
					
					<tr>
						<td>
						   <label for="j_password">Password</label>:
						</td>
						<td>
							<input id="j_password" name="j_password" size="20" maxlength="50" type="password" value=""/>
						</td>						
					</tr>
					
					<tr>
					    <td></td>
						<td align="center">																				
							<sj:submit formIds="loginForm" 	effect="pulsate" value="Login" indicator="indicator" button="true" />	
						</td>
					</tr>
					
				</table>
				
				<br/>
				
				<img id="loginIndicator" src="images/loading.gif" alt="Loading..." style="display:none"/>				
				
			  </div>
           
	    </form>
		<!-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ -->
	    </td>
    </tr>
    
    <tr class="footerStyle">
          <!-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^ FOOTER ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ -->
          <td align="center">
	          Copyright &copy; AndOffline			  
	      </td>
	      <!-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ -->
    </tr>

  
</table>

</body>

</html>
